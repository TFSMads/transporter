package ml.volder.unikapi.event.events.mainmenuopenevent.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.mainmenuopenevent.MainMenuOpenEvent;
import ml.volder.unikapi.wrappers.guiscreen.impl.Laby4GuiScreenImpl;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.lss.style.StyleSheet;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4MainMenuOpenEvent implements EventImpl {

  private boolean isMainMenu(ScreenInstance screenInstance) {
    if(screenInstance == null || screenInstance.wrap() == null || !screenInstance.wrap().isActivity() || screenInstance.wrap().asActivity() == null)
      return false;
    Activity activity = screenInstance.wrap().asActivity();
    for (StyleSheet styleSheet : activity.getStylesheets()) {
      if(styleSheet.file() == null)
        continue;
      String[] absolutePath = styleSheet.file().getAbsolutePath();
      if(absolutePath[absolutePath.length-1].equals("main-menu.lss"))
          return true;
    }
    return false;
  }

  @Subscribe
  public void onGuiOpen(ScreenDisplayEvent event){
    if(isMainMenu(event.getScreen())) {
      MainMenuOpenEvent openEvent = new MainMenuOpenEvent(EventType.PRE, getName());
      EventManager.callEvent(openEvent);
      if(openEvent.getNewScreen() != null)
        event.setScreen(openEvent.getNewScreen().getHandle(Laby4GuiScreenImpl.class));
      if(openEvent.isCancelled())
        event.setScreen(null);
    }
  }

  @Override
  public String getName() {
      return "laby4-openmainmenuevent";
  }

  private static Laby4MainMenuOpenEvent instance;

  @Override
  public void register() {
    instance = this;
    Laby.labyAPI().eventBus().registerListener(this);
  }

  public static void checkMainMenu() {
    if(instance == null)
      return;
    if(instance.isMainMenu(Laby.labyAPI().minecraft().minecraftWindow().currentScreen().unwrap())) {
      MainMenuOpenEvent openEvent = new MainMenuOpenEvent(EventType.PRE, instance.getName());
      EventManager.callEvent(openEvent);
      if(openEvent.getNewScreen() != null)
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(openEvent.getNewScreen().getHandle(Laby4GuiScreenImpl.class));
    }
  }

}