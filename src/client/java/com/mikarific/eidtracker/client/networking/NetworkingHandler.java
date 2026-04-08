package com.mikarific.eidtracker.client.networking;

import com.mikarific.eidtracker.EIDTracker;
import com.mikarific.eidtracker.client.EIDTrackerClient;
import com.mikarific.eidtracker.client.EntityIDSource;
import com.mikarific.eidtracker.networking.packets.EIDPayload;
import com.mikarific.eidtracker.networking.packets.HelloPayload;
import com.mikarific.eidtracker.networking.packets.SubscribePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public class NetworkingHandler {
    public static void init() {
        Optional<ModContainer> optionalMod = FabricLoader.getInstance().getModContainer(EIDTracker.MOD_ID);
        ModContainer mod = optionalMod.orElse(null);

        ClientPlayNetworking.registerGlobalReceiver(HelloPayload.TYPE, (payload, context) -> {
            if (mod == null) return;
            context.responseSender().sendPacket(new HelloPayload(mod.getMetadata().getVersion().getFriendlyString()));
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            EIDTrackerClient.entityIDSource = EntityIDSource.GUESS;
            EIDTrackerClient.currentEntityID = null;
        });

        ClientPlayNetworking.registerGlobalReceiver(EIDPayload.TYPE, (payload, context) -> {
            EIDTrackerClient.entityIDSource = EntityIDSource.EIDTRACKER;
            EIDTrackerClient.setCurrentEntityID(payload.currentEntityId());
        });

        ClientPlayNetworking.registerGlobalReceiver(SubscribePayload.TYPE, (payload, context) -> {
            if (!payload.subscription()) EIDTrackerClient.entityIDSource = EntityIDSource.GUESS;
            if (payload.subscription()) EIDTrackerClient.entityIDSource = EntityIDSource.EIDTRACKER;
        });
    }
}
