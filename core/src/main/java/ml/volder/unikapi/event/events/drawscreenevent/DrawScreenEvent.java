package ml.volder.unikapi.event.events.drawscreenevent;

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

public class DrawScreenEvent extends Event {

    private int mouseX;
    private int mouseY;
    private float renderPartialTicks;

    private static List<Handler> handlerList = new ArrayList<>();

    public DrawScreenEvent(EventType eventType, String eventName, int mouseX, int mouseY, float renderPartialTicks) {
        super(eventType, eventName);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.renderPartialTicks = renderPartialTicks;
    }

    public DrawScreenEvent(EventType eventType, int mouseX, int mouseY, float renderPartialTicks) {
        super(eventType);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.renderPartialTicks = renderPartialTicks;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
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

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("DrawScreenEvent");

    //Registers event impl if not registred
    public static void registerEvent() {
        if(!isRegistred()){
            Class<? extends EventImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.event.events.drawscreenevent.impl", ApiReferenceStorage::getVersionedDrawScreenEvent,EventImpl.class);
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
