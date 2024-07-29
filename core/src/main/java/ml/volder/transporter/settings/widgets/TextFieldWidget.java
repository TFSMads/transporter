package ml.volder.transporter.settings.widgets;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.switchable.StringSwitchableHandler;
import net.labymod.api.configuration.settings.widget.WidgetFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SettingWidget
public class TextFieldWidget extends net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget {
    @SettingElement(switchable = StringSwitchableHandler.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TextFieldSetting {

        /**
         * The maximum length of the text field. Default is -1 (no maximum length).
         *
         * @return the maximum length
         */
        int maxLength() default -1;

        String dataManager() default "%common%/settings.json";
        String configEntryName() default "";
    }

    @SettingFactory
    public static class Factory implements WidgetFactory<TextFieldWidget.TextFieldSetting, net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget> {

        @Override
        public net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget[] create(Setting setting, TextFieldWidget.TextFieldSetting annotation,
                                                                                               SettingAccessor accessor) {
            net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget widget = TransporterWidgetFactory.createWidget(
                    net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.class,
                    new TransporterAction<String>(accessor::set),
                    DataManager.getOrCreateDataManager(annotation.dataManager()),
                    annotation.configEntryName(),
                    accessor.get()
            );

            return new net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget[]{widget};
        }

        @Override
        public Class<?>[] types() {
            return new Class[]{String.class};
        }
    }
}
