package ml.volder.transporter.modules.guimodules;

import ml.volder.transporter.gui.elements.*;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.transporter.modules.guimodules.elements.ModuleCategoryElement;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiModule {
    //Integer mellem 0 og 1000 der representere modulets position
    private int screenX;
    private int screenY;

    private String key;
    private String prefix;
    private ControlElement.IconData iconData;

    private ModuleCategoryElement category;

    private Color valueColor;
    private Color keyColor;
    private Color bracketColor;

    private boolean bold;
    private boolean italic;
    private boolean underline;

    private boolean isEnabled;

    public GuiModule(int defaultX, int defaultY, String key, String defaultPrefix, boolean defaultIsEnabled, DataManager<Data> dataManager, ModuleCategoryElement category) {
        this.category = category;
        this.screenX = dataManager.getSettings().getData().has("modules." + key + ".screenX") ? dataManager.getSettings().getData().get("modules." + key + ".screenX").getAsInt() : defaultX;
        this.screenY = dataManager.getSettings().getData().has("modules." + key + ".screenY") ? dataManager.getSettings().getData().get("modules." + key + ".screenY").getAsInt() : defaultY;

        this.isEnabled = dataManager.getSettings().getData().has("modules." + key + ".isEnabled")
                ? dataManager.getSettings().getData().get("modules." + key + ".isEnabled").getAsBoolean()
                : defaultIsEnabled;
        this.key = key;
        this.prefix = dataManager.getSettings().getData().has("modules." + key + ".prefix")
                ? dataManager.getSettings().getData().get("modules." + key + ".prefix").getAsString()
                : defaultPrefix;

        this.valueColor = dataManager.getSettings().getData().has("modules." + key + ".valueColor") ? new Color(dataManager.getSettings().getData().get("modules." + key + ".valueColor").getAsInt()) : ModColor.WHITE.getColor();
        this.keyColor = dataManager.getSettings().getData().has("modules." + key + ".prefixColor") ? new Color(dataManager.getSettings().getData().get("modules." + key + ".prefixColor").getAsInt()) : ModColor.GREEN.getColor();
        this.bracketColor = dataManager.getSettings().getData().has("modules." + key + ".bracketColor") ? new Color(dataManager.getSettings().getData().get("modules." + key + ".bracketColor").getAsInt()) : ModColor.DARK_GRAY.getColor();

        this.bold = dataManager.getSettings().getData().has("modules." + key + ".bold") ? dataManager.getSettings().getData().get("modules." + key + ".bold").getAsBoolean() : false;
        this.italic = dataManager.getSettings().getData().has("modules." + key + ".italic") ? dataManager.getSettings().getData().get("modules." + key + ".italic").getAsBoolean() : false;
        this.underline = dataManager.getSettings().getData().has("modules." + key + ".underline") ? dataManager.getSettings().getData().get("modules." + key + ".underline").getAsBoolean() : false;
    }

    public List<SettingsElement> getSubSettings(DataManager<Data> dataManager) {
        List<SettingsElement> subSettings = new ArrayList<>();


        StringElement prefixElement = new StringElement("Prefix", "modules." + key + ".prefix", iconData, prefix == null ? key : prefix, dataManager);
        prefixElement.addCallback(s -> this.prefix = s);
        subSettings.add(prefixElement);

        //Color picker and Checkboxes bulk element
        ColorPickerCheckBoxBulkElement bulkElement = new ColorPickerCheckBoxBulkElement("");
        bulkElement.setCheckBoxRightBound(true);

        //Color pickers
        ColorPicker bracketPicker = new ColorPicker( "Brackets" , bracketColor, () -> ModColor.DARK_GRAY.getColor(), 0 , 0 , 0 , 0);
        bracketPicker.setHasDefault( true );
        bracketPicker.setUpdateListener(accepted -> {
            dataManager.getSettings().getData().addProperty("modules." + key + ".bracketColor", accepted != null ? accepted.getRGB() : ModColor.DARK_GRAY.getColor().getRGB());
            dataManager.save();
            bracketColor = accepted != null ? accepted : ModColor.DARK_GRAY.getColor();
        });
        bracketPicker.setHasAdvanced(true);
        bulkElement.addColorPicker( bracketPicker );

        ColorPicker valuePicker = new ColorPicker( "Value" , valueColor, () -> ModColor.WHITE.getColor(), 0 , 0 , 0 , 0);
        valuePicker.setHasDefault( true );
        valuePicker.setUpdateListener(accepted -> {
            dataManager.getSettings().getData().addProperty("modules." + key + ".valueColor", accepted != null ? accepted.getRGB() : ModColor.WHITE.getColor().getRGB());
            dataManager.save();
            valueColor = accepted != null ? accepted : ModColor.WHITE.getColor();
        });
        valuePicker.setHasAdvanced(true);
        bulkElement.addColorPicker( valuePicker );

        ColorPicker prefixPicker = new ColorPicker( "Prefix" , keyColor, () -> ModColor.GREEN.getColor(), 0 , 0 , 0 , 0);
        prefixPicker.setHasDefault( true );
        prefixPicker.setUpdateListener(accepted -> {
            dataManager.getSettings().getData().addProperty("modules." + key + ".prefixColor", accepted != null ? accepted.getRGB() : ModColor.GREEN.getColor().getRGB());
            dataManager.save();
            keyColor = accepted != null ? accepted : ModColor.GREEN.getColor();
        });
        prefixPicker.setHasAdvanced(true);
        bulkElement.addColorPicker( prefixPicker );

        //Checkboxes
        CheckBox boldCheckBox = new CheckBox( "Bold", bold == true ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, () -> CheckBox.EnumCheckBoxValue.DISABLED, 0 , 0 , 0 , 0);
        boldCheckBox.setHasDefault( true );
        boldCheckBox.setUpdateListener(accepted -> {
            bold = accepted == CheckBox.EnumCheckBoxValue.ENABLED;
            dataManager.getSettings().getData().addProperty("modules." + key + ".bold", bold);
            dataManager.save();
        });
        bulkElement.addCheckbox( boldCheckBox );

        CheckBox italicCheckBox = new CheckBox( "Italic", italic == true ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, () -> CheckBox.EnumCheckBoxValue.DISABLED, 0 , 0 , 0 , 0);
        italicCheckBox.setHasDefault( true );
        italicCheckBox.setUpdateListener(accepted -> {
            italic = accepted == CheckBox.EnumCheckBoxValue.ENABLED;
            dataManager.getSettings().getData().addProperty("modules." + key + ".italic", italic);
            dataManager.save();
        });
        bulkElement.addCheckbox( italicCheckBox );

        CheckBox underlineCheckBox = new CheckBox( "Underline", underline == true ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, () -> CheckBox.EnumCheckBoxValue.DISABLED, 0 , 0 , 0 , 0);
        underlineCheckBox.setHasDefault( true );
        underlineCheckBox.setUpdateListener(accepted -> {
            underline = accepted == CheckBox.EnumCheckBoxValue.ENABLED;
            dataManager.getSettings().getData().addProperty("modules." + key + ".underline", underline);
            dataManager.save();
        });
        bulkElement.addCheckbox( underlineCheckBox );

        subSettings.add( bulkElement );

        return subSettings;
    }

    public void drawModule() {
        draw(getDrawX(DrawAPI.getAPI().getScaledWidth()), getDrawY(DrawAPI.getAPI().getScaledHeight()));
    }

    public double getDrawX(double screenWidth) {
        double ratio = screenWidth/1000;
        return screenX*ratio;
    }

    public double getDrawY(double screenHeight) {
        double ratio = screenHeight/1000;
        return screenY*ratio;
    }

    public int getX() {
        return screenX;
    }

    public int getY() {
        return screenY;
    }

    public List<Text> getText() {
        List<Text> textList = new ArrayList<>();
        textList.add(new Text("[", bracketColor.getRGB(), bold, italic, underline));
        textList.add(new Text(prefix == null ? key : prefix, keyColor.getRGB(), bold, italic, underline));
        textList.add(new Text("] ", bracketColor.getRGB(), bold, italic, underline));
        textList.add(new Text(getDisplayValue(), valueColor.getRGB(), bold, italic, underline));
        return textList;
    }

    public int getWidth() {
        String text = "";
        for(Text t : getText()) {
            text += t.getText();
        }
        return DrawAPI.getAPI().getStringWidth(text);
    }

    public void draw(double screenX, double screenY) {
        if(isEnabled != true)
            return;
        Iterator<Text> textIterator = getText().iterator();
        while(textIterator.hasNext()) {
            Text text = textIterator.next();
            int stringWidth = DrawAPI.getAPI().getStringWidth(text.getText());
            DrawAPI.getAPI().drawStringWithShadow(text.getText(), screenX, screenY, text.getColor());
            screenX += (double)stringWidth;
        }
    }

    public void savePosition(DataManager<Data> dataManager) {
        dataManager.getSettings().getData().addProperty("modules." + key + ".screenX", screenX);
        dataManager.getSettings().getData().addProperty("modules." + key + ".screenY", screenY);
        dataManager.save();
    }

    public void setX(int x) {
        this.screenX = x;
    }

    public void setY(int y) {
        this.screenY = y;
    }

    public String getDisplayValue(){
        return "Dette modul er ikke aktivt!";
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Color getValueColor() {
        return valueColor;
    }

    public void setValueColor(Color valueColor) {
        this.valueColor = valueColor;
    }

    public Color getKeyColor() {
        return keyColor;
    }

    public void setKeyColor(Color keyColor) {
        this.keyColor = keyColor;
    }

    public Color getBracketColor() {
        return bracketColor;
    }

    public void setBracketColor(Color bracketColor) {
        this.bracketColor = bracketColor;
    }

    public String getKey() {
        return key;
    }
    public String getPrefix() {
        return prefix == null ? key : prefix;
    }

    public ControlElement.IconData getIconData() {
        return iconData == null ? new ControlElement.IconData(Material.PAPER) : iconData;
    }

    public void setIconData(ControlElement.IconData iconData) {
        this.iconData = iconData;
    }

    public ModuleCategoryElement getCategory() {
        return category;
    }

    public void setCategory(ModuleCategoryElement categorySettingsElement) {
        this.category = categorySettingsElement;
    }
}
