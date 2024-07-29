package ml.volder.unikapi.keysystem.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.KeyMapper;
import ml.volder.unikapi.keysystem.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4KeyMapper extends KeyMapper {

  public static net.labymod.api.client.gui.screen.key.Key convert(Key key) {
    return net.labymod.api.client.gui.screen.key.mapper.KeyMapper.getKey(key.getName());
  }

  public static net.labymod.api.client.gui.screen.key.MouseButton convert(MouseButton mouseButton) {
    Collection<net.labymod.api.client.gui.screen.key.MouseButton> mouseButtonCollection = new ArrayList<>();
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.LEFT);
    mouseButtonCollection.add(
            (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.RIGHT);
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.MIDDLE);
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.M4);
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.M5);
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.M6);
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.M7);
    mouseButtonCollection.add(
        (net.labymod.api.client.gui.screen.key.MouseButton) net.labymod.api.client.gui.screen.key.MouseButton.M8);

    for (net.labymod.api.client.gui.screen.key.MouseButton button : mouseButtonCollection) {
      if(button.getActualName().equals(mouseButton.getName()))
        return button;
    }
    return null;
  }

  public static Key convert(net.labymod.api.client.gui.screen.key.Key key) {
    return getKey(key.getActualName());
  }

  public static MouseButton convert(net.labymod.api.client.gui.screen.key.MouseButton mouseButton) {
    Collection<MouseButton> mouseButtonCollection = new ArrayList<>();
    mouseButtonCollection.add(
        (MouseButton) MouseButton.LEFT);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.RIGHT);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.MIDDLE);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.M4);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.M5);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.M6);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.M7);
    mouseButtonCollection.add(
        (MouseButton) MouseButton.M8);

    for (MouseButton button : mouseButtonCollection) {
      if(button.getName().equals(mouseButton.getActualName()))
        return button;
    }
    return null;
  }

  @Override
  public @NotNull String getNameByKey(@NotNull Key key) {
    return key.getName();
  }

  @Override
  public int getIdByKey(@NotNull Key key) {
    return key.getId();
  }

  @Override
  public int getIdByMouseButton(@NotNull MouseButton mouseButton) {
    return mouseButton.getId();
  }

  @Override
  public @Nullable Key getKeyById(int id) {
    for (Key key : this.keys.values()) {
      if (key.getId() == id)
        return key;
    }
    return null;
  }

  @Override
  public @Nullable MouseButton getMouseButtonById(int i) {
    for (MouseButton mouseButton : this.mouseButtons.values()) {
      if (mouseButton.getId() == i)
        return mouseButton;
    }
    return null;
  }

  @Override
  public char getChar(@NotNull Key key) {
    return key.getCharacter();
  }

  @Override
  public boolean isKeyPressed(@NotNull Key key) {
    return net.labymod.api.client.gui.screen.key.mapper.KeyMapper.isPressed(convert(key));
  }

  @Override
  public void initialize() {

  }

  @Override
  public void register(@NotNull Key key, int i) {

  }

  private static Laby4KeyMapper instance;

  public static Laby4KeyMapper getAPI() {
    if(instance == null)
      instance = new Laby4KeyMapper();
    return instance;
  }
}
