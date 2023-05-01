package ml.volder.transporter.gui.elements;

import ml.volder.transporter.gui.ModTextures;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;

import java.util.Iterator;
import java.util.List;

public class CategorySettingsElement extends SettingsElement {
    private SettingsCategory category;
    private ClickedCallback callback;

    public CategorySettingsElement(SettingsCategory category, ClickedCallback callback) {
        super(category.getTitle(), (String)null);
        this.category = category;
        this.callback = callback;
    }

    public void init() {
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        DrawAPI drawAPI = DrawAPI.getAPI();
        double textureSize = 292.0D;
        double textureScale = 36.0D;
        int elementWidth = maxX - x;
        int elementHeight = maxY - y;
        drawAPI.bindTexture(this.isMouseOver() ? ModTextures.BUTTON_LARGE_PRESSED : ModTextures.BUTTON_LARGE);
        drawAPI.drawTexture((double)x, (double)y, textureSize / 200.0D * (double)elementWidth / 2.0D, textureSize / 200.0D * (double)elementHeight / 2.0D, (double)(elementWidth / 2), (double)(elementHeight / 2));
        drawAPI.drawTexture((double)x, (double)(y + elementHeight / 2), 0.0D, -textureScale + textureSize - textureSize / 200.0D * (double)elementHeight / 2.0D, textureSize / 200.0D * (double)elementWidth / 2.0D, textureSize / 200.0D * (double)elementHeight / 2.0D, (double)(elementWidth / 2), (double)(elementHeight / 2));
        drawAPI.drawTexture((double)(x + elementWidth / 2), (double)y, -textureScale + textureSize - textureSize / 200.0D * (double)elementWidth / 2.0D, 0.0D, textureSize / 200.0D * (double)elementWidth / 2.0D, textureSize / 200.0D * (double)elementHeight / 2.0D, (double)(elementWidth / 2), (double)(elementHeight / 2));
        drawAPI.drawTexture((double)(x + elementWidth / 2), (double)(y + elementHeight / 2), -textureScale + textureSize - textureSize / 200.0D * (double)elementWidth / 2.0D, -textureScale + textureSize - textureSize / 200.0D * (double)elementHeight / 2.0D, textureSize / 200.0D * (double)elementWidth / 2.0D, textureSize / 200.0D * (double)elementHeight / 2.0D, (double)(elementWidth / 2), (double)(elementHeight / 2));
        if (this.category.getResourceLocation() != null) {
            drawAPI.bindTexture(this.category.getResourceLocation());
            drawAPI.drawTexture((double)(x + 2), (double)(y + 1), 256.0D, 256.0D, (double)(maxY - y - 4), (double)(maxY - y - 4));
        }

        List<String> list = drawAPI.listFormattedStringToWidth(this.category.getTitle(), maxX - x - 64 + 25, 2);
        int posY = list.size() * -5 + 5;

        for(Iterator var16 = list.iterator(); var16.hasNext(); posY += 10) {
            String line = (String)var16.next();
            drawAPI.drawString(line, x + maxY - y + 2, y + 7 + posY, this.isMouseOver() ? 16777120 : 14737632);
        }

    }

    public int getEntryHeight() {
        return 22;
    }

    public void drawDescription(int x, int y, int screenWidth) {
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if (this.isMouseOver()) {
            this.callback.clicked(this.category);
        }

    }

    public void keyTyped(char typedChar, Key key) {
    }

    public void mouseRelease(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public void unfocus(int mouseX, int mouseY, MouseButton mouseButton) {
    }

    public interface ClickedCallback {
        void clicked(SettingsCategory var1);
    }
}