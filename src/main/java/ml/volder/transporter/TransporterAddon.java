package ml.volder.transporter;

import ml.volder.transporter.classes.exceptions.LoadingFailedException;
import ml.volder.transporter.classes.items.ItemManager;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.listeners.KeyboardListener;
import ml.volder.transporter.listeners.MainMenuOpenListener;
import ml.volder.transporter.modules.*;
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
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;

import java.io.File;
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
        UnikAPI.LOGGER.info("Loading TransporterAddon");

        try {
            instance = this;

            transporterItemManager = new ItemManager();
            this.dataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "settings.json"));

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
            e.printStackTrace();
            throw new LoadingFailedException();
        }



        isEnabled = true;
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
