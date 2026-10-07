package io.github.techtastic.ochexed.oc.driver;

import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import at.petrak.hexcasting.common.lib.HexBlockEntities;
import io.github.techtastic.ochexed.oc.env.AkashicBookshelfEnvironment;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class AkashicBookShelfDriver extends HexBlockEntityDriver<BlockEntityAkashicBookshelf> {
    public AkashicBookShelfDriver() {
        super(BlockEntityAkashicBookshelf.class);
    }

    @Override
    public ManagedEnvironment createEnvironment(Level world, BlockPos pos, Direction side) {
        if (world == null || world.isClientSide || pos == null || !(world.getBlockEntity(pos, HexBlockEntities.AKASHIC_BOOKSHELF_TILE.get()).orElse(null) instanceof BlockEntityAkashicBookshelf akashic))
            return null;
        return new AkashicBookshelfEnvironment(akashic);
    }
}
