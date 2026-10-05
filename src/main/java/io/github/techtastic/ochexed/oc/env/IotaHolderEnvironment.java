package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class IotaHolderEnvironment extends AbstractManagedEnvironment {
    private final EnvironmentHost host;
    private final ADIotaHolder holder;

    public IotaHolderEnvironment(EnvironmentHost host, ItemStack stack, String type) {
        this.host = host;
        this.holder = IXplatAbstractions.INSTANCE.findDataHolder(stack);

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent(type, Visibility.Neighbors)
                .withConnector().create());
    }

    @Callback
    public Object[] readIota(final Context context, final Arguments args) {
        return new Object[] { this.holder.readIota() };
    }

    @Callback
    public Object[] emptyIota(final Context context, final Arguments args) {
        return new Object[] { this.holder.emptyIota() };
    }

    @Callback
    public Object[] writeIota(final Context context, final Arguments args) {
        if (args.checkAny(0) instanceof Iota iota)
            return new Object[] { this.holder.writeIota(iota, args.checkBoolean(1)) };
        return new Object[] { false };
    }

    @Callback
    public Object[] writeable(final Context context, final Arguments args) {
        return new Object[] { this.holder.writeable() };
    }
}
