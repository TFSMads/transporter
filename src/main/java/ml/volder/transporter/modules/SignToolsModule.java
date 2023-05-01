package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.ModuleElement;
import ml.volder.transporter.gui.elements.Settings;
import ml.volder.transporter.modules.signtoolsmodule.SignGui;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;

public class SignToolsModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    public SignToolsModule(String moduleName) {
        super(moduleName);
        EventManager.registerEvents(this);
        fillSettings();
    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    @EventHandler
    public void onSignOpen(OpenSignEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        event.setScreen(new SignGui(getDataManager(), event.getTileEntitySign()));
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Sign Tools", "En feature til at forbedre skilte så man kan kopiere og indsætte.", ModTextures.MISC_HEAD_QUESTION, isActive -> {
            isFeatureActive = isActive;
            setConfigEntry("isFeatureActive", isFeatureActive);
        });
        moduleElement.setActive(isFeatureActive);

        Settings subSettings = moduleElement.getSubSettings();

        TransporterModulesMenu.addSetting(moduleElement);
    }
}
