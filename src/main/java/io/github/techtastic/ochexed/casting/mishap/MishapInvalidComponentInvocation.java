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

public class MishapInvalidComponentInvocation extends Mishap {
    public final String address;
    public final String method;
    public final Object[] args;

    public MishapInvalidComponentInvocation(String address, String method, Object[] args) {
        this.address = address;
        this.method = method;
        this.args = args;
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
        return Component.translatable("ochexed.mishap.component.invoke.invalid", this.method, this.address, List.of(this.args));
    }
}
