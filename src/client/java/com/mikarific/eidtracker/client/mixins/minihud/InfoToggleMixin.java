package com.mikarific.eidtracker.client.mixins.minihud;

import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.info.InfoLineType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Pseudo
@Mixin(value = InfoToggle.class, remap = false)
public abstract class InfoToggleMixin {
    @Shadow @Final @Mutable private static InfoToggle[] $VALUES;

    @Invoker("<init>")
    static InfoToggle invokeInit(String enumName, int enumOrdinal, String name, InfoLineType<?> type, boolean defaultValue, String defaultHotkey) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lfi/dy/masa/minihud/config/InfoToggle;$VALUES:[Lfi/dy/masa/minihud/config/InfoToggle;", shift = At.Shift.AFTER))
    private static void addCustomInfo(CallbackInfo ci) {
        List<InfoToggle> infoToggles = new ArrayList<>(Arrays.asList($VALUES));

        infoToggles.add(invokeInit(
                "CURRENT_ENTITY_ID",
                $VALUES.length,
                "infoCurrentEntityID",
                null,
                false,
                ""
        ));

        $VALUES = infoToggles.toArray(new InfoToggle[0]);
    }
}
