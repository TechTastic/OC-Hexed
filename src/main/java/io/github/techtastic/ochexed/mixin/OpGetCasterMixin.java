package io.github.techtastic.ochexed.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetCaster;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import li.cil.oc.api.internal.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OpGetCaster.class)
public class OpGetCasterMixin {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private void executeAsComputer(List<? extends Iota> args, CastingEnvironment ctx, CallbackInfoReturnable<List<Iota>> cir) {
        if (ctx instanceof ArchitectureCastEnv ace) {
            // Check for Entity iota for Drone handling
            // Check for BlockEntity position iota for Computer and Robot handling
            // Check ItemStack(?) for Tablet handling

            switch (ace.machine.host()) {
                case Case computer -> cir.setReturnValue(List.of(new Vec3Iota(new Vec3(computer.xPosition(), computer.yPosition(), computer.zPosition()))));
                case Tablet tablet -> cir.setReturnValue(List.of(new EntityIota(tablet.player())));
                case Drone drone -> cir.setReturnValue(List.of(new EntityIota(drone.ownerUUID(), Component.literal(drone.name()), false)));
                case Agent agent -> cir.setReturnValue(List.of(new EntityIota(agent.player())));
                default -> {}
            }
        }
    }
}
