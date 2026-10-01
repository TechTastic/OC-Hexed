package io.github.techtastic.ochexed.init;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import io.github.techtastic.ochexed.casting.iotas.StringIota;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.techtastic.ochexed.OCHexed.MODID;

public class OCHIotas {
    private static final DeferredRegister<IotaType<?>> IOTAS = DeferredRegister.create(HexIotaTypes.REGISTRY, MODID);

    /**
     * This iota is temporary until MoreIotas gets ported.
     */
    public static final DeferredHolder<IotaType<?>, IotaType<StringIota>> STRING = IOTAS.register("string", () -> StringIota.TYPE);

    public static void register(IEventBus bus) {
        IOTAS.register(bus);
    }
}
