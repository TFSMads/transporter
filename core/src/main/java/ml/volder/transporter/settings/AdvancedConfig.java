package ml.volder.transporter.settings;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.CsvEditor;
import ml.volder.transporter.modules.BalanceModule;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.settings.widgets.IconButtonWidget;
import ml.volder.transporter.settings.widgets.SwitchWidget;
import ml.volder.unikapi.UnikAPI;
import net.labymod.api.Laby;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.MethodOrder;

import java.io.File;

public class AdvancedConfig extends Config {
    @SpriteSlot(page = 1, x = 4, y = 4)
    private LoggerConfig loggerOptions = new LoggerConfig();

    @SpriteSlot(y = 7)
    @SwitchWidget.SwitchSetting(configEntryName = "updateItemsFromGithub")
    private ConfigProperty<Boolean> updateItems = new ConfigProperty<>(true);

    @MethodOrder(after = "updateItems")
    @SpriteSlot(page = 1, x = 3, y = 1)
    @IconButtonWidget.IconButtonSetting(ids="sub-settings-button")
    public void openItemsEditor(Setting setting) {
        CsvEditor.openEditor(
                new File(UnikAPI.getCommonDataFolder(), "transporter-items.csv"),
                ';',
                csvFile -> TransporterAddon.getInstance().getTransporterItemManager().reloadItemsFromCSV()
        );
        Laby.labyAPI().minecraft().sounds().playButtonPress();
    }

    @SpriteSlot(y = 7)
    @SwitchWidget.SwitchSetting(configEntryName = "updateMessagesFromGithub")
    private ConfigProperty<Boolean> updateMessages = new ConfigProperty<>(true);

    @MethodOrder(after = "updateMessages")
    @SpriteSlot(page = 1, x = 4, y = 4)
    @IconButtonWidget.IconButtonSetting(ids="sub-settings-button")
    public void openMessagesEditor(Setting setting) {
        CsvEditor.openEditor(
                new File(UnikAPI.getCommonDataFolder(), "transporter-messages.csv"),
                ';',
                csvFile -> ModuleManager.getInstance().getModule(MessagesModule.class).reloadMessagesFromCSV()
        );
        Laby.labyAPI().minecraft().sounds().playButtonPress();
    }

    @SpriteSlot(y = 7)
    @SwitchWidget.SwitchSetting(configEntryName = "updateBalanceMessagesFromGithub")
    private ConfigProperty<Boolean> updateBalanceMessages = new ConfigProperty<>(true);

    @MethodOrder(after = "updateBalanceMessages")
    @SpriteSlot(page = 1, size = 32, x = 3, y = 3)
    @IconButtonWidget.IconButtonSetting(ids="sub-settings-button")
    public void openBalanceMessagesEditor(Setting setting) {
        CsvEditor.openEditor(
                new File(UnikAPI.getCommonDataFolder(), "transporter-balance-messages.csv"),
                ';',
                csvFile -> ModuleManager.getInstance().getModule(BalanceModule.class).reloadMessagesFromCSV()
        );
        Laby.labyAPI().minecraft().sounds().playButtonPress();
    }
}
