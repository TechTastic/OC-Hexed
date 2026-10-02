package io.github.techtastic.ochexed.oc.driver;

import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
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
        return null;
    }
}
