package ml.volder.unikapi.event.events.serverswitchevent;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.clienttickevent.ClientTickEvent;
import ml.volder.unikapi.event.events.serverswitchevent.ServerSwitchEvent.SWITCH_TYPE;
import net.labymod.api.Laby;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.jetbrains.annotations.NotNull;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4ServerSwitchEvent implements EventImpl {
  @Subscribe
  public void onSubServerSwitch(@NotNull SubServerSwitchEvent event){
    EventManager.callEvent(new ServerSwitchEvent(EventType.PRE, getName(), SWITCH_TYPE.SWITCH_SUBSERVER));
  }

  @Subscribe
  public void onDisconnect(@NotNull ServerDisconnectEvent event){
    EventManager.callEvent(new ServerSwitchEvent(EventType.PRE, getName(), SWITCH_TYPE.LEAVE));
  }

  @Subscribe
  public void onJoin(@NotNull ServerJoinEvent event){
    EventManager.callEvent(new ServerSwitchEvent(EventType.PRE, getName(), SWITCH_TYPE.JOIN));
  }

  @Override
  public String getName() {
      return "laby4-serverswitchevent";
  }

  @Override
  public void register() {
    Laby.labyAPI().eventBus().registerListener(this);
  }


}
