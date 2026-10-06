package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import li.cil.oc.api.internal.Agent;
import li.cil.oc.api.internal.Tablet;
import li.cil.oc.api.machine.Machine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ArchitectureMishapEnv extends MishapEnvironment {
    private final Machine machine;

    protected ArchitectureMishapEnv(Machine machine) {
        super((ServerLevel) machine.host().getEnvironmentLevel(), machine.host() instanceof Agent agent ? (ServerPlayer) agent.player() : machine.host() instanceof Tablet tablet ? (ServerPlayer) tablet.player() : null);
        this.machine = machine;
    }

    @Override
    public void yeetHeldItemsTowards(Vec3 vec3) {
        if (this.caster != null) {
            var pos = this.caster.position();
            var delta = vec3.subtract(pos).normalize().scale(0.5);

            for (var hand : InteractionHand.values()) {
                var stack = this.caster.getItemInHand(hand);
                this.caster.setItemInHand(hand, ItemStack.EMPTY);
                this.yeetItem(stack, pos, delta);
            }
        }
    }

    @Override
    public void dropHeldItems() {
        if (this.caster != null) {
            var delta = this.caster.getLookAngle();
            this.yeetHeldItemsTowards(this.caster.position().add(delta));
        }
    }

    @Override
    public void drown() {
        if (this.caster != null) {
            if (this.caster.getAirSupply() < 200) {
                this.caster.hurt(this.caster.damageSources().drown(), 2f);
            }
            this.caster.setAirSupply(0);
        }
    }

    @Override
    public void damage(float v) {
        this.machine.stop();
    }

    @Override
    public void removeXp(int i) {
        if (this.caster != null)
            this.caster.giveExperiencePoints(-i);
    }

    @Override
    public void blind(int i) {
        if (this.caster != null)
            this.caster.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, i));
    }

    @Override
    public void nauseate(int i) {
        if (this.caster != null)
            this.caster.addEffect(new MobEffectInstance(MobEffects.CONFUSION, i));
    }
}
