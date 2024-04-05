package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ResourceLocation;


public abstract class SettingsElement {
    public static final ResourceLocation buttonTextures = ModTextures.WIDGETS;
    //public static final ResourceLocation BUTTON_PRESS_SOUND;
    protected Settings subSettings = new Settings();
    protected String displayName;
    private String descriptionText;
    protected boolean mouseOver;
    private int sortingId = 0;
    private boolean visible = true;
    private String configEntryName;

    public SettingsElement(String displayName, String description) {
        this.displayName = displayName;
        this.descriptionText = description;
        this.preInit();
    }

    public SettingsElement(String displayName) {
        this.displayName = displayName;
        this.preInit();
    }

    public SettingsElement bindDescription(String customDescription) {
        this.descriptionText = customDescription;
        return this;
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        this.mouseOver = mouseX > x && mouseX < maxX && mouseY > y && mouseY < maxY;
    }

    public abstract void drawDescription(int var1, int var2, int var3);

    public abstract void mouseClicked(int var1, int var2, MouseButton mouseButton);

    public abstract void mouseRelease(int var1, int var2, MouseButton mouseButton);

    public abstract void mouseClickMove(int var1, int var2, MouseButton mouseButton);

    public abstract void keyTyped(char var1, Key key);

    public abstract void unfocus(int var1, int var2, MouseButton mouseButton);

    public abstract int getEntryHeight();

    public void preInit() {
    }

    public void init() {
    }

    public void updateScreen() {
    }



    public Settings getSubSettings() {
        return this.subSettings;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDescriptionText() {
        return this.descriptionText;
    }

    public boolean isMouseOver() {
        return this.mouseOver;
    }

    public int getSortingId() {
        return this.sortingId;
    }

    public String getConfigEntryName() {
        return this.configEntryName;
    }

    public void setSubSettings(Settings subSettings) {
        this.subSettings = subSettings;
    }

    public boolean hasSubSettings() {
        return !this.subSettings.getElements().isEmpty();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setSortingId(int sortingId) {
        this.sortingId = sortingId;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setConfigEntryName(String configEntryName) {
        this.configEntryName = configEntryName;
    }

    public boolean shouldOpenSubSettings() {
        if(this instanceof ModuleElement) {
            return ((ModuleElement) this).getHoverButtonId() == 0;
        }else if(this instanceof ControlElement) {
            return ((ControlElement)this).getButtonAdvanced().isMouseOver() && ((ControlElement)this).getButtonAdvanced().isEnabled();
        }
        return false;
    }

    static {
        //BUTTON_PRESS_SOUND = new ResourceLocation("gui.button.press"/*Source.ABOUT_MC_VERSION.startsWith("1.8") ?  : "ui.button.click"*/); //TODO find press sound
    }
}
