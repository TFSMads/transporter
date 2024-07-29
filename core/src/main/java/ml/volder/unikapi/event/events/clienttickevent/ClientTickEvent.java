package ml.volder.unikapi.event.events.clienttickevent;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.event.Event;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.Handler;
import ml.volder.unikapi.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClientTickEvent extends Event {

    private static List<Handler> handlerList = new ArrayList<>();

    public ClientTickEvent(EventType eventType, String eventName) {
        super(eventType, eventName);
    }

    public ClientTickEvent(EventType eventType) {
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

    //Registers event impl if not registred

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("ClientTickEvent");
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.clienttickevent.impl", ApiReferenceStorage::getVersionedClientTickEvent,EventImpl.class);
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
}
