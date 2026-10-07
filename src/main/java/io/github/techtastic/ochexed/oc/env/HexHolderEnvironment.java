package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.item.HexHolderItem;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.github.techtastic.ochexed.util.IotaConversion;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import li.cil.oc.internal.scalalib.util.control.TailCalls;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import ram.talia.moreiotas.api.casting.iota.ItemStackIota;

import java.util.List;

public class HexHolderEnvironment extends AbstractManagedEnvironment {
    protected final EnvironmentHost host;
    protected final ItemStack stack;
    private final ADHexHolder holder;

    public HexHolderEnvironment(EnvironmentHost host, ItemStack stack, String type) {
        this.host = host;
        this.stack = stack;
        this.holder = IXplatAbstractions.INSTANCE.findHexHolder(stack);

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent(type, Visibility.Neighbors)
                .withConnector().create());
    }

    @Callback
    public Object[] canDrawMediaFromInventory(final Context context, final Arguments args) {
        return new Object[] { this.holder.canDrawMediaFromInventory() };
    }

    @Callback
    public Object[] hasHex(final Context context, final Arguments args) {
        return new Object[] { this.holder.hasHex() };
    }

    @Callback
    public Object[] getHex(final Context context, final Arguments args) {
        return new Object[] {this.holder.getHex((ServerLevel) this.host.getEnvironmentLevel())};
    }

    @Callback
    public Object[] writeHex(final Context context, final Arguments args) {
        if (IotaConversion.toIota(args.checkAny(0)) instanceof ListIota list)
            this.holder.writeHex(list.getList(), this.holder.getPigment(), this.holder instanceof HexHolderItem item ? item.getMedia(this.stack) : 0 );
        return new Object[0];
    }

    @Callback
    public Object[] clearHex(final Context context, final Arguments args) {
        this.holder.clearHex();
        return new Object[0];
    }
}
