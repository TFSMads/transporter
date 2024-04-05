package ml.volder.unikapi.event.events.serverswitchevent;

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

public class ServerSwitchEvent extends Event {

    public enum SWITCH_TYPE {
        JOIN,
        LEAVE,
        SWITCH_SUBSERVER
    }

    private SWITCH_TYPE switchType;

    private static List<Handler> handlerList = new ArrayList<>();

    public ServerSwitchEvent(EventType eventType, String eventName, SWITCH_TYPE switchType) {
        super(eventType, eventName);
        this.switchType = switchType;
    }

    public ServerSwitchEvent(EventType eventType, SWITCH_TYPE switchType) {
        super(eventType);
        this.switchType = switchType;
    }

    public SWITCH_TYPE getSwitchType() {
        return switchType;
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


    private static ApiProvider<Class> apiProvider = new ApiProvider<>("ServerSwitchEvent");

    //Registers event impl if not registred
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.serverswitchevent.impl", ApiReferenceStorage::getVersionedServerSwitchEvent,EventImpl.class);
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
