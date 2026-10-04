package io.github.techtastic.ochexed.oc.gui;

import li.cil.oc.api.manual.ImageProvider;
import li.cil.oc.api.manual.ImageRenderer;
import li.cil.oc.api.manual.InteractiveImageRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatchouliImageProvider implements ImageProvider {
    private static final Pattern PATTERN = Pattern.compile("^([a-z0-9_.-]+:[a-z0-9_./-]+):([a-z0-9_.-]+:[a-z0-9_./-]+):(\\d+)$");

    @Override
    public ImageRenderer getImage(String data) {
        Matcher matcher = PATTERN.matcher(data);
        if (matcher.matches())
            if (ResourceLocation.tryParse(matcher.group(1)) instanceof ResourceLocation bookLoc && BookRegistry.INSTANCE.books.get(bookLoc) instanceof Book book)
                if (ResourceLocation.tryParse(matcher.group(2)) instanceof ResourceLocation entryLoc && book.getContents().entries.get(entryLoc) instanceof BookEntry entry)
                    if (entry.getPages().get(Integer.parseInt(matcher.group(3))) instanceof BookPage page) {
                        PatchouliAPI.get().openBookEntry(bookLoc, entryLoc, Integer.parseInt(matcher.group(3)));
                        return null;
                    } else
                        throw new RuntimeException("Failed to parse page #" + matcher.group(3) + " of \"" + matcher.group(2) + "\" entry of \"" + bookLoc + "\" book!");
                else
                    throw new RuntimeException("Failed to parse ResourceLocation for \"" + matcher.group(2) + "\" entry of \"" + bookLoc + "\" book!");
            else
                throw new RuntimeException("Failed to parse ResourceLocation for \"" + matcher.group(1) + "\" book!");
        throw new RuntimeException("Failed to parse Patchouli link: " + data);
    }

    static class PatchouliImageRenderer implements InteractiveImageRenderer {
        private final BookPage page;

        public PatchouliImageRenderer(BookPage page) {
            this.page = page;
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
            this.page.render(graphics, mouseX, mouseY, 1);
        }

        @Override
        public String getTooltip(String tooltip) {
            return "";
        }

        @Override
        public boolean onMouseClick(int mouseX, int mouseY) {
            return this.page.mouseClicked(mouseX, mouseY, 0);
        }
    }
}
