package ml.volder.transporter.modules.guimodules;

import ml.volder.transporter.gui.elements.*;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.transporter.modules.GuiModulesModule;
import ml.volder.transporter.modules.guimodules.elements.ModuleCategoryElement;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GuiModule {
    private RenderRelative renderRelative = RenderRelative.LEFT_TOP;
    private int distanceFromXRelative;
    private int distanceFromYRelative;
    private int moduleHeight;
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

    private GuiModule attachedModule;

    private GuiModule parentModule;

    private float scaleMultiplier = 2;


    public GuiModule(int defaultX, int defaultY, String key, String defaultPrefix, boolean defaultIsEnabled, DataManager<Data> dataManager, ModuleCategoryElement category) {
        this.category = category;
        this.moduleHeight = (int) (DrawAPI.getAPI().getFontHeight()*scaleMultiplier);
        this.isEnabled = dataManager.getSettings().getData().has("modules." + key + ".isEnabled")
                ? dataManager.getSettings().getData().get("modules." + key + ".isEnabled").getAsBoolean()
                : defaultIsEnabled;
        this.key = key;
        this.prefix = dataManager.getSettings().getData().has("modules." + key + ".prefix")
                ? dataManager.getSettings().getData().get("modules." + key + ".prefix").getAsString()
                : defaultPrefix;
        this.distanceFromXRelative = dataManager.getSettings().getData().has("modules." + key + ".distanceFromXRelative") ? dataManager.getSettings().getData().get("modules." + key + ".distanceFromXRelative").getAsInt() : defaultX;
        this.distanceFromYRelative = dataManager.getSettings().getData().has("modules." + key + ".distanceFromYRelative") ? dataManager.getSettings().getData().get("modules." + key + ".distanceFromYRelative").getAsInt() : defaultY;
    }

    public void loadConfig(DataManager<Data> dataManager) {

        this.scaleMultiplier = dataManager.getSettings().getData().has("modules." + key + ".textSize")
                ? dataManager.getSettings().getData().get("modules." + key + ".textSize").getAsFloat()
                : 1;
        this.moduleHeight = (int) (DrawAPI.getAPI().getFontHeight()*scaleMultiplier);

        this.attachedModule = dataManager.getSettings().getData().has("modules." + key + ".attachedModule")
                ? GuiModulesModule.getModuleByKey(dataManager.getSettings().getData().get("modules." + key + ".attachedModule").getAsString())
                : null;
        this.parentModule = dataManager.getSettings().getData().has("modules." + key + ".parentModule")
                ? GuiModulesModule.getModuleByKey(dataManager.getSettings().getData().get("modules." + key + ".parentModule").getAsString())
                : null;

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

        FloatElement numberElement = new FloatElement("Tekst Størelse", dataManager, "modules." + key + ".textSize", new ControlElement.IconData(Material.PAPER), 1);
        numberElement.setMaxValue(1.5F);
        numberElement.setMinValue(0.5F);
        numberElement.addCallback(integer -> {
            scaleMultiplier = integer;
            this.moduleHeight = (int) (DrawAPI.getAPI().getFontHeight()*scaleMultiplier);
        });
        subSettings.add(numberElement);

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
        draw(getDrawX(), getDrawY());
    }

    public double getDrawX() {
        return this.getDrawX(DrawAPI.getAPI().getScaledWidth());
    }

    public double getDrawX(int scaledWidth) {
        return getRelativePointX(scaledWidth) == 0
                ? getRelativePointX(scaledWidth) + convertScaledRatio(getDistanceFromXRelative(), DrawAPI.getAPI().getScaledWidth(), scaledWidth)
                : getRelativePointX(scaledWidth) - convertScaledRatio(getDistanceFromXRelative(), DrawAPI.getAPI().getScaledWidth(), scaledWidth);
    }

    public double getDrawY() {
        return this.getDrawY(DrawAPI.getAPI().getScaledHeight());
    }

    public double getDrawY(int scaledHeight) {
        return getRelativePointY(scaledHeight) == 0
                ? getRelativePointY(scaledHeight) + convertScaledRatio(getDistanceFromYRelative(), DrawAPI.getAPI().getScaledHeight(), scaledHeight)
                : getRelativePointY(scaledHeight) - convertScaledRatio(getDistanceFromYRelative(), DrawAPI.getAPI().getScaledHeight(), scaledHeight);
    }

    public RenderRelative getRenderRelative() {
        return renderRelative;
    }

    public int getDistanceFromXRelative() {
        return distanceFromXRelative;
    }

    public int getDistanceFromYRelative() {
        return distanceFromYRelative;
    }

    private int getRelativePointX(int scaledWidth) {
        switch (renderRelative) {
            case LEFT_TOP:
            case LEFT_BOTTOM:
                return 0;
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
                return scaledWidth;
            default:
                return 0;
        }
    }

    private int getRelativePointY(int scaledHeight) {
        switch (renderRelative) {
            case LEFT_TOP:
            case RIGHT_TOP:
                return 0;
            case LEFT_BOTTOM:
            case RIGHT_BOTTOM:
                return scaledHeight;
            default:
                return 0;
        }
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
        return (int) (DrawAPI.getAPI().getStringWidth(text)*scaleMultiplier);
    }

    public int getModuleHeight() {
        return moduleHeight;
    }

    public void draw(double screenX, double screenY) {
        if(isEnabled != true)
            return;
        Iterator<Text> textIterator = getText().iterator();
        while(textIterator.hasNext()) {
            Text text = textIterator.next();
            int stringWidth = (int) (DrawAPI.getAPI().getStringWidth(text.getText()) * scaleMultiplier);
            DrawAPI.getAPI().renderString(text.getText(), (float) screenX, (float) screenY, true, false, scaleMultiplier, text.getColor());
            screenX += stringWidth;
        }
    }

    public void savePosition(DataManager<Data> dataManager) {
        savePosition(dataManager, true);
    }

    public void savePosition(DataManager<Data> dataManager, boolean saveAttached) {
        dataManager.getSettings().getData().addProperty("modules." + key + ".distanceFromXRelative", distanceFromXRelative);
        dataManager.getSettings().getData().addProperty("modules." + key + ".distanceFromYRelative", distanceFromYRelative);
        if(attachedModule == null)
            dataManager.getSettings().getData().remove("modules." + key + ".attachedModule");
        else
            dataManager.getSettings().getData().addProperty("modules." + key + ".attachedModule", attachedModule.key);
        if(parentModule == null)
            dataManager.getSettings().getData().remove("modules." + key + ".parentModule");
        else
            dataManager.getSettings().getData().addProperty("modules." + key + ".parentModule", parentModule.key);
        dataManager.save();
        if(saveAttached && attachedModule != null)
            attachedModule.savePosition(dataManager, true);
    }

    public void setDistanceFromXRelative(int distance) {
        this.distanceFromXRelative = distance;
    }

    public void setDistanceFromYRelative(int distance) {
        this.distanceFromYRelative = distance;
    }

    public void updatePosition(int distanceFromXRelative, int distanceFromYRelative, RenderRelative renderRelative, boolean moveAttachedModules) {
        setDistanceFromXRelative(distanceFromXRelative);
        setDistanceFromYRelative(distanceFromYRelative);
        this.renderRelative = renderRelative;
        if(moveAttachedModules && hasAttachedModule())
            attachedModule.updatePosition(distanceFromXRelative, distanceFromYRelative + moduleHeight + 1, renderRelative, true);
    }

    public void updatePosition(int x, int y, int scaledWidth, int scaledHeight) {
        updatePosition(x, y, scaledWidth, scaledHeight, true);
    }

    public void updatePosition(int x, int y, int scaledWidth, int scaledHeight, boolean moveAttachedModules) {
        renderRelative = scaledWidth / 2 <= x ? RenderRelative.RIGHT_TOP : RenderRelative.LEFT_TOP;
        setDistanceFromXRelative(getDistanceFromRelative(
                        x,
                        scaledWidth,
                        DrawAPI.getAPI().getScaledWidth(),
                        true
                )
        );
        setDistanceFromYRelative(getDistanceFromRelative(
                        y,
                        scaledHeight,
                        DrawAPI.getAPI().getScaledHeight(),
                        false
                )
        );
        if(moveAttachedModules && hasAttachedModule())
            attachedModule.updatePosition(getDistanceFromXRelative(), getDistanceFromYRelative() + moduleHeight + 1, renderRelative, true);
    }

    private int getDistanceFromRelative(int point, int scale, int actualScale, boolean isHorizontal) {
        point = convertScaledRatio(point, scale, actualScale);
        boolean isRelativeToZero;
        if(isHorizontal)
            isRelativeToZero = renderRelative == RenderRelative.LEFT_TOP || renderRelative == RenderRelative.LEFT_BOTTOM;
        else
            isRelativeToZero = renderRelative == RenderRelative.LEFT_TOP || renderRelative == RenderRelative.RIGHT_TOP;
        return isRelativeToZero ? point : actualScale - point;
    }

    private int convertScaledRatio(int point, int currentScale, int targetScale) {
        double ratio = (double) targetScale / currentScale;
        return (int) (point * ratio);
    }

    public String getDisplayValue(){
        return "Dette modul er ikke aktivt!";
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        if(isEnabled)
            return;
        if(parentModule != null && attachedModule != null) {
            parentModule.attachedModule = attachedModule;
            attachedModule.parentModule = parentModule;
            this.parentModule = null;
            this.attachedModule = null;
        }else if (parentModule != null) {
            parentModule.attachedModule = null;
            this.parentModule = null;
        }else if (attachedModule != null) {
            attachedModule.parentModule = null;
            this.attachedModule = null;
        }
        savePosition(GuiModulesModule.getModulesDataManager());
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

    public void attachModule(GuiModule module) {
        if(module == this)
            return;
        if(module.isAttachedToModule())
            module.deattachFromParent();
        if(attachedModule != null) {
            module.attachModule(attachedModule);
        }
        attachedModule = module;
        attachedModule.parentModule = this;
        updatePosition(getDistanceFromXRelative(), getDistanceFromYRelative(), renderRelative, true);
    }

    public void deattachModule() {
        if(attachedModule == null)
            return;
        attachedModule.parentModule = null;
        attachedModule = null;
    }

    public void deattachFromParent() {
        if(parentModule != null)
            parentModule.deattachModule();
    }

    public boolean hasAttachedModule() {
        return attachedModule != null;
    }

    public boolean isAttachedToModule() {
        return parentModule != null;
    }

    public Collection<GuiModule> getSubModules() {
        Collection<GuiModule> subModules = new HashSet<>();
        subModules.add(this);
        if(hasAttachedModule())
            subModules.addAll(attachedModule.getSubModules());
        return subModules;
    }

    public GuiModule getNextModule() {
        return attachedModule;
    }

    public boolean hasNextModule() {
        return attachedModule != null;
    }

    public GuiModule getTopMostModule() {
        if(parentModule != null)
            return parentModule.getTopMostModule();
        return this;
    }

    public GuiModule getBottomMostModule() {
        if(attachedModule != null)
            return attachedModule.getBottomMostModule();
        return this;
    }

    public int getDistancetoBottomMostModule(boolean includeThis) {
        int distance = 0;
        if(includeThis)
            distance += moduleHeight + 1;
        if(attachedModule != null)
            distance += attachedModule.getDistancetoBottomMostModule(true);
        return distance;
    }
}
