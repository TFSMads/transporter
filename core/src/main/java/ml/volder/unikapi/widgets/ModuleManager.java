package ml.volder.unikapi.widgets;

import ml.volder.unikapi.types.Material;

import java.util.function.Function;

public interface ModuleManager {
    Object registerCategory(String displayName, Material icon, String description);

    default Object registerCategory(String displayName, Material icon) {
        return registerCategory(displayName, icon, "");
    }

    default Object registerCategory(String displayName, String description) {
        return registerCategory(displayName, Material.PAPER, description);
    }

    default Object registerCategory(String displayName) {
        return registerCategory(displayName, Material.PAPER, "");
    }

    void registerModule(String key, String defaultPrefix, boolean defaultIsEnabled, Object category, Material icon, Function<String, String> getDisplayValue);

    void openEditor();

}
