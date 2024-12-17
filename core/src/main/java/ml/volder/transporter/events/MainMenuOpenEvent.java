package ml.volder.transporter.events;

import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.Cancellable;
import net.labymod.api.event.Event;

public class MainMenuOpenEvent implements Event, Cancellable {

    ScreenInstance newScreen;

    public void setScreen(ScreenInstance guiScreen) {
        this.newScreen = guiScreen;
    }

    public ScreenInstance getNewScreen() {
        return newScreen;
    }

    private boolean isCancelled = false;

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

}
