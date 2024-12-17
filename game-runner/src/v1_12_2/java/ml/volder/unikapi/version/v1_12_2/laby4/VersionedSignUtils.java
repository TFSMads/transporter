package ml.volder.unikapi.version.v1_12_2.laby4;

import ml.volder.transporter.utils.SignUtils;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.models.Implements;
import net.minecraft.client.gui.inventory.GuiEditSign;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implements(SignUtils.class)
public class VersionedSignUtils extends SignUtils {

  @Inject
  public VersionedSignUtils() {

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
