package io.github.techtastic.ochexed.oc.gui;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.client.render.PatternColors;
import at.petrak.hexcasting.client.render.PatternRenderer;
import at.petrak.hexcasting.client.render.PatternSettings;
import at.petrak.hexcasting.client.render.WorldlyPatternRenderHelpers;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import li.cil.oc.api.manual.ImageProvider;
import li.cil.oc.api.manual.ImageRenderer;
import li.cil.oc.api.manual.InteractiveImageRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HexPatternImageProvider implements ImageProvider {
    @Override
    public ImageRenderer getImage(String data) {
        ResourceLocation loc = ResourceLocation.tryParse(data);
        if (loc != null) {
            if (HexActions.REGISTRY.getOptional(loc).isPresent())
                return new HexPatternImageRenderer(loc);
            throw new RuntimeException("Not a valid HexPattern registry location!");
        }
        throw new RuntimeException("Not a valid ResourceLocation!");
    }

    static class HexPatternImageRenderer implements InteractiveImageRenderer {
        private final ResourceLocation loc;

        public HexPatternImageRenderer(ResourceLocation loc) {
            this.loc = loc;
        }

        @Override
        public String getTooltip(String tooltip) {
            return "action." + this.loc.getNamespace() + "." + this.loc.getPath();
        }

        @Override
        public boolean onMouseClick(int mouseX, int mouseY) {
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
                            new PatternSettings.StrokeSettings(1, 1, 2, 1),
                            WorldlyPatternRenderHelpers.READABLE_SCROLL_SETTINGS.zapSets
                    ),
                    PatternColors.DEFAULT_GRADIENT_COLOR.withDots(true, true),
                    0, 512);
        }
    }
}
