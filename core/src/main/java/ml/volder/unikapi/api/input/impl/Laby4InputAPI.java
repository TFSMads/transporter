package ml.volder.unikapi.api.input.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.input.InputAPI;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.KeyboardUser;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.key.mapper.KeyMapper;
@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4InputAPI implements InputAPI {

  public static float SCROLL_DELTA = 0;
  private static Laby4InputAPI instance;

  public static Laby4InputAPI getAPI() {
    if(instance == null)
      instance = new Laby4InputAPI();
    return instance;
  }

  @Override
  public boolean isKeyDown(ml.volder.unikapi.keysystem.Key key) {
    return ml.volder.unikapi.keysystem.KeyMapper.isPressed(key);
  }

  @Override
  public int getEventDWheel() {
    return (int) SCROLL_DELTA;
  }

  @Override
  public boolean isCtrlKeyDown() {
    return Key.L_CONTROL.isPressed() || Key.R_CONTROL.isPressed();
  }

  @Override
  public boolean isShiftKeyDown() {
    return Key.L_SHIFT.isPressed() || Key.R_SHIFT.isPressed();
  }

  @Override
  public boolean isAltKeyDown() {
    return Key.L_ALT.isPressed() || Key.R_ALT.isPressed();
  }

  @Override
  public String getKeyName(ml.volder.unikapi.keysystem.Key key) {
    return ml.volder.unikapi.keysystem.KeyMapper.getKeyName(key);
  }

  @Override
  public void enableRepeatEvents(boolean enable) {
    Laby.labyAPI().minecraft().updateKeyRepeatingMode(enable);
  }

}
