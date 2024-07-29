package ml.volder.unikapi.event.events.clientmessageevent;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.event.*;
import ml.volder.unikapi.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClientMessageEvent extends Event implements Cancellable {

    private String message;
    private String cleanMessage;

    private static List<Handler> handlerList = new ArrayList<>();

    public ClientMessageEvent(EventType eventType, String eventName, String message, String cleanMessage) {
        super(eventType, eventName);
        this.message = message;
        this.cleanMessage = cleanMessage;
    }

    public ClientMessageEvent(EventType eventType, String message, String cleanMessage) {
        super(eventType);
        this.message = message;
        this.cleanMessage = cleanMessage;
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

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("ClientMessageEvent");

    //Registers event impl if not registred
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.clientmessageevent.impl", ApiReferenceStorage::getVersionedClientMessageEvent,EventImpl.class);
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

    public String getMessage() {
        return message;
    }

    public String getCleanMessage() {
        return cleanMessage;
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
