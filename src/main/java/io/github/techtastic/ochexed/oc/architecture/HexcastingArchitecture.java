package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.TreeList;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.item.Memory;
import li.cil.oc.api.machine.Architecture;
import li.cil.oc.api.machine.ExecutionResult;
import li.cil.oc.api.machine.Machine;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import li.cil.oc.server.component.Drone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Architecture.Name("Hexcasting")
@Architecture.NoMemoryRequirements
public class HexcastingArchitecture implements Architecture {
    private final Machine machine;
    private CastingVM vm;
    private SpellContinuation continuation;
    private boolean enlightened = false;
    private double ambitRadius = PlayerBasedCastEnv.DEFAULT_AMBIT_RADIUS;
    private double sentinelRadius = PlayerBasedCastEnv.DEFAULT_SENTINEL_RADIUS;
    private FrozenPigment pigment = FrozenPigment.DEFAULT.get();
    private long maxStackSize = 1024;
    private String error;

    private List<AbstractManagedEnvironment> apis = new ArrayList<>();

    public HexcastingArchitecture(Machine machine) {
        this.machine = machine;

        this.apis.add(new OSEnvironment(this.machine));
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

    public boolean isEnlightened() {
        return this.enlightened;
    }

    public double getAmbitRadius() {
        return this.ambitRadius;
    }

    public void setAmbitRadius(double ambitRadius) {
        this.ambitRadius = ambitRadius;
    }

    public double getSentinelRadius() {
        return this.sentinelRadius;
    }

    public void setSentinelRadius(double sentinelRadius) {
        this.sentinelRadius = sentinelRadius;
    }

    public FrozenPigment getPigment() {
        return this.pigment;
    }

    public @Nullable FrozenPigment setPigment(FrozenPigment pigment) {
        this.pigment = pigment;
        return pigment;
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
            for (AbstractManagedEnvironment api : this.apis) {
                this.machine.node().connect(api.node());
            }

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

        for (AbstractManagedEnvironment api : this.apis) {
            this.machine.node().disconnect(api.node());
        }
        this.apis.clear();
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

        for (OperatorSideEffect effect : result.getSideEffects()) {
            if (effect instanceof OperatorSideEffect.DoMishap doMishap && doMishap.getMishap() instanceof Mishap mishap && doMishap.getErrorCtx() instanceof Mishap.Context context && mishap.errorMessageWithName(this.vm.getEnv(), context) instanceof Component component)
                this.error = component.getString();
        }

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
    public void onSignal() {}

    @Override
    public void onConnect() {}

    @Override
    public void loadData(CompoundTag nbt) {
        if (this.vm == null)
            this.vm = CastingVM.empty(new ArchitectureCastEnv(this.machine));

        CastingImage.getCODEC().decode(NbtOps.INSTANCE, nbt.getCompound("image"))
                .ifSuccess(p -> this.vm.setImage(p.getFirst()));
        SpellContinuation.getCODEC().decode(NbtOps.INSTANCE, nbt.getCompound("continuation"))
                .ifSuccess(p -> this.continuation = p.getFirst());
        if (nbt.contains("enlightened"))
            this.enlightened = nbt.getBoolean("enlightened");
        if (nbt.contains("ambitRadius"))
            this.ambitRadius = nbt.getDouble("ambitRadius");
        if (nbt.contains("sentinelRadius"))
            this.sentinelRadius = nbt.getDouble("sentinelRadius");
        FrozenPigment.CODEC.decode(NbtOps.INSTANCE, nbt.getCompound("pigment"))
                .ifSuccess(p -> this.pigment = p.getFirst());
    }

    @Override
    public void saveData(CompoundTag nbt) {
        if (this.vm != null)
            CastingImage.getCODEC().encodeStart(NbtOps.INSTANCE, this.vm.getImage())
                .ifSuccess(t -> nbt.put("image", t));
        if (this.continuation != null)
            SpellContinuation.getCODEC().encodeStart(NbtOps.INSTANCE, this.continuation)
                .ifSuccess(t -> nbt.put("continuation", t));
        nbt.putBoolean("enlightened", this.enlightened);
        nbt.putDouble("ambitRadius", this.ambitRadius);
        nbt.putDouble("sentinelRadius", this.sentinelRadius);
        if (this.pigment != null)
            FrozenPigment.CODEC.encodeStart(NbtOps.INSTANCE, this.pigment)
                    .ifSuccess(t -> nbt.put("pigment", t));
    }
}
