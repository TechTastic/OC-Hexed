package io.github.techtastic.ochexed.util;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import io.github.techtastic.ochexed.casting.iotas.StringIota;

import java.util.List;

public class OperatorUtils {
    public static String getString(List<Iota> args, int idx, int argc) {
        if (idx >= args.size()) throw new MishapNotEnoughArgs(idx + 1, args.size());
        Iota x = args.get(idx);
        if (x instanceof StringIota s)
            return s.getString();
        throw MishapInvalidIota.ofType(x, argc == 0 ? idx : argc - (idx - 1), "string");
    }
}