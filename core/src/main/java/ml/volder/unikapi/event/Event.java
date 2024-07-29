package ml.volder.unikapi.event;

import java.util.List;

public abstract class Event {
    private String name;
    private EventType eventType;

    public Event(EventType eventType, String eventName){
        this.eventType = eventType;
    }

    public Event(EventType eventType){
        this.eventType = eventType;
    }

    public EventType getEventType(){
        return this.eventType;
    }



    public String getEventName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }
        return name;
    }

    public abstract List<Handler> getHandlerList();
}
