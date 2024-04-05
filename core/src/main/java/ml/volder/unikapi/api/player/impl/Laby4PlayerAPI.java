package ml.volder.unikapi.api.player.impl;

import java.util.UUID;
import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;
import ml.volder.unikapi.wrappers.guiscreen.impl.Laby4GuiScreenImpl;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenInstance;
@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4PlayerAPI implements PlayerAPI {
  private static Laby4PlayerAPI instance;
  public static Laby4PlayerAPI getAPI() {
    if(instance == null)
      instance = new Laby4PlayerAPI();
    return instance;
  }

  public void sendCommand(String command) {
    Laby.references().chatExecutor().chat("/" + command, false);
  }

  @Override
  public void openGuiScreen(WrappedGuiScreen screen) {
    if(screen == null) {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
    }else{
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(screen.getHandle(Laby4GuiScreenImpl.class));
    }
  }

  @Override
  public WrappedGuiScreen getCurrentScreen() {
    if(Laby.labyAPI().minecraft().minecraftWindow().currentScreen().mostInnerScreen() instanceof Laby4GuiScreenImpl) {
      Laby4GuiScreenImpl screen = (Laby4GuiScreenImpl) Laby.labyAPI().minecraft().minecraftWindow().currentScreen().mostInnerScreen();
      return screen.getWrapper();
    }
    return null;
  }

  @Override
  public boolean hasOpenScreen() {
    return Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened();
  }

  @Override
  public UUID getUUID() {
    return Laby.labyAPI().getUniqueId();
  }

  @Override
  public void displayChatMessage(String message) {
    Laby.references().chatExecutor().displayClientMessage(message);
  }

  @Override
  public void displayActionBarMessage(String message) {
    Laby.references().chatExecutor().displayActionBar(message);
  }
}
