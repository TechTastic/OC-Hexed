package io.github.techtastic.ochexed.oc.architecture;

import li.cil.oc.api.machine.Machine;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.world.phys.Vec3;

public class OSEnvironment extends AbstractManagedEnvironment {
    private Machine machine;

    public OSEnvironment(Machine machine) {
        this.machine = machine;

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent("os", Visibility.Neighbors)
                .withConnector().create());
    }

    public HexcastingArchitecture getArchitecture() {
        return (HexcastingArchitecture) this.machine.architecture();
    }

    public Vec3 getPosition() {
        return new Vec3(this.machine.host().xPosition(), this.machine.host().yPosition(), this.machine.host().zPosition());
    }

    @Callback
    public Object[] getAmbitRadius(final Context context, final Arguments arguments) {
        return new Object[] { getArchitecture().getAmbitRadius() };
    }

    @Callback
    public Object[] getSentinelRadius(final Context context, final Arguments arguments) {
        return new Object[] { getArchitecture().getSentinelRadius() };
    }

    @Callback
    public Object[] getPigmentColor(final Context context, final Arguments arguments) {
        return new Object[] { getArchitecture().getPigment().getColorProvider().getColor(this.machine.host().getEnvironmentLevel().getTimeOfDay(1), getPosition()) };
    }

    @Callback
    public Object[] getMaxStackSize(final Context context, final Arguments arguments) {
        return new Object[] { getArchitecture().getMaxStackSize() };
    }

    @Callback
    public Object[] isEnlightened(final Context context, final Arguments arguments) {
        return new Object[] { getArchitecture().isEnlightened() };
    }
}
