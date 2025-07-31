package ml.volder.unikapi.guisystem.elements;


import ml.volder.transporter.gui.TransporterActivity;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.utils.ColorUtils;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.util.function.Consumer;

public class StringElement extends ControlElement{
    private String currentValue;
    private Consumer<String> changeListener;
    private ModTextField textField;
    private Consumer<String> callback;
    private boolean hoverExpandButton;

    public StringElement(String displayName, final String configEntryName, IconData iconData, String currentValue, DataManager<Data> dataManager) {
        super(displayName, configEntryName, iconData);
        this.hoverExpandButton = false;
        this.currentValue = dataManager.getSettings().getData().has(configEntryName) ? dataManager.getSettings().getData().get(configEntryName).getAsString() : currentValue;

        this.changeListener = accepted -> {
            dataManager.getSettings().getData().addProperty(configEntryName, accepted);
            dataManager.save();

            if (StringElement.this.callback != null) {
                StringElement.this.callback.accept(accepted);
            }
        };
        this.createTextfield();
    }

    public void createTextfield() {
        this.textField = new ModTextField(-2, 0, 0, this.getObjectWidth() - 5, 20);
        this.textField.setMaxStringLength(500);
        this.updateValue();
        this.textField.setCursorPositionEnd();
        this.textField.setFocused(false);
    }

    private void updateValue() {
        this.textField.setText(this.currentValue == null ? "" : this.currentValue);
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int width = this.getObjectWidth() - 5;
        if (this.textField != null) {
            this.textField.xPosition = maxX - width - 2;
            this.textField.yPosition = y + 1;
            this.textField.drawTextBox();

            DrawAPI drawAPI = DrawAPI.getAPI();

            drawAPI.drawRectangle(x - 1, y, x, maxY, ColorUtils.toRGB(120, 120, 120, 120));
            //drawAPI.glStateManagerColor(1.0F, 1.0F, 1.0F);
            drawAPI.bindTexture(ModTextures.BUTTON_EXPAND);
            this.hoverExpandButton = mouseX > maxX - this.getObjectWidth() - 12 && mouseX < maxX - this.getObjectWidth() - 7 + 8 && mouseY > y + 1 && mouseY < y + 1 + 8;
            drawAPI.drawTexture(maxX - this.getObjectWidth() - 7, y + 1, 0.0D, this.hoverExpandButton ? 130.0D : 0.0D, 256.0D, 128.0D, 8.0D, 8.0D);
        }
    }

    public void unfocus(int mouseX, int mouseY, MouseButton mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        if (this.hoverExpandButton) {
            this.hoverExpandButton = false;
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ExpandedStringElementGui(this.textField, Laby.labyAPI().minecraft().minecraftWindow().currentScreen(), modTextField -> {
                StringElement.this.textField.setText(modTextField.getText());
                StringElement.this.textField.setFocused(true);
                StringElement.this.textField.setCursorPosition(modTextField.getCursorPosition());
                StringElement.this.textField.setSelectionPos(modTextField.getSelectionEnd());
                StringElement.this.changeListener.accept(StringElement.this.textField.getText());
            }));
        }
        this.textField.setFocused(false);
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, (MouseButton) MouseButton.LEFT);
    }

    public void keyTyped(char typedChar, Key key) {
        if (this.textField.textboxKeyTyped(typedChar, key)) {
            this.changeListener.accept(this.textField.getText());
        }

    }

    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }

    public StringElement maxLength(int maxLength) {
        this.textField.setMaxStringLength(maxLength);
        return this;
    }

    public StringElement addCallback(Consumer<String> callback) {
        this.callback = callback;
        return this;
    }

    public int getObjectWidth() {
        return 85;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    @AutoActivity
    public class ExpandedStringElementGui extends TransporterActivity {
        private TransporterActivity backgroundScreen;
        private Consumer<ModTextField> callback;
        private ModTextField preField;
        private ModTextField expandedField;

        public ExpandedStringElementGui(ModTextField preField, ScreenWrapper backgroundScreen, Consumer<ModTextField> callback) {
            if(backgroundScreen instanceof TransporterActivity)
                this.backgroundScreen = (TransporterActivity) backgroundScreen;
            this.callback = callback;
            this.preField = preField;
        }

        public void initGui() {
            if(backgroundScreen != null) {
                this.backgroundScreen.setWidth(this.getWidth());
                this.backgroundScreen.setHeight(this.getHeight());
            }

            this.expandedField = new ModTextField(0, this.getWidth() / 2 - 150, this.getHeight() / 4 + 45, 300, 20);
            this.expandedField.setMaxStringLength(this.preField.getMaxStringLength());
            this.expandedField.setFocused(true);
            this.expandedField.setText(this.preField.getText());
            this.expandedField.setCursorPosition(this.preField.getCursorPosition());
            this.expandedField.setSelectionPos(this.preField.getSelectionEnd());
            this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 -50, this.getHeight() / 4 + 85, 100, 20, "Done"));
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            if(backgroundScreen != null) {
                this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            }
            DrawAPI drawAPI = DrawAPI.getAPI();
            drawAPI.drawRect(0, 0, this.getWidth(), this.getHeight(), -2147483648);
            drawAPI.drawRect(this.getWidth() / 2 - 165, this.getHeight() / 4 + 35, this.getWidth() / 2 + 165, this.getHeight() / 4 + 120, -2147483648);
            this.expandedField.drawTextBox();
        }

        public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
            this.expandedField.mouseClicked(mouseX, mouseY, mouseButton);
            this.callback.accept(this.expandedField);
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
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.backgroundScreen);
            }

            if (this.expandedField.textboxKeyTyped(typedChar, key)) {
                this.callback.accept(this.expandedField);
            }

        }

        @Override
        public void onGuiClosed() {

        }

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
