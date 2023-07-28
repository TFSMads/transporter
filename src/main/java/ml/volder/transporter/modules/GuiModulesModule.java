package ml.volder.transporter.modules;

import ml.volder.transporter.gui.TransporterModulesMenu;

import ml.volder.transporter.modules.guimodules.ModuleRegistry;
import ml.volder.transporter.modules.serverlistmodule.ServerSelecterGui;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.ControlElement;
import ml.volder.unikapi.guisystem.elements.ListContainerElement;
import ml.volder.unikapi.guisystem.elements.ModuleElement;
import ml.volder.unikapi.guisystem.elements.Settings;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;

import java.util.function.Consumer;

public class GuiModulesModule extends SimpleModule {
    private boolean isFeatureActive;
    private ModuleRegistry moduleManager;

    public GuiModulesModule(String moduleName) {
        super(moduleName);
        instance = this;
        fillSettings();
        moduleManager = new ModuleRegistry(this);
        moduleManager.registerCategories();
        moduleManager.registerModules();
    }



    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    private static GuiModulesModule instance;


    private void open() {
        if(PlayerAPI.getAPI().hasOpenScreen())
            return;
        PlayerAPI.getAPI().openGuiScreen(new ServerSelecterGui(getDataManager()));
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Gui Moduler", "En feature der kan vise relevant info omkring din transporter.", ModTextures.MISC_HEAD_QUESTION, isActive -> {
            isFeatureActive = isActive;
            setConfigEntry("isFeatureActive", isFeatureActive);
        });
        moduleElement.setActive(isFeatureActive);
        Settings subSettings = moduleElement.getSubSettings();

        ListContainerElement editorGuiButton = new ListContainerElement("Gui Editor", new ControlElement.IconData(Material.PAINTING));
        editorGuiButton.setAdvancedButtonCallback(aBoolean -> ModuleSystem.openEditor());

        subSettings.add(editorGuiButton);

        TransporterModulesMenu.addSetting(moduleElement);
    }


    public boolean isFeatureActive() {
        return isFeatureActive;
    }

}
