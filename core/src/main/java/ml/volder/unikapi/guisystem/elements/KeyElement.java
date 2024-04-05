package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.KeyMapper;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.types.ModColor;

import java.util.function.Consumer;

public class KeyElement extends ControlElement {
    private Key currentKey;
    private boolean allowMouseButtons;
    private Consumer<Key> changeListener;
    private ModTextField textField;
    private Consumer<Key> callback;

    public KeyElement(String displayName, IconData iconData, DataManager<Data> configManager, final String attributeName, boolean allowMouseButtons, Key defaultKey) {
        this(
                displayName,
                iconData,
                configManager,
                attributeName,
                configManager.getSettings().getData().has(attributeName)
                        ? configManager.getSettings().getData().get(attributeName).getAsString() != null
                            ? KeyMapper.getKey(configManager.getSettings().getData().get(attributeName).getAsString())
                            : defaultKey
                        : defaultKey,
                allowMouseButtons
            );
    }



    public KeyElement(String displayName, IconData iconData, DataManager<Data> configManager, final String attributeName, Key currentKey, boolean allowMouseButtons) {
        super(displayName, iconData);
        this.allowMouseButtons = true;
        this.allowMouseButtons = allowMouseButtons;
        this.currentKey = currentKey;
        this.changeListener = accepted -> {
            configManager.getSettings().getData().addProperty(attributeName, accepted.getName());
            configManager.save();
            if (KeyElement.this.callback != null) {
                KeyElement.this.callback.accept(accepted);
            }

        };
        this.createTextfield();
    }

    public void createTextfield() {
        this.textField = new ModTextField(-2, 0, 0, this.getObjectWidth() - 5, 20);
        this.updateValue();
        this.textField.setCursorPositionEnd();
        this.textField.setFocused(false);
    }

    private void updateValue() {
        if (this.currentKey == null || this.currentKey.equals(Key.NONE)) {
            this.textField.setText("NONE");
        } else {
            try {
                this.textField.setText(currentKey.getName());
            } catch (Exception e) {
                this.currentKey = Key.NONE;
                UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.FINE, e);
            }
        }

    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int width = this.getObjectWidth() - 5;
        if (this.textField != null) {
            this.textField.xPosition = maxX - width - 2;
            this.textField.yPosition = y + 1;
            this.textField.drawTextBox();
            DrawAPI.getAPI().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        }
    }

    public void unfocus(int mouseX, int mouseY, MouseButton mouseButton) {
        boolean prevFocus = this.textField.isFocused();
        super.unfocus(mouseX, mouseY, mouseButton);
        this.textField.setFocused(false);
        if (prevFocus && this.allowMouseButtons) {
            this.textField.setFocused(false);
            this.currentKey = mouseButton;
            this.changeListener.accept(this.currentKey);
            this.updateValue();
            this.updateScreen();
        }

    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton.isLeft()) {
            this.textField.mouseClicked(mouseX, mouseY, (MouseButton) MouseButton.LEFT);
        }

    }

    public void keyTyped(char typedChar, Key key) {
        if (key.equals(Key.ESCAPE)) {
            key = Key.NONE;
        }

        if (this.textField.isFocused()) {
            this.textField.setFocused(false);
            this.currentKey = key;
            this.changeListener.accept(key);
            this.updateValue();
        }

    }

    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }

    public KeyElement maxLength(int maxLength) {
        this.textField.setMaxStringLength(maxLength);
        return this;
    }

    public KeyElement setAllowMouseButtons(boolean allowMouseButtons) {
        this.allowMouseButtons = allowMouseButtons;
        return this;
    }

    public KeyElement addCallback(Consumer<Key> callback) {
        this.callback = callback;
        return this;
    }

    public int getObjectWidth() {
        return 53;
    }

    public ModTextField getTextField() {
        return this.textField;
    }

    public Key getCurrentKey() {
        return currentKey;
    }

}
