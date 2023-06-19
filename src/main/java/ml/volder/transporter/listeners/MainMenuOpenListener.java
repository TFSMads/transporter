package ml.volder.transporter.listeners;

import ml.volder.transporter.gui.UpdateScreen;
import ml.volder.transporter.updater.UpdateManager;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.mainmenuopenevent.MainMenuOpenEvent;

public class MainMenuOpenListener implements Listener {

    private boolean hasCheckedForUpdate = false;

    @EventHandler
    public void onMainMenuOpen(MainMenuOpenEvent event) {
        if(!hasCheckedForUpdate && !UpdateManager.isUpToDate()) {
            event.setScreen(new UpdateScreen());
            hasCheckedForUpdate = true;
        }
    }

}
