package ml.volder.transporter.modules.guimodules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.transporter.modules.GuiModulesModule;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.drawscreenevent.DrawScreenEvent;

public class GuiModuleRenderer implements Listener {

    private GuiModulesModule guiModulesModule;

    public GuiModuleRenderer(GuiModulesModule guiModulesModule, DataManager<Data> dataManager) {
        this.guiModulesModule = guiModulesModule;
    }

    @EventHandler
    public void drawScreenEvent(DrawScreenEvent event) {
        if(!guiModulesModule.isFeatureActive() || !TransporterAddon.isEnabled())
            return;
        guiModulesModule.getGuiModuleList().forEach(guiModule -> {
            if(guiModule.isEnabled())
                guiModule.drawModule();
        });
    }
}
