package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.types.ResourceLocation;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ControlElement extends SettingsElement{
    protected IconData iconData;
    private WrappedGuiButton buttonAdvanced;
    private boolean selected;
    private boolean hoverable;
    private boolean hideSubList;
    private boolean settingEnabled;
    private int lastMaxX;
    private Consumer<Boolean> advancedButtonCallback;
    private boolean blocked = false;

    public ControlElement(String elementName, String configEntryName, IconData iconData) {
        super(elementName, configEntryName);
        this.setConfigEntryName(configEntryName);
        this.iconData = iconData;
        this.createButton();
    }

    public ControlElement(IconData iconData, String displayName) {
        super(displayName, (String)null);
        this.iconData = iconData;
        this.createButton();
    }

    public ControlElement(String displayName, IconData iconData) {
        super(displayName, (String)null);
        this.iconData = iconData;
        this.createButton();
    }

    private void createButton() {
        this.buttonAdvanced = new WrappedGuiButton(-2, 0, 0, 23, 20, "");
    }

    public WrappedGuiButton getButtonAdvanced() {
        return this.buttonAdvanced;
    }

    public void setAdvancedButtonCallback(Consumer<Boolean> callback) {
        this.advancedButtonCallback = callback;
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        DrawAPI drawAPI = DrawAPI.getAPI();
        this.lastMaxX = maxX;
        if (this.displayName != null) {
            drawAPI.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(80, 80, 80, this.selected ? 130 : (this.hoverable && this.mouseOver ? 80 : 60)));
            int iconWidth = this.iconData != null ? 25 : 2;
            if (this.iconData != null) {
                if (this.iconData.hasTextureIcon()) {
                    drawAPI.bindTexture(this.iconData.getTextureIcon());
                    drawAPI.drawTexture((double)(x + 3), (double)(y + 3), 256.0D, 256.0D, 16.0D, 16.0D);
                } else if (this.iconData.hasMaterialIcon()) {
                    drawAPI.drawItem(this.iconData.getMaterialIcon(), iconData.getItemDamage(), (double)(x + 3), (double)(y + 2), (String)null);
                }
            }

            List<String> list = drawAPI.listFormattedStringToWidth(this.getDisplayName().isEmpty() ? ModColor.cl("4") + "Unknown" : this.getDisplayName(), maxX - (x + iconWidth) - this.getObjectWidth() - 5 - (this.hasSubList() ? iconWidth : 0));
            int listY = y + 7 - ((list.size() > 2 ? 2 : list.size()) - 1) * 5;
            int i = 0;
            Iterator var11 = list.iterator();

            while(var11.hasNext()) {
                String line = (String)var11.next();
                drawAPI.drawString(line, (double)(x + iconWidth), (double)listY);
                listY += 10;
                ++i;
                if (i > 1) {
                    break;
                }
            }

            this.renderAdvancedButton(x, y, maxX - this.getObjectWidth(), maxY, this.mouseOver, mouseX, mouseY);
            this.hideSubList = false;
            boolean blocked = this.blocked;
            /*if (!this.permissions.isEmpty()) {
                boolean defaultDisabled = false;
                boolean isEnabledByServer = true;
                String allowedPermissions = "";
                Iterator var15 = this.permissions.iterator();

                while(var15.hasNext()) {
                    Permissions.Permission permission = (Permissions.Permission)var15.next();
                    if (!permission.isDefaultEnabled()) {
                        defaultDisabled = true;
                    }

                    if (!allowedPermissions.isEmpty()) {
                        allowedPermissions = allowedPermissions + ModColor.cl("7") + ", ";
                    }

                    allowedPermissions = allowedPermissions + ModColor.cl("e") + permission.getDisplayName();
                    if (!Permissions.isAllowed(permission)) {
                        isEnabledByServer = false;
                    }
                }

                if (defaultDisabled || !isEnabledByServer) {
                    boolean hover = mouseX > x - 13 && mouseX < x - 13 + 7 && mouseY > y + 3 && mouseY < y + 3 + 16;
                    if (isEnabledByServer) {
                        drawAPI.bindTexture(ModTextures.BUTTON_CHECKBOX);
                        drawAPI.drawTexture((double)(x - 13), (double)(y + 7), 0.0D, 0.0D, 255.0D, 255.0D, 10.0D, 10.0D);
                    } else {
                        drawAPI.bindTexture(ModTextures.BUTTON_EXCLAMATION);
                        drawAPI.drawTexture((double)(x - 13), (double)(y + 3), hover ? 127.0D : 0.0D, 0.0D, 127.0D, 255.0D, 7.0D, 16.0D);
                        if (!MinecraftAPI.getAPI().isSingleplayer() && MinecraftAPI.getAPI().isInGame()) {
                            blocked = true;
                        }
                    }

                    if (hover) {
                        String text = "Enabled";
                        TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, (String[])((String[])draw.listFormattedStringToWidth(text, draw.getWidth() / 3).toArray()));
                    }
                }
            }*/

            if (blocked) {
                drawAPI.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(0, 0, 0, 200));
                drawAPI.drawString(ModColor.RED + "✖ " + "Fra" + " ✖", (double)(x + 5), (double)(y + (maxY - y) / 2 - 4));
            }
        }

    }

    private void renderAdvancedButton(int x, int y, int maxX, int maxY, boolean mouseOver, int mouseX, int mouseY) {
        if (this.hasSubList()) {
            if (this.buttonAdvanced != null) {
                if (!this.hideSubList) {
                    boolean enabled = this.settingEnabled;

                    this.buttonAdvanced.setX(maxX - this.getSubListButtonWidth() - 2);
                    this.buttonAdvanced.setY(y + 1);

                    this.buttonAdvanced.setEnabled(enabled);
                    buttonAdvanced.drawButton(mouseX, mouseY);

                    DrawAPI drawAPI = DrawAPI.getAPI();

                    drawAPI.bindTexture(ModTextures.BUTTON_ADVANCED);
                    drawAPI.drawTexture((double)(this.buttonAdvanced.getX() + 4), (double)(this.buttonAdvanced.getY() + 3), 0.0D, 0.0D, 256.0D, 256.0D, 14.0D, 14.0D, 2.0F);
                }
            }
        }
    }

    public int getObjectWidth() {
        return 50;
    }

    public int getSubListButtonWidth() {
        return this.hasSubList() ? 23 : 0;
    }

    public void drawDescription(int x, int y, int screenWidth) {
        String description = this.getDescriptionText();
        if (description != null) {
            if (this.buttonAdvanced == null || !this.buttonAdvanced.isMouseOver()) {
                if (x <= this.lastMaxX - this.getObjectWidth() - 2) {
                    DrawAPI drawAPI = DrawAPI.getAPI();
                    List<String> list = drawAPI.listFormattedStringToWidth(description, screenWidth / 3);
                    //TooltipHelper.getHelper().pointTooltip(x, y, 500L, (String[])((String[])list.toArray())); TODO implement tooltip in api
                }
            }
        }
    }

    public void keyTyped(char typedChar, Key key) {
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public int getEntryHeight() {
        return 23;
    }

    public boolean hasSubList() {
        return this.advancedButtonCallback != null || !this.subSettings.getElements().isEmpty() && !this.hideSubList;
    }

    public ControlElement setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public ControlElement setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
        return this;
    }

    public ControlElement hideSubListButton() {
        this.hideSubList = true;
        return this;
    }

    public void mouseRelease(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void unfocus(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public IconData getIconData() {
        return this.iconData;
    }

    public void setIconData(IconData iconData) {
        this.iconData = iconData;
    }

    public void setSettingEnabled(boolean settingEnabled) {
        this.settingEnabled = settingEnabled;
    }

    public Consumer<Boolean> getAdvancedButtonCallback() {
        return this.advancedButtonCallback;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public static class IconData {
        private Material materialIcon;
        private int itemDamage;
        private ResourceLocation textureIcon;

        public IconData(Material materialIcon) {
            this.materialIcon = materialIcon;
        }

        public IconData(ResourceLocation textureIcon) {
            this.textureIcon = textureIcon;
        }

        public IconData(String resourceLocationPath) {
            this.textureIcon = new ResourceLocation(resourceLocationPath);
        }

        public Material getMaterialIcon() {
            return this.materialIcon;
        }

        public ResourceLocation getTextureIcon() {
            return this.textureIcon;
        }

        public boolean hasTextureIcon() {
            return this.textureIcon != null;
        }

        public boolean hasMaterialIcon() {
            return this.materialIcon != null;
        }

        public int getItemDamage() {
            return itemDamage;
        }

        public void setItemDamage(int itemDamage) {
            this.itemDamage = itemDamage;
        }
    }
}
