package io.github.techtastic.ochexed.oc.env;

import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import net.minecraft.world.item.ItemStack;

public class SpellbookEnvironment extends HexHolderEnvironment {
    public SpellbookEnvironment(EnvironmentHost host, ItemStack stack) {
        super(host, stack, "spellbook");
    }

    @Callback
    public Object[] turnPage(final Context context, final Arguments args) {
        ItemSpellbook.rotatePageIdx(this.stack, args.checkBoolean(0), this.host.getEnvironmentLevel());
        return new Object[0];
    }

    @Callback
    public Object[] getCurrentPage(final Context context, final Arguments args) {
        return new Object[] { ItemSpellbook.getPage(this.stack, 0) };
    }

    @Callback
    public Object[] getHighestPage(final Context context, final Arguments args) {
        return new Object[] { ItemSpellbook.highestPage(this.stack) };
    }

    @Callback
    public Object[] isSealed(final Context context, final Arguments args) {
        return new Object[] { ItemSpellbook.isSealed(this.stack) };
    }

    @Callback
    public Object[] setSealed(final Context context, final Arguments args) {
        ItemSpellbook.setSealed(this.stack, args.checkBoolean(0));
        return new Object[0];
    }
}
