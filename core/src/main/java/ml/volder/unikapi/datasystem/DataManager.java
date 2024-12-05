package ml.volder.unikapi.datasystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.utils.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DataManager<T> {
    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final File file;
    private final Class<? extends T> configDefaults;
    private T settings;

    protected DataManager(File file, Class<? extends T> configDefaults) {
        if(file == null || configDefaults == null)
            throw new NullPointerException();
        this.file = file;
        this.configDefaults = configDefaults;
        this.loadConfig(false);
    }

    private void loadConfig(boolean reload) {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdir();
        }

        boolean createdNewFile = false;
        if (reload && this.file.exists()) {
            createdNewFile = true;
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                createdNewFile = true;
            } catch (IOException e) {
                UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            }
        }

        FileInputStream stream = null;

        try {
            stream = new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
        }

        try {
            this.settings = GSON.fromJson(createdNewFile ? GSON.toJson(this.configDefaults.newInstance()) : IOUtils.toString(stream, StandardCharsets.UTF_8), this.configDefaults);
            if (!reload && this.settings == null) {
                this.loadConfig(true);
            } else if (this.settings != null) {
                this.save();
            }
        } catch (Exception e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            if (!reload) {
                this.loadConfig(true);
            }
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            }

        }

    }

    public void save() {
        try {
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8), true);
            w.print(GSON.toJson(this.settings));
            w.flush();
            w.close();
        } catch (Exception e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
        }

    }

    public T getSettings() {
        return this.settings;
    }

    public File getFile() {
        return this.file;
    }

    /**
     * Get boolean from key if T is class Data
     *
     * @param key the boolean to get from file
     *
     * @return boolean at key (false if not present or T is not class Data)
     */
    public boolean getBoolean(String key) {
        if(!(this.getSettings() instanceof Data))
            return false;
        JsonObject jsonObject = ((Data)this.getSettings()).getData();
        if(!jsonObject.has(key))
            return false;
        return jsonObject.get(key).getAsBoolean();
    }

    private static Map<File, DataManager<Data>> dataManagerMap = new HashMap<>();
    public static DataManager<Data> getOrCreateDataManager(File file) {
        if(dataManagerMap.containsKey(file))
            return dataManagerMap.get(file);
        DataManager<Data> dataManager = new DataManager<>(file, Data.class);
        dataManagerMap.put(file, dataManager);
        return dataManager;
    }

    public static DataManager<Data> getOrCreateDataManager(String file) {
        file = file.replace("%common%", UnikAPI.getCommonDataFolder().getPath());
        return getOrCreateDataManager(new File(file));
    }
}
