package ml.volder.transporter.settings;

import ml.volder.transporter.settings.widgets.DropDownWidget;
import ml.volder.transporter.settings.widgets.SwitchWidget;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class LoggerConfig extends Config {

    @SpriteSlot(page = 2, y= 3)
    @DropDownWidget.DropdownSetting(dataManager = "%common%/loggerOptions.json", configEntryName = "logLevel")
    private ConfigProperty<Logger.LOG_LEVEL> logLevel = new ConfigProperty<>(Logger.LOG_LEVEL.INFO);

    @SpriteSlot(page = 2, x = 1, y= 7)
    @DropDownWidget.DropdownSetting(dataManager = "%common%/loggerOptions.json", configEntryName = "debugLevel")
    private ConfigProperty<Logger.DEBUG_LEVEL> debugLevel = new ConfigProperty<>(Logger.DEBUG_LEVEL.DISABLED);

    @SpriteSlot(page = 2, x = 4, y= 6)
    @SwitchWidget.SwitchSetting(dataManager = "%common%/loggerOptions.json", configEntryName = "printStackTrace")
    private ConfigProperty<Boolean> printStacktrace = new ConfigProperty<>(false);

    public LoggerConfig() {
        logLevel.addChangeListener(Logger.logLevel::set);
        debugLevel.addChangeListener(Logger.debugLevel::set);
        printStacktrace.addChangeListener(Logger.printStackTrace::set);
    }

}
