package io.github.techtastic.ochexed.casting.actions.signal;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import io.github.techtastic.ochexed.util.IotaConversion;
import li.cil.oc.api.machine.Signal;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.api.casting.iota.StringIota;

import java.util.Arrays;
import java.util.List;

public class OpPopSignal implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch) {
            Signal signal = arch.machine.popSignal();
            if (signal == null)
                return List.of(new NullIota());
            List<Iota> args = Arrays.stream(signal.args()).map(IotaConversion::toIota).toList();
            args.addFirst(StringIota.make(signal.name()));
            return args;
        }
        throw new MishapNotComputer();
    }
}
