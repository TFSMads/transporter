package ml.volder.unikapi.loader;

import ml.volder.transporter.TransporterAddon;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.logger.Logger;

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
