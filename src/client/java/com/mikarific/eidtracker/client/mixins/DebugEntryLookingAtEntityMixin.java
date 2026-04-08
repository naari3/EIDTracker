package com.mikarific.eidtracker.client.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.debug.DebugEntryLookingAtEntity;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugEntryLookingAtEntity.class)
public class DebugEntryLookingAtEntityMixin {
    @Inject(method = "display", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/debug/DebugScreenDisplayer.addToGroup (Lnet/minecraft/resources/Identifier;Ljava/util/Collection;)V"))
    private void addEntityID(DebugScreenDisplayer displayer, Level level, LevelChunk clientChunk, LevelChunk serverChunk, CallbackInfo ci, @Local Entity entity, @Local List<String> list) {
        if (entity != null) list.add("Entity ID: " + entity.getId());
    }
}
