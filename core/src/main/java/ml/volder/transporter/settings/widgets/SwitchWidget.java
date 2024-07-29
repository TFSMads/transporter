package ml.volder.transporter.settings.widgets;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.switchable.BooleanSwitchableHandler;
import net.labymod.api.configuration.settings.widget.WidgetFactory;
import net.labymod.api.util.PrimitiveHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SettingWidget
public class SwitchWidget extends SimpleWidget {
    @SettingElement(switchable = BooleanSwitchableHandler.class)
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SwitchSetting {
        String dataManager() default "%common%/settings.json";
        String configEntryName() default "";
    }

    @SettingFactory
    public static class Factory implements WidgetFactory<SwitchSetting, Widget> {

        @Override
        public Widget[] create(Setting setting, SwitchSetting annotation, SettingAccessor accessor) {
            net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget switchWidget = TransporterWidgetFactory.createWidget(
                    net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.class,
                    new TransporterAction<Boolean>(accessor::set),
                    DataManager.getOrCreateDataManager(annotation.dataManager()),
                    annotation.configEntryName(),
                    accessor.get()
            );

            return new net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget[]{switchWidget};
        }

        @Override
        public Class<?>[] types() {
            return PrimitiveHelper.BOOLEAN;
        }
    }
}
