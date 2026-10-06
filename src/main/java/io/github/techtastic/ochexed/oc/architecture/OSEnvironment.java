package io.github.techtastic.ochexed.oc.architecture;

import li.cil.oc.api.component.RackMountable;
import li.cil.oc.api.internal.*;
import li.cil.oc.api.machine.*;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.EnvironmentHost;
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

    @Callback
    public Object[] isDrone(final Context context, final Arguments arguments) {
        return new Object[] { this.machine.host() instanceof Drone };
    }

    @Callback
    public Object[] isRobot(final Context context, final Arguments arguments) {
        return new Object[] { this.machine.host() instanceof Robot };
    }

    @Callback
    public Object[] isCase(final Context context, final Arguments arguments) {
        return new Object[] { this.machine.host() instanceof Case };
    }

    @Callback
    public Object[] isServer(final Context context, final Arguments arguments) {
        return new Object[] { this.machine.host() instanceof Server };
    }

    @Callback
    public Object[] isMicrocontroller(final Context context, final Arguments arguments) {
        return new Object[] { this.machine.host() instanceof Microcontroller };
    }

    @Callback
    public Object[] isTablet(final Context context, final Arguments arguments) {
        return new Object[] { this.machine.host() instanceof Tablet };
    }
}
