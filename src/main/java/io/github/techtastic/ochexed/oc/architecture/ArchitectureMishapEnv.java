package io.github.techtastic.ochexed.oc.architecture;

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import li.cil.oc.api.machine.Machine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ArchitectureMishapEnv extends MishapEnvironment {
    private final Machine machine;

    protected ArchitectureMishapEnv(Machine machine) {
        super((ServerLevel) machine.host().getEnvironmentLevel(), null);
        this.machine = machine;
    }

    @Override
    protected void yeetItem(ItemStack stack, Vec3 srcPos, Vec3 delta) {}

    @Override
    public void yeetHeldItemsTowards(Vec3 vec3) {}

    @Override
    public void dropHeldItems() {}

    @Override
    public void drown() {}

    @Override
    public void damage(float v) {}

    @Override
    public void removeXp(int i) {}

    @Override
    public void blind(int i) {}

    @Override
    public void nauseate(int i) {}
}
