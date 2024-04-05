package ml.volder.transporter.modules;

import ml.volder.transporter.modules.guimodules.ModuleRegistry;
import ml.volder.unikapi.guisystem.elements.BooleanElement;
import ml.volder.unikapi.guisystem.elements.ControlElement;
import ml.volder.unikapi.guisystem.elements.ListContainerElement;
import ml.volder.unikapi.guisystem.elements.Settings;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;

public class GuiModulesModule extends SimpleModule {
    private boolean viewOnSelected = true;
    private ModuleRegistry moduleManager;


    public GuiModulesModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }


    @Override
    public SimpleModule init() {
        moduleManager = new ModuleRegistry(this);
        moduleManager.registerCategories();
        return this;
    }

    @Override
    public SimpleModule enable() {
        moduleManager.registerModules();
        return this;
    }

    @Override
    public void fillSettings(Settings subSettings) {
        ListContainerElement editorGuiButton = new ListContainerElement("Gui Editor", new ControlElement.IconData(Material.PAINTING));
        editorGuiButton.setAdvancedButtonCallback(aBoolean -> ModuleSystem.openEditor());

        subSettings.add(editorGuiButton);

        BooleanElement viewOnSelected = new BooleanElement(
                "Vis kun i lobbyer!",
                getDataManager(),
                "viewOnSelected",
                new ControlElement.IconData(Material.DIODE),
                true
        );
        this.viewOnSelected = viewOnSelected.getCurrentValue();
        viewOnSelected.addCallback(b -> this.viewOnSelected = b);
        subSettings.add(viewOnSelected);


    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public boolean isViewOnSelected() {
        return viewOnSelected;
    }

    public static ModuleRegistry getModuleRegistry() {
       return ModuleManager.getInstance().getModule(GuiModulesModule.class).moduleManager;
    }
}
