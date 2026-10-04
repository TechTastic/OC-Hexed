package io.github.techtastic.ochexed;

import at.petrak.hexcasting.common.lib.HexItems;
import io.github.techtastic.ochexed.init.OCHActions;
import io.github.techtastic.ochexed.oc.architecture.HexcastingArchitecture;
import io.github.techtastic.ochexed.oc.driver.HexItemDriver;
import io.github.techtastic.ochexed.oc.gui.HexPatternImageProvider;
import li.cil.oc.api.*;
import li.cil.oc.api.prefab.ResourceContentProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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

        OCHActions.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Driver.add(new HexItemDriver());

            Machine.add(HexcastingArchitecture.class);
        });
    }

    @EventBusSubscriber(value = { Dist.CLIENT })
    static class Client {
        @SubscribeEvent
        private static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork( () -> {
                Manual.addProvider("hexpattern", new HexPatternImageProvider());

                Manual.addProvider(new ResourceContentProvider(MODID, "doc/"));
                Manual.addTab(graphics -> graphics.renderFakeItem(new ItemStack(HexItems.FOCUS.get()), 0, 0), "tab.ochexed.manual", "ochexed/%LANGUAGE%/index.md");

            /*
            li.cil.oc.api.Items.registerStack(Items.SABLE_UPGRADE.toStack(), "", Constants.SectionName$.MODULE$.Component());

            Manual.addProvider(new PathProvider() {
                @Override
                public String pathFor(ItemStack stack) {
                    if (stack.is(Items.SABLE_UPGRADE.asItem()))
                        return "ocsable";
                    return null;
                }

                @Override
                public String pathFor(Level world, BlockPos pos) {
                    return null;
                }
            });

            Manual.addProvider(new ResourceContentProvider(MODID, "doc/"));

            Manual.addTab(graphics -> graphics.renderFakeItem(Items.SABLE_UPGRADE.toStack(), 0, 0), "tab.ocsable.manual", "ocsable/%LANGUAGE%/index.md");
            */
            });
        }
    }
}
