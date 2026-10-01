package io.github.techtastic.ochexed.util;

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.TreeList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;

public class NBTUtils {
    public static CastingImage fromNBT(CompoundTag nbt) {
        CompoundTag image = nbt.getCompound("image");
        return new CastingImage(
                fromNBT(image, "stack"),
                image.getInt("parenCount"),
                fromNBTParenthesised(image),
                image.getBoolean("escapeNext"),
                image.getBoolean("simulateNext"),
                image.getLong("opsConsumed"),
                image.getCompound("userData")
        );
    }

    public static TreeList<Iota> fromNBT(CompoundTag nbt, String name) {
        TreeList<Iota> iotas = TreeList.empty();
        ListTag stack = nbt.getList(name, ListTag.TAG_COMPOUND);
        for (int i = 0; i < stack.size(); i++) {
            CompoundTag p = stack.getCompound(i);
            int finalI = i;
            IotaType.TYPED_CODEC.decode(NbtOps.INSTANCE, p)
                    .ifSuccess(pair -> iotas.set(finalI, pair.getFirst()))
                    .ifError(_e -> iotas.set(finalI, new GarbageIota()));
        }
        return iotas;
    }

    public static TreeList<CastingImage.ParenthesizedIota> fromNBTParenthesised(CompoundTag nbt) {
        TreeList<CastingImage.ParenthesizedIota> parenthesised = TreeList.empty();
        ListTag stack = nbt.getList("parenthesised", ListTag.TAG_COMPOUND);
        for (int i = 0; i < stack.size(); i++) {
            CompoundTag p = stack.getCompound(i);
            int finalI = i;
            IotaType.TYPED_CODEC.decode(NbtOps.INSTANCE, p.getCompound("iota"))
                    .ifSuccess(pair -> parenthesised.set(finalI, new CastingImage.ParenthesizedIota(pair.getFirst(), p.getBoolean("escaped"))))
                    .ifError(_e -> parenthesised.set(finalI, new CastingImage.ParenthesizedIota(new GarbageIota(), p.getBoolean("escaped"))));
        }

        return parenthesised;
    }

    public static void toNBT(CastingImage image, CompoundTag nbt) {
        CompoundTag castingImage = new CompoundTag();
        nbt.put("image", castingImage);

        TreeList<Iota> stack = image.getStack();
        toNBT(stack, castingImage, "stack");

        castingImage.putInt("parenCount", image.getParenCount());

        TreeList<CastingImage.ParenthesizedIota> parenthesized = image.getParenthesized();
        toNBT(parenthesized, castingImage);

        castingImage.putBoolean("escapeNext", image.getEscapeNext());
        castingImage.putBoolean("simulateNext", image.getSimulateNext());
        castingImage.putLong("opsConsumed", image.getOpsConsumed());

        castingImage.put("userData", image.getUserData());
    }

    public static void toNBT(TreeList<Iota> iotas, CompoundTag nbt, String name) {
        ListTag stack = new ListTag();
        nbt.put(name, stack);
        for (Iota iota : iotas) {
            IotaType.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, iota)
                    .ifSuccess(stack::add)
                    .ifError(_e -> stack.add(IotaType.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, new GarbageIota()).getOrThrow()));
        }
    }

    public static void toNBT(TreeList<CastingImage.ParenthesizedIota> iotas, CompoundTag nbt) {
        ListTag stack = new ListTag();
        nbt.put("parenthesised", stack);
        for (CastingImage.ParenthesizedIota iota : iotas) {
            CompoundTag parenthesised = new CompoundTag();
            IotaType.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, iota.getIota())
                    .ifSuccess(t -> parenthesised.put("iota", t))
                    .ifError(_e -> parenthesised.put("iota", IotaType.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, new GarbageIota()).getOrThrow()));
            parenthesised.putBoolean("escaped", iota.getEscaped());
            stack.add(parenthesised);
        }
    }
}
