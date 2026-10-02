package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class IotaHolderEnvironment extends AbstractManagedEnvironment implements ADIotaHolder {
    private final EnvironmentHost host;
    private final ADIotaHolder holder;

    public IotaHolderEnvironment(EnvironmentHost host, ItemStack stack, String type) {
        this.host = host;
        this.holder = IXplatAbstractions.INSTANCE.findDataHolder(stack);

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent(type, Visibility.Neighbors)
                .withConnector().create());
    }

    @Override
    public @Nullable Iota readIota() {
        return this.holder.readIota();
    }

    @Override
    public @Nullable Iota emptyIota() {
        return this.holder.emptyIota();
    }

    @Override
    public boolean writeIota(@Nullable Iota iota, boolean b) {
        return this.holder.writeIota(iota, b);
    }

    @Override
    public boolean writeable() {
        return this.holder.writeable();
    }
}
