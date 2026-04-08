package com.mikarific.eidtracker.networking.packets;

import com.mikarific.eidtracker.EIDTracker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record HelloPayload(String version) implements CustomPacketPayload {
    public static final Identifier HELLO_PAYLOAD_ID = Identifier.fromNamespaceAndPath(EIDTracker.MOD_ID, "hello");
    public static final CustomPacketPayload.Type<HelloPayload> TYPE = new CustomPacketPayload.Type<>(HELLO_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, HelloPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, HelloPayload::version, HelloPayload::new);

    @Override
    public @NonNull Type<HelloPayload> type() { return TYPE; }
}
