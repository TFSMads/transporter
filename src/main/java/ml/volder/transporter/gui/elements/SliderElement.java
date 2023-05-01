package ml.volder.transporter.gui.elements;


import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.types.ResourceLocation;

import java.util.function.Consumer;

public class SliderElement extends ControlElement{
    public static final ResourceLocation buttonTextures = ModTextures.WIDGETS;
    private Integer currentValue;
    private Consumer<Integer> changeListener;
    private Consumer<Integer> callback;
    private int minValue;
    private int maxValue;
    private boolean dragging;
    private boolean hover;
    private int dragValue;
    private int steps;

    public SliderElement(String displayName, DataManager<Data> configManager, final String configEntryName, IconData iconData, int defaultValue) {
        super(displayName, configEntryName, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        if (!configEntryName.isEmpty()) {
            this.currentValue = configManager.getSettings().getData().has(configEntryName) ? configManager.getSettings().getData().get(configEntryName).getAsInt() : defaultValue;
        }

        if (this.currentValue == null) {
            this.currentValue = this.minValue;
        }

        this.changeListener = accepted -> {
            try {
                configManager.getSettings().getData().addProperty(configEntryName, currentValue);
                configManager.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            if (SliderElement.this.callback != null) {
                SliderElement.this.callback.accept(accepted);
            }

        };
    }

    public SliderElement(String displayName, DataManager<Data> configManager, final String configEntryName, IconData iconData) {
        this(displayName, configManager, configEntryName, iconData, 0);
    }

    public SliderElement(String displayName, IconData iconData, int currentValue) {
        super(displayName, (String)null, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        this.currentValue = currentValue;
        this.changeListener = accepted -> {
            if (SliderElement.this.callback != null) {
                SliderElement.this.callback.accept(accepted);
            }

        };
    }

    public SliderElement(String displayName, IconData iconData, DataManager<Data> configManager, final String attribute, int currentValue) {
        super(displayName, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        this.currentValue = currentValue;

        this.changeListener = accepted -> {
            configManager.getSettings().getData().addProperty(attribute, accepted);
            configManager.save();
            if (SliderElement.this.callback != null) {
                SliderElement.this.callback.accept(accepted);
            }

        };
    }

    public SliderElement setMinValue(int minValue) {
        this.minValue = minValue;
        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
        }

        return this;
    }

    public SliderElement setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
        }
        return this;
    }

    public SliderElement setRange(int min, int max) {
        this.setMinValue(min);
        this.setMaxValue(max);
        return this;
    }

    public SliderElement setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        DrawAPI drawAPI = DrawAPI.getAPI();
        int width = this.getObjectWidth();
        if (this.displayName != null) {
            drawAPI.drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        }

        drawAPI.bindTexture(buttonTextures);
        //drawAPI.glStateManagerColor(1.0F, 1.0F, 1.0F);
        double maxSliderPos = (double)maxX;
        double sliderWidth = (double)(width - 8);
        double sliderWidthBackground = (double)width;
        double minSliderPos = maxSliderPos - (double)width;
        double totalValueDiff = (double)(this.maxValue - this.minValue);
        double currentValue = (double)this.currentValue;
        double pos = minSliderPos + sliderWidth / totalValueDiff * (currentValue - (double)this.minValue);
        drawAPI.drawTexturedModalRect(minSliderPos, (double)(y + 1), 0.0D, 46.0D, sliderWidthBackground / 2.0D, 20.0D);
        drawAPI.drawTexturedModalRect(minSliderPos + sliderWidthBackground / 2.0D, (double)(y + 1), 200.0D - sliderWidthBackground / 2.0D, 46.0D, sliderWidthBackground / 2.0D, 20.0D);
        this.hover = mouseX > x && mouseX < maxX && mouseY > y + 1 && mouseY < maxY;
        drawAPI.drawTexturedModalRect(pos, (double)(y + 1), 0.0D, 66.0D, 4.0D, 20.0D);
        drawAPI.drawTexturedModalRect(pos + 4.0D, (double)(y + 1), 196.0D, 66.0D, 4.0D, 20.0D);
        if (!this.isMouseOver()) {
            this.mouseRelease(mouseX, mouseY, (MouseButton) MouseButton.LEFT);
        } else {
            double mouseToMinSlider = (double)mouseX - minSliderPos;
            double finalValue = (double)this.minValue + totalValueDiff / sliderWidth * (mouseToMinSlider - 1.0D);
            if (this.dragging) {
                this.dragValue = (int)finalValue;
                this.mouseClickMove(mouseX, mouseY, (MouseButton) MouseButton.LEFT);
            }
        }

        drawAPI.drawCenteredString("" + this.currentValue, minSliderPos + sliderWidthBackground / 2.0D, (double)(y + 7));
    }

    public void unfocus(int mouseX, int mouseY, MouseButton mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hover) {
            this.dragging = true;
        }

    }

    public void mouseRelease(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.dragging = false;
            this.currentValue = (int)((double)this.dragValue / (double)this.steps) * this.steps;
            if (this.currentValue > this.maxValue) {
                this.currentValue = this.maxValue;
            }

            if (this.currentValue < this.minValue) {
                this.currentValue = this.minValue;
            }

            this.changeListener.accept(this.currentValue);
        }

    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseClickMove(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.currentValue = (int)Math.round((double)this.dragValue / (double)this.steps * (double)this.steps);
            if (this.currentValue > this.maxValue) {
                this.currentValue = this.maxValue;
            }

            if (this.currentValue < this.minValue) {
                this.currentValue = this.minValue;
            }

            this.changeListener.accept(this.currentValue);
        }

    }

    public SliderElement addCallback(Consumer<Integer> callback) {
        this.callback = callback;
        return this;
    }

    public int getObjectWidth() {
        return 50;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getCurrentValue() {
        return this.currentValue;
    }
}
