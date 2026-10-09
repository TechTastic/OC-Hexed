package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.network.chat.Component;

public class ImpetusEnvironment<T extends BlockEntityAbstractImpetus> extends AbstractManagedEnvironment {
    protected final T impetus;

    public ImpetusEnvironment(T impetus) {
        this.impetus = impetus;
    }

    @Callback
    public Object[] getMedia(final Context context, final Arguments arguments) {
        return new Object[] { this.impetus.getMedia() };
    }

    @Callback
    public Object[] getRemainingMediaCapacity(final Context context, final Arguments arguments) {
        return new Object[] { this.impetus.remainingMediaCapacity() };
    }

    @Callback
    public Object[] getDisplayMessage(final Context context, final Arguments arguments) {
        return new Object[] { this.impetus.getDisplayMsg() instanceof Component comp ? comp.getString() : null };
    }

    @Callback
    public Object[] getStartDirection(final Context context, final Arguments arguments) {
        return new Object[] { this.impetus.getStartDirection() };
    }

    @Callback
    public Object[] startExecution(final Context context, final Arguments arguments) {
        this.impetus.startExecution(null);
        return new Object[0];
    }

    @Callback
    public Object[] endExecution(final Context context, final Arguments arguments) {
        this.impetus.endExecution();
        return new Object[0];
    }
}
