package ml.volder.transporter.events;

import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.Cancellable;
import net.labymod.api.event.Event;

public class OpenSignEvent implements Event, Cancellable {

    WrappedTileEntitySign tileEntitySign;
    ScreenInstance newScreen;

    private boolean isCancelled = false;

    public WrappedTileEntitySign getTileEntitySign() {
        return tileEntitySign;
    }

    public void setScreen(ScreenInstance guiScreen) {
        this.newScreen = guiScreen;
    }

    public ScreenInstance getNewScreen() {
        return newScreen;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public OpenSignEvent(WrappedTileEntitySign tileEntitySign) {
        this.tileEntitySign = tileEntitySign;
    }
}
