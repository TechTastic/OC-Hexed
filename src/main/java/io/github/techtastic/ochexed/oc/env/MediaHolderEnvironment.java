package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.world.item.ItemStack;

public class MediaHolderEnvironment extends AbstractManagedEnvironment implements ADMediaHolder {
    private final EnvironmentHost host;
    private final ADMediaHolder holder;

    public MediaHolderEnvironment(EnvironmentHost host, ItemStack stack, String type) {
        this.host = host;
        this.holder = IXplatAbstractions.INSTANCE.findMediaHolder(stack);

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent(type, Visibility.Neighbors)
                .withConnector().create());
    }

    @Override
    public long getMedia() {
        return this.holder.getMedia();
    }

    @Override
    public long getMaxMedia() {
        return this.holder.getMaxMedia();
    }

    @Override
    public void setMedia(long l) {
        this.holder.setMedia(l);
    }

    @Override
    public boolean canRecharge() {
        return this.holder.canRecharge();
    }

    @Override
    public boolean canProvide() {
        return this.holder.canProvide();
    }

    @Override
    public int getConsumptionPriority() {
        return this.holder.getConsumptionPriority();
    }

    @Override
    public boolean canConstructBattery() {
        return this.holder.canConstructBattery();
    }

    @Override
    public long withdrawMedia(long cost, boolean simulate) {
        return this.holder.withdrawMedia(cost, simulate);
    }

    @Override
    public long insertMedia(long amount, boolean simulate) {
        return this.holder.insertMedia(amount, simulate);
    }
}
