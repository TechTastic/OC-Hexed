package io.github.techtastic.ochexed.oc.driver;

import at.petrak.hexcasting.api.item.HexHolderItem;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.item.MediaHolderItem;
import at.petrak.hexcasting.common.items.storage.ItemSlate;
import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import io.github.techtastic.ochexed.oc.env.HexHolderEnvironment;
import io.github.techtastic.ochexed.oc.env.IotaHolderEnvironment;
import io.github.techtastic.ochexed.oc.env.MediaHolderEnvironment;
import io.github.techtastic.ochexed.oc.env.SpellbookEnvironment;
import li.cil.oc.common.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class HexItemDriver extends DriverItem {
    public HexItemDriver() {
        super(
                BuiltInRegistries.ITEM.stream().filter(i ->
                        i instanceof IotaHolderItem || i instanceof HexHolderItem || i instanceof MediaHolderItem
                ).map(ItemStack::new).toList().toArray(new ItemStack[0])
        );
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        if ((host.getEnvironmentLevel() != null && host.getEnvironmentLevel().isClientSide))
            return null;
        String type = stack.getItem().getDescription().getString().toLowerCase();
        return switch (stack.getItem()) {
            case ItemSpellbook s -> new SpellbookEnvironment(host, stack);
            case ItemSlate s -> null;
            case IotaHolderItem i -> new IotaHolderEnvironment(host, stack, type);
            case HexHolderItem h -> new HexHolderEnvironment(host, stack, type);
            case MediaHolderItem m -> new MediaHolderEnvironment(host, stack, type);
            default -> null;
        };
    }

    @Override
    public String slot(ItemStack stack) {
        return switch (stack.getItem()) {
            case ItemSpellbook s -> Slot.HDD();
            case IotaHolderItem i -> Slot.EEPROM();
            case HexHolderItem h -> Slot.Floppy();
            case MediaHolderItem m -> Slot.Container();
            default -> "";
        };
    }
}
