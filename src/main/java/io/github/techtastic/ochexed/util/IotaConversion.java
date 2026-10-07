package io.github.techtastic.ochexed.util;

import at.petrak.hexcasting.api.casting.iota.*;
import com.mojang.datafixers.util.Pair;
import io.github.techtastic.ochexed.oc.convert.IoticConverter;
import io.github.techtastic.ochexed.oc.convert.NBTValue;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import ram.talia.moreiotas.api.casting.iota.ItemStackIota;
import ram.talia.moreiotas.api.casting.iota.StringIota;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IotaConversion {
    public static Iota toIota(Object obj) {
        if (obj == null) return new NullIota();
        if (obj instanceof Iota i) return i;
        return switch (obj) {
            case Number n -> new DoubleIota(n.doubleValue());
            case Boolean b -> new BooleanIota(b);
            case String s -> StringIota.make(s);
            case Vec3 v -> new Vec3Iota(v);
            case ItemStack s -> ItemStackIota.createFiltered(s);
            case Object[] a -> new ListIota(Arrays.stream(a).map(IotaConversion::toIota).toList());
            case List<?> l -> new ListIota(l.stream().map(IotaConversion::toIota).toList());
            case Map<?,?> m -> {
                if (m.get("iota") instanceof NBTValue v)
                    yield IotaType.TYPED_CODEC.decode(NbtOps.INSTANCE, (Tag) v.tag(null, null)[0]).map(Pair::getFirst).result().orElse(new GarbageIota());
                yield new GarbageIota();
            }
            default -> new GarbageIota();
        };
    }
}
