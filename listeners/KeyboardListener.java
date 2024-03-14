package ml.volder.transporter.listeners;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.keysystem.Key;

public class KeyboardListener implements Listener {
    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled())
            return;
        if(TransporterAddon.getInstance().getSettingsKeybind() == null || TransporterAddon.getInstance().getSettingsKeybind().equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(TransporterAddon.getInstance().getSettingsKeybind()) && !PlayerAPI.getAPI().hasOpenScreen())
            PlayerAPI.getAPI().openGuiScreen(new TransporterModulesMenu(null));
    }
}
