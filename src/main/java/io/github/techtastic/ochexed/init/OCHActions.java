package io.github.techtastic.ochexed.init;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.techtastic.ochexed.OCHexed.MODID;

public class OCHActions {
    private static final DeferredRegister<ActionRegistryEntry> ACTIONS = DeferredRegister.create(HexActions.REGISTRY, MODID);

    // List Components
    // Get Component Type
    // Find Component
    // Get Methods
    // Invoke

    // Has Signal
    // Pop Signal
    // Push Signal

    public static void register(IEventBus bus) {
        ACTIONS.register(bus);
    }
}
