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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IotaConversion {
    public static Object fromIota(Iota iota) {
        return switch (iota) {
            case NullIota n -> null;
            case BooleanIota b -> b.getBool();
            case DoubleIota d -> d.getDouble();
            case ListIota l -> l.getList().map(IotaConversion::fromIota);
            case ItemStackIota s -> s.getItemStack();
            case Vec3Iota v -> v.getVec3();
            case StringIota s -> s.getString();
            default -> {
                Map<Object, Object> map = new HashMap<>();
                IoticConverter.INSTANCE.convert(iota, map);
                yield map;
            }
        };
    }

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
                    yield IotaType.TYPED_CODEC.decode(NbtOps.INSTANCE, (Tag) v.tag(null, null)[0])
                            .map(Pair::getFirst).result().orElse(new GarbageIota());
                yield new ListIota(List.of(
                        new ListIota(m.keySet().stream().map(IotaConversion::toIota).toList()),
                        new ListIota(m.values().stream().map(IotaConversion::toIota).toList())
                ));
            }
            default -> new GarbageIota();
        };
    }
}
