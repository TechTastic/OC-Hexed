package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.utils.TreeList;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.item.Memory;
import li.cil.oc.api.machine.Architecture;
import li.cil.oc.api.machine.ExecutionResult;
import li.cil.oc.api.machine.Machine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@Architecture.Name("Hexcasting")
@Architecture.NoMemoryRequirements
public class HexcastingArchitecture implements Architecture {
    private final Machine machine;

    private CastingVM vm;
    private SpellContinuation continuation;
    private long maxStackSize = 0;

    private String error;

    public HexcastingArchitecture(Machine machine) {
        this.machine = machine;
    }

    private Iota loadInitialProgram() {
        for (ItemStack stack : this.machine.host().internalComponents()) {
            ADIotaHolder holder = IXplatAbstractions.INSTANCE.findDataHolder(stack);
            if (holder != null)
                return holder.readIota();
        }
        return null;
    }

    public long getMaxStackSize() {
        return this.maxStackSize;
    }

    @Override
    public boolean isInitialized() {
        return this.vm != null;
    }

    @Override
    public boolean recomputeMemory(Iterable<ItemStack> components) {
        this.maxStackSize = 0;
        components.forEach(stack -> this.maxStackSize += Driver.driverFor(stack) instanceof Memory mem ? ((Double) mem.amount(stack)).longValue() : 0L);
        return this.maxStackSize >= 256;
    }

    @Override
    public boolean initialize() {
        Iota program = loadInitialProgram();
        if (program != null) {
            this.vm = CastingVM.empty(new ArchitectureCastEnv(this.machine));
            this.vm.setImage(new CastingImage(TreeList.from(List.of(program)), this.vm.getImage().getParenCount(), this.vm.getImage().getParenthesized(), this.vm.getImage().getEscapeNext(), this.vm.getImage().getSimulateNext(), this.vm.getImage().getOpsConsumed(), this.vm.getImage().getComponents()));
            this.continuation = SpellContinuation.Done.INSTANCE.pushFrame(new FrameEvaluate(
                    TreeList.from(List.of(new PatternIota(HexActions.EVAL.value().prototype()))), false));
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        this.vm = null;
        this.continuation = null;
    }

    @Override
    public void runSynchronized() {
        if (!(this.continuation instanceof SpellContinuation.NotDone not)) return;

        CastResult result = not.getFrame().evaluate(
                not.getNext(),
                this.vm.getEnv().getWorld(),
                this.vm
        );

        if (!result.getResolutionType().getSuccess())
            this.error = result.getResolutionType().name();

        if (result.getNewData() != null)
            this.vm.setImage(result.getNewData());

        this.vm.getEnv().postCast(this.vm.getImage());
        this.vm.getEnv().postExecution(result);
        this.vm.performSideEffects(result.getSideEffects());

        this.continuation = result.getContinuation();
    }

    @Override
    public ExecutionResult runThreaded(boolean isSynchronizedReturn) {
        if (isSynchronizedReturn) {
            if (this.error != null) return new ExecutionResult.Error(this.error);
            else if (this.continuation instanceof SpellContinuation.Done) return new ExecutionResult.Sleep(1);
            else return new ExecutionResult.SynchronizedCall();
        } else if (this.continuation instanceof SpellContinuation.NotDone)
            return new ExecutionResult.SynchronizedCall();
        return new ExecutionResult.Sleep(1);
    }

    @Override
    public void onSignal() {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void loadData(CompoundTag nbt) {
        if (this.vm == null)
            this.vm = CastingVM.empty(new ArchitectureCastEnv(this.machine));

        CastingImage.getCODEC().decode(NbtOps.INSTANCE, nbt.getCompound("image"))
                .ifSuccess(p -> this.vm.setImage(p.getFirst()));
        SpellContinuation.getCODEC().decode(NbtOps.INSTANCE, nbt.getCompound("continuation"))
                .ifSuccess(p -> this.continuation = p.getFirst());
    }

    @Override
    public void saveData(CompoundTag nbt) {
        if (this.vm != null)
            CastingImage.getCODEC().encodeStart(NbtOps.INSTANCE, this.vm.getImage())
                .ifSuccess(t -> nbt.put("image", t));
        if (this.continuation != null)
            SpellContinuation.getCODEC().encodeStart(NbtOps.INSTANCE, this.continuation)
                .ifSuccess(t -> nbt.put("continuation", t));
    }
}
