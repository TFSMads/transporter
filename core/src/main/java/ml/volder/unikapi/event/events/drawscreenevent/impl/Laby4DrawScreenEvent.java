package ml.volder.unikapi.event.events.drawscreenevent.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.drawscreenevent.DrawScreenEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.overlay.IngameOverlayRenderEvent;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4DrawScreenEvent implements EventImpl {
  @Subscribe
  public void onDrawScreen(IngameOverlayRenderEvent event){
    if(event.phase() == Phase.POST){
      Stack currentStack = Laby4DrawAPI.CURRENT_RENDER_STACK;
      Laby4DrawAPI.CURRENT_RENDER_STACK = event.stack();
      MutableMouse mouse = Laby.labyAPI().minecraft().mouse();
      EventManager.callEvent(new DrawScreenEvent(EventType.PRE, getName(), mouse.getX(), mouse.getY(), -1));
      Laby4DrawAPI.CURRENT_RENDER_STACK = currentStack;
    }
  }

  @Override
  public String getName() {
      return "laby4-drawscreenevent";
  }

  @Override
  public void register() {
    Laby.labyAPI().eventBus().registerListener(this);
  }


}
