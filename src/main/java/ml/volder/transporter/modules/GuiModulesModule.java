package ml.volder.transporter.modules;

import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.*;
import ml.volder.transporter.modules.guimodules.GuiModule;
import ml.volder.transporter.modules.guimodules.GuiModuleRenderer;
import ml.volder.transporter.modules.guimodules.ModuleManager;
import ml.volder.transporter.modules.guimodules.editor.GuiEditor;
import ml.volder.transporter.modules.guimodules.elements.ModuleCategoryElement;
import ml.volder.transporter.modules.serverlistmodule.ServerSelecterGui;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.types.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiModulesModule extends SimpleModule {
    private boolean isFeatureActive;
    private GuiModuleRenderer guiModuleRenderer;
    private ModuleManager moduleManager;

    private List<GuiModule> guiModuleList = new ArrayList<>();
    private List<ModuleCategoryElement> guiModuleCategories = new ArrayList<>();

    public GuiModulesModule(String moduleName) {
        super(moduleName);
        guiModuleRenderer = new GuiModuleRenderer(this, getDataManager());
        EventManager.registerEvents(guiModuleRenderer);
        fillSettings();
        moduleManager = new ModuleManager(this);
        moduleManager.registerCategories();
        moduleManager.registerModules();
    }



    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    public List<GuiModule> getGuiModuleList() {
        return guiModuleList;
    }

    private void open() {
        if(PlayerAPI.getAPI().hasOpenScreen())
            return;
        PlayerAPI.getAPI().openGuiScreen(new ServerSelecterGui(getDataManager()));
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Gui Moduler", "En feature der kan vise relevant info omkring din transporter.", ModTextures.MISC_HEAD_QUESTION, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isActive) {
                isFeatureActive = isActive;
                setConfigEntry("isFeatureActive", isFeatureActive);
            }
        });
        moduleElement.setActive(isFeatureActive);
        Settings subSettings = moduleElement.getSubSettings();

        ListContainerElement editorGuiButton = new ListContainerElement("Gui Editor", new ControlElement.IconData(Material.PAINTING));
        editorGuiButton.setAdvancedButtonCallback(aBoolean -> {
            PlayerAPI.getAPI().openGuiScreen(new GuiEditor(this, PlayerAPI.getAPI().getCurrentScreen()));
        });


        subSettings.add(editorGuiButton);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public List<ModuleCategoryElement> getGuiModuleCategories() {
        return guiModuleCategories;
    }

    public void addModule(GuiModule module, ModuleCategoryElement category) {
        module.setCategory(category);
        guiModuleList.add(module);
    }

    public void addModule(GuiModule module) {
        guiModuleList.add(module);
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

}
