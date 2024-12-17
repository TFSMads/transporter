package ml.volder.unikapi.version.v1_20_1.laby4;

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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;

@Singleton
@Implements(Laby4EventOpenSign.class)
public class VersionedOpenSignEvent extends Laby4EventOpenSign {

  @Inject
  public VersionedOpenSignEvent() {

  }

  @Override
  public boolean isSignScreen(Object guiScreen) {
    return guiScreen instanceof SignEditScreen;
  }

  @Override
  public WrappedTileEntitySign getSign(Object guiScreen) {
    assert isSignScreen(guiScreen); // isSignScreen() should always be called before this method to ensure the type
    SignEditScreen sign = (SignEditScreen) guiScreen;
    return new VersionedTileEntitySign(sign.sign);
  }
}
