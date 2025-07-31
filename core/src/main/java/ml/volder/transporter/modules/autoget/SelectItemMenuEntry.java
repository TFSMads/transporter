package ml.volder.transporter.modules.autoget;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.utils.ColorUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.render.matrix.Stack;

public class SelectItemMenuEntry extends ScrollableGrid.Entry {
    private int width = 120;
    private int height = 37;

    private int outlineWidth = 1;
    private boolean isMouseOver = false;

    private Item item;
    private ScreenInstance lastScreen;

    public SelectItemMenuEntry(Item item, int width, int height, ScreenInstance lastScreen) {
        this.item = item;
        this.width = width;
        this.height = height;
        this.lastScreen = lastScreen;
    }

    public void render(int x, int y, int mouseX, int mouseY, Stack stack) {
        if(item == null)
            return;
        isMouseOver = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        DrawAPI drawAPI = DrawAPI.getAPI();
        ColorUtils.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        int color = item.getAmountInTransporter() == null || item.getAmountInTransporter() <= 0
                ? ColorUtils.toRGB(100, 50, 50, isMouseOver ? 90 : 70)
                : ColorUtils.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        if(!ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive())
            color = ColorUtils.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        drawAPI.drawRect(x, y, x + width, y + height, color);
        drawAPI.drawRect(x, y, x + width, y + outlineWidth, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y + height - outlineWidth, x + width, y + height, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y, x + outlineWidth, y + height, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x + width - outlineWidth, y, x + width, y + height, ColorUtils.toRGB(128,128, 128, 255));

        drawAPI.drawItem(item.getMaterial(), item.getItemDamage(), x + 2, y + 3, "", 2);
        drawAPI.drawString(item.getDisplayName().length() >= 15 ? item.getDisplayName().substring(0, 14) : item.getDisplayName(), x + 32 + 4, y + 5, 0xFFFFFF);
        if(ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive())
            drawAPI.drawString("Du har " + getAmountString(), x + 32 + 4, y + 14, 0x808080, 0.8);
    }

    private String getAmountString() {
        return item.getAmountInTransporter() == null ? "0" : FormatingUtils.formatNumber(item.getAmountInTransporter());
    }

    @Override
    public void renderHoverText() {

    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(!mouseButton.isLeft() || !isMouseOver)
            return;
        if(lastScreen instanceof AutoGetMenu) {
            ((AutoGetMenu)lastScreen).selectItem(item);
        }
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(lastScreen);
    }
}
