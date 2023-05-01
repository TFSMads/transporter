package ml.volder.transporter.gui.elements;

import ml.volder.transporter.gui.ModTextures;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.types.ResourceLocation;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModuleElement extends SettingsElement{
    private boolean canHover = true;
    private boolean isActive = false;
    private int hoverButtonId = -1;
    private boolean showSettingsButton = true;
    private ResourceLocation icon;
    private Consumer<Boolean> toggleConsumer;

    public int getHoverButtonId() {
        return hoverButtonId;
    }

    public ModuleElement(String displayName, String description,ResourceLocation icon) {
        super(displayName, description);
        this.icon = icon;
    }

    public ModuleElement(String displayName, String description, ResourceLocation icon, Consumer<Boolean> toggleConsumer) {
        super(displayName, description);
        this.icon = icon;
        this.toggleConsumer = toggleConsumer;
    }

    @Override
    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        this.mouseOver = mouseX > x && mouseX < maxX && mouseY > y && mouseY < maxY && this.canHover;
        DrawAPI drawAPI = DrawAPI.getAPI();
        int iconWidth = this.getEntryHeight();
        int textLineY = y + 3;

        if(isActive) {
            drawAPI.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(50, 100, 50, this.isMouseOver() ? 90 : 70));
        } else {
            drawAPI.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(100, 50, 50, this.isMouseOver() ? 90 : 70));
        }

        this.drawIcon(icon, x, y, iconWidth, this.getEntryHeight());

        ModColor modColor = isActive ? ModColor.GREEN : ModColor.RED;

        String titleString = modColor + displayName;
        drawAPI.drawString(titleString, (double)(x + iconWidth + 5), (double)textLineY);
        textLineY += 15;

        drawAPI.drawString(modColor + (isActive ? "Denne feature er aktiv!" : "Denne feature er deaktiveret!"), (double)(x + iconWidth + 5), (double)(y + 12), 0.5D);
        List<String> descriptionLines = drawAPI.listFormattedStringToWidth(ModColor.createColors(this.getDescriptionText()), (int)((double)(maxX - x - iconWidth - 60) / 0.7D));
        int lineCount = 0;
        Iterator var18 = descriptionLines.iterator();

        while(var18.hasNext()) {
            String descriptionLine = (String)var18.next();
            if (lineCount >= 3) {
                descriptionLine = descriptionLine + "...";
            }

            drawAPI.drawString(ModColor.cl("7") + descriptionLine, (double)(x + iconWidth + 5), (double)textLineY, 0.7D);
            textLineY = (int)((double)textLineY + 7.0D);
            ++lineCount;
            if (lineCount >= 3) {
                break;
            }
        }

        this.hoverButtonId = -1;
        int marginX = 10;
        int marginY = (maxY - y - 14) / 2;
        if (this.drawButton(ModTextures.BUTTON_TOGGLE, y, 14, 6, marginX, marginY, maxX, maxY, mouseX, mouseY)) {
            this.hoverButtonId = 1;
        }

        marginX = 30;
        showSettingsButton = isActive && subSettings.getElements().size() > 0;
        if (showSettingsButton && this.drawButton(ModTextures.BUTTON_SETTINGS, y, 14, 6, marginX, marginY, maxX, maxY, mouseX, mouseY)) {
            this.hoverButtonId = 0;
        }
    }

    private boolean drawButton(ResourceLocation resourceLocation, int y, int buttonSize, int buttonPadding, int marginX, int marginY, int maxX, int maxY, int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        boolean hover = mouseX > maxX - buttonSize - marginX + 1 && mouseX < maxX - buttonSize + buttonSize - marginX + 1 && mouseY > y + marginY + 1 && mouseY < y + buttonSize + marginY + 1;
        marginX += hover ? 1 : 0;
        int colorA = hover ? ModColor.toRGB(10, 10, 10, 255) : ModColor.toRGB(220, 220, 220, 255);
        int colorB = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(0, 0, 0, 255);
        int colorC = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(180, 180, 180, 255);
        drawAPI.drawRectangle(maxX - buttonSize - marginX, y + marginY, maxX - marginX, y + buttonSize + marginY, colorA);
        drawAPI.drawRectangle(maxX - buttonSize - marginX + 1, y + marginY + 1, maxX - marginX + 1, y + buttonSize + marginY + 1, colorB);
        drawAPI.drawRectangle(maxX - buttonSize - marginX + 1, y + marginY + 1, maxX - marginX, y + buttonSize + marginY, colorC);
        drawAPI.bindTexture(resourceLocation);
        drawAPI.drawTexture((double)(maxX - buttonSize - marginX + buttonPadding / 2 + (hover ? 1 : 0)), (double)(y + marginY + buttonPadding / 2 + (hover ? 1 : 0)), 256.0D, 256.0D, (double)(buttonSize - buttonPadding), (double)(buttonSize - buttonPadding), 0.8F);
        return hover;
    }

    public void drawIcon(ResourceLocation resourceLocation, int x, int y, int width, int height) {
        DrawAPI.getAPI().bindTexture(resourceLocation);
        DrawAPI.getAPI().drawTexture((double)x, (double)y, 256.0D, 256.0D, (double)width, (double)height);
    }

    @Override
    public void drawDescription(int var1, int var2, int var3) {

    }

    @Override
    public void mouseClicked(int var1, int var2, MouseButton mouseButton) {
        switch(this.hoverButtonId) {
            case 1:
                isActive = !isActive;
                if(toggleConsumer != null)
                    toggleConsumer.accept(isActive);
            default:
                break;
        }
    }

    @Override
    public void mouseRelease(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public void mouseClickMove(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, Key key) {

    }

    @Override
    public void unfocus(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public int getEntryHeight() {
        return 40;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
