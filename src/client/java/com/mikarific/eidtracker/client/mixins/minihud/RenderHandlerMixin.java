package com.mikarific.eidtracker.client.mixins.minihud;

import com.mikarific.eidtracker.client.EIDTrackerClient;
import com.mikarific.eidtracker.client.EntityIDSource;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.minihud.Reference;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = RenderHandler.class, remap = false)
public abstract class RenderHandlerMixin {
    @Shadow public abstract void addLine(String text);

    @Inject(method = "addLine(Lfi/dy/masa/minihud/config/InfoToggle;)V", at = @At("TAIL"))
    private void addLine(InfoToggle type, CallbackInfo ci) {
        if (type.name().equals("CURRENT_ENTITY_ID")) {
            var percentageColor = EIDTrackerClient.overflowPercentage > 99.99 ? GuiBase.TXT_RED : (EIDTrackerClient.overflowPercentage > 99.9 ? GuiBase.TXT_YELLOW : GuiBase.TXT_GREEN);
            this.addLine(StringUtils.translate(Reference.MOD_ID + ".info_line.current_entity_id" + (EIDTrackerClient.entityIDSource == EntityIDSource.GUESS ? ".est" : ""), GuiBase.TXT_GREEN, EIDTrackerClient.currentEntityID, GuiBase.TXT_RST, percentageColor, EIDTrackerClient.overflowPercentage, GuiBase.TXT_RST));
        }
    }
}
