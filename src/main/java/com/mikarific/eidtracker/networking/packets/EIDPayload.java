package com.mikarific.eidtracker.networking.packets;

import com.mikarific.eidtracker.EIDTracker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record EIDPayload(int currentEntityId) implements CustomPacketPayload {
    public static final Identifier EID_PAYLOAD_ID = Identifier.fromNamespaceAndPath(EIDTracker.MOD_ID, "eid");
    public static final CustomPacketPayload.Type<EIDPayload> TYPE = new CustomPacketPayload.Type<>(EID_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, EIDPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, EIDPayload::currentEntityId, EIDPayload::new);

    @Override public @NonNull Type<EIDPayload> type() {
        return TYPE;
    }
}