package io.github.techtastic.ochexed.casting.actions.component;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import io.github.techtastic.ochexed.casting.mishap.MishapNotComputer;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.api.OperatorUtilsKt;
import ram.talia.moreiotas.api.casting.iota.StringIota;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OpFindComponent implements ConstMediaAction {
    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment instanceof ArchitectureCastEnv arch) {
            String type = OperatorUtilsKt.getString(list, 0, this.getArgc());
            List<Iota> components = new ArrayList<>();
            for (Map.Entry<String, String> comp : arch.machine.components().entrySet()) {
                if (Objects.equals(comp.getValue(), type))
                    components.add(StringIota.make(comp.getKey()));
            }
            return List.of(new ListIota(components));
        }
        throw new MishapNotComputer();
    }
}
