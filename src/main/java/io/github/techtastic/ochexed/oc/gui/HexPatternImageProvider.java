package io.github.techtastic.ochexed.oc.gui;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.client.render.PatternColors;
import at.petrak.hexcasting.client.render.PatternRenderer;
import at.petrak.hexcasting.client.render.PatternSettings;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import li.cil.oc.api.manual.ImageProvider;
import li.cil.oc.api.manual.ImageRenderer;
import li.cil.oc.api.manual.InteractiveImageRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Optional;

public class HexPatternImageProvider implements ImageProvider {
    @Override
    public ImageRenderer getImage(String data) {
        ResourceLocation loc = ResourceLocation.tryParse(data);
        if (loc != null) {
            Optional<Holder.Reference<ActionRegistryEntry>> action = HexActions.REGISTRY.getHolder(loc);
            if (action.isPresent())
                return new HexPatternImageRenderer(loc, action.get());
            throw new RuntimeException("Not a valid HexPattern registry location!");
        }
        throw new RuntimeException("Not a valid ResourceLocation!");
    }

    static class HexPatternImageRenderer implements InteractiveImageRenderer {
        private final ResourceLocation loc;
        private final Boolean readable;

        public HexPatternImageRenderer(ResourceLocation loc, Holder.Reference<ActionRegistryEntry> action) {
            this.loc = loc;
            this.readable = !action.is(HexTags.Actions.PER_WORLD_PATTERN);
        }

        @Override
        public String getTooltip(String tooltip) {
            MutableComponent name = Component.translatable( HexAPI.MOD_ID + ".action." + this.loc);
            return this.readable ? name.getString() : "[REDACTED]";
        }

        @Override
        public boolean onMouseClick(int mouseX, int mouseY) {
            PatchouliAPI.get().openBookEntry(HexAPI.modLoc("thehexbook"), HexAPI.modLoc("basics/media"), 0);
            return false;
        }

        @Override
        public int getWidth() {
            return 48;
        }

        @Override
        public int getHeight() {
            return 48;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY) {
            PatternRenderer.renderPattern(HexActions.REGISTRY.get(this.loc).prototype(), graphics.pose(),
                    new PatternSettings(this.getTooltip(""),
                            new PatternSettings.PositionSettings(
                                this.getWidth(),
                                this.getHeight(),
                                0,
                                0,
                                PatternSettings.AxisAlignment.CENTER_FIT,
                                PatternSettings.AxisAlignment.CENTER_FIT,
                                1,
                                48,
                                48
                            ),
                            PatternSettings.StrokeSettings.fromStroke(5),
                            this.readable ? PatternSettings.ZappySettings.READABLE : PatternSettings.ZappySettings.STATIC
                    ),
                    PatternColors.DEFAULT_GRADIENT_COLOR.withDots(false, true),
                    0, 512);
        }
    }
}
