package io.github.techtastic.ochexed.init;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import io.github.techtastic.ochexed.casting.actions.component.*;
import io.github.techtastic.ochexed.casting.actions.signal.OpPopSignal;
import io.github.techtastic.ochexed.casting.actions.signal.OpPushSignal;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.techtastic.ochexed.OCHexed.MODID;

public class OCHActions {
    private static final DeferredRegister<ActionRegistryEntry> ACTIONS = DeferredRegister.create(HexActions.REGISTRY, MODID);

    // List Components
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> LIST_COMPONENT = ACTIONS.register("list_component", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqqaawadeeed", HexDir.WEST), new OpListComponents()));
    // Get Component Type
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> GET_COMPONENT_TYPE = ACTIONS.register("get_component_type", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqqaawadeeedewadeeed", HexDir.WEST), new OpGetComponentType()));
    // Find Component
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> FIND_COMPONENT = ACTIONS.register("find_component", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqqaawadeeede", HexDir.WEST), new OpFindComponent()));
    // Get Methods
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> GET_COMPONENT_METHODS = ACTIONS.register("get_component_methods", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqaawqqqae", HexDir.WEST), new OpGetComponentMethods()));
    // Invoke
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> COMPONENT_INVOKE = ACTIONS.register("component_invoke", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqqaawadeadedaed", HexDir.WEST), new OpComponentInvoke()));

    // Pop Signal
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> POP_SIGNAL = ACTIONS.register("pop_signal", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqqaaweaqa", HexDir.WEST), new OpPopSignal()));
    // Push Signal
    public static final DeferredHolder<ActionRegistryEntry, ActionRegistryEntry> PUSH_SIGNAL = ACTIONS.register("push_signal", () ->
            new ActionRegistryEntry(HexPattern.fromAngleString("qqqqaawqded", HexDir.WEST), new OpPushSignal()));

    public static void register(IEventBus bus) {
        ACTIONS.register(bus);
    }
}
