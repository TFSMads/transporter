package ml.volder.transporter.listeners;

import ml.volder.transporter.events.MainMenuOpenEvent;
import ml.volder.transporter.gui.UpdateScreen;
import ml.volder.transporter.updater.UpdateManager;
import net.labymod.api.event.Subscribe;

public class MainMenuOpenListener {

    private boolean hasCheckedForUpdate = false;

    @Subscribe
    public void onMainMenuOpen(MainMenuOpenEvent event) {
        if(!hasCheckedForUpdate && !UpdateManager.isUpToDate()) {
            event.setScreen(new UpdateScreen());
            hasCheckedForUpdate = true;
        }
    }

}
