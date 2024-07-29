package ml.volder.unikapi.guisystem;


import ml.volder.unikapi.types.ResourceLocation;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.theme.ThemeChangeEvent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Singleton
public class ModTextures {

    public static final String TEXTURE_FOLDER = "transporter/textures/";

    public static final ResourceLocation WIDGETS = new ResourceLocation("transporter", TEXTURE_FOLDER + "gui/widgets.png");
    public static final ResourceLocation SERVER_SELECTION = new ResourceLocation("transporter", TEXTURE_FOLDER + "gui/server_selection.png");
    public static final ResourceLocation BUTTON_LARGE_PRESSED = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/button_large_pressed.png");
    public static final ResourceLocation BUTTON_LARGE = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/button_large.png");
    public static final ResourceLocation BUTTON_LARGE_DISABLED = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/button_large_disabled.png");
    public static final ResourceLocation BUTTON_CHECKBOX = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/checkbox.png");
    public static final ResourceLocation BUTTON_EXCLAMATION = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/exclamation_mark.png");
    public static final ResourceLocation BUTTON_ADVANCED = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/advanced.png");
    public static final ResourceLocation BUTTON_SETTINGS = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/addon_settings.png");
    public static final ResourceLocation BUTTON_TOGGLE = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/toggle.png");
    public static final ResourceLocation BUTTON_GET = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/get.png");
    public static final ResourceLocation BUTTON_PUT = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/put.png");
    public static final ResourceLocation MISC_HEAD_QUESTION = new ResourceLocation("transporter",TEXTURE_FOLDER + "misc/question.png");
    public static final ResourceLocation SA_LOGO = new ResourceLocation("transporter", TEXTURE_FOLDER + "misc/sa.png");
    public static final ResourceLocation BUTTON_EXPAND = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/expand.png");
    public static final ResourceLocation MINECRAFT_BACKGROUND = new ResourceLocation("transporter", TEXTURE_FOLDER + "misc/minecraft_background.png");
    public static final ResourceLocation SIGN = new ResourceLocation("transporter", TEXTURE_FOLDER + "misc/sign.png");
    public static final ResourceLocation BUTTON_HOVER_DEFAULT = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/hover_default.png");
    public static final ResourceLocation BUTTON_PASTE = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/paste.png");
    public static final ResourceLocation BUTTON_COPY = new ResourceLocation("transporter", TEXTURE_FOLDER + "buttons/copy.png");

    public static net.labymod.api.client.resources.ResourceLocation SETTINGS_ICONS = Laby.labyAPI().themeService().currentTheme().resource("labymod", "textures/settings/main/laby.png");
    public static net.labymod.api.client.resources.ResourceLocation SETTINGS_ICONS_1 = Laby.labyAPI().themeService().currentTheme().resource("labymod", "textures/settings/main/laby_1.png");
    public static net.labymod.api.client.resources.ResourceLocation WIDGET_EDITOR_ICONS = Laby.labyAPI().themeService().currentTheme().resource("labymod", "textures/activities/hud/widget_editor.png");
    public static net.labymod.api.client.resources.ResourceLocation COMMON_ICONS = Laby.labyAPI().themeService().currentTheme().resource("labymod", "textures/activities/common.png");
    public static net.labymod.api.client.resources.ResourceLocation FLINT_ICONS = Laby.labyAPI().themeService().currentTheme().resource("labymod", "textures/activities/flint/flint.png");



    private ModTextures() {
        Laby.labyAPI().eventBus().registerListener(this);
    }

    private static ModTextures instance;

    @Subscribe
    public void onThemeChange(@NotNull ThemeChangeEvent event) {
        SETTINGS_ICONS = event.newTheme().resource("labymod", "textures/settings/main/laby.png");
        SETTINGS_ICONS_1 = event.newTheme().resource("labymod", "textures/settings/main/laby_1.png");
        WIDGET_EDITOR_ICONS = event.newTheme().resource("labymod", "textures/activities/hud/widget_editor.png");
        COMMON_ICONS = event.newTheme().resource("labymod", "textures/activities/common.png");
        FLINT_ICONS = event.newTheme().resource("labymod", "textures/activities/flint/flint.png");
    }

    private static ModTextures getInstance() {
        if (instance == null) {
            instance = new ModTextures();
        }
        return instance;
    }

    static {
        // Create instance
        getInstance();
    }

}
