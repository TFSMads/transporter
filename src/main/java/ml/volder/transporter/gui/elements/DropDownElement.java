package ml.volder.transporter.gui.elements;


import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;

import java.util.function.Consumer;

public class DropDownElement<T> extends ControlElement {
    private DropDownMenu dropDownMenu;
    private Consumer<T> changeListener;
    private Consumer<T> changeCallback;

    public DropDownElement(String diplayName, final String configEntryName, DropDownMenu<T> dropDownMenu, IconData iconData, DropDownLoadValue<T> loadValue, DataManager<Data> dataManager) {
        super(diplayName, configEntryName, iconData);
        this.dropDownMenu = dropDownMenu;
        if (!configEntryName.isEmpty()) {
            try {
                this.dropDownMenu.setSelected(loadValue.load(dataManager.getSettings().getData().has(configEntryName) ? dataManager.getSettings().getData().get(configEntryName).getAsString() : "ACTIONBAR_MESSAGES"));
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        this.changeListener = accepted -> {
            try {
                dataManager.getSettings().getData().addProperty(configEntryName, String.valueOf(accepted));
                dataManager.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            if (DropDownElement.this.changeCallback != null) {
                DropDownElement.this.changeCallback.accept(accepted);
            }

        };
    }

    public void init() {
        if (this.dropDownMenu != null) {
            this.dropDownMenu.setOpen(false);
        }

    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        this.mouseOver = this.dropDownMenu.isMouseOver(mouseX, mouseY);
        if (this.iconData == null) {
            this.dropDownMenu.setX(x);
            this.dropDownMenu.setY(y + 15);
            this.dropDownMenu.setWidth(maxX - x - 2);
            this.dropDownMenu.setHeight(maxY - y - 15 - 3);
            this.dropDownMenu.draw(mouseX, mouseY);
        } else {
            super.draw(x, y, maxX, maxY, mouseX, mouseY);
            DrawAPI.getAPI().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
            int width = 100;
            this.dropDownMenu.setX(maxX - width - 5);
            this.dropDownMenu.setY(y + 3);
            this.dropDownMenu.setWidth(width);
            this.dropDownMenu.setHeight(maxY - y - 6);
            this.dropDownMenu.draw(mouseX, mouseY);
        }

    }

    public int getEntryHeight() {
        return this.iconData == null ? 35 : 23;
    }

    public int getObjectWidth() {
        return this.dropDownMenu.getWidth() + 5;
    }

    public boolean onClickDropDown(int mouseX, int mouseY, MouseButton mouseButton) {
        if (this.dropDownMenu.onClick(mouseX, mouseY, mouseButton)) {
            if (this.changeListener != null) {
                this.changeListener.accept((T) this.dropDownMenu.getSelected());
            }

            return true;
        } else {
            return false;
        }
    }

    public void mouseRelease(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        this.dropDownMenu.onRelease(mouseX, mouseY, mouseButton);
    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton) {
        super.mouseClickMove(mouseX, mouseY, mouseButton);
        this.dropDownMenu.onDrag(mouseX, mouseY, mouseButton);
    }

    public void onScrollDropDown() {
        this.dropDownMenu.onScroll();
    }

    public DropDownElement setCallback(Consumer<T> consumer) {
        this.changeCallback = consumer;
        return this;
    }

    public DropDownMenu getDropDownMenu() {
        return this.dropDownMenu;
    }

    public void setChangeListener(Consumer<T> changeListener) {
        this.changeListener = changeListener;
    }

    public interface DropDownLoadValue<T> {
        T load(String var1);
    }
}
