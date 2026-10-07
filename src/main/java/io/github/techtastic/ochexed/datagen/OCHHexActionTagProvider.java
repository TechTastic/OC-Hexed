package io.github.techtastic.ochexed.datagen;

import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.datagen.tag.HexActionTagProvider;
import io.github.techtastic.ochexed.init.OCHTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class OCHHexActionTagProvider extends HexActionTagProvider {
    public OCHHexActionTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(OCHTags.Actions.DENY_COMPUTER)
                .add(HexActions.ADD_MOTION.getKey())
                .add(HexActions.BLINK.getKey())
                .add(HexActions.ALTIORA.getKey())
                .add(HexActions.BONEMEAL.getKey())
                .add(HexActions.BRAINSWEEP.getKey())
                .add(HexActions.BREAK_BLOCK.getKey())
                .add(HexActions.CONJURE_LIGHT.getKey())
                .add(HexActions.CONJURE_BLOCK.getKey())
                .add(HexActions.CONSTRUCT.getKey())
                .add(HexActions.CONSTRUCT_VEC.getKey())
                .add(HexActions.CRAFT$ARTIFACT.getKey())
                .add(HexActions.CRAFT$BATTERY.getKey())
                .add(HexActions.CRAFT$CYPHER.getKey())
                .add(HexActions.CRAFT$TRINKET.getKey())
                .add(HexActions.CREATE_LAVA.getKey())
                .add(HexActions.CREATE_WATER.getKey())
                .add(HexActions.DECONSTRUCT.getKey())
                .add(HexActions.DECONSTRUCT_VEC.getKey())
                .add(HexActions.DESTROY_WATER.getKey())
                .add(HexActions.DISPEL_RAIN.getKey())
                .add(HexActions.EDIFY.getKey())
                .add(HexActions.EXPLODE.getKey())
                .add(HexActions.EXPLODE$FIRE.getKey())
                .add(HexActions.EXTINGUISH.getKey())
                .add(HexActions.FLIGHT$CAN_FLY.getKey())
                .add(HexActions.FLIGHT$RANGE.getKey())
                .add(HexActions.FLIGHT$TIME.getKey())
                .add(HexActions.IGNITE.getKey())
                .add(HexActions.LIGHTNING.getKey())
                .add(HexActions.PLACE_BLOCK.getKey())
                .add(HexActions.POTION$ABSORPTION.getKey())
                .add(HexActions.POTION$HASTE.getKey())
                .add(HexActions.POTION$LEVITATION.getKey())
                .add(HexActions.POTION$NIGHT_VISION.getKey())
                .add(HexActions.POTION$POISON.getKey())
                .add(HexActions.POTION$REGENERATION.getKey())
                .add(HexActions.POTION$SLOWNESS.getKey())
                .add(HexActions.POTION$STRENGTH.getKey())
                .add(HexActions.POTION$WEAKNESS.getKey())
                .add(HexActions.POTION$WITHER.getKey())
                .add(HexActions.SUMMON_RAIN.getKey())
                .add(HexActions.TELEPORT.getKey());
    }
}
