package ml.volder.unikapi.event.events.clientmessageevent.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.event.Event;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4MessageEvent implements EventImpl {
  @Subscribe
  public void onMessage(ChatReceiveEvent event){
    ClientMessageEvent messageEvent = new ClientMessageEvent(EventType.PRE, getName(), event.chatMessage().getFormattedText(), event.chatMessage().getPlainText());
    EventManager.callEvent(messageEvent);
    event.setCancelled(messageEvent.isCancelled());
  }

  @Override
  public String getName() {
      return "laby4-messageevent";
  }

  @Override
  public void register() {
    Laby.labyAPI().eventBus().registerListener(this);
  }


}
