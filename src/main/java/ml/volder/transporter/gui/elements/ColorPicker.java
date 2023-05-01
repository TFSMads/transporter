package ml.volder.transporter.gui.elements;

import ml.volder.transporter.gui.ModTextures;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.awt.*;
import java.util.function.Consumer;

public class ColorPicker {
    private static final int[] ADVANCED_COLORS = new int[]{-4842468, -7795121, -11922292, -13624430, -15064194, -15841375, -16754788, -16687004, -16757697, -14918112, -13407970, -8292586, -753898, -37120, -1683200, -4246004, -12704222, -14606047, -14208456};
    private String title;
    private int x;
    private int y;
    private int width;
    private int height;
    private Color selectedColor;
    private Color colorForPreview;
    private boolean openedSelector;
    private boolean hoverSlider = false;
    private boolean hasAdvanced = false;
    private boolean hoverAdvancedButton = false;
    private boolean hoverDefaultButton;
    private boolean hasDefault = false;
    private boolean isDefault = true;
    private DefaultColorCallback defaultColor;
    private Consumer<Color> updateListener;

    public ColorPicker(String title, Color selectedColor, DefaultColorCallback defaultColorCallback, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.selectedColor = selectedColor;
        this.colorForPreview = selectedColor;
        this.defaultColor = defaultColorCallback;
    }

    public void onGuiClosed() {
        if (this.colorForPreview != this.selectedColor && this.updateListener != null) {
            this.updateListener.accept(this.selectedColor);
        }

    }

    public void drawColorPicker(int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawCenteredString(this.title, (double)(this.x + this.width / 2), (double)(this.y - 5), 0.5D);
        drawAPI.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.openedSelector ? -1 : 2147483647);
        int bgColor = this.colorForPreview == null ? (this.defaultColor == null ? (this.openedSelector ? -2147483648 : 2147483647) : this.defaultColor.getDefaultColor().getRGB()) : this.colorForPreview.getRGB();
        drawAPI.drawRect(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, bgColor);
        if (this.hasDefault && this.selectedColor == null && this.isDefault) {
            drawAPI.bindTexture(ModTextures.BUTTON_HOVER_DEFAULT);
            Color color = new Color(bgColor);
            int luma = (int)(0.2126F * (float)color.getRed() + 0.7152F * (float)color.getGreen() + 0.0722F * (float)color.getBlue());
            luma = 255 - luma;
            if (luma < 80) {
                //drawAPI.glStateManagerColor((float)luma / 255.0F, (float)luma / 255.0F, (float)luma / 255.0F, 1.0F);
                drawAPI.drawTexture((double)(this.x + 2), (double)(this.y + 2), 256.0D, 256.0D, (double)(this.width - 4), (double)(this.height - 4), 1.1F);
            } else {
                drawAPI.drawTexture((double)(this.x + 2), (double)(this.y + 2), 256.0D, 256.0D, (double)(this.width - 4), (double)(this.height - 4));
            }
        }

