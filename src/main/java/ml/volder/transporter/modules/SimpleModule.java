package ml.volder.transporter.modules;

import com.google.gson.JsonObject;
import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;

import javax.annotation.Nullable;
import java.io.File;

public abstract class SimpleModule {
    private String moduleName;

    private DataManager<Data> dataManager;

    public SimpleModule(String moduleName) {
        this.moduleName = moduleName;
        initDataManager();
        loadConfig();
    }

    public String getModuleName() {
        return moduleName;
    }

    private void initDataManager() {
        this.dataManager = new DataManager<Data>(new File(TransporterAddon.getInstance().getCommonDataFolder(), moduleName + ".json"), Data.class);
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

    protected abstract void loadConfig();
}
