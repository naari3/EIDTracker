package com.mikarific.eidtracker.client.masa;

import net.fabricmc.loader.api.FabricLoader;

public class MasaHelper {
    private static void invokeIfPresent(String modId, String className, String method) {
        if (!FabricLoader.getInstance().isModLoaded(modId)) return;

        try {
            Class.forName(className).getMethod(method).invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getTweak(String tweak) {
        if (!FabricLoader.getInstance().isModLoaded("tweakeroo")) return false;

        try {
            Class<?> clazz = Class.forName("fi.dy.masa.tweakeroo.config.FeatureToggle");
            Object toggle = Enum.valueOf(clazz.asSubclass(Enum.class), tweak);
            return (boolean) clazz.getMethod("getBooleanValue").invoke(toggle);
        } catch (Exception e) {
            return false;
        }
    }

    public static void loadConfigs() {
        invokeIfPresent("minihud", "fi.dy.masa.minihud.config.Configs", "loadFromFile");
        invokeIfPresent("tweakeroo", "fi.dy.masa.tweakeroo.config.Configs", "loadFromFile");
    }
}
