package ml.volder.unikapi.version.v1_8_9.laby4;

import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;
import ml.volder.unikapi.event.events.opensignevent.impl.Laby4EventOpenSign;
import ml.volder.unikapi.utils.ReflectionUtils;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
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
    GuiScreen guiScreen = (GuiScreen) event.getScreen().wrap().getVersionedScreen();
    if(guiScreen instanceof GuiEditSign){
      TileEntitySign sign = (TileEntitySign) ReflectionUtils.getPrivateFieldValueByType(guiScreen, GuiEditSign.class, TileEntitySign.class);
      OpenSignEvent openSignEvent = new OpenSignEvent(EventType.PRE, eventName, new VersionedTileEntitySign(sign));
      EventManager.callEvent(openSignEvent);
      if(openSignEvent.getNewScreen() != null && openSignEvent.getNewScreen().getHandle(Laby4GuiScreenImpl.class) != null){
        ScreenInstance screenInstance = openSignEvent.getNewScreen().getHandle(Laby4GuiScreenImpl.class);
        event.setScreen(screenInstance);
      }
      if(openSignEvent.isCancelled()) {
        event.setScreen(null);
      }
    }
  }
}
