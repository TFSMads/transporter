package ml.volder.transporter.settings.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.Pressable;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


@AutoWidget
@SettingWidget
@Link("button.lss")
@Link("style.lss")
public class IconButtonWidget extends ButtonWidget{

    protected IconButtonWidget(Component component, Icon icon) {
        super(component, icon);
    }

    @SettingFactory
    public static class Factory implements WidgetFactory<IconButtonWidget.IconButtonSetting, IconButtonWidget> {
        public Factory() {
        }

        public IconButtonWidget[] create(Setting setting, IconButtonWidget.IconButtonSetting annotation, SettingInfo<?> info, SettingAccessor accessor) {

            IconButtonWidget widget = new IconButtonWidget(null , null);
            widget.setPressable(this.invokeButtonPress(setting, info));

            widget.addId(annotation.ids());

            return new IconButtonWidget[]{widget};
        }

        public Class<?>[] types() {
            return new Class[0];
        }

        private Pressable invokeButtonPress(Setting setting, SettingInfo<?> settingInfo) {
            return () -> {
                try {
                    Method method = (Method)settingInfo.getMember();
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length == 0) {
                        method.invoke(settingInfo.config());
                        return;
                    }

                    Object[] arguments = new Object[parameters.length];

                    for(int i = 0; i < parameters.length; ++i) {
                        Parameter parameter = parameters[i];
                        if (parameter.getType() == Setting.class) {
                            arguments[i] = setting;
                        }
                    }

                    method.invoke(settingInfo.config(), arguments);
                } catch (InvocationTargetException | IllegalAccessException var7) {
                    var7.printStackTrace();
                }

            };
        }
    }

    @SettingElement
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IconButtonSetting {
        String[] ids() default {};
    }

}
