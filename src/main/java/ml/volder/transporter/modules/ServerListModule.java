package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.*;
import ml.volder.transporter.modules.serverlistmodule.ServerSelecterGui;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

import java.util.function.Consumer;

public class ServerListModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    private Key openKey = Key.L; // Default key = L

    public ServerListModule(String moduleName) {
        super(moduleName);
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
        PlayerAPI.getAPI().openGuiScreen(new ServerSelecterGui(getDataManager()));
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Server Selector", "En feature til at skifte nemt og hurtigt mellem server på sa.", ModTextures.MISC_HEAD_QUESTION, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isActive) {
                isFeatureActive = isActive;
                setConfigEntry("isFeatureActive", isFeatureActive);
            }
        });
        moduleElement.setActive(isFeatureActive);

        Settings subSettings = moduleElement.getSubSettings();

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "serverListKeybind", false, openKey);
        this.openKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.openKey = key);
        subSettings.add(keyElement);

        TransporterModulesMenu.addSetting(moduleElement);
    }
}
