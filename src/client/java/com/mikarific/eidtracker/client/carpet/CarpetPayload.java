package com.mikarific.eidtracker.client.carpet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record CarpetPayload(CompoundTag data) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, CarpetPayload> STREAM_CODEC = CustomPacketPayload.codec(CarpetPayload::write, CarpetPayload::new);
    public static final Type<CarpetPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("carpet", "hello"));

    public CarpetPayload(FriendlyByteBuf input) {
        this(input.readNbt());
    }

    public void write(FriendlyByteBuf output) {
        output.writeNbt(data);
    }

    @Override public @NonNull Type<CarpetPayload> type() {
        return TYPE;
    }
}