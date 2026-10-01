package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.ContinuationIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.utils.TreeList;
import at.petrak.hexcasting.common.casting.actions.spells.OpFlight;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import io.github.techtastic.ochexed.util.NBTUtils;
import li.cil.oc.api.machine.Architecture;
import li.cil.oc.api.machine.ExecutionResult;
import li.cil.oc.api.machine.Machine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@Architecture.Name("Hexcasting")
@Architecture.NoMemoryRequirements
public class HexcastingArchitecture implements Architecture {
    private final Machine machine;

    private CastingVM vm;
    private SpellContinuation continuation;
    private String error;

    public HexcastingArchitecture(Machine machine) {
        this.machine = machine;
    }

    private TreeList<Iota> loadInitialProgram() {
        return TreeList.from(List.of(new PatternIota(HexActions.GET_CASTER.value().prototype()), new PatternIota(HexActions.PRINT.value().prototype())));
    }

    @Override
    public boolean isInitialized() {
        return this.vm != null;
    }

    @Override
    public boolean recomputeMemory(Iterable<ItemStack> components) {
        return true;
    }

    @Override
    public boolean initialize() {
        this.vm = CastingVM.empty(new ArchitectureCastEnv(this.machine));
        this.continuation = SpellContinuation.Done.INSTANCE.pushFrame(new FrameEvaluate(TreeList.from(loadInitialProgram()), false));
        return true;
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
        this.vm.setImage(NBTUtils.fromNBT(nbt));
        SpellContinuation.getCODEC().decode(NbtOps.INSTANCE, nbt.getCompound("continuation"))
                .ifSuccess(p -> this.continuation = p.getFirst());
    }

    @Override
    public void saveData(CompoundTag nbt) {
        if (this.vm != null) NBTUtils.toNBT(this.vm.getImage(), nbt);
        if (this.continuation != null) SpellContinuation.getCODEC().encodeStart(NbtOps.INSTANCE, this.continuation)
                .ifSuccess(t -> nbt.put("continuation", t));
    }
}
