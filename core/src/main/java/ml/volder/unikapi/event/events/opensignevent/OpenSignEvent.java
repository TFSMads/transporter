package ml.volder.unikapi.event.events.opensignevent;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.event.*;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.client.gui.screen.ScreenInstance;

import java.util.ArrayList;
import java.util.List;

public class OpenSignEvent extends Event implements Cancellable {

    WrappedTileEntitySign tileEntitySign;
    ScreenInstance newScreen;

    private static List<Handler> handlerList = new ArrayList<>();

    public OpenSignEvent(EventType eventType, String eventName, WrappedTileEntitySign tileEntitySign) {
        super(eventType, eventName);
        this.tileEntitySign = tileEntitySign;
    }

    public OpenSignEvent(EventType eventType) {
        super(eventType);
    }

    public static List<Handler> getHandlers(){
        return handlerList;
    }

    @Override
    public List<Handler> getHandlerList() {
        return handlerList;
    }

    private static EventImpl eventImpl;

    public static boolean isRegistred() {
        return eventImpl != null;
    }

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("OpenSignEvent");

    //Registers event impl if not registred
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.opensignevent.impl", ApiReferenceStorage::getVersionedOpenSignEvent,EventImpl.class);
            if(klass != null) {
                try {
                    eventImpl = klass.newInstance();
                    eventImpl.register();
                } catch (Exception e) {
                    UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
                }
            }
        }
    }

    public WrappedTileEntitySign getTileEntitySign() {
        return tileEntitySign;
    }

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
