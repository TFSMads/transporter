package ml.volder.transporter;

import com.google.gson.JsonElement;
import ml.volder.transporter.classes.exceptions.LoadingFailedException;
import ml.volder.transporter.classes.items.ItemManager;
import ml.volder.transporter.gui.CsvEditor;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.listeners.KeyboardListener;
import ml.volder.transporter.listeners.MainMenuOpenListener;
import ml.volder.transporter.modules.*;
import ml.volder.transporter.updater.UpdateManager;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.AddonMain;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.widgets.ModuleSystem;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
        if (instance == null || !instance.isEnabled)
            return false;
        SocketAddress socketAddress = MinecraftAPI.getAPI().getSocketAddress();
        if(socketAddress instanceof InetSocketAddress) {
            boolean isSa = false;
            if  (((InetSocketAddress) socketAddress).getAddress().getHostAddress().equals("138.201.34.120")
                    || ((InetSocketAddress) socketAddress).getAddress().getHostAddress().equals("144.76.33.88")
                    || ((InetSocketAddress) socketAddress).getAddress().getHostAddress().equals("144.76.33.88")
                )
                isSa = true;
            else if(((InetSocketAddress) socketAddress).getAddress().getHostName().endsWith("superawesome.dk"))
                isSa = true;
            else if(((InetSocketAddress) socketAddress).getAddress().getCanonicalHostName().endsWith("superawesome.dk"))
                isSa = true;
            if(!isSa)
                return false;
        }
        return true;
    }

    public static void setEnabled(boolean isEnabled) {
        instance.isEnabled = isEnabled;
    }

    public static TransporterAddon getInstance() {
        return instance;
    }

    public void onEnable() {
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

        UnikAPI.LOGGER.info("Loading TransporterAddon");
        UnikAPI.LOGGER.info("**************************************************");
        UnikAPI.LOGGER.info("Debug level is " + Logger.debugLevel.get());
        UnikAPI.LOGGER.info("Log level is " + Logger.logLevel.get());
        UnikAPI.LOGGER.info("Print stack trace is " + Logger.printStackTrace.get());

        if(Logger.debugLevel.get() == Logger.DEBUG_LEVEL.TESTING) {
            UnikAPI.LOGGER.info("Testing logger:");
            UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.TESTING);
            UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.LOWEST);
            UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.LOW);
            UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.MEDIUM);
            UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.HIGH);
            UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.HIGHEST);
            UnikAPI.LOGGER.finest("This is a finest message");
            UnikAPI.LOGGER.finer("This is a finer message");
            UnikAPI.LOGGER.fine("This is a fine message");
            UnikAPI.LOGGER.info("This is an info message");
            UnikAPI.LOGGER.warning("This is a warning message");
            UnikAPI.LOGGER.severe("This is a severe message");
        }

        try {
            instance = this;
            this.dataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "settings.json"));

            UpdateManager.updateCsvFiles(dataManager);

            transporterItemManager = new ItemManager();

            KeyElement keyElement = new KeyElement("Indstillinger - Keybind", new ControlElement.IconData(Material.OAK_BUTTON), dataManager, "settingsKeybind", false, Key.R_SHIFT);
            keyElement.addCallback(key -> settingsKeybind = key);
            this.settingsKeybind = keyElement.getCurrentKey();
            TransporterModulesMenu.addSetting(keyElement);

            DropDownMenu<FormatingUtils.FORMATTING_MODE> dropDownMenu = new DropDownMenu<>("", 0, 0, 0, 0);
            dropDownMenu.fill(FormatingUtils.FORMATTING_MODE.values());
            DropDownElement<FormatingUtils.FORMATTING_MODE> dropDownElement = new DropDownElement<>("Tal Formatering", "selectedNumberFormat", dropDownMenu, new ControlElement.IconData(Material.PAPER), (DropDownElement.DropDownLoadValue<FormatingUtils.FORMATTING_MODE>) value -> {
                if(value.equals("INGEN")) {
                    return FormatingUtils.FORMATTING_MODE.INGEN;
                }else if(value.equals("PUNKTUM")) {
                    return FormatingUtils.FORMATTING_MODE.PUNKTUM;
                }
                return FormatingUtils.FORMATTING_MODE.ENDELSE;
            }, dataManager);
            FormatingUtils.formattingMode = (FormatingUtils.FORMATTING_MODE) dropDownElement.getDropDownMenu().getSelected();
            dropDownElement.setCallback(mode -> FormatingUtils.formattingMode = mode);

            TransporterModulesMenu.addSetting(dropDownElement);

            StringElement element = new StringElement(
                    "Lobbyer",
                    "updateServere",
                    new ControlElement.IconData(Material.PAPER),
                    "limbo,larmelobby,shoppylobby,maskinrummet,creepylobby",
                    dataManager
            );
            element.addCallback(this::updateServers);
            updateServers(element.getCurrentValue());
            TransporterModulesMenu.addSetting(element);

            ListContainerElement advancedSettings = new ListContainerElement("Avanceret", new ControlElement.IconData(Material.DIODE));

            ListContainerElement loggerOptionsElement = new ListContainerElement("Logger indstillinger", new ControlElement.IconData(Material.PAPER));


            DropDownMenu<Logger.LOG_LEVEL> logLevelDropDownMenu = new DropDownMenu<>("", 0, 0, 0, 0);
            logLevelDropDownMenu.fill(Logger.LOG_LEVEL.values());
            DropDownElement<Logger.LOG_LEVEL> logLevelDropDownElement = new DropDownElement<>("Log Level", "logLevel", logLevelDropDownMenu, new ControlElement.IconData(Material.PAPER), Logger.LOG_LEVEL::valueOf, loggerOptions);
            logLevelDropDownElement.setCallback(Logger.logLevel::set);
            loggerOptionsElement.getSubSettings().add(logLevelDropDownElement);

            DropDownMenu<Logger.DEBUG_LEVEL> debugLevelDropDownMenu = new DropDownMenu<>("", 0, 0, 0, 0);
            debugLevelDropDownMenu.fill(Logger.DEBUG_LEVEL.values());
            DropDownElement<Logger.DEBUG_LEVEL> debugLevelDropDownElement = new DropDownElement<>("Debug Level", "debugLevel", debugLevelDropDownMenu, new ControlElement.IconData(Material.PAPER), Logger.DEBUG_LEVEL::valueOf, loggerOptions);
            debugLevelDropDownElement.setCallback(Logger.debugLevel::set);
            loggerOptionsElement.getSubSettings().add(debugLevelDropDownElement);

            BooleanElement printStackTraceElement = new BooleanElement("Print stack trace", loggerOptions, "printStackTrace", new ControlElement.IconData(Material.DIODE), Logger.printStackTrace.get());
            printStackTraceElement.addCallback(Logger.printStackTrace::set);
            loggerOptionsElement.getSubSettings().add(printStackTraceElement);

            advancedSettings.getSubSettings().add(loggerOptionsElement);

            //transporter-item.csv editor
            BooleanElement updateItemsElement = new BooleanElement("Opdatere items automatisk", dataManager, "updateItemsFromGithub", new ControlElement.IconData(Material.DIODE), true);

            ListContainerElement itemsEditor = new ListContainerElement("Items", new ControlElement.IconData(Material.IRON_INGOT));
            itemsEditor.setAdvancedButtonCallback(aBoolean -> CsvEditor.openEditor(new File(UnikAPI.getCommonDataFolder(), "transporter-items.csv"), ',', csvFile -> transporterItemManager.reloadItemsFromCSV()));

            advancedSettings.getSubSettings().add(updateItemsElement);
            advancedSettings.getSubSettings().add(itemsEditor);

            //transporter-messages.csv editor
            BooleanElement updateMessagesElement = new BooleanElement("Opdatere beskeder automatisk", dataManager, "updateMessagesFromGithub", new ControlElement.IconData(Material.DIODE), true);

            ListContainerElement messagesEditor = new ListContainerElement("Beskeder", new ControlElement.IconData(Material.PAINTING));
            messagesEditor.setAdvancedButtonCallback(aBoolean -> CsvEditor.openEditor(new File(UnikAPI.getCommonDataFolder(), "transporter-messages.csv"), ',', csvFile -> ModuleManager.getInstance().getModule(MessagesModule.class).reloadMessagesFromCSV()));

            advancedSettings.getSubSettings().add(updateMessagesElement);
            advancedSettings.getSubSettings().add(messagesEditor);

            //transporter-balance-messages.csv editor
            BooleanElement updateBalanceMessagesElement = new BooleanElement("Opdatere balance beskeder automatisk", dataManager, "updateBalanceMessagesFromGithub", new ControlElement.IconData(Material.DIODE), true);

            ListContainerElement balanceMessagesEditor = new ListContainerElement("Balance beskeder", new ControlElement.IconData(Material.EMERALD));
            balanceMessagesEditor.setAdvancedButtonCallback(aBoolean -> CsvEditor.openEditor(new File(UnikAPI.getCommonDataFolder(), "transporter-balance-messages.csv"), ';', csvFile -> ModuleManager.getInstance().getModule(BalanceModule.class).reloadMessagesFromCSV()));

            advancedSettings.getSubSettings().add(updateBalanceMessagesElement);
            advancedSettings.getSubSettings().add(balanceMessagesEditor);

            TransporterModulesMenu.addSetting(advancedSettings);


            TransporterModulesMenu.addSetting(new HeaderElement(ModColor.WHITE + "Transporter Addon" + ModColor.GRAY + " - " + ModColor.WHITE + "Features"));


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
        UnikAPI.LOGGER.info("TransporterAddon finished loading");
    }

    private List<String> serverList;

    private void updateServers(String serverString) {
        serverList = Arrays.stream(serverString.split(",")).collect(Collectors.toList());
    }

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
