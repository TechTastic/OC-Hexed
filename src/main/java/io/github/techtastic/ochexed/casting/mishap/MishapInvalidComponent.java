package io.github.techtastic.ochexed.casting.mishap;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.TreeList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MishapInvalidComponent extends Mishap {
    public final String address;

    public MishapInvalidComponent(String address) {
        this.address = address;
    }

    @Override
    public @NotNull FrozenPigment accentColor(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        return castingEnvironment.getPigment();
    }

    @Override
    public @NotNull TreeList<Iota> execute(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context, @NotNull TreeList<Iota> treeList) {
        return TreeList.empty();
    }

    @Override
    protected @Nullable Component errorMessage(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        return Component.translatable("ochexed.mishap.component.invalid", this.address);
    }
}
