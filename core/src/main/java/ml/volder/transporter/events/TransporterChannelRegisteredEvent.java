package ml.volder.transporter.events;

import ml.volder.unikapi.event.Event;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.Handler;

import java.util.ArrayList;
import java.util.List;

public class TransporterChannelRegisteredEvent extends Event {
    private static List<Handler> handlerList = new ArrayList<>();

    public TransporterChannelRegisteredEvent(EventType eventType, String eventName) {
        super(eventType, eventName);
    }

    public TransporterChannelRegisteredEvent(EventType eventType) {
        this(eventType, "transporter_channel_registered_event");
    }

    @Override
    public List<Handler> getHandlerList() {
        return handlerList;
    }

    public static List<Handler> getHandlers(){
        return handlerList;
    }
}
