package ml.volder.unikapi.version.v1_16_5.laby4;

import ml.volder.transporter.utils.SignUtils;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.models.Implements;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;

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
    return guiScreen instanceof SignEditScreen;
  }

  @Override
  public WrappedTileEntitySign getSign(Object guiScreen) {
    assert isSignScreen(guiScreen); // isSignScreen() should always be called before this method to ensure the type
    SignEditScreen sign = (SignEditScreen) guiScreen;
    return new VersionedTileEntitySign(sign.sign);
  }
}
