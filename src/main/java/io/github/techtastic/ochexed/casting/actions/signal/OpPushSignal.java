package io.github.techtastic.ochexed.casting.actions.signal;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.utils.TreeList;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import io.github.techtastic.ochexed.util.IotaConversion;
import io.github.techtastic.ochexed.util.OperatorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class OpPushSignal implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 2;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch) {
            String name = OperatorUtils.getString((List<Iota>) list, 0, this.getArgc());
            TreeList<Iota> args = at.petrak.hexcasting.api.casting.OperatorUtils.getList(list, 2, this.getArgc());
            return List.of(new BooleanIota(arch.machine.signal(name, args.map(IotaConversion::fromIota).toArray(new Object[0]))));
        }
        throw new MishapNotComputer();
    }
}
