package ml.volder.unikapi.api.player.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.player.PlayerAPI;
import net.labymod.api.Laby;

import java.util.UUID;
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
