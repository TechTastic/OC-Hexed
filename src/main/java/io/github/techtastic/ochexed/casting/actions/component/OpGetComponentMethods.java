package io.github.techtastic.ochexed.casting.actions.component;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import io.github.techtastic.ochexed.casting.iotas.StringIota;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import io.github.techtastic.ochexed.util.OperatorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpGetComponentMethods implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch)
            return List.of(new ListIota(arch.machine.methods(OperatorUtils.getString((List<Iota>) list, 0, this.getArgc()))
                    .keySet().stream().map(s -> (Iota) new StringIota(s)).toList()));
        throw new MishapNotComputer();
    }
}
