package ml.volder.unikapi.version.v1_12_2.laby4;

import javax.inject.Inject;
import javax.inject.Singleton;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;
import ml.volder.unikapi.event.events.opensignevent.impl.Laby4EventOpenSign;
import ml.volder.unikapi.utils.ReflectionUtils;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;

@Singleton
@Implements(Laby4EventOpenSign.class)
public class VersionedOpenSignEvent extends Laby4EventOpenSign {

  @Inject
  public VersionedOpenSignEvent() {

  }


  @Override
  public boolean isSignScreen(Object guiScreen) {
    return guiScreen instanceof GuiEditSign;
  }

  @Override
  public WrappedTileEntitySign getSign(Object guiScreen) {
    assert guiScreen instanceof GuiEditSign; // isSignScreen() should always be called before this method to ensure the type
    GuiEditSign sign = (GuiEditSign) guiScreen;
    return new VersionedTileEntitySign(sign.tileSign);
  }
}
