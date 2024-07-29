package ml.volder.transporter.events;

import ml.volder.unikapi.event.Event;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.Handler;

import java.util.ArrayList;
import java.util.List;

public class ItemAmountUpdatedEvent extends Event {

  private static List<Handler> handlerList = new ArrayList<>();

  public ItemAmountUpdatedEvent(EventType eventType, String eventName) {
    super(eventType, eventName);
  }

  public ItemAmountUpdatedEvent(EventType eventType) {
    this(eventType, "item_amount_updated_event");
  }

  @Override
  public List<Handler> getHandlerList() {
    return handlerList;
  }

  public static List<Handler> getHandlers(){
    return handlerList;
  }
}
