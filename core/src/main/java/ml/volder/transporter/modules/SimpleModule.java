package ml.volder.transporter.modules;

import com.google.gson.JsonObject;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.widgets.TransporterModuleWidget;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.guisystem.ModTextures;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public abstract class SimpleModule {
    private String moduleName;
    private String displayName;
    private String moduleDescription;

    protected boolean isFeatureActive;
    private DataManager<Data> dataManager;

    public SimpleModule(ModuleManager.ModuleInfo moduleInfo) {
        this.moduleName = moduleInfo.name;
        this.displayName = moduleInfo.displayName;
        this.moduleDescription = moduleInfo.description;
        initDataManager();
    }

    public abstract SimpleModule init();
    public abstract SimpleModule enable();
    public abstract void fillSettings(SettingRegistryAccessor subSettings);

    public void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public TransporterModuleWidget getModuleWidget() {
        return new TransporterModuleWidget(
                moduleName,
                displayName,
                moduleDescription,
                ModTextures.MISC_HEAD_QUESTION,
                isActive -> {
                    isFeatureActive = isActive;
                    setConfigEntry("isFeatureActive", isFeatureActive);
                },
                isFeatureActive
        );
    }


    //DATA STUFF:

    private void initDataManager() {
        this.dataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), moduleName + ".json"));
    }

    private JsonObject getJsonObject() {
        return dataManager.getSettings().getData();
    }

    private void saveConfig() {
        dataManager.save();
    }

    public boolean hasConfigEntry(String jsonPath) {
        return getJsonObject().has(jsonPath);
    }

    @Nullable
    public <T> T getConfigEntry(String jsonPath, Class<T> expectedReturnType) {
        return dataManager.getSettings().getEntry(jsonPath, expectedReturnType);
    }

    public void setConfigEntry(String jsonPath, String string) {
        getJsonObject().addProperty(jsonPath, string);
        saveConfig();
    }

    public void setConfigEntry(String jsonPath, Number number) {
        getJsonObject().addProperty(jsonPath, number);
        saveConfig();
    }

    public void setConfigEntry(String jsonPath, Boolean bool) {
        getJsonObject().addProperty(jsonPath, bool);
        saveConfig();
    }

    public void setConfigEntry(String jsonPath, Character character) {
        getJsonObject().addProperty(jsonPath, character);
        saveConfig();
    }

    public DataManager<Data> getDataManager() {
        return dataManager;
    }
}
