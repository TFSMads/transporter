package ml.volder.transporter.modules.autoget;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

public class SelectItemMenuEntry {
    private int width = 120;
    private int height = 37;

    private int outlineWidth = 1;
    private boolean isMouseOver = false;

    private Item item;
    private WrappedGuiScreen lastScreen;

    public SelectItemMenuEntry(Item item, int width, int height, WrappedGuiScreen lastScreen) {
        this.item = item;
        this.width = width;
        this.height = height;
        this.lastScreen = lastScreen;
    }

    public void draw(int x, int y, int mouseX, int mouseY) {
        if(item == null)
            return;
        isMouseOver = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        DrawAPI drawAPI = DrawAPI.getAPI();
        ModColor.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        int color = item.getAmountInTransporter() == null || item.getAmountInTransporter() <= 0
                ? ModColor.toRGB(100, 50, 50, isMouseOver ? 90 : 70)
                : ModColor.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        if(!ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive())
            color = ModColor.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        drawAPI.drawRect(x, y, x + width, y + height, color);
        drawAPI.drawRect(x, y, x + width, y + outlineWidth, ModColor.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y + height - outlineWidth, x + width, y + height, ModColor.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y, x + outlineWidth, y + height, ModColor.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x + width - outlineWidth, y, x + width, y + height, ModColor.toRGB(128,128, 128, 255));

        drawAPI.drawItem(item.getMaterial(), item.getItemDamage(), x + 2, y + 3, "", 2);
        drawAPI.drawString(item.getDisplayName(), x + 32 + 4, y + 5, 0xFFFFFF);
        if(ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive())
            drawAPI.drawString("Du har " + getAmountString(), x + 32 + 4, y + 14, 0x808080, 0.8);
    }

    private String getAmountString() {
        return item.getAmountInTransporter() == null ? "0" : FormatingUtils.formatNumber(item.getAmountInTransporter());
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(!mouseButton.isLeft() || !isMouseOver)
            return;
        if(lastScreen instanceof AutoGetMenu) {
            ((AutoGetMenu)lastScreen).selectItem(item);
        }
        PlayerAPI.getAPI().openGuiScreen(lastScreen);
    }
}
