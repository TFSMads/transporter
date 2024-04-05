package ml.volder.unikapi.guisystem.elements;

import ml.volder.unikapi.types.ResourceLocation;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;

import java.util.ArrayList;
import java.util.Iterator;

public class SettingsCategory {
    private String title;
    private Settings settings;
    private ArrayList<SettingsCategory> subList;
    private WrappedGuiButton guiButton;
    private SettingsCategory parentCategory;
    private ResourceLocation resourceLocation;
    private int iconSize;

    public SettingsCategory(String title) {
        this(title, (SettingsCategory)null);
    }

    public SettingsCategory(String title, SettingsCategory parentCategory) {
        this.settings = new Settings();
        this.iconSize = 10;
        this.title = title;
        this.subList = new ArrayList();
        this.parentCategory = parentCategory;
    }

    public SettingsCategory setParent(SettingsCategory parentCategory) {
        this.parentCategory = parentCategory;
        return this;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public SettingsCategory setSettings(Settings openSettings) {
        this.settings = openSettings;
        return this;
    }

    public SettingsCategory setSettings(ArrayList<SettingsElement> elements) {
        this.settings = new Settings(elements.toArray(new SettingsElement[elements.size()]));
        return this;
    }

    public SettingsCategory addSetting(SettingsElement settingsElement) {
        this.settings.add(settingsElement);
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ArrayList<SettingsCategory> getSubList() {
        return this.subList;
    }

    public void setGuiButton(WrappedGuiButton guiButton) {
        this.guiButton = guiButton;
    }

    public WrappedGuiButton getGuiButton() {
        return this.guiButton;
    }

    public SettingsCategory getParentCategory() {
        return this.parentCategory;
    }

    public boolean isCategoryElementsOnly() {
        Iterator var1 = this.settings.getElements().iterator();

        SettingsElement element;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            element = (SettingsElement) var1.next();
        } while(element instanceof CategorySettingsElement);

        return false;
    }

    public SettingsCategory setIcon(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public SettingsCategory setIconSize(int iconSize) {
        this.iconSize = iconSize;
        return this;
    }



    /*public void bindCustomBooleanToAll(String... args) {
        Iterator var2 = this.settings.getElements().iterator();

        while(var2.hasNext()) {
            net.labymod.settings.elements.SettingsElement element = (SettingsElement)var2.next();
            if (element instanceof BooleanElement) {
                ((BooleanElement)element).custom(args);
            }
        }

    }*/

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public int getIconSize() {
        return this.iconSize;
    }
}
