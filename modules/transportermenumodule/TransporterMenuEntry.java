package ml.volder.transporter.modules.transportermenumodule;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.TransporterMenuModule;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.types.ResourceLocation;

import java.util.Collections;

public class TransporterMenuEntry {
    private int width = 120;
    private int height = 37;

    private int outlineWidth = 1;

    private Item item;

    private boolean hoverGetButton = false;
    private boolean hoverPutButton = false;

    public TransporterMenuEntry(Item item, int width, int height) {
        this.item = item;
        this.width = width;
        this.height = height;
    }

    public void draw(int x, int y, int mouseX, int mouseY) {
        hoverText = null;
        hoverTextX = -1000;
        hoverTextY = -1000;
        if(item == null)
            return;
        boolean isMouseOver = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
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
        drawAPI.drawString(item.getDisplayName().length() >= 15 ? item.getDisplayName().substring(0, 14) : item.getDisplayName(), x + 32 + 4, y + 5, 0xFFFFFF);
        if(ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive())
            drawAPI.drawString("Du har " + getAmountString(), x + 32 + 4, y + 14, 0x808080, 0.8);
        this.hoverGetButton = this.drawButton(ModTextures.BUTTON_GET, x + 36 ,y + 22, 12, mouseX, mouseY);
        this.hoverPutButton = this.drawButton(ModTextures.BUTTON_PUT, x + 52 ,y + 22, 12, mouseX, mouseY);

        if(hoverGetButton || hoverPutButton) {
            hoverText = hoverGetButton
                    ? "Klik for at tage " + item.getDisplayName() + " fra din transporter!"
                    : "Klik for at gemme " + item.getDisplayName() + " i din transporter!";
            hoverTextX = mouseX;
            hoverTextY = mouseY;
        }
    }

    private String hoverText = null;
    private int hoverTextX = -1000;
    private int hoverTextY = -1000;

    public void drawHoverText() {
        if(hoverText == null || hoverTextX == -1000 || hoverTextY == -1000)
            return;
        DrawAPI.getAPI().drawHoverText(Collections.singletonList(hoverText), hoverTextX, hoverTextY);
    }

    private String getAmountString() {
        return item.getAmountInTransporter() == null ? "0" : FormatingUtils.formatNumber(item.getAmountInTransporter());
    }

    private boolean drawButton(ResourceLocation resourceLocation, int x, int y, int buttonSize, int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        boolean hover = mouseX > x && mouseX < x + buttonSize && mouseY > y && mouseY < y + buttonSize;
        int colorA = hover ? ModColor.toRGB(10, 10, 10, 255) : ModColor.toRGB(220, 220, 220, 255);
        int colorB = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(0, 0, 0, 255);
        int colorC = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(180, 180, 180, 255);
        drawAPI.drawRectangle(x, y, x + buttonSize, y + buttonSize, colorA);
        drawAPI.drawRectangle(x + 1, y + 1, x+ buttonSize + 1, y + buttonSize + 1, colorB);
        drawAPI.drawRectangle(x + 1, y + 1, x + buttonSize, y + buttonSize, colorC);
        drawAPI.bindTexture(resourceLocation);
        drawAPI.drawTexture(x + (hover ? 1 : 0), y + (hover ? 1 : 0), 256.0D, 256.0D, buttonSize, buttonSize, 0.8F);
        return hover;
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(!mouseButton.isLeft())
            return;
        if(hoverGetButton) {
            if(InputAPI.getAPI().isShiftKeyDown()) {
                PlayerAPI.getAPI().sendCommand("transporter get " + item.getName());
            } else {
                PlayerAPI.getAPI().sendCommand("transporter get " + item.getName() + " " + ModuleManager.getInstance().getModule(TransporterMenuModule.class).getWithdrawAmount());

            }
            PlayerAPI.getAPI().openGuiScreen(null);
        }else if (hoverPutButton) {
            PlayerAPI.getAPI().sendCommand("transporter put " + item.getName());
            PlayerAPI.getAPI().openGuiScreen(null);
        }
    }
}
