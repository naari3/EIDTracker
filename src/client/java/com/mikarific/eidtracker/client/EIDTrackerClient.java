package com.mikarific.eidtracker.client;

import com.mikarific.eidtracker.EIDTracker;
import com.mikarific.eidtracker.client.carpet.CarpetHandler;
import com.mikarific.eidtracker.client.commands.EIDCommand;
import com.mikarific.eidtracker.client.debug.DebugEntryCurrentEntityID;
import com.mikarific.eidtracker.client.masa.MasaHelper;
import com.mikarific.eidtracker.client.networking.NetworkingHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.resources.Identifier;

public class EIDTrackerClient implements ClientModInitializer {
    public static EntityIDSource entityIDSource = EntityIDSource.GUESS;
    public static Integer currentEntityID = null;
    public static Double overflowPercentage = 0.0;

    public static void setCurrentEntityID(int value) {
        currentEntityID = value;
        overflowPercentage = 100.0 * ((1L << 32) - (currentEntityID > 0 ? ((1L << 32) - currentEntityID) : -1L * currentEntityID)) / (1L << 32);
    }

    @Override
    public void onInitializeClient() {
        MasaHelper.loadConfigs();
        DebugScreenEntries.register(Identifier.fromNamespaceAndPath(EIDTracker.MOD_ID, "current_entity_id"), new DebugEntryCurrentEntityID());
        CarpetHandler.register();
        NetworkingHandler.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            EIDCommand.register(dispatcher, environment);
        });
    }
}
