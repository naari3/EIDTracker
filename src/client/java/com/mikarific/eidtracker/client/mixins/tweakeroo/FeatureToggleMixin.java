package com.mikarific.eidtracker.client.mixins.tweakeroo;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Pseudo
@Mixin(value = FeatureToggle.class, remap = false)
public class FeatureToggleMixin {
    @Shadow @Final @Mutable private static FeatureToggle[] $VALUES;

    @Invoker("<init>")
    static FeatureToggle invokeInit(String enumName, int enumOrdinal, String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lfi/dy/masa/tweakeroo/config/FeatureToggle;$VALUES:[Lfi/dy/masa/tweakeroo/config/FeatureToggle;", shift = At.Shift.AFTER))
    private static void addCustomInfo(CallbackInfo ci) {
        List<FeatureToggle> featureToggles = new ArrayList<>(Arrays.asList($VALUES));

        featureToggles.add(invokeInit(
                "TWEAK_SINGLEPLAYER_ENTITY_ID_FIX",
                $VALUES.length,
                "tweakSingleplayerEntityIdFix",
                false,
                true,
                ""
        ));

        $VALUES = featureToggles.toArray(new FeatureToggle[0]);
    }
}
