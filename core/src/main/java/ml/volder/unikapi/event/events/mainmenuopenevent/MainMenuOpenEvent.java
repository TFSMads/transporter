package ml.volder.unikapi.event.events.mainmenuopenevent;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.event.*;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.client.gui.screen.ScreenInstance;

import java.util.ArrayList;
import java.util.List;

public class MainMenuOpenEvent extends Event implements Cancellable {
    ScreenInstance newScreen;

    private static List<Handler> handlerList = new ArrayList<>();

    public MainMenuOpenEvent(EventType eventType, String eventName) {
        super(eventType, eventName);
    }

    public MainMenuOpenEvent(EventType eventType) {
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

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("MainMenuOpenEvent");

    //Registers event impl if not registred
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.mainmenuopenevent.impl", ApiReferenceStorage::getVersionedMainMenuOpenEvent,EventImpl.class);
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
