package ml.volder.transporter.gui.elements;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;

public class HeaderElement extends SettingsElement{
    private double textSize;

    public HeaderElement(String displayName, double textSize) {
        super(displayName, (String)null);
        this.textSize = textSize;
    }

    public HeaderElement(String displayName) {
        this(displayName, 1.0D);
    }

    public void init() {
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int absoluteY = y + 7;
        DrawAPI.getAPI().drawCenteredString(this.getDisplayName(), (double)(x + (maxX - x) / 2), (double)absoluteY, this.textSize);
    }

    public int getEntryHeight() {
        return 22;
    }

    public void drawDescription(int x, int y, int screenWidth) {
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void keyTyped(char typedChar, Key keyCode) {
    }

    public void mouseRelease(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void unfocus(int mouseX, int mouseY, MouseButton mouseButton) {
    }
}
