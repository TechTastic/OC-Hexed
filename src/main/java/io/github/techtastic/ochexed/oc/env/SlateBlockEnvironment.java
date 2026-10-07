package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate;
import io.github.techtastic.ochexed.util.IotaConversion;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.integration.ManagedBlockEntityEnvironment;

public class SlateBlockEnvironment extends ManagedBlockEntityEnvironment<BlockEntitySlate> {
    public SlateBlockEnvironment(BlockEntitySlate blockEntity) {
        super(blockEntity, "slate");
    }

    @Callback
    public Object[] getPattern(final Context context, final Arguments arguments) {
        return new Object[] { this.blockEntity.pattern instanceof HexPattern pattern ? new PatternIota(pattern) : null };
    }

    @Callback
    public Object[] setPattern(final Context context, final Arguments arguments) {
        if (IotaConversion.toIota(arguments.checkAny(0)) instanceof PatternIota iota) {
            this.blockEntity.pattern = iota.getPattern();
            this.blockEntity.setChanged();
        }
        return new Object[0];
    }
}
