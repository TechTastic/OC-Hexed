package io.github.techtastic.ochexed;

import at.petrak.hexcasting.common.lib.HexItems;
import io.github.techtastic.ochexed.datagen.OCHHexActionTagProvider;
import io.github.techtastic.ochexed.init.OCHActions;
import io.github.techtastic.ochexed.oc.architecture.HexcastingArchitecture;
import io.github.techtastic.ochexed.oc.convert.IoticConverter;
import io.github.techtastic.ochexed.oc.driver.HexItemDriver;
import io.github.techtastic.ochexed.oc.gui.HexPatternImageProvider;
import io.github.techtastic.ochexed.oc.gui.PatchouliImageProvider;
import li.cil.oc.api.*;
import li.cil.oc.api.prefab.ResourceContentProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import java.io.FileNotFoundException;

@Mod(OCHexed.MODID)
public class OCHexed {
    public static final String MODID = "ochexed";
    public static final Logger LOGGER = LogUtils.getLogger();

    public OCHexed(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::datagen);

        OCHActions.register(modEventBus);
    }

    private void datagen(GatherDataEvent event) {
        event.addProvider(new OCHHexActionTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Driver.add(new HexItemDriver());
            Driver.add(IoticConverter.INSTANCE);

            Machine.add(HexcastingArchitecture.class);
        });
    }

    @EventBusSubscriber(value = { Dist.CLIENT })
    static class Client {
        @SubscribeEvent
        private static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork( () -> {
                Manual.addProvider("hexpattern", new HexPatternImageProvider());
                Manual.addProvider("patchouli", new PatchouliImageProvider());

                Manual.addProvider(new ResourceContentProvider(MODID, "doc/"));
                Manual.addTab(graphics -> graphics.renderFakeItem(new ItemStack(HexItems.FOCUS.get()), 0, 0), "tab.ochexed.manual", "ochexed/%LANGUAGE%/index.md");
            });
        }
    }
}
