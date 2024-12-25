package ml.volder.transporter.gui.pricegui;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.TransporterActivity;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.guisystem.elements.ModTextField;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ResourceLocation;
import ml.volder.unikapi.utils.ColorUtils;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.render.matrix.Stack;

import java.util.Collections;

public class PriceMenuEntry extends ScrollableGrid.Entry {
    private int width = 120;
    private int height = 37;

    private int outlineWidth = 1;

    private Item item;

    private boolean hoverUpdateButton = false;
    private boolean hoverSetButton = false;
    private boolean hoverAutoButton = false;
    private boolean isUpdating = false;
    private PriceMenu backgroundScreen;


    public PriceMenuEntry(PriceMenu priceMenu, Item item, int width, int height) {
        this.backgroundScreen = priceMenu;
        this.item = item;
        this.width = width;
        this.height = height;
    }

    public void render(int x, int y, int mouseX, int mouseY, Stack stack) {
        hoverText = null;
        hoverTextX = -1000;
        hoverTextY = -1000;
        if(item == null)
            return;
        boolean isMouseOver = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        if(backgroundScreen.isInBackground())
            isMouseOver = false;
        DrawAPI drawAPI = DrawAPI.getAPI();

        int color = !item.getAutoUpdateSellValue()
                ? ColorUtils.toRGB(100, 50, 50, isMouseOver ? 90 : 70)
                : ColorUtils.toRGB(50, 100, 50, isMouseOver ? 90 : 70);

        if(isUpdating)
            color = ColorUtils.toRGB(150, 90, 50, isMouseOver ? 90 : 70);

        drawAPI.drawRect(x, y, x + width, y + height, color);
        drawAPI.drawRect(x, y, x + width, y + outlineWidth, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y + height - outlineWidth, x + width, y + height, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y, x + outlineWidth, y + height, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x + width - outlineWidth, y, x + width, y + height, ColorUtils.toRGB(128,128, 128, 255));

        drawAPI.drawItem(item.getMaterial(), item.getItemDamage(), x + 2, y + 3, "", 2);
        drawAPI.drawString(item.getDisplayName().length() >= 15 ? item.getDisplayName().substring(0, 14) : item.getDisplayName(), x + 32 + 4, y + 5, 0xFFFFFF);
        drawAPI.drawString("Værdi: " + getValueString(), x + 32 + 4, y + 14, 0x808080, 0.8);
        this.hoverUpdateButton = this.drawButton(new ResourceLocation("transporter/textures/buttons/reload.png"), x + 36 ,y + 22, 12, mouseX, mouseY);
        this.hoverSetButton = this.drawButton(new ResourceLocation("transporter/textures/buttons/settings.png"), x + 52 ,y + 22, 12, mouseX, mouseY);
        this.hoverAutoButton = this.drawButton(new ResourceLocation("transporter/textures/buttons/toggle2.png"), x + 52 + 16 ,y + 22, 12, mouseX, mouseY);



        if(hoverUpdateButton || hoverSetButton || hoverAutoButton) {
            hoverText = hoverUpdateButton ? "Klik her for at opdatere prisen fra pris serveren!" : hoverText;
            hoverText = hoverSetButton ? "Klik her for at sætte prisen for " + item.getDisplayName() : hoverText;
            hoverText = hoverAutoButton ? "Klik her for at " + (item.getAutoUpdateSellValue() ? "deaktivere" : "aktivere") + " automatisk pris opdatering." : hoverText;
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

    private String getValueString() {
        return item.getSellValue() == null ? "0 EMs" : FormatingUtils.formatNumber(item.getSellValue()) + " EMs";
    }

    private boolean drawButton(ResourceLocation resourceLocation, int x, int y, int buttonSize, int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        boolean hover = mouseX > x && mouseX < x + buttonSize && mouseY > y && mouseY < y + buttonSize;
        if(backgroundScreen.isInBackground())
            hover = false;
        int colorA = hover ? ColorUtils.toRGB(10, 10, 10, 255) : ColorUtils.toRGB(220, 220, 220, 255);
        int colorB = hover ? ColorUtils.toRGB(150, 150, 150, 255) : ColorUtils.toRGB(0, 0, 0, 255);
        int colorC = hover ? ColorUtils.toRGB(150, 150, 150, 255) : ColorUtils.toRGB(180, 180, 180, 255);
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
        if(hoverUpdateButton) {
            updateSellValueFromPriceServer();
        }else if(hoverAutoButton) {
            item.setAutoUpdateSellValue(!item.getAutoUpdateSellValue());
        }else if (hoverSetButton) {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new SetSellValueGui(item, backgroundScreen));
        }
    }

    public void updateSellValueFromPriceServer() {
        isUpdating = true;
        item.updateSellValueFromPriceServer(integer -> isUpdating = false);
        item.setAutoUpdateSellValue(true);
    }

    @AutoActivity
    public class SetSellValueGui extends TransporterActivity {
        private PriceMenu backgroundScreen;
        private ModTextField expandedField;

        Item item;

        public SetSellValueGui(Item item, PriceMenu backgroundScreen) {
            this.backgroundScreen = backgroundScreen;
            this.item = item;
        }

        public void initGui() {
            backgroundScreen.setInBackground(true);
            this.backgroundScreen.setWidth(this.getWidth());
            this.backgroundScreen.setHeight(this.getHeight());
            this.expandedField = new ModTextField(0, this.getWidth() / 2 - 150, this.getHeight() / 4 + 45, 300, 20);
            this.expandedField.setFocused(true);
            this.expandedField.setText(item.getSellValue().toString());
            this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 50, this.getHeight() / 4 + 85, 100, 20, "Done"));
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            DrawAPI drawAPI = DrawAPI.getAPI();
            drawAPI.drawRect(0.0, 0.0, (double)this.getWidth(), (double)this.getHeight(), Integer.MIN_VALUE);
            drawAPI.drawRect((double)(this.getWidth() / 2 - 165), (double)(this.getHeight() / 4 + 35), (double)(this.getWidth() / 2 + 165), (double)(this.getHeight() / 4 + 120), Integer.MIN_VALUE);
            this.expandedField.drawTextBox();
        }

        public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
            this.expandedField.mouseClicked(mouseX, mouseY, mouseButton);
            try {
                int i = Integer.parseInt(this.expandedField.getText());
                if(i != item.getSellValue())
                    item.setAutoUpdateSellValue(false);
                item.setSellValue(i);
            }catch (Exception ignored) {}
        }

        public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        }

        public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        }

        public void handleMouseInput() {
        }

        public void keyTyped(char typedChar, Key key) {
            if (key.equals(Key.ESCAPE)) {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.backgroundScreen);
            }

            //Return if key is not a number
            if(!Character.isDigit(typedChar) && !key.equals(Key.BACK) && !key.equals(Key.DELETE))
                return;

            if (this.expandedField.textboxKeyTyped(typedChar, key)) {
                try {
                    int i = Integer.parseInt(this.expandedField.getText());
                    if(i != item.getSellValue())
                        item.setAutoUpdateSellValue(false);
                    item.setSellValue(i);
                }catch (Exception ignored) {}
            }

        }

        public void onGuiClosed() {}

        public void updateScreen() {
            this.backgroundScreen.updateScreen();
            this.expandedField.updateCursorCounter();
        }

        public void actionPerformed(WrappedGuiButton button) {
            if (button.getId() == 1) {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.backgroundScreen);
            }
        }

        public TransporterActivity getBackgroundScreen() {
            return this.backgroundScreen;
        }
    }
}
