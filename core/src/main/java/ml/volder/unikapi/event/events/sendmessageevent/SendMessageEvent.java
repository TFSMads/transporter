package ml.volder.unikapi.event.events.sendmessageevent;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.event.*;
import ml.volder.unikapi.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class SendMessageEvent extends Event implements Cancellable {

    private String message;

    private static List<Handler> handlerList = new ArrayList<>();

    public SendMessageEvent(EventType eventType, String eventName, String message) {
        super(eventType, eventName);
        this.message = message;
    }

    public SendMessageEvent(EventType eventType, String message) {
        super(eventType);
        this.message = message;
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

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("SendMessageEvent");

    //Registers event impl if not registred
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.sendmessageevent.impl", ApiReferenceStorage::getVersionedSendMessageEvent,EventImpl.class);
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
