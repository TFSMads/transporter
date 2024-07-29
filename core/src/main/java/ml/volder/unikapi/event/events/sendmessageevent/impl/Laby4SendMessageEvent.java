package ml.volder.unikapi.event.events.sendmessageevent.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.sendmessageevent.SendMessageEvent;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import org.jetbrains.annotations.NotNull;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4SendMessageEvent implements EventImpl {
  @Subscribe
  public void onMessage(@NotNull ChatMessageSendEvent event){
    SendMessageEvent e = new SendMessageEvent(EventType.PRE, getName(), event.getMessage());
    EventManager.callEvent(e);
    event.setCancelled(e.isCancelled());
  }

  @Override
  public String getName() {
      return "laby4-sendmessageevent";
  }

  @Override
  public void register() {
    Laby.labyAPI().eventBus().registerListener(this);
  }


}
