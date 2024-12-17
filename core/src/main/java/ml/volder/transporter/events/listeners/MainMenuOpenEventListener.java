package ml.volder.transporter.events.listeners;

import ml.volder.transporter.events.MainMenuOpenEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.lss.style.StyleSheet;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;

public class MainMenuOpenEventListener {

    private static MainMenuOpenEventListener instance;

    public static MainMenuOpenEventListener getInstance() {
        if(instance == null)
            instance = new MainMenuOpenEventListener();
        return instance;
    }

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
            MainMenuOpenEvent openEvent = new MainMenuOpenEvent();
            Laby.fireEvent(openEvent);
            if(openEvent.getNewScreen() != null)
                event.setScreen(openEvent.getNewScreen());
            if(openEvent.isCancelled())
                event.setScreen(null);
        }
    }

    public static void checkMainMenu() {
        if(getInstance().isMainMenu(Laby.labyAPI().minecraft().minecraftWindow().currentScreen().unwrap())) {
            MainMenuOpenEvent openEvent = new MainMenuOpenEvent();
            Laby.fireEvent(openEvent);
            if(openEvent.getNewScreen() != null)
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(openEvent.getNewScreen());
        }
    }

}
