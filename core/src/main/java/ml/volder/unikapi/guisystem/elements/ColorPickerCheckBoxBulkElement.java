package ml.volder.unikapi.guisystem.elements;

import ml.volder.unikapi.keysystem.MouseButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColorPickerCheckBoxBulkElement extends ControlElement{
    private List<ColorPicker> colorPickers = new ArrayList();
    private List<CheckBox> checkBoxes = new ArrayList();
    private boolean checkBoxRightBound = false;

    public ColorPickerCheckBoxBulkElement(String displayName) {
        super(displayName, "", (IconData)null);
    }

    public void addColorPicker(ColorPicker colorPicker) {
        this.colorPickers.add(colorPicker);
    }

    public void addCheckbox(CheckBox checkBox) {
        this.checkBoxes.add(checkBox);
    }

    public void init() {
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        int bulkList = 0;

        Iterator var8;
        for(var8 = this.colorPickers.iterator(); var8.hasNext(); bulkList += 25) {
            ColorPicker colorPicker = (ColorPicker)var8.next();
            colorPicker.setX(x + bulkList);
            colorPicker.setY(y + 7);
            colorPicker.setWidth(20);
            colorPicker.setHeight(20);
            colorPicker.drawColorPicker(mouseX, mouseY);
        }

        CheckBox checkBox;
        if (this.checkBoxRightBound) {
            bulkList = maxX;

            for(var8 = this.checkBoxes.iterator(); var8.hasNext(); bulkList -= 25) {
                checkBox = (CheckBox)var8.next();
                checkBox.setX(bulkList - 20);
                checkBox.setY(y + 7);
                checkBox.setWidth(20);
                checkBox.setHeight(20);
                checkBox.drawCheckbox(mouseX, mouseY);
            }
        } else {
            for(var8 = this.checkBoxes.iterator(); var8.hasNext(); bulkList += 25) {
                checkBox = (CheckBox)var8.next();
                checkBox.setX(x + bulkList);
                checkBox.setY(y + 7);
                checkBox.setWidth(20);
                checkBox.setHeight(20);
                checkBox.drawCheckbox(mouseX, mouseY);
            }
        }

    }

    public int getEntryHeight() {
        return 30;
    }

    public void drawDescription(int x, int y, int screenWidth) {
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        onClickBulkEntry(mouseX, mouseY, mouseButton);
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public boolean onClickBulkEntry(int mouseX, int mouseY, MouseButton mouseButton) {
        Iterator var4 = this.colorPickers.iterator();

        ColorPicker colorPicker;
        while(var4.hasNext()) {
            colorPicker = (ColorPicker)var4.next();
            if (!colorPicker.isMouseOver(mouseX, mouseY) && !colorPicker.isHoverAdvancedButton() && !colorPicker.isHoverSlider()) {
                colorPicker.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        var4 = this.colorPickers.iterator();

        do {
            if (!var4.hasNext()) {
                var4 = this.checkBoxes.iterator();

                CheckBox checkBox;
                do {
                    if (!var4.hasNext()) {
                        return false;
                    }

                    checkBox = (CheckBox)var4.next();
                } while(!checkBox.mouseClicked(mouseX, mouseY, mouseButton));

                return true;
            }

            colorPicker = (ColorPicker)var4.next();
        } while(!colorPicker.mouseClicked(mouseX, mouseY, mouseButton));

        return true;
    }

    public void setCheckBoxRightBound(boolean checkBoxRightBound) {
        this.checkBoxRightBound = checkBoxRightBound;
    }
}
