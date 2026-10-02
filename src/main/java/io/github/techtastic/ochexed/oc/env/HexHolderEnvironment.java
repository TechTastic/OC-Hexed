package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HexHolderEnvironment extends AbstractManagedEnvironment implements ADHexHolder {
    private final EnvironmentHost host;
    private final ADHexHolder holder;

    public HexHolderEnvironment(EnvironmentHost host, ItemStack stack, String type) {
        this.host = host;
        this.holder = IXplatAbstractions.INSTANCE.findHexHolder(stack);

        this.setNode(Network.newNode(this, Visibility.Neighbors)
                .withComponent(type, Visibility.Neighbors)
                .withConnector().create());
    }

    @Override
    public boolean canDrawMediaFromInventory() {
        return this.holder.canDrawMediaFromInventory();
    }

    @Override
    public boolean hasHex() {
        return this.holder.hasHex();
    }

    @Override
    public @Nullable List<Iota> getHex(ServerLevel serverLevel) {
        return this.holder.getHex(serverLevel);
    }

    @Override
    public void writeHex(List<Iota> list, @Nullable FrozenPigment frozenPigment, long l) {
        this.holder.writeHex(list, frozenPigment, l);
    }

    @Override
    public void clearHex() {
        this.holder.clearHex();
    }

    @Override
    public @Nullable FrozenPigment getPigment() {
        return this.holder.getPigment();
    }
}
