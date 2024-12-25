package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.utils.ColorUtils;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;

import java.util.function.Consumer;

public class BooleanElement extends ControlElement {
    private boolean currentValue;
    private Consumer<Boolean> toggleListener;
    private WrappedGuiButton buttonToggle;
    private String stringEnabled;
    private String stringDisabled;
    private Consumer<Boolean> callback;

    public BooleanElement(String displayName, DataManager<Data> configManager, final String configEntryName, IconData iconData, boolean defaultValue) {
        super(displayName, configEntryName, iconData);
        this.stringEnabled = "ON";
        this.stringDisabled = "OFF";
        this.currentValue = defaultValue;
        if (configEntryName != null) {
            if (!configEntryName.isEmpty()) {
                this.currentValue = configManager.getSettings().getData().has(configEntryName) ? configManager.getSettings().getData().get(configEntryName).getAsBoolean() : defaultValue;
            }

            this.toggleListener = accepted -> {
                configManager.getSettings().getData().addProperty(configEntryName, accepted);
                configManager.save();
                BooleanElement.this.setSettingEnabled(accepted);
            };
        }

        this.createButton();
    }

    public void createButton() {
        this.buttonToggle = new WrappedGuiButton(-2, 0, 0, 0, 20, "");
        this.setSettingEnabled(this.currentValue);
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        DrawAPI drawAPI = DrawAPI.getAPI();
        int width = this.getObjectWidth();
        if (this.buttonToggle != null) {
            this.buttonToggle.setEnabled(false);
            buttonToggle.setX(maxX - width - 2);
            buttonToggle.setY(y + 1);
            this.buttonToggle.setWidth(width);
            buttonToggle.drawButton(mouseX, mouseY);
            this.buttonToggle.setEnabled(true);
            int buttonWidth = this.buttonToggle.getWidth();
            int valueXPos = this.currentValue ? (buttonWidth - 4) / 2 : (buttonWidth - 4) / 2 + 6;
            String displayString = (this.currentValue ? this.stringEnabled : this.stringDisabled);
            drawAPI.drawCenteredString(displayString, buttonToggle.getX() + valueXPos, buttonToggle.getY() + 6);
            //drawAPI.drawString(this.currentValue ? ModColor.GREEN.toString() : ModColor.RED.toString(), 0.0D, 0.0D);
            drawAPI.bindTexture(buttonTextures);
            int pos = (this.currentValue ? maxX - 8 : maxX - width) - 2;
            drawAPI.drawTexturedModalRect(pos, y + 1, 0, 66, 4, 20);
            drawAPI.drawTexturedModalRect(pos + 4, y + 1, 196, 66, 4, 20);
            drawAPI.drawRectangle(x - 1, y, x, maxY, this.currentValue ? ColorUtils.toRGB(20, 120, 20, 120) : ColorUtils.toRGB(120, 20, 20, 120));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.buttonToggle.mousePressed(mouseX, mouseY)) {
            this.currentValue = !this.currentValue;
            if (this.toggleListener != null) {
                this.toggleListener.accept(this.currentValue);
            }

            if (this.callback != null) {
                this.callback.accept(this.currentValue);
            }

            //this.buttonToggle.playPressSound(this.mc.getSoundHandler()); TODO add sound
        }

    }

    public BooleanElement custom(String... args) {
        if (args.length >= 1) {
            this.stringEnabled = args[0];
        }

        if (args.length >= 2) {
            this.stringDisabled = args[1];
        }

        return this;
    }

    public BooleanElement addCallback(Consumer<Boolean> callback) {
        this.callback = callback;
        return this;
    }

    public boolean getCurrentValue() {
        return this.currentValue;
    }
}
