package ml.volder.unikapi.event.events.clientkeypressevent.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4KeyPressEvent implements EventImpl {
  @Subscribe
  public void onKeyPress(KeyEvent event){
        EventManager.callEvent(new ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent(EventType.PRE, getName()));
  }

  @Override
  public String getName() {
      return "laby4-keypressevent";
  }

  @Override
  public void register() {
    Laby.labyAPI().eventBus().registerListener(this);
  }


}
