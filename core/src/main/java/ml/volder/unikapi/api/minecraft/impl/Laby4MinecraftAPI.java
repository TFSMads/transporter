package ml.volder.unikapi.api.minecraft.impl;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ml.volder.core.generated.DefaultReferenceStorage;
import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.loader.Laby4Loader;
import net.labymod.api.Laby;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.gui.screen.NamedScreen;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4MinecraftAPI implements MinecraftAPI {

  private static Laby4MinecraftAPI instance;

  public static Laby4MinecraftAPI getAPI() {
    if(instance == null){
      instance = new Laby4MinecraftAPI();
    }
    return instance;
  }
  @Override
  public boolean isInGame() {
    return Laby.labyAPI().minecraft().isIngame();
  }

  @Override
  public boolean isSingleplayer() {
    return Laby.labyAPI().minecraft().isSingleplayer();
  }

  @Override
  public boolean isF3MenuOpen() {
    return Laby.labyAPI().minecraft().options().isDebugEnabled();
  }

  @Override
  public String filterAllowedCharacters(String inputString) {
    StringBuilder stringbuilder = new StringBuilder();

    for (char c0 : inputString.toCharArray())
    {
      if (isAllowedCharacter(c0))
      {
        stringbuilder.append(c0);
      }
    }

    return stringbuilder.toString();
  }

  public boolean isAllowedCharacter(char character)
  {
    return character != 167 && character >= 32 && character != 127;
  }

  @Override
  public String translateLanguageKey(String translateKey) {
    return Laby.labyAPI().minecraft().getTranslation(translateKey);
  }

  @Override
  public void openMainMenu() {
    Laby.labyAPI().minecraft().minecraftWindow().displayScreen(NamedScreen.MAIN_MENU.create());
  }

  @Override
  public boolean isLegacy() {
    if(Laby.labyAPI().minecraft().getVersion().equals("1.8.9") || Laby.labyAPI().minecraft().getVersion().equals("1.12.2"))
      return true;
    return false;
  }

  @Override
  public Map<String, Integer> getScoreBoardLines() {
    Map<String, Integer> scoreBoardLines = new HashMap<>();
    ScoreboardObjective scoreboard = Laby.labyAPI().minecraft().getScoreboard().getObjective(DisplaySlot.SIDEBAR);
    if(scoreboard == null)
      return scoreBoardLines;
    Collection<ScoreboardScore> scoreboardScores = Laby.labyAPI().minecraft().getScoreboard().getScores(scoreboard);
    for (ScoreboardScore score : scoreboardScores) {
      scoreBoardLines.put(score.getName(), score.getValue());
    }
    return scoreBoardLines;
  }

  @Override
  public String getScoreBoardTitle() {
    ScoreboardObjective scoreboard = Laby.labyAPI().minecraft().getScoreboard().getObjective(DisplaySlot.SIDEBAR);
    if(scoreboard == null || !(scoreboard.getTitle() instanceof TextComponent))
      return "";
    return ((TextComponent) scoreboard.getTitle()).getText();
  }
}
