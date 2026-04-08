package com.mikarific.eidtracker.client.mixins;

import com.mikarific.eidtracker.client.commands.EIDCommand;
import com.mikarific.eidtracker.client.masa.MasaHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    private static final AtomicInteger CLIENT_ENTITY_COUNTER = new AtomicInteger();

    @Redirect(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/atomic/AtomicInteger;incrementAndGet()I"))
    private int getCurrentId(AtomicInteger SERVER_ENTITY_COUNTER, EntityType<?> entityType, Level level) {
        if(level.isClientSide() && (MasaHelper.getTweak("TWEAK_SINGLEPLAYER_ENTITY_ID_FIX") || EIDCommand.singleplayerEntityIdFix)) {
            return CLIENT_ENTITY_COUNTER.incrementAndGet();
        } else {
            return SERVER_ENTITY_COUNTER.incrementAndGet();
        }
    }
}
