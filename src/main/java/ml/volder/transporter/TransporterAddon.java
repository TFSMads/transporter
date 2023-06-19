package ml.volder.transporter;

import ml.volder.transporter.classes.items.ItemManager;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.ControlElement;
import ml.volder.transporter.gui.elements.HeaderElement;
import ml.volder.transporter.gui.elements.KeyElement;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.transporter.listeners.KeyboardListener;
import ml.volder.transporter.listeners.MainMenuOpenListener;
import ml.volder.transporter.modules.*;
import ml.volder.unikapi.AddonMain;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

//Feature todo list
// Auto Transporter feature - Done
// Auto Get                 - Done
// Sign Tools               -
// Messages module          - Done
// Transporter info module  - Done
// Mcmmo module - update    - Venter
// Server selector          - Done
// Transporter Menu         - Done
// Gui Modules på skærm     - Done

public class TransporterAddon extends AddonMain {

    private boolean isEnabled = false;
    private static TransporterAddon instance;

    private ItemManager transporterItemManager;

    private AutoTransporter autoTransporter;
    private MessagesModule messagesModule;
    private AutoGetModule autoGetModule;

    private File commonDataFolder;
    private File playerDataFolder;
    private UUID lastUUID;

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
        UnikAPI.LOGGER.info("Loading TransporterAddon");
        instance = this;
        initDataFolders();

        transporterItemManager = new ItemManager();
        this.dataManager = new DataManager<>(new File(TransporterAddon.getInstance().getCommonDataFolder(), "settings.json"), Data.class);

        KeyElement keyElement = new KeyElement("Indstillinger - Keybind", new ControlElement.IconData(Material.OAK_BUTTON), dataManager, "settingsKeybind", false, Key.R_SHIFT);
        keyElement.addCallback(key -> settingsKeybind = key);
        this.settingsKeybind = keyElement.getCurrentKey();
        TransporterModulesMenu.addSetting(keyElement);
        TransporterModulesMenu.addSetting(new HeaderElement(ModColor.WHITE + "Transporter Addon" + ModColor.GRAY + " - " + ModColor.WHITE + "Features"));

        //Modules
        autoTransporter = new AutoTransporter("autoTransporter", this);
        new ServerListModule("serverSelector");
        messagesModule = new MessagesModule("messageModule");
        new GuiModulesModule("guiModule");
        new TransporterMenuModule("transporterMenuModule");
        autoGetModule = new AutoGetModule("autoGetModule", this);
        new SignToolsModule("signToolsModule");

        //Events
        EventManager.registerEvents(new KeyboardListener());
        EventManager.registerEvents(new MainMenuOpenListener());

        isEnabled = true;
        UnikAPI.LOGGER.info("TransporterAddon finished loading");
    }

    public ItemManager getTransporterItemManager() {
        return transporterItemManager;
    }

    private boolean hasUUIDChanged() {
        if(lastUUID == null)
            return true;
        return !lastUUID.equals(PlayerAPI.getAPI().getUUID());
    }

    private void initDataFolders() {
        File file = new File("TransporterAddon/");
        file.mkdirs();
        this.commonDataFolder = file;
        if(lastUUID != null){
            file = new File("TransporterAddon/" + PlayerAPI.getAPI().getUUID());
            file.mkdirs();
            this.playerDataFolder = file;
        }
    }

    public File getCommonDataFolder() {
        return this.commonDataFolder;
    }

    public File getPlayerDataFolder() {
        if(hasUUIDChanged()){
            this.lastUUID = PlayerAPI.getAPI().getUUID();
            initDataFolders();
        }
        return this.playerDataFolder;
    }

    public MessagesModule getMessagesModule() {
        return messagesModule;
    }

    public AutoTransporter getAutoTransporter() {
        return autoTransporter;
    }

    public Key getSettingsKeybind() {
        return settingsKeybind;
    }

    public AutoGetModule getAutoGetModule() {
        return autoGetModule;
    }

    //@Override
    public void openSettings(Object o) {
        if (PlayerAPI.getAPI().getCurrentScreen() instanceof TransporterModulesMenu)
            return;
        PlayerAPI.getAPI().openGuiScreen(new TransporterModulesMenu(TransporterAddon.getInstance(), null));
    }
}
