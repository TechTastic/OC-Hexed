package io.github.techtastic.ochexed.casting.iotas;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.techtastic.ochexed.init.OCHIotas;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

/**
 * This iota is temporary until MoreIotas gets ported.
 */
public class StringIota extends Iota {
    public final String value;
    public static final IotaType<StringIota> TYPE = new IotaType<>() {
        public static final MapCodec<StringIota> CODEC;
        public static final StreamCodec<RegistryFriendlyByteBuf, StringIota> STREAM_CODEC;

        @Override
        public MapCodec<StringIota> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StringIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            try {
                return ChatFormatting.GREEN.getColor();
            } catch (NullPointerException ignored) {
                return 0x00FF00;
            }
        }

        static {
            CODEC = Codec.STRING.xmap(StringIota::new, StringIota::getString).fieldOf("value");
            STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(StringIota::new, StringIota::getString).mapStream(b -> b);
        }
    };

    protected StringIota(String value) {
        super(OCHIotas.STRING::get);
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

    @Override
    public boolean isTruthy() {
        return !this.value.isEmpty();
    }

    @Override
    protected boolean toleratesOther(Iota iota) {
        return typesMatch(this, iota) && iota instanceof StringIota that && Objects.equals(that.getString(), this.getString());
    }

    @Override
    public Component display() {
        return Component.translatable(this.value).withColor(TYPE.color());
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
