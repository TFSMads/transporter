package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.*;
import ml.volder.transporter.modules.transportermenumodule.TransporterMenu;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

public class TransporterMenuModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    private int withdrawAmount = 64;

    private Key openKey = Key.K;

    public TransporterMenuModule(String moduleName) {
        super(moduleName);
        instance = this;
        EventManager.registerEvents(this);
        fillSettings();
    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(openKey == null || openKey.equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(openKey))
            this.open();
    }

    private void open() {
        if(PlayerAPI.getAPI().hasOpenScreen())
            return;
        PlayerAPI.getAPI().openGuiScreen(new TransporterMenu());
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Transporter Menu", "En feature til nemt at flytte items til og fra din transporter.", ModTextures.MISC_HEAD_QUESTION, isActive -> {
            isFeatureActive = isActive;
            setConfigEntry("isFeatureActive", isFeatureActive);
        });
        moduleElement.setActive(isFeatureActive);

        Settings subSettings = moduleElement.getSubSettings();

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "transporterMenuKeybind", false, openKey);
        this.openKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.openKey = key);
        subSettings.add(keyElement);

        NumberElement numberElement = new NumberElement("Get Antal", getDataManager(), "withdraw.amount", new ControlElement.IconData(Material.PAPER), -1);
        numberElement.setMinValue(-1);
        withdrawAmount = numberElement.getCurrentValue();
        numberElement.addCallback(integer -> withdrawAmount = integer);
        subSettings.add(numberElement);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public int getWithdrawAmount() {
        return withdrawAmount;
    }

    private static TransporterMenuModule instance;
    public static TransporterMenuModule getInstance() {
        return instance;
    }
}
