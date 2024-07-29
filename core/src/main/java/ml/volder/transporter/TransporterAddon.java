package ml.volder.transporter;

import ml.volder.transporter.classes.exceptions.LoadingFailedException;
import ml.volder.transporter.classes.items.ItemManager;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.listeners.KeyboardListener;
import ml.volder.transporter.listeners.MainMenuOpenListener;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.updater.UpdateManager;
import ml.volder.unikapi.AddonMain;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.utils.LoadTimer;
import net.labymod.api.Laby;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Feature todo list
// Auto Transporter feature - Done
// Auto Get                 - Done
// Sign Tools               -
// Messages module          - Done
// Transporter info module  - Done
// Mcmmo module - update    - Done
// Server selector          - Done
// Transporter Menu         - Done
// Gui Modules på skærm     - Done

public class TransporterAddon extends AddonMain {

    private boolean isEnabled = false;
    private static TransporterAddon instance;

    private ItemManager transporterItemManager;
    DataManager<Data> dataManager;
    private Key settingsKeybind;

    public static boolean isEnabled() {
        if (instance == null || !instance.isEnabled || Laby.references().labyNetController().getCurrentServer().isEmpty())
            return false;
        if(!Laby.references().labyNetController().getCurrentServer().get().getDirectIp().endsWith("superawesome.dk"))
          return false;
        return true;
    }

    public static void setEnabled(boolean isEnabled) {
        instance.isEnabled = isEnabled;
    }

    public static TransporterAddon getInstance() {
        if (instance == null)
            throw new IllegalStateException("TransporterAddon is not enabled");
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public void onEnable() {
        if(instance != null)
          throw new IllegalStateException("TransporterAddon is already enabled");
        LoadTimer.start("TransporterAddon");

        setupLogger();

        UnikAPI.LOGGER.info("Loading TransporterAddon");
        UnikAPI.LOGGER.info("**************************************************");
        UnikAPI.LOGGER.printLoggerInfo();

        try {
            instance = this;

            this.dataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "settings.json"));

            UpdateManager.updateCsvFiles(dataManager);

            transporterItemManager = new ItemManager();
            transporterItemManager.loadItems();

            //Modules
            ModuleManager.getInstance().registerModules();
            ModuleManager.getInstance().initModules();
            ModuleManager.getInstance().enableModules();

            //Events
            EventManager.registerEvents(new KeyboardListener());
            EventManager.registerEvents(new MainMenuOpenListener());
        }catch (Exception e) {
            UnikAPI.getCommonDataFolder().delete();
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.SEVERE, e);
            throw new LoadingFailedException();
        }

        isEnabled = true;
        UnikAPI.LOGGER.info("**************************************************");
        UnikAPI.LOGGER.info("TransporterAddon finished loading in " + LoadTimer.finishLoadingTask("TransporterAddon"));
    }

    private void setupLogger() {
        //Setup logger
        //Get loggerOptions.json dataManager
        DataManager<Data> loggerOptions = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "loggerOptions.json"));
        //Parse Logger.DEBUG_LEVEL from loggerOptions entry debugLevel. entry can be either enum or enum ordinal
        String data = loggerOptions.getSettings().getEntry("debugLevel", String.class);
        try {
            int ordinal = Integer.parseInt(data);
            Logger.debugLevel.set(Logger.DEBUG_LEVEL.values()[ordinal]);
        }catch (Exception ignored) {
            try {
                Logger.debugLevel.set(Logger.DEBUG_LEVEL.valueOf(data));
            }catch (Exception ignored2) {
                loggerOptions.getSettings().getData().addProperty("debugLevel", Logger.DEBUG_LEVEL.DISABLED.toString());
            }
        }
        //Parse Logger.LOG_LEVEL from loggerOptions entry logLevel. entry can be either enum or enum ordinal
        data = loggerOptions.getSettings().getEntry("logLevel", String.class);
        try {
            int ordinal = Integer.parseInt(data);
            Logger.logLevel.set(Logger.LOG_LEVEL.values()[ordinal]);
        }catch (Exception ignored) {
            try {
                Logger.logLevel.set(Logger.LOG_LEVEL.valueOf(data));
            }catch (Exception ignored2) {
                loggerOptions.getSettings().getData().addProperty("logLevel", Logger.LOG_LEVEL.INFO.toString());
            }
        }
        //Parse Logger.printStackTrace from loggerOptions entry printStackTrace. entry must be boolean
        Boolean d = loggerOptions.getSettings().getEntry("printStackTrace", Boolean.class);
        if(d != null)
            Logger.printStackTrace.set(d);
        else
            loggerOptions.getSettings().getData().addProperty("printStackTrace", false);
        loggerOptions.save();
    }

    private final List<String> serverList = Arrays.stream(new String[]{"limbo", "larmelobby", "shoppylobby", "maskinrummet", "creepylobby"}).collect(Collectors.toList());

    public List<String> getServerList() {
        return serverList;
    }

    public ItemManager getTransporterItemManager() {
        return transporterItemManager;
    }
    public Key getSettingsKeybind() {
        return settingsKeybind;
    }

    //@Override
    public void openSettings(Object o) {
        if (PlayerAPI.getAPI().getCurrentScreen() instanceof TransporterModulesMenu)
            return;
        PlayerAPI.getAPI().openGuiScreen(new TransporterModulesMenu(null));
    }
}
