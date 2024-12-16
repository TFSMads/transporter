package ml.volder.transporter.listeners;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.keysystem.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

public class KeyboardListener implements Listener {
    @Subscribe
    public void onKeyPress(KeyEvent event){
        if(!TransporterAddon.isEnabled())
            return;
        if(TransporterAddon.getInstance().getSettingsKeybind() == null || TransporterAddon.getInstance().getSettingsKeybind().equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(TransporterAddon.getInstance().getSettingsKeybind()) && !PlayerAPI.getAPI().hasOpenScreen())
            PlayerAPI.getAPI().openGuiScreen(new TransporterModulesMenu(null));
    }
}