        if (this.openedSelector) {
            this.drawColorsAndButtons(mouseX, mouseY);
        }

    }

    private Color getContrastColor(int r, int g, int b) {
        double y = (double)((299 * r + 587 * g + 114 * b) / 1000);
        return y >= 128.0D ? Color.black : Color.white;
    }

    private void drawColorsAndButtons(int mouseX, int mouseY) {
        int widthPerColor = 13;
        int sliderHeight = 13;
        int sliderWidth = 0;
        ModColor[] var6 = ModColor.values();
        int sliderY = var6.length;

        int minX;
        for(minX = 0; minX < sliderY; ++minX) {
            ModColor color = var6[minX];
            if (color.getColor() != null) {
                sliderWidth += widthPerColor;
            }
        }

        int sliderX = this.x - sliderWidth / 2 + this.width / 2;
        sliderY = this.y + this.height + 4;
        if (this.hasAdvanced) {
            sliderX -= 20;
        }

        DrawAPI drawAPI = DrawAPI.getAPI();


        minX = this.hasDefault && this.selectedColor != null ? 20 : 5;
        int maxX = drawAPI.getScaledWidth() - 5;
        int maxY = drawAPI.getScaledHeight() - 5;
        if (sliderX + sliderWidth > maxX) {
            sliderX -= sliderX + sliderWidth - maxX;
        }

        if (sliderX < minX) {
            sliderX = minX;
        }

        if (sliderY > maxY) {
            sliderY = maxY - sliderHeight - this.height;
        } else {
            drawAPI.drawRect(this.x + this.width / 2 - 1, sliderY - 3, this.x + this.width / 2 + 1, sliderY - 2, 2147483647);
            drawAPI.drawRect(this.x + this.width / 2 - 2, sliderY - 2, this.x + this.width / 2 + 2, sliderY - 1, 2147483647);
        }

        if (!(PlayerAPI.getAPI().getCurrentScreen() instanceof AdvancedColorSelectorGui)) {
            this.drawSlider(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight, widthPerColor);
            this.drawButtons(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight, widthPerColor);
        }

        this.hoverAdvancedButton = mouseX > sliderX + sliderWidth + 3 - 1 && mouseX < sliderX + sliderWidth + 3 + widthPerColor + 1 && mouseY > sliderY - 1 && mouseY < sliderY + sliderHeight + 1;
        this.hoverSlider = mouseX > sliderX && mouseX < sliderX + sliderWidth + widthPerColor && mouseY > sliderY && mouseY < sliderY + sliderHeight;
        this.hoverDefaultButton = mouseX > sliderX - 3 - widthPerColor - 1 && mouseX < sliderX - 3 + 1 && mouseY > sliderY - 1 && mouseY < sliderY + sliderHeight + 1;
    }

    private void drawSlider(int mouseX, int mouseY, int sliderX, int sliderY, int sliderWidth, int sliderHeight, int widthPerColor) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawRect(sliderX - 1, sliderY - 1, sliderX + sliderWidth + 1, sliderY + sliderHeight + 1, 2147483647);
        int pos = 0;
        int hoverPos = -1;
        int selectedPos = -1;
        ModColor hoverColorType = null;
        ModColor selectedColorType = null;
        ModColor[] var13 = ModColor.values();
        int var14 = var13.length;

        for(int var15 = 0; var15 < var14; ++var15) {
            ModColor color = var13[var15];
            if (color.getColor() != null) {
                drawAPI.drawRect(sliderX + pos, sliderY, sliderX + pos + widthPerColor, sliderY + sliderHeight, color.getColor().getRGB());
                boolean hoverColor = mouseX > sliderX + pos && mouseX < sliderX + pos + widthPerColor + 1 && mouseY > sliderY && mouseY < sliderY + sliderHeight;
                if (hoverPos == -1 && hoverColorType == null && hoverColor) {
                    hoverPos = pos;
                    hoverColorType = color;
                }

                if (color.getColor() == this.selectedColor) {
                    selectedPos = pos;
                    selectedColorType = color;
                }

                pos += widthPerColor;
            }
        }

        if (hoverColorType != null) {
            drawAPI.drawRect(sliderX + hoverPos - 1, sliderY - 1, sliderX + hoverPos + widthPerColor + 1, sliderY + sliderHeight + 1, hoverColorType.getColor().getRGB());
            this.colorForPreview = hoverColorType.getColor();
            if (this.updateListener != null) {
                this.updateListener.accept(this.colorForPreview);
            }
        } else {
            this.colorForPreview = this.selectedColor;
            if (this.updateListener != null) {
                this.updateListener.accept(this.selectedColor);
            }
        }

        if (selectedColorType != null) {
            drawAPI.drawRect(sliderX + selectedPos - 1, sliderY - 1, sliderX + selectedPos + widthPerColor + 1, sliderY + sliderHeight + 1, -1);
            drawAPI.drawRect(sliderX + selectedPos, sliderY, sliderX + selectedPos + widthPerColor, sliderY + sliderHeight, selectedColorType.getColor().getRGB());
        }

    }

    private void drawAdvanced(int mouseX, int mouseY, int advancedX, int advancedY, int advancedWidth) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        int alphaCount = 12;
        double widthPerColor = (double)advancedWidth / (double)ADVANCED_COLORS.length;
        double heightPerColor = widthPerColor;
        drawAPI.drawRect(advancedX - 1, advancedY - 1, (int)((double)advancedX + widthPerColor * (double)ADVANCED_COLORS.length + 1.0D), (int)((double)advancedY + widthPerColor * (double)alphaCount + 1.0D), 2147483647);
        double hoverPosX = -1.0D;
        double hoverPosY = -1.0D;
        Color hoveredColorType = null;
        double selectedPosX = -1.0D;
        double selectedPosY = -1.0D;
        Color selectedColorType = null;
        double posX = 0.0D;
        int[] var23 = ADVANCED_COLORS;
        int var24 = var23.length;

        for(int var25 = 0; var25 < var24; ++var25) {
            int color = var23[var25];

            for(int posY = 0; posY < alphaCount; ++posY) {
                int rgb = ModColor.changeBrightness(new Color(color), 0.07F * (float)posY).getRGB();
                drawAPI.drawRect((int)((double)advancedX + posX), (int)((double)advancedY + (double)posY * heightPerColor), (int)((double)advancedX + posX + widthPerColor), (int)((double)advancedY + (double)posY * heightPerColor + heightPerColor), rgb);
                boolean hoverColor = (double)mouseX > (double)advancedX + posX && (double)mouseX < (double)advancedX + posX + widthPerColor + 1.0D && (double)mouseY > (double)advancedY + (double)posY * heightPerColor && (double)mouseY < (double)advancedY + (double)posY * heightPerColor + heightPerColor + 1.0D;
                if (hoverColor) {
                    hoverPosX = posX;
                    hoverPosY = (double)posY;
                    hoveredColorType = new Color(rgb);
                }

                if (this.selectedColor != null && rgb == this.selectedColor.getRGB()) {
                    selectedPosX = posX;
                    selectedPosY = (double)posY;
                    selectedColorType = this.selectedColor;
                }
            }

            posX += widthPerColor;
        }

        if (hoveredColorType != null) {
            drawAPI.drawRect((int)((double)advancedX + hoverPosX - 1.0D), (int)((double)advancedY + hoverPosY * heightPerColor - 1.0D), (int)((double)advancedX + hoverPosX + widthPerColor + 1.0D), (int)((double)advancedY + hoverPosY * heightPerColor + heightPerColor + 1.0D), hoveredColorType.getRGB());
            this.colorForPreview = hoveredColorType;
            if (this.updateListener != null) {
                this.updateListener.accept(this.colorForPreview);
            }
        } else {
            this.colorForPreview = this.selectedColor;
            if (this.updateListener != null) {
                this.updateListener.accept(this.selectedColor);
            }
        }

        if (selectedColorType != null) {
            drawAPI.drawRect((int)((double)advancedX + selectedPosX - 1.0D), (int)((double)advancedY + selectedPosY * heightPerColor - 1.0D), (int)((double)advancedX + selectedPosX + widthPerColor + 1.0D), (int)((double)advancedY + selectedPosY * heightPerColor + heightPerColor + 1.0D), -1);
            drawAPI.drawRect((int)((double)advancedX + selectedPosX), (int)((double)advancedY + selectedPosY * heightPerColor), (int)((double)advancedX + selectedPosX + widthPerColor), (int)((double)advancedY + selectedPosY * heightPerColor + heightPerColor), selectedColorType.getRGB());
        }

    }

    private void drawButtons(int mouseX, int mouseY, int sliderX, int sliderY, int sliderWidth, int sliderHeight, int widthPerColor) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        if (this.hasDefault && this.selectedColor != null) {
            drawAPI.drawRect(sliderX - 3 - widthPerColor - 1, sliderY - 1, sliderX - 3 + 1, sliderY + sliderHeight + 1, this.hoverDefaultButton ? -1 : 2147483647);
            drawAPI.drawRect(sliderX - 3 - widthPerColor, sliderY, sliderX - 3, sliderY + sliderHeight, -2147483648);
            drawAPI.drawString("D", (float)(sliderX - 3 - widthPerColor / 2 - 3), (float)(sliderY + sliderHeight / 2 - 3));
            if (this.hoverDefaultButton) {
                this.updateListener.accept(null);
                this.colorForPreview = this.defaultColor.getDefaultColor();
            }
        }

        if (this.hasAdvanced) {
            drawAPI.drawRect(sliderX + sliderWidth + 3 - 1, sliderY - 1, sliderX + sliderWidth + 3 + widthPerColor + 1, sliderY + sliderHeight + 1, this.hoverAdvancedButton ? -1 : 2147483647);
            drawAPI.drawRect(sliderX + sliderWidth + 3, sliderY, sliderX + sliderWidth + 3 + widthPerColor, sliderY + sliderHeight, -1);
            int iconX = sliderX + sliderWidth + 3;
            int iconY = sliderY;
            double pxlX = (double)iconX;
            int[] var12 = ADVANCED_COLORS;
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
                int color = var12[var14];
                int pxlY = iconY;

                for(int i = 0; i < 13; ++i) {
                    Color theColor = new Color(color + i * 2000);
                    int rgb = ModColor.toRGB(theColor.getRed(), theColor.getGreen(), theColor.getBlue(), 255 - i * 18);
                    drawAPI.drawRect((int)pxlX, pxlY, (int)pxlX + 1, pxlY + 1, rgb);
                    ++pxlY;
                }

                pxlX += 0.7D;
            }
        }

    }

    public boolean mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if (this.isMouseOver(mouseX, mouseY)) {
            this.openedSelector = !this.openedSelector;
            return true;
        } else {
            if (this.openedSelector) {
                if (this.hoverSlider) {
                    this.selectedColor = this.colorForPreview;
                    if (this.updateListener != null) {
                        this.updateListener.accept(this.selectedColor);
                    }
                }

                if (this.hasDefault && this.selectedColor != null && this.hoverDefaultButton) {
                    this.selectedColor = null;
                    this.colorForPreview = this.defaultColor.getDefaultColor();
                    if (this.updateListener != null) {
                        this.updateListener.accept(this.selectedColor);
                    }

                    return true;
                }

                if (this.hasAdvanced && this.hoverAdvancedButton) {
                    PlayerAPI.getAPI().openGuiScreen(new AdvancedColorSelectorGui(this, PlayerAPI.getAPI().getCurrentScreen(), new Consumer<WrappedGuiScreen>() {
                        @Override
                        public void accept(WrappedGuiScreen lastScreen) {
                            PlayerAPI.getAPI().openGuiScreen(lastScreen);
                        }
                    }));
                    return true;
                }
            }

            boolean flag = this.openedSelector;
            this.openedSelector = false;
            return flag != this.openedSelector;
        }
    }

    public boolean mouseDragging(int mouseX, int mouseY, MouseButton mouseButton) {
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        return false;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        this.colorForPreview = selectedColor;
    }

    public boolean isHoverAdvancedButton() {
        return this.hoverAdvancedButton;
    }

    public boolean isHoverDefaultButton() {
        return this.hoverDefaultButton;
    }

    public boolean isHoverSlider() {
        return this.hoverSlider;
    }

    public Color getSelectedColor() {
        return this.selectedColor;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
    }

    public String getTitle() {
        return this.title;
    }

    public Color getColorForPreview() {
        return this.colorForPreview;
    }

    public void setHasDefault(boolean hasDefault) {
        this.hasDefault = hasDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    public void setHasAdvanced(boolean hasAdvanced) {
        this.hasAdvanced = hasAdvanced;
    }

    public void setUpdateListener(Consumer<Color> updateListener) {
        this.updateListener = updateListener;
    }

    public interface DefaultColorCallback {
        Color getDefaultColor();
    }

    public class AdvancedColorSelectorGui extends WrappedGuiScreen {
        private WrappedGuiScreen backgroundScreen;
        private Consumer<WrappedGuiScreen> callback;
        private ColorPicker colorPicker;
        private ModTextField fieldHexColor;
        private Color lastColor = null;
        private boolean validHex = true;

        public AdvancedColorSelectorGui(ColorPicker colorPicker, WrappedGuiScreen backgroundScreen, Consumer<WrappedGuiScreen> callback) {
            this.backgroundScreen = backgroundScreen;
            this.callback = callback;
            this.colorPicker = colorPicker;
        }

        public void initGui() {
            this.backgroundScreen.setWidth(this.getWidth());
            this.backgroundScreen.setHeight(this.getHeight());

            this.fieldHexColor = new ModTextField(0, this.getWidth() / 2 - 70, this.getHeight() / 4 + 115, 100, 16);
            this.fieldHexColor.setMaxStringLength(7);
            this.lastColor = null;
            this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 + 40, this.getHeight() / 4 + 113, 60, 20, "Done"));
        }

        public void onGuiClosed() {
            this.backgroundScreen.onGuiClosed();
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            DrawAPI drawAPI = DrawAPI.getAPI();
            this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            drawAPI.drawRect(0, 0, this.getWidth(), this.getHeight(), -2147483648);
            this.colorPicker.drawColorPicker(mouseX, mouseY);
            drawAPI.drawRect(this.getWidth() / 2 - 105, this.getHeight() / 4 - 25, this.getWidth() / 2 + 105, this.getHeight() / 4 + 140, -2147483648);
            this.colorPicker.drawAdvanced(mouseX, mouseY, this.getWidth() / 2 - 100, this.getHeight() / 4 - 20, 200);
            drawAPI.drawRect(fieldHexColor.xPosition - 2, fieldHexColor.yPosition - 2, fieldHexColor.xPosition + 100 + 2, fieldHexColor.yPosition + 16 + 2, this.validHex ? ModColor.toRGB(85, 255, 85, 100) : ModColor.toRGB(255, 85, 85, 100));
            this.fieldHexColor.drawTextBox();
            if (this.colorPicker.colorForPreview == null) {
                this.colorPicker.colorForPreview = this.colorPicker.defaultColor.getDefaultColor();
            }

            drawAPI.drawRect(this.getWidth() / 2 - 100, this.getHeight() / 4 + 113, this.getWidth() / 2 - 100 + 20, this.getHeight() / 4 + 113 + 20, 2147483647);
            drawAPI.drawRect(this.getWidth() / 2 - 100 + 1, this.getHeight() / 4 + 113 + 1, this.getWidth() / 2 - 100 + 20 - 1, this.getHeight() / 4 + 113 + 20 - 1, this.colorPicker.colorForPreview.getRGB());
            if (this.lastColor == null || !this.lastColor.equals(this.colorPicker.colorForPreview)) {
                this.lastColor = this.colorPicker.colorForPreview;
                String hex = String.format("#%02x%02x%02x", this.lastColor.getRed(), this.lastColor.getGreen(), this.lastColor.getBlue());
                this.fieldHexColor.setText(hex);
                this.validHex = true;
            }

            drawAPI.drawCenteredString("Advanced colors", this.getWidth() / 2, this.getHeight() / 4 - 35, -1);
        }

        public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
            this.colorPicker.selectedColor = this.colorPicker.colorForPreview;
            if (this.colorPicker.updateListener != null) {
                this.colorPicker.updateListener.accept(this.colorPicker.selectedColor);
            }

            this.fieldHexColor.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {

        }

        @Override
        public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {

        }

        @Override
        public void handleMouseInput() {

        }

        public void keyTyped(char typedChar, Key key) {
            if (key.equals(Key.ESCAPE)) {
                PlayerAPI.getAPI().openGuiScreen(this.backgroundScreen);
            }

            if (this.fieldHexColor.textboxKeyTyped(typedChar, key)) {
                String hex = this.fieldHexColor.getText();
                if (hex.length() == 7) {
                    try {
                        this.colorPicker.selectedColor = new Color(Integer.valueOf(hex.substring(1, 3), 16), Integer.valueOf(hex.substring(3, 5), 16), Integer.valueOf(hex.substring(5, 7), 16));
                        this.colorPicker.colorForPreview = this.colorPicker.selectedColor;
                        this.validHex = true;
                    } catch (Exception var5) {
                        this.validHex = false;
                    }
                } else {
                    this.validHex = false;
                }
            }

        }

        public void updateScreen() {
            this.backgroundScreen.updateScreen();
            this.fieldHexColor.updateCursorCounter();
        }

        public void actionPerformed(WrappedGuiButton button) {
            if (button.getId() == 1) {
                this.colorPicker.openedSelector = false;
                PlayerAPI.getAPI().openGuiScreen(this.backgroundScreen);
            }

        }

        public WrappedGuiScreen getBackgroundScreen() {
            return this.backgroundScreen;
        }
    }
}
