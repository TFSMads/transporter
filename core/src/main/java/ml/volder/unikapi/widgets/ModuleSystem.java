package ml.volder.unikapi.widgets;

import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.types.Material;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ModuleSystem {

    private static ModuleManager moduleManager;

    public static BooleanSupplier shouldRenderPredicate = () -> true;

    public static boolean shouldRenderModules() {
        if(MinecraftAPI.getAPI().isF3MenuOpen())
            return false;
        return shouldRenderPredicate.getAsBoolean();
    }

    public static Object registerCategory(String displayName, Material icon, String description) {
        return getModuleManager().registerCategory(displayName, icon, description);
    }

    public static Object registerCategory(String displayName, Material icon) {
        return registerCategory(displayName, icon, "");
    }

    public static Object registerCategory(String displayName, String description) {
        return registerCategory(displayName, Material.PAPER, description);
    }

    public static Object registerCategory(String displayName) {
        return registerCategory(displayName, Material.PAPER, "");
    }

    public static void registerModule(String key, String defaultPrefix, boolean defaultIsEnabled, Object category, Material icon, Function<String, String> getDisplayValue) {
        getModuleManager().registerModule(key, defaultPrefix, defaultIsEnabled, category, icon, getDisplayValue);
    }

    public static void openEditor() {
        getModuleManager().openEditor();
    }


    public static void setModuleManager(ModuleManager moduleManager) {
        ModuleSystem.moduleManager = moduleManager;
    }

    public static ModuleManager getModuleManager() {
        if(moduleManager == null)
            moduleManager = new DefaultModuleManager();
        return moduleManager;
    }
}
