package ml.volder.unikapi.widgets;


import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.event.Listener;

public class GuiModuleRenderer implements Listener {

    private DefaultModuleManager moduleManager;

    public GuiModuleRenderer(DefaultModuleManager moduleManager, DataManager<Data> dataManager) {
        this.moduleManager = moduleManager;
    }

    public void drawScreenEvent(/*DrawScreenEvent event*/) {
        if(!ModuleSystem.shouldRenderModules())
            return;
        // Not used anymore - drawn through the labymod widget system
        //TODO remove old module system
    }
}
