package ml.volder.unikapi.widgets;


import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.drawscreenevent.DrawScreenEvent;

public class GuiModuleRenderer implements Listener {

    private DefaultModuleManager moduleManager;

    public GuiModuleRenderer(DefaultModuleManager moduleManager, DataManager<Data> dataManager) {
        this.moduleManager = moduleManager;
    }

    @EventHandler
    public void drawScreenEvent(DrawScreenEvent event) {
        if(!ModuleSystem.shouldRenderModules())
            return;
        moduleManager.getGuiModuleList().forEach(guiModule -> {
            if(guiModule.isEnabled())
                guiModule.drawModule();
        });
    }
}
