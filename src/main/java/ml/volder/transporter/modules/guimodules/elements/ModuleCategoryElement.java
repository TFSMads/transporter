package ml.volder.transporter.modules.guimodules.elements;

import ml.volder.transporter.gui.elements.BooleanElement;
import ml.volder.transporter.gui.elements.ControlElement;
import ml.volder.transporter.gui.elements.SettingsElement;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;

public class ModuleCategoryElement extends SettingsElement {

    private ControlElement.IconData iconData;

    public ModuleCategoryElement(String displayName, ControlElement.IconData iconData) {
        super(displayName, (String)null);
        this.iconData = iconData;
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int absoluteY = y + 7;
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(200, 200, 200, this.mouseOver ? 50 : 30));
        int imageSize = maxY - y;
        if (this.iconData.hasTextureIcon()) {
            drawAPI.bindTexture(this.iconData.getTextureIcon());
            drawAPI.drawTexture((double) (x + 2), (double) (y + 2), 256.0D, 256.0D, 18.0D, 18.0D);
        } else if (this.iconData.hasMaterialIcon()) {
            drawAPI.drawItem(this.iconData.getMaterialIcon(), this.iconData.getItemDamage(), x + 3, y + 3, null);
        }
        drawAPI.drawString(this.getDisplayName(), (double)(x + imageSize + 5), (double)absoluteY);
        drawAPI.drawRightString(getSubSettings().getElements().stream().filter(element -> {
            if(element instanceof BooleanElement) {
                return ((BooleanElement) element).getCurrentValue();
            }
            return false;
        }).count() + ModColor.cl("7") + "/" + ModColor.cl("f") + getSubSettings().getElements().size(), (double)(maxX - 5), (double)absoluteY);
    }

    @Override
    public void drawDescription(int var1, int var2, int var3) {

    }

    @Override
    public void mouseClicked(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public void mouseRelease(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public void mouseClickMove(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public void keyTyped(char var1, Key key) {

    }

    @Override
    public void unfocus(int var1, int var2, MouseButton mouseButton) {

    }

    @Override
    public int getEntryHeight() {
        return 22;
    }
}
