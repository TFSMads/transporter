package ml.volder.unikapi.widgets;

import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.types.Material;
import net.labymod.api.client.component.Component;

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

    /***
     * Register a module through the labymod api
     * @param key the key of the module
     * @param defaultPrefix the default prefix of the module
     * @param defaultIsEnabled the default state of the module
     * @param category the category of the module
     * @param icon the icon of the module
     * @param getDisplayValue the function that returns the display value of the module
     */
    public static void registerModule(String key, String defaultPrefix, boolean defaultIsEnabled, Object category, Material icon, Function<String, Component> getDisplayValue) {
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
