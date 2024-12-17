package ml.volder.v1_20_5;

import ml.volder.unikapi.wrappers.tileentitysign.impl.Laby4WrappedTileEntitySign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class VersionedTileEntitySign extends Laby4WrappedTileEntitySign {
  private String[] signLines = new String[4];

  private SignBlockEntity tileEntitySign;

  public VersionedTileEntitySign(SignBlockEntity tileEntitySign) {
    this.tileEntitySign = tileEntitySign;
  }

  @Override
  public void setEditable(boolean isEditable) {
    if(!isEditable) {
      tileEntitySign.setAllowedPlayerEditor(null);
    }
  }

  @Override
  public void sendUpdatePacket() {
    ClientPacketListener lvt_1_1_ = Minecraft.getInstance().getConnection();
    if (lvt_1_1_ != null) {
      lvt_1_1_.send(new ServerboundSignUpdatePacket(
          this.tileEntitySign.getBlockPos(),
          true, //Is front text
          signLines[0],
          signLines[1],
          signLines[2],
          signLines[3])
      );
    }
  }

  @Override
  public void markDirty() {
    tileEntitySign.setChanged();
  }

  @Override
  public String getLine(int selected) {
    return signLines.length >= selected + 1 ? signLines[selected] : null;
  }

  @Override
  public void setLine(int selected, String value) {
    if(signLines.length < selected + 1)
      return;
    signLines[selected] = value;
  }
}
