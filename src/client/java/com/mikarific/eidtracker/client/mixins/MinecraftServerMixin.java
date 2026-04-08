package com.mikarific.eidtracker.client.mixins;

import com.mikarific.eidtracker.client.EIDTrackerClient;
import com.mikarific.eidtracker.client.EntityIDSource;
import com.mikarific.eidtracker.mixins.EntityAccessor;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract boolean isDedicatedServer();

    @Inject(at = @At("RETURN"), method = "tickServer")
    private void onEndTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (EIDTrackerClient.currentEntityID != null) {
            if (!this.isDedicatedServer()) {
                EIDTrackerClient.entityIDSource = EntityIDSource.EIDTRACKER;
                int newCurrentEntityID = EntityAccessor.getCurrentId().get();
                if (EIDTrackerClient.currentEntityID != newCurrentEntityID) EIDTrackerClient.setCurrentEntityID(newCurrentEntityID);
            }
        }
    }
}
