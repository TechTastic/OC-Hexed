package io.github.techtastic.ochexed;

import io.github.techtastic.ochexed.init.OCHIotas;
import io.github.techtastic.ochexed.oc.architecture.HexcastingArchitecture;
import io.github.techtastic.ochexed.oc.driver.HexItemDriver;
import li.cil.oc.api.API;
import li.cil.oc.api.Driver;
import li.cil.oc.api.FileSystem;
import li.cil.oc.api.Machine;
import li.cil.oc.api.machine.Architecture;
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

        OCHIotas.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            //Driver.add(new MindspliceDriver());
            Driver.add(new HexItemDriver());

            Machine.add(HexcastingArchitecture.class);
        });
    }

    @EventBusSubscriber(value = { Dist.CLIENT })
    static class Client {
        @SubscribeEvent
        private static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork( () -> {
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
