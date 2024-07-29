package ml.volder.transporter.settings;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.pricegui.PriceMenu;
import ml.volder.transporter.settings.widgets.DropDownWidget;
import ml.volder.transporter.settings.widgets.IconButtonWidget;
import ml.volder.transporter.settings.widgets.TextFieldWidget;
import ml.volder.transporter.settings.widgets.TransporterModulesWidget;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.api.player.PlayerAPI;
import net.labymod.api.Laby;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.MethodOrder;

import java.util.Arrays;
import java.util.List;

@SpriteTexture("settings/icons")
public class TransporterAddonConfig extends AddonConfig {

    public TransporterAddonConfig() {

        selectedNumberFormat.addChangeListener(formattingMode -> FormatingUtils.formattingMode = (formattingMode));
        lobbyServers.addChangeListener(servers -> {
            if(!TransporterAddon.isInitialized())
                return;
            List<String> serverList = TransporterAddon.getInstance().getServerList();
            serverList.clear();
            serverList.addAll(Arrays.stream(servers.split(",")).toList());
        });
    }

    @Override
    public ConfigProperty<Boolean> enabled() {
        return new ConfigProperty<>(true);
    }

    @SpriteSlot(page = 1, x = 4, y = 4)
    @DropDownWidget.DropdownSetting(configEntryName = "selectedNumberFormat")
    private ConfigProperty<FormatingUtils.FORMATTING_MODE> selectedNumberFormat = new ConfigProperty<>(FormatingUtils.FORMATTING_MODE.PUNKTUM);

    @SpriteSlot(page = 1, x = 4, y = 1)
    @TextFieldWidget.TextFieldSetting(configEntryName = "updateServere")
    private ConfigProperty<String> lobbyServers = new ConfigProperty<>("limbo,larmelobby,shoppylobby,maskinrummet,creepylobby");

    @SpriteSlot(page = 1, x = 3, y = 2)
    private AdvancedConfig advancedSubSettings = new AdvancedConfig();

    @MethodOrder(after = "advancedSubSettings")
    @SpriteSlot(page = 1, size = 32, x = 3, y = 3)
    @IconButtonWidget.IconButtonSetting(ids="sub-settings-button")
    public void openValueSettings(Setting setting) {
        PlayerAPI.getAPI().openGuiScreen(new PriceMenu(Laby.labyAPI().minecraft().minecraftWindow().currentScreen()));
        Laby.labyAPI().minecraft().sounds().playButtonPress();
    }

    @TransporterModulesWidget.ModuleWidget
    private ConfigProperty<Boolean> modules = new ConfigProperty<>(true);
}
