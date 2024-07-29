package ml.volder.transporter.settings.widgets;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.DefaultEntryRenderer;
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
public class DropDownWidget<T> extends DropdownWidget<T> {

    @SettingElement(switchable = StringSwitchableHandler.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DropdownSetting {
        String dataManager() default "%common%/settings.json";
        String configEntryName() default "";
    }

    @SettingFactory
    public static class Factory
            implements WidgetFactory<DropDownWidget.DropdownSetting, DropdownWidget<?>> {

        @Override
        public DropdownWidget<?>[] create(
                Setting setting,
                DropDownWidget.DropdownSetting annotation,
                SettingAccessor accessor
        ) {
            DropdownWidget<?> widget = TransporterWidgetFactory.createWidget(
                    DropdownWidget.class,
                    new TransporterAction<>(accessor::set),
                    DataManager.getOrCreateDataManager(annotation.dataManager()),
                    annotation.configEntryName(),
                    accessor.get()
            );

            // Translation key prefix
            DropdownEntryTranslationPrefix prefixAnnotation = accessor.getField().getAnnotation(
                    DropdownEntryTranslationPrefix.class
            );
            DefaultEntryRenderer<Object> entryRenderer = (DefaultEntryRenderer<Object>) widget.entryRenderer();
            if (prefixAnnotation == null) {
                entryRenderer.setTranslationKeyPrefix(accessor.setting().getTranslationKey() + ".entries");
            } else {
                entryRenderer.setTranslationKeyPrefix(prefixAnnotation.value());
            }

            return new DropdownWidget[]{widget};
        }

        @Override
        public Class<?>[] types() {
            return new Class[0];
        }
    }


}
