package ml.volder.transporter.modules.transportermenumodule;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.TransporterMenuModule;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.types.ResourceLocation;

import java.util.Collections;

public class TransporterMenuLayoutEntry extends ScrollableGrid.Entry {
    private int width;
    private int height;

    private int outlineWidth = 1;

    private Item item;

    private boolean hoverLeftButton = false;
    private boolean hoverTrashButton = false;
    private boolean hoverRightButton = false;
    private boolean isUpdating = false;

    private TransporterMenuLayout backgroundScreen;

    public TransporterMenuLayoutEntry(Item item, int width, int height, TransporterMenuLayout backgroundScreen) {
        this.item = item;
        this.width = width;
        this.height = height;
        this.backgroundScreen = backgroundScreen;
    }

    public void render(int x, int y, int mouseX, int mouseY) {
        hoverText = null;
        hoverTextX = -1000;
        hoverTextY = -1000;
        if(item == null)
            return;
        boolean isMouseOver = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        DrawAPI drawAPI = DrawAPI.getAPI();

        int color = !item.getAutoUpdateSellValue()
                ? ModColor.toRGB(100, 50, 50, isMouseOver ? 90 : 70)
                : ModColor.toRGB(50, 100, 50, isMouseOver ? 90 : 70);

        if(isUpdating)
            color = ModColor.toRGB(150, 90, 50, isMouseOver ? 90 : 70);

        drawAPI.drawRect(x, y, x + width, y + height, color);
        drawAPI.drawRect(x, y, x + width, y + outlineWidth, ModColor.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y + height - outlineWidth, x + width, y + height, ModColor.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y, x + outlineWidth, y + height, ModColor.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x + width - outlineWidth, y, x + width, y + height, ModColor.toRGB(128,128, 128, 255));

        drawAPI.drawItem(item.getMaterial(), item.getItemDamage(), x + 2, y + 3, "", 2);
        drawAPI.drawString(item.getDisplayName().length() >= 15 ? item.getDisplayName().substring(0, 14) : item.getDisplayName(), x + 32 + 4, y + 5, 0xFFFFFF);
        this.hoverLeftButton = this.drawButton(new ResourceLocation("transporter/textures/buttons/left-arrow.png"), x + 36 ,y + 22, 12, mouseX, mouseY);
        this.hoverTrashButton = this.drawButton(new ResourceLocation("transporter/textures/buttons/trash.png"), x + 52 ,y + 22, 12, mouseX, mouseY);
        this.hoverRightButton = this.drawButton(new ResourceLocation("transporter/textures/buttons/right-arrow.png"), x + 52 + 16 ,y + 22, 12, mouseX, mouseY);



        if(hoverLeftButton || hoverTrashButton || hoverRightButton) {
            hoverText = hoverTrashButton ? "Klik her for at fjerne " + item.getDisplayName() + " fra transporter menuen." : hoverText;
            hoverText = hoverLeftButton ? "Klik her for at flytte " + item.getDisplayName() + " til venstre."  : hoverText;
            hoverText = hoverRightButton ? "Klik her for at flytte " + item.getDisplayName() + " til højre."  : hoverText;
            hoverTextX = mouseX;
            hoverTextY = mouseY;
        }
    }

    private String hoverText = null;
    private int hoverTextX = -1000;
    private int hoverTextY = -1000;

    public void renderHoverText() {
        if(hoverText == null || hoverTextX == -1000 || hoverTextY == -1000)
            return;
        DrawAPI.getAPI().drawHoverText(Collections.singletonList(hoverText), hoverTextX, hoverTextY);
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
        if(resourceLocation != null) {
            drawAPI.bindTexture(resourceLocation);
            drawAPI.drawTexture(x + (hover ? 1 : 0), y + (hover ? 1 : 0), 256.0D, 256.0D, buttonSize, buttonSize, 0.8F);
        }
        return hover;
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(!mouseButton.isLeft())
            return;
        if(hoverLeftButton) {
            TransporterMenuModule module = ModuleManager.getInstance().getModule(TransporterMenuModule.class);
           //Move left
            int slot = module.getSlot(item);
            int newSlot = slot - 1;
            if(newSlot < 0)
                return;
            ModuleManager.getInstance().getModule(TransporterMenuModule.class).swap(slot, newSlot);
            backgroundScreen.flagChanged();
        }else if(hoverTrashButton) {
            //Remove item
            ModuleManager.getInstance().getModule(TransporterMenuModule.class).removeActiveItem(item);
            backgroundScreen.flagChanged();
        }else if (hoverRightButton) {
            TransporterMenuModule module = ModuleManager.getInstance().getModule(TransporterMenuModule.class);
            //Move right
            int slot = module.getSlot(item);
            int newSlot = slot + 1;
            if(!module.hasSlot(newSlot))
                return;
            ModuleManager.getInstance().getModule(TransporterMenuModule.class).swap(slot, newSlot);
            backgroundScreen.flagChanged();
        }
    }
}
