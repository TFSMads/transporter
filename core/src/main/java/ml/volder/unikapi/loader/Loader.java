package ml.volder.unikapi.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.volder.transporter.TransporterAddon;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.KeyMapper;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class Loader {

    public static void onEnable() {
        UnikAPI.LOGGER.info("[UnikAPI-Core] Enabling addon!");
        initializeKeySystem();
        enableAddon();
    }



    private static void initializeKeySystem() {
        Key.initialize();
        MouseButton.initialize();
    }

    private static void enableAddon() {
        try {
            if(!TransporterAddon.isInitialized()) {
              TransporterAddon transporterAddon = new TransporterAddon();
              transporterAddon.onEnable();
            }
        } catch (Exception e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.SEVERE, e);
            UnikAPI.LOGGER.warning("Kunne ikke aktivere addonet!");
        }
    }
}
