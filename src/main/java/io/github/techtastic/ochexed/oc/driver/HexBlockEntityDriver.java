package io.github.techtastic.ochexed.oc.driver;

import at.petrak.hexcasting.api.block.HexBlockEntity;
import li.cil.oc.api.prefab.DriverSidedBlockEntity;

public abstract class HexBlockEntityDriver<T extends HexBlockEntity> extends DriverSidedBlockEntity {
    private final Class<T> blockEntityClass;

    public HexBlockEntityDriver(Class<T> blockEntityClass) {
        this.blockEntityClass = blockEntityClass;
    }

    @Override
    public Class<?> getBlockEntityClass() {
        return this.blockEntityClass;
    }
}
