package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import li.cil.oc.api.internal.Agent;
import li.cil.oc.api.machine.Machine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ArchitectureCastEnv extends CastingEnvironment {
    public final Machine machine;

    protected ArchitectureCastEnv(Machine machine) {
        super((ServerLevel) machine.host().getEnvironmentLevel());

        this.machine = machine;
    }

    @Override
    public @Nullable LivingEntity getCastingEntity() {
        return this.machine.host() instanceof Agent agent ? agent.player() : null;
    }

    @Override
    public MishapEnvironment getMishapEnvironment() {
        return new ArchitectureMishapEnv(this.machine);
    }

    @Override
    public Vec3 mishapSprayPos() {
        return new Vec3(this.machine.host().xPosition(), this.machine.host().yPosition(), this.machine.host().zPosition());
    }

    @Override
    protected long extractMediaEnvironment(long l, boolean b) {
        return 0;
    }

    @Override
    protected boolean isVecInRangeEnvironment(Vec3 vec3) {
        return false;
    }

    @Override
    protected boolean hasEditPermissionsAtEnvironment(BlockPos blockPos) {
        return false;
    }

    @Override
    public InteractionHand getCastingHand() {
        return InteractionHand.MAIN_HAND;
    }

    @Override
    public List<ItemStack> getUsableStacks(StackDiscoveryMode stackDiscoveryMode) {
        return List.of();
    }

    @Override
    public List<HeldItemInfo> getPrimaryStacks() {
        return List.of();
    }

    @Override
    public boolean replaceItem(Predicate<ItemStack> predicate, ItemStack itemStack, @Nullable InteractionHand interactionHand) {
        return false;
    }

    @Override
    public FrozenPigment getPigment() {
        return FrozenPigment.DEFAULT.get();
    }

    @Override
    public @Nullable FrozenPigment setPigment(@Nullable FrozenPigment frozenPigment) {
        return null;
    }

    @Override
    public void produceParticles(ParticleSpray particleSpray, FrozenPigment frozenPigment) {

    }

    @Override
    public void printMessage(Component component) {
        System.out.println(component.getString());
    }
}
