package io.github.techtastic.ochexed.util;

import at.petrak.hexcasting.api.casting.iota.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import ram.talia.moreiotas.api.casting.iota.ItemStackIota;
import ram.talia.moreiotas.api.casting.iota.StringIota;

import java.util.Arrays;
import java.util.List;

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
            default -> new GarbageIota();
        };
    }

    public static Object fromIota(Iota iota) {
        return switch (iota) {
            case DoubleIota d -> d.getDouble();
            case BooleanIota b -> b.getBool();
            case StringIota s -> s.getString();
            case Vec3Iota v -> v.getVec3();
            case ItemStackIota s -> s.getItemStack();
            case ListIota l -> l.getList().map(IotaConversion::fromIota);
            default -> null;
        };
    }
}
