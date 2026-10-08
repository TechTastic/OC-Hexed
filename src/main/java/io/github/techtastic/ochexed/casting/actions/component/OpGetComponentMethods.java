package io.github.techtastic.ochexed.casting.actions.component;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import io.github.techtastic.ochexed.casting.mishap.MishapInvalidComponent;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import li.cil.oc.api.network.Node;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.api.OperatorUtilsKt;
import ram.talia.moreiotas.api.casting.iota.StringIota;

import java.util.List;
import java.util.Objects;

public class OpGetComponentMethods implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch) {
            String address = OperatorUtilsKt.getString(list, 0, this.getArgc());
            for (Node node : arch.machine.node().reachableNodes()) {
                if (Objects.equals(node.address(), address))
                    return List.of(new ListIota(arch.machine.methods(node.host())
                            .keySet().stream().map(s -> (Iota) StringIota.make(s)).toList()));
            }
            throw new MishapInvalidComponent(address);
        }
        throw new MishapNotComputer();
    }
}
