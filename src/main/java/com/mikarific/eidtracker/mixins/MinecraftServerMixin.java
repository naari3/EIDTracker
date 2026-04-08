package com.mikarific.eidtracker.mixins;

import com.mikarific.eidtracker.commands.EIDCommand;
import com.mikarific.eidtracker.networking.packets.EIDPayload;
import com.mikarific.eidtracker.tracker.TrackerManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract boolean isDedicatedServer();
    @Shadow private PlayerList playerList;
    @Shadow private int tickCount;

    @Unique private int currentEntityID = 0;

    @Inject(at = @At("RETURN"), method = "tickServer")
    private void onEndTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (this.isDedicatedServer()) {
            for (UUID uuid : EIDCommand.subscribers) {
                ServerPlayer player = this.playerList.getPlayer(uuid);
                if (player != null) {
                    int newCurrentEntityID = EntityAccessor.getCurrentId().get();
                    if (currentEntityID != newCurrentEntityID) {
                        currentEntityID = newCurrentEntityID;
                        ServerPlayNetworking.send(player, new EIDPayload(newCurrentEntityID));
                    }
                }
            }
        }
        TrackerManager.updateTracker(this.tickCount);
    }
}
