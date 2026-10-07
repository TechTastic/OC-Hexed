package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNoAkashicRecord;
import at.petrak.hexcasting.common.blocks.akashic.AkashicFloodfiller;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import at.petrak.hexcasting.common.lib.HexBlockEntities;
import io.github.techtastic.ochexed.util.IotaConversion;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.integration.ManagedBlockEntityEnvironment;
import net.minecraft.core.BlockPos;

import java.util.Optional;

public class AkashicBookshelfEnvironment extends ManagedBlockEntityEnvironment<BlockEntityAkashicBookshelf> {
    public AkashicBookshelfEnvironment(BlockEntityAkashicBookshelf blockEntity) {
        super(blockEntity, "akashic");
    }

    @Callback
    public Object[] getIota(final Context context, final Arguments arguments) {
        if (IotaConversion.toIota(arguments.checkAny(0)) instanceof PatternIota pattern) {
            BlockPos position = AkashicFloodfiller.floodFillFor(this.blockEntity.getBlockPos(), this.blockEntity.getLevel(), (pos, state, level) -> {
                Optional<BlockEntityAkashicBookshelf> be = level.getBlockEntity(pos, HexBlockEntities.AKASHIC_BOOKSHELF_TILE.get());
                return be.isPresent() && be.get().getPattern() == pattern.getPattern();
            });
            if (position != null) {
                Optional<BlockEntityAkashicBookshelf> be = this.blockEntity.getLevel().getBlockEntity(position, HexBlockEntities.AKASHIC_BOOKSHELF_TILE.get());
                if (be.isPresent())
                    return new Object[] { be.get().getIota() };
                throw new MishapNoAkashicRecord(position);
            }
        }
        return new Object[0];
    }

    @Callback
    public Object[] setIota(final Context context, final Arguments arguments) {
        if (IotaConversion.toIota(arguments.checkAny(0)) instanceof Iota iota)
            this.blockEntity.setNewMapping(this.blockEntity.getPattern(), iota);
        return new Object[0];
    }

    @Callback
    public Object[] setPattern(final Context context, final Arguments arguments) {
        if (IotaConversion.toIota(arguments.checkAny(0)) instanceof PatternIota iota)
            this.blockEntity.setNewMapping(iota.getPattern(), this.blockEntity.getIota());
        return new Object[0];
    }
}
