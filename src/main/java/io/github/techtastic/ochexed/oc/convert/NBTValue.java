package io.github.techtastic.ochexed.oc.convert;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.prefab.AbstractValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class NBTValue extends AbstractValue {
    private Tag tag;

    public NBTValue(Tag tag) {
        this.tag = tag;
    }

    @Callback(getter = true)
    public Object[] tag(final Context context, final Arguments arguments) {
        return new Object[] { this.tag };
    }

    @Override
    public void saveData(MutableDataComponentHolder holder, @NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveData(holder, nbt, provider);
        nbt.put("tag", this.tag);
    }

    @Override
    public void loadData(DataComponentHolder holder, @NonNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadData(holder, nbt, provider);
        this.tag = nbt.get("tag");
    }
}
