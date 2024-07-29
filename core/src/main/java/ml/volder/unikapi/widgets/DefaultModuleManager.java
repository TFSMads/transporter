package ml.volder.unikapi.widgets;


import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.guisystem.elements.ControlElement;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.editor.GuiEditor;
import ml.volder.unikapi.widgets.elements.ModuleCategoryElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DefaultModuleManager implements ModuleManager{

    public DefaultModuleManager() {
        instance = this;
        GuiModuleRenderer guiModuleRenderer = new GuiModuleRenderer(this, DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "guiModule" + ".json")));
        EventManager.registerEvents(guiModuleRenderer);
    }

    private List<GuiModule> guiModuleList = new ArrayList<>();
    private List<ModuleCategoryElement> guiModuleCategories = new ArrayList<>();

    public List<GuiModule> getGuiModuleList() {
        return guiModuleList;
    }

    private static DefaultModuleManager instance;

    public static GuiModule getModuleByKey(String key) {
        for(GuiModule guiModule : instance.guiModuleList)
            if(guiModule.getKey().equals(key))
                return guiModule;
        return null;
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

    private final DataManager<Data> dataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "guiModule" + ".json"));

    @Override
    public Object registerCategory(String displayName, Material icon, String description) {
        ModuleCategoryElement category = new ModuleCategoryElement(displayName, new ControlElement.IconData(icon));
        getGuiModuleCategories().add(category);
        return category;
    }

    @Override
    public void registerModule(String key, String defaultPrefix, boolean defaultIsEnabled, Object category, Material icon, Function<String, String> getDisplayValue) {


        if(!(category instanceof ModuleCategoryElement))
            return;
        GuiModule module = new GuiModule(2,2, key, defaultPrefix, defaultIsEnabled, dataManager, (ModuleCategoryElement) category) {
            @Override
            public String getDisplayValue() {
                return getDisplayValue.apply("");
            }
        };
        ControlElement.IconData iconData = new ControlElement.IconData(icon);
        iconData.setItemDamage(icon.getItemDamage(0));
        module.setIconData(iconData);
        getGuiModuleList().add(module);
    }

    @Override
    public void openEditor() {
        PlayerAPI.getAPI().openGuiScreen(new GuiEditor(this, PlayerAPI.getAPI().getCurrentScreen()));
    }

    public static DataManager<Data> getDataManager() {
        if(instance != null && instance.dataManager != null)
            return instance.dataManager;
        return DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "guiModule" + ".json"));
    }

}
