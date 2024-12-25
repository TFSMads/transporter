package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.utils.ColorUtils;

public class ListContainerElement extends ControlElement{

    private boolean openSubSettings = true;

    public ListContainerElement(String displayName, IconData iconData) {
        super(displayName, (String)null, iconData);
        this.setDisplayName(displayName);
        this.setSettingEnabled(true);
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        DrawAPI.getAPI().drawRectangle(x - 1, y, x, maxY, ColorUtils.toRGB(120, 120, 120, 120));
    }

    public int getObjectWidth() {
        return 0;
    }

    public boolean openSubSettings() {
        return openSubSettings;
    }

    public void setOpenSubSettings(boolean openSubSettings) {
        this.openSubSettings = openSubSettings;
    }
}
