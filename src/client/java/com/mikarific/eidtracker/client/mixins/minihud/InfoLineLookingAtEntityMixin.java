package com.mikarific.eidtracker.client.mixins.minihud;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.minihud.info.InfoLineContext;
import fi.dy.masa.minihud.info.entity.InfoLineLookingAtEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = InfoLineLookingAtEntity.class, remap = false)
public class InfoLineLookingAtEntityMixin {
    @WrapOperation(method = "parse", at = @At(value = "INVOKE", target = "fi/dy/masa/minihud/info/entity/InfoLineLookingAtEntity.qt (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    private String parse(InfoLineLookingAtEntity instance, String str, Object[] args, Operation<String> original, @Local(argsOnly = true) InfoLineContext ctx) {
        if (ctx.ent() instanceof LivingEntity living && str.endsWith(".livingentity")) {
            args[0] = args[0] + "/" + living.getId();
        }
        return original.call(instance, str, args);
    }
}
