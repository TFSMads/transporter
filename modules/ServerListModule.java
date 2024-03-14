package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.serverlistmodule.ServerSelecterGui;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.guisystem.elements.ControlElement;
import ml.volder.unikapi.guisystem.elements.KeyElement;
import ml.volder.unikapi.guisystem.elements.Settings;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

public class ServerListModule extends SimpleModule implements Listener {
    private Key openKey = Key.L; // Default key = L

    public ServerListModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        return this;
    }

    @Override
    public SimpleModule enable() {
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void fillSettings(Settings subSettings) {
        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "serverListKeybind", false, openKey);
        this.openKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.openKey = key);
        subSettings.add(keyElement);
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
}
