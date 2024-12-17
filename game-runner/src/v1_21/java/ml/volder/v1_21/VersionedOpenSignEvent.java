package ml.volder.v1_21;

import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;
import ml.volder.unikapi.event.events.opensignevent.impl.Laby4EventOpenSign;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implements(Laby4EventOpenSign.class)
public class VersionedOpenSignEvent extends Laby4EventOpenSign {

  @Inject
  public VersionedOpenSignEvent() {

  }

  @Override
  public void onScreenOpen(ScreenDisplayEvent event, String eventName) {
    if(event.getScreen() == null || event.getScreen().wrap() == null || event.getScreen().wrap().getVersionedScreen() == null)
      return;
    Screen guiScreen = (Screen) event.getScreen().wrap().getVersionedScreen();
    if(guiScreen instanceof SignEditScreen){
      SignBlockEntity sign = ((SignEditScreen) guiScreen).sign;
      OpenSignEvent openSignEvent = new OpenSignEvent(EventType.PRE, eventName, new VersionedTileEntitySign(sign));
      EventManager.callEvent(openSignEvent);
      if(openSignEvent.getNewScreen() != null){
        ScreenInstance screenInstance = openSignEvent.getNewScreen();
        event.setScreen(screenInstance);
      }
      if(openSignEvent.isCancelled()) {
        event.setScreen(null);
      }
    }
  }
}
