package io.github.techtastic.ochexed.init;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import static io.github.techtastic.ochexed.OCHexed.MODID;

public class OCHTags {
    public static class Actions {
        public static final TagKey<ActionRegistryEntry> DENY_COMPUTER =
                TagKey.create(IXplatAbstractions.INSTANCE.getActionRegistry().key(), ResourceLocation.fromNamespaceAndPath(MODID, "deny_computer"));
    }
}
