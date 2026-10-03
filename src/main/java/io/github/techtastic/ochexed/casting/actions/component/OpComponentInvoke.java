package io.github.techtastic.ochexed.casting.actions.component;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException;
import at.petrak.hexcasting.api.utils.TreeList;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import io.github.techtastic.ochexed.util.IotaConversion;
import io.github.techtastic.ochexed.util.OperatorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class OpComponentInvoke implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 3;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch) {
            String address = OperatorUtils.getString((List<Iota>) list, 0, this.getArgc());
            String method = OperatorUtils.getString((List<Iota>) list, 1, this.getArgc());
            TreeList<Iota> args = at.petrak.hexcasting.api.casting.OperatorUtils.getList(list, 2, this.getArgc());
            try {
                return Arrays.stream(arch.machine.invoke(address, method, args.map(IotaConversion::fromIota).toArray(new Object[0]))).map(IotaConversion::toIota).toList();
            } catch (Exception e) {
                throw new MishapInternalException(e);
            }
        }
        throw new MishapNotComputer();
    }
}
