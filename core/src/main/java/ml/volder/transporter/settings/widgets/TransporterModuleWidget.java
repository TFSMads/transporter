package ml.volder.transporter.settings.widgets;

import ml.volder.transporter.settings.classes.TransporterSettingRegistry;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.types.ResourceLocation;
import ml.volder.unikapi.utils.ColorUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.activities.labymod.child.SettingContentActivity;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.sound.SoundType;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.AbstractSetting;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class TransporterModuleWidget extends SimpleWidget {

    private String moduleName;
    private String title;
    private String description;
    private boolean canHover = true;
    private boolean isActive;
    private int hoverButtonId = -1;
    private boolean showSettingsButton = true;
    private ResourceLocation icon;
    private Consumer<Boolean> toggleConsumer;
    private final TransporterSettingRegistry settingRegistry;

    public TransporterModuleWidget(String moduleName, String title, String description, ResourceLocation icon, Consumer<Boolean> toggleConsumer, boolean initialActiveState) {
        super();
        this.moduleName = moduleName;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.toggleConsumer = toggleConsumer;
        this.isActive = initialActiveState;

        this.settingRegistry = new TransporterSettingRegistry(moduleName, null);
    }

    @Override
    public void renderWidget(Stack stack, MutableMouse mouse, float partialTicks) {
        super.renderWidget(stack, mouse, partialTicks);
        TextRenderer textRenderer = Laby4DrawAPI.getTextRenderer();

        float x = bounds().getX();
        float y = bounds().getY();
        float maxX = bounds().getMaxX();
        float maxY = bounds().getMaxY();

        float iconSize = maxY - y;
        float textLineY = y + 3;

        // Draw background
        int color = isActive ? ColorUtils.toRGB(50, 100, 50, this.isHovered() ? 90 : 70)
            : ColorUtils.toRGB(100, 50, 50, this.isHovered() ? 90 : 70);

        Laby.references().rectangleRenderer()
                .pos(x, y, maxX, maxY)
                .color(color)
                .render(stack);

        // Draw icon
        Laby.references().resourceRenderer()
                .pos(x, y)
                .sprite(0, 0, 256, 256)
                .size(iconSize, iconSize)
                .texture(icon.toLaby())
                .render(stack);


        TextColor modColor = isActive ? NamedTextColor.GREEN : NamedTextColor.RED;

        // Draw Title
        textRenderer.pos(x + iconSize + 5, textLineY)
                    .text(title)
                    .color(modColor.getValue())
                    .shadow(true)
                    .render(stack);
        textLineY += 15;

        // Draw active status
        textRenderer.pos(x + iconSize + 5, y + 12)
                    .text((isActive ? "Denne feature er aktiv!" : "Denne feature er deaktiveret!"))
                    .color(modColor.getValue())
                    .scale(0.5F)
                    .shadow(true)
                    .render(stack);

        // Draw description
        drawDescription(x + iconSize + 5, textLineY, (int) ((maxX - x - iconSize - 60) / 0.7D), stack);

        // Render Buttons
        renderButtons((int) x, (int) y, (int) maxX, (int) maxY, mouse.getX(), mouse.getY(), stack);
    }

    private void renderButtons(int x, int y, int maxX, int maxY, int mouseX, int mouseY, Stack stack) {
        this.hoverButtonId = -1;
        int marginX = 10;
        int marginY = (maxY - y - 14) / 2;
        if (this.drawButton(ModTextures.BUTTON_TOGGLE, y, 14, 6, marginX, marginY, maxX, maxY, mouseX, mouseY, stack)) {
            this.hoverButtonId = 1;
        }

        marginX = 30;
        showSettingsButton = isActive && !settingRegistry.getElements().isEmpty();
        if (showSettingsButton && this.drawButton(ModTextures.BUTTON_SETTINGS, y, 14, 6, marginX, marginY, maxX, maxY, mouseX, mouseY, stack)) {
            this.hoverButtonId = 0;
        }
    }

    private void drawDescription(float x, float y, int wrapWidth, Stack stack) {
        List<String> descriptionLines = DrawAPI.getAPI().listFormattedStringToWidth(description, wrapWidth);
        int lineCount = 0;

        for (String descriptionLine : descriptionLines) {
            Laby4DrawAPI.getTextRenderer().pos(x, y)
                    .text(descriptionLine)
                    .color(NamedTextColor.GRAY.getValue())
                    .scale(0.7F)
                    .render(stack);
            y += 7;
            lineCount++;
            if (lineCount >= 3) {
                break;
            }
        }
    }

    private boolean drawButton(ResourceLocation resourceLocation, int y, int buttonSize, int buttonPadding, int marginX, int marginY, int maxX, int maxY, int mouseX, int mouseY, Stack stack) {
        boolean hover = mouseX > maxX - buttonSize - marginX + 1 && mouseX < maxX - buttonSize + buttonSize - marginX + 1 && mouseY > y + marginY + 1 && mouseY < y + buttonSize + marginY + 1;
        marginX += hover ? 1 : 0;
        int colorA = hover ? ColorUtils.toRGB(10, 10, 10, 255) : ColorUtils.toRGB(220, 220, 220, 255);
        int colorB = hover ? ColorUtils.toRGB(150, 150, 150, 255) : ColorUtils.toRGB(0, 0, 0, 255);
        int colorC = hover ? ColorUtils.toRGB(150, 150, 150, 255) : ColorUtils.toRGB(180, 180, 180, 255);

        RectangleRenderer rectangleRenderer = Laby.references().rectangleRenderer();

        rectangleRenderer.pos(maxX - buttonSize - marginX, y + marginY, maxX - marginX, y + buttonSize + marginY)
                        .color(colorA)
                        .render(stack);
        rectangleRenderer.pos(maxX - buttonSize - marginX + 1, y + marginY + 1, maxX - marginX + 1, y + buttonSize + marginY + 1)
                        .color(colorB)
                        .render(stack);
        rectangleRenderer.pos(maxX - buttonSize - marginX + 1, y + marginY + 1, maxX - marginX, y + buttonSize + marginY)
                        .color(colorC)
                        .render(stack);

        Laby.references().resourceRenderer()
                .pos(maxX - buttonSize - marginX + buttonPadding / 2 + (hover ? 1 : 0), y + marginY + buttonPadding / 2 + (hover ? 1 : 0))
                .sprite(0, 0, 256, 256)
                .size(buttonSize - buttonPadding, buttonSize - buttonPadding)
                .texture(resourceLocation.toLaby())
                .render(stack);

        return hover;
    }

    @Override
    public boolean mouseClicked(MutableMouse mouse, net.labymod.api.client.gui.screen.key.MouseButton mouseButton) {
        if (this.hoverButtonId == 1) {
            isActive = !isActive;
            if (toggleConsumer != null)
                toggleConsumer.accept(isActive);
        } else if (this.hoverButtonId == 0) {
            Laby.references().soundService().play(SoundType.BUTTON_CLICK);
            setCurrentHolder();
        }
        return true;
    }

    // Private fields from SettingContentActivity
    private static Field currentHolderField;
    private static Field screenCallbackField;


    static {
        try {
            currentHolderField = SettingContentActivity.class.getDeclaredField("currentHolder");
            currentHolderField.setAccessible(true);
            screenCallbackField = SettingContentActivity.class.getDeclaredField("screenCallback");
            screenCallbackField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            currentHolderField = null;
            screenCallbackField = null;
        }
    }

    /**
     * Sets the currentHolder in {@code SettingContentActivity} using reflection.
     */

    private void setCurrentHolder() {

        if(currentHolderField == null)
            return;
        Parent parent = this;
        while (parent.getParent() != null && !(parent instanceof SettingContentActivity))
            parent = parent.getParent();
        if(!(parent instanceof SettingContentActivity settingContentActivity))
            return;

        try {
            settingRegistry.setParent((Setting) currentHolderField.get(settingContentActivity));

            currentHolderField.set(settingContentActivity, settingRegistry);

            Object screenCallbackObj = screenCallbackField.get(settingContentActivity);
            Function<Setting, Setting> screenCallback = (screenCallbackObj instanceof Function) ? (Function<Setting, Setting>) screenCallbackObj : null;
            Setting currentHolder = (Setting) currentHolderField.get(settingContentActivity);

            if (screenCallback != null) {
                currentHolder = screenCallback.apply(currentHolder);
                currentHolderField.set(settingContentActivity, currentHolder);
            }

            if (currentHolder != null) {
                settingContentActivity.reload();
            }

        } catch (IllegalAccessException ignored) {}

    }

    public void addSubSetting(AbstractSetting setting) {
        settingRegistry.addSetting(setting);
    }

    public void addSubSettings(List<Setting> settings) {
        settingRegistry.addSettings(settings);
    }

    /**
     * A method that resets the subSettings if the settingRegistry is resettable.
     *
     * @return true if the subSettings was reset and false if not.
     */
    public boolean resetSubSettings() {
        if(!settingRegistry.isResettable())
            return false;
        settingRegistry.reset();
        return true;
    }
}
