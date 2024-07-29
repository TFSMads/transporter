package ml.volder.unikapi.event;

import java.lang.reflect.Method;

public class Handler {
    private Method method;
    private Listener instance;
    private EventType eventType;
    private Class<? extends Event> event;

    public Handler(Method method, Listener instance, EventType eventType, Class<? extends Event> event) {
        this.method = method;
        this.instance = instance;
        this.eventType = eventType;
        this.event = event;
    }

    public Method getMethod() {
        return method;
    }

    public Listener getInstance() {
        return instance;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }
}
