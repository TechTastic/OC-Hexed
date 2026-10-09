package io.github.techtastic.ochexed.oc.driver;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import io.github.techtastic.ochexed.oc.env.ImpetusEnvironment;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AbstractImpetusDriver extends HexBlockEntityDriver<BlockEntityAbstractImpetus> {
    public AbstractImpetusDriver() {
        super(BlockEntityAbstractImpetus.class);
    }

    @Override
    public ManagedEnvironment createEnvironment(Level world, BlockPos pos, Direction side) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BlockEntityAbstractImpetus impetus)
            return new ImpetusEnvironment<>(impetus);
        return null;
    }
}
