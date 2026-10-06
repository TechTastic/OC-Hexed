package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.api.utils.MediaHelper;
import li.cil.oc.api.internal.Agent;
import li.cil.oc.api.internal.Tablet;
import li.cil.oc.api.machine.Machine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        return this.machine.host() instanceof Agent agent ? agent.player() : this.machine.host() instanceof Tablet tablet ? tablet.player() : null;
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
    protected long extractMediaEnvironment(long costLeft, boolean simulate) {
        if (this.getCastingEntity() instanceof ServerPlayer player) {
            List<ADMediaHolder> sources = MediaHelper.scanPlayerForMediaStuff(player);

            for (var source : sources) {
                var found = MediaHelper.extractMedia(source, costLeft, false, simulate);
                costLeft -= found;
                if (costLeft <= 0)
                    break;
            }
        }

        return costLeft;
    }

    @Override
    protected boolean isVecInRangeEnvironment(Vec3 vec3) {
        HexcastingArchitecture architecture = (HexcastingArchitecture) this.machine.architecture();
        double ambitRadius = architecture.getAmbitRadius();
        if (this.getCastingEntity() instanceof ServerPlayer player) {
            double sentinelRadius = architecture.getSentinelRadius();
            Sentinel sentinel = HexAPI.instance().getSentinel(player);
            if (sentinel != null && sentinel.extendsRange() && player.level().dimension() == sentinel.dimension()
                    && vec3.distanceToSqr(sentinel.position()) <= sentinelRadius * sentinelRadius + 0.00000000001)
                return true;
        }
        return vec3.distanceToSqr(new Vec3(this.machine.host().xPosition(), this.machine.host().yPosition(), this.machine.host().zPosition())) <= ambitRadius * ambitRadius + 0.00000000001;
    }

    @Override
    protected boolean hasEditPermissionsAtEnvironment(BlockPos blockPos) {
        if (this.getCastingEntity() instanceof ServerPlayer player)
            return player.gameMode.getGameModeForPlayer() != GameType.ADVENTURE && this.world.mayInteract(player, blockPos);
        return false;
    }

    @Override
    public InteractionHand getCastingHand() {
        return this.getCastingEntity() instanceof ServerPlayer player ? player.getUsedItemHand() : InteractionHand.MAIN_HAND;
    }

    @Override
    public List<ItemStack> getUsableStacks(StackDiscoveryMode stackDiscoveryMode) {
        if (this.getCastingEntity() instanceof ServerPlayer player)
            return getUsableStacksForPlayer(stackDiscoveryMode, this.getCastingHand(), player);
        return List.of();
    }

    @Override
    public List<HeldItemInfo> getPrimaryStacks() {
        if (this.machine.host() instanceof Agent agent) {
            List<HeldItemInfo> info = new ArrayList<>();
            for (int slot = 0; slot < agent.mainInventory().getContainerSize(); slot++) {
                info.add(new HeldItemInfo(agent.mainInventory().getItem(slot), slot == agent.selectedSlot() ? InteractionHand.MAIN_HAND : null));
            }
            return info;
        } else if (this.machine.host() instanceof Tablet tablet) {
            return getPrimaryStacksForPlayer(tablet.player().getUsedItemHand(), (ServerPlayer) tablet.player());
        } else if (this.getCastingEntity() instanceof ServerPlayer player)
            return getPrimaryStacksForPlayer(player.getUsedItemHand(), player);
        return List.of();
    }

    @Override
    public boolean replaceItem(Predicate<ItemStack> predicate, ItemStack itemStack, @Nullable InteractionHand interactionHand) {
        if (this.getCastingEntity() instanceof ServerPlayer player)
            return replaceItemForPlayer(predicate, itemStack, interactionHand, player);
        return false;
    }

    @Override
    public FrozenPigment getPigment() {
        return ((HexcastingArchitecture) this.machine.architecture()).getPigment();
    }

    @Override
    public @Nullable FrozenPigment setPigment(@Nullable FrozenPigment frozenPigment) {
        return ((HexcastingArchitecture) this.machine.architecture()).setPigment(frozenPigment);
    }

    @Override
    public void produceParticles(ParticleSpray particleSpray, FrozenPigment frozenPigment) {}

    @Override
    public void printMessage(Component component) {
        this.machine.signal("print", component.getString());
    }

    @Override
    public boolean isEnlightened() {
        return ((HexcastingArchitecture) this.machine.architecture()).isEnlightened();
    }
}
