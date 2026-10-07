package io.github.techtastic.ochexed.oc.convert;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import li.cil.oc.api.driver.Converter;
import net.minecraft.nbt.NbtOps;

import java.util.Map;

public class IoticConverter implements Converter {
    public static final IoticConverter INSTANCE = new IoticConverter();

    @Override
    public void convert(Object value, Map<Object, Object> output) {
        if (value instanceof Iota iota)
            IotaType.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, iota).ifSuccess(nbt -> output.put("iota", new NBTValue(nbt)));
    }
}
