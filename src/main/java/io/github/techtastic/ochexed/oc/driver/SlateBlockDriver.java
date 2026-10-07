package io.github.techtastic.ochexed.oc.driver;

import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate;
import at.petrak.hexcasting.common.lib.HexBlockEntities;
import io.github.techtastic.ochexed.oc.env.SlateBlockEnvironment;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class SlateBlockDriver extends HexBlockEntityDriver<BlockEntitySlate> {
    public SlateBlockDriver() {
        super(BlockEntitySlate.class);
    }

    @Override
    public ManagedEnvironment createEnvironment(Level world, BlockPos pos, Direction side) {
        if (world == null || world.isClientSide || pos == null || !(world.getBlockEntity(pos, HexBlockEntities.SLATE_TILE.get()).orElse(null) instanceof BlockEntitySlate slate))
            return null;
        return new SlateBlockEnvironment(slate);
    }
}
