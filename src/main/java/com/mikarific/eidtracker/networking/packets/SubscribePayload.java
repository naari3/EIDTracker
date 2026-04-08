package com.mikarific.eidtracker.networking.packets;

import com.mikarific.eidtracker.EIDTracker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record SubscribePayload(boolean subscription) implements CustomPacketPayload {
    public static final Identifier SUBSCRIBE_PAYLOAD_ID = Identifier.fromNamespaceAndPath(EIDTracker.MOD_ID, "subscribe");
    public static final CustomPacketPayload.Type<SubscribePayload> TYPE = new CustomPacketPayload.Type<>(SUBSCRIBE_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SubscribePayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SubscribePayload::subscription, SubscribePayload::new);

    @Override public @NonNull Type<SubscribePayload> type() {
        return TYPE;
    }
}