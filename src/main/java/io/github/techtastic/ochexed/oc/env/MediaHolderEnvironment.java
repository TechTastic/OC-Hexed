package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.world.item.ItemStack;

public class MediaHolderEnvironment extends AbstractManagedEnvironment {
    private final EnvironmentHost host;
    private final ADMediaHolder holder;

    public MediaHolderEnvironment(EnvironmentHost host, ItemStack stack, String type) {
        this.host = host;
        this.holder = IXplatAbstractions.INSTANCE.findMediaHolder(stack);

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent(type, Visibility.Neighbors)
                .withConnector().create());
    }

    @Callback
    public Object[] getMedia(final Context context, final Arguments args) {
        return new Object[] { this.holder.getMedia() };
    }

    @Callback
    public Object[] getMaxMedia(final Context context, final Arguments args) {
        return new Object[] { this.holder.getMaxMedia() };
    }

    public void setMedia(long l) {
        this.holder.setMedia(l);
    }

    @Callback
    public Object[] canRecharge(final Context context, final Arguments args) {
        return new Object[] { this.holder.canRecharge() };
    }

    @Callback
    public Object[] canProvide(final Context context, final Arguments args) {
        return new Object[] { this.holder.canProvide() };
    }

    @Callback
    public Object[] getConsumptionPriority(final Context context, final Arguments args) {
        return new Object[] { this.holder.getConsumptionPriority() };
    }

    @Callback
    public Object[] canConstructBattery() {
        return new Object[] { this.holder.canConstructBattery() };
    }

    public long withdrawMedia(long cost, boolean simulate) {
        return this.holder.withdrawMedia(cost, simulate);
    }

    public long insertMedia(long amount, boolean simulate) {
        return this.holder.insertMedia(amount, simulate);
    }
}
