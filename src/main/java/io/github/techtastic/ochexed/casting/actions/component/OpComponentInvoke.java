package io.github.techtastic.ochexed.casting.actions.component;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException;
import at.petrak.hexcasting.api.utils.TreeList;
import io.github.techtastic.ochexed.casting.mishap.MishapInvalidComponent;
import io.github.techtastic.ochexed.casting.mishap.MishapInvalidComponentInvocation;
import io.github.techtastic.ochexed.casting.mishap.MishapInvalidComponentMethod;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import io.github.techtastic.ochexed.util.IotaConversion;
import li.cil.oc.api.network.Node;
import li.cil.oc.server.driver.Registry;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.api.OperatorUtilsKt;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OpComponentInvoke implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 3;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch) {
            String address = OperatorUtilsKt.getString(list, 0, this.getArgc());
            String method = OperatorUtilsKt.getString(list, 1, this.getArgc());
            Object[] args = at.petrak.hexcasting.api.casting.OperatorUtils.getList(list, 2, this.getArgc())
                    .map(IotaConversion::fromIota).toArray(new Object[0]);

            for (Node node : arch.machine.node().reachableNodes()) {
                if (Objects.equals(node.address(), address)) {
                    if (arch.machine.methods(node.host()).containsKey(method))
                        try {
                            Object[] results = arch.machine.invoke(address, method, args);
                            return Arrays.stream(results)
                                    .map(IotaConversion::toIota).toList();
                        } catch (Exception e) {
                            throw new MishapInvalidComponentInvocation(address, method, args);
                        }
                    throw new MishapInvalidComponentMethod(address, method);
                }
            }
            throw new MishapInvalidComponent(address);
        }
        throw new MishapNotComputer();
    }
}