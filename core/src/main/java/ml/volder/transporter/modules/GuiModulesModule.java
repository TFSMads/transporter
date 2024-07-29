package ml.volder.transporter.modules;

import ml.volder.transporter.modules.guimodules.ModuleRegistry;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.guisystem.ModTextures;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;

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
    public void fillSettings(SettingRegistryAccessor subSettings) {
        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.viewOnSelected = b),
                        getDataManager(),
                        "viewOnSelected",
                        true))
                .id("viewOnSelected")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 4, 1))
                .build()
        );
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
