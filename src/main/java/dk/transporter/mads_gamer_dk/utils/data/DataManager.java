package dk.transporter.mads_gamer_dk.utils.data;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import net.labymod.support.util.Debug;
import net.labymod.support.util.Debug.EnumDebugMode;
import org.apache.commons.io.IOUtils;

public class DataManager<T> {
    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final File file;
    private final Class<? extends T> configDefaults;
    private T settings;

    public DataManager(File file, Class<? extends T> configDefaults) {
        this.file = (File)Preconditions.checkNotNull(file);
        this.configDefaults = (Class)Preconditions.checkNotNull(configDefaults);
        try {
            System.out.println("ConfigDefaults PRE: " + GSON.toJson(this.configDefaults.newInstance()));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
            } catch (IOException var16) {
                var16.printStackTrace();
            }
        }

        FileInputStream stream = null;

        try {
            stream = new FileInputStream(this.file);
        } catch (FileNotFoundException var15) {
            var15.printStackTrace();
        }

        try {
            System.out.println("ConfigDefaults: " + GSON.toJson(this.configDefaults.newInstance()));
            System.out.println("PRE: " + this.settings);
            this.settings = GSON.fromJson(createdNewFile ? GSON.toJson(this.configDefaults.newInstance()) : IOUtils.toString(stream, StandardCharsets.UTF_8), this.configDefaults);
            System.out.println("POST: " + GSON.toJson(this.settings));
            Debug.log(EnumDebugMode.CONFIG_MANAGER, this.settings != null ? "Loaded " + this.file.getName() + "!" : "Loaded file but settings is null");
            if (!reload && this.settings == null) {
                this.loadConfig(true);
            } else if (this.settings != null) {
                this.save();
            }
        } catch (Exception var17) {
            var17.printStackTrace();
            Debug.log(EnumDebugMode.CONFIG_MANAGER, "Failed to load " + this.file.getName() + " config!");
            if (!reload) {
                this.loadConfig(true);
            }
        } finally {
            try {
                stream.close();
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

    }

    public void save() {
        try {
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8), true);
            w.print(GSON.toJson(this.settings));
            w.flush();
            w.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public T getSettings() {
        return this.settings;
    }

    public File getFile() {
        return this.file;
    }
}
