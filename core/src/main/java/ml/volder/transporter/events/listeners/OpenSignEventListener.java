package ml.volder.transporter.events.listeners;

import ml.volder.transporter.events.OpenSignEvent;
import ml.volder.transporter.utils.SignUtils;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;

public class OpenSignEventListener {

    @Subscribe
    public void onGuiOpen(ScreenDisplayEvent event){
        if(event.getScreen() == null || event.getScreen().wrap() == null || event.getScreen().wrap().getVersionedScreen() == null)
            return;
        if(SignUtils.getVersionedInstance().isSignScreen(event.getScreen().wrap().getVersionedScreen())){
            WrappedTileEntitySign sign = SignUtils.getVersionedInstance().getSign(event.getScreen().wrap().getVersionedScreen());
            OpenSignEvent openSignEvent = new OpenSignEvent(sign);
            Laby.fireEvent(openSignEvent);
            if(openSignEvent.getNewScreen() != null && openSignEvent.getNewScreen() != null){
                event.setScreen(openSignEvent.getNewScreen());
            }
            if(openSignEvent.isCancelled()) {
                event.setScreen(null);
            }
        }
    }


}
