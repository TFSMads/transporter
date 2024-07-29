package ml.volder.transporter.settings.widgets;

import ml.volder.transporter.modules.ModuleManager;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;
import net.labymod.api.util.PrimitiveHelper;
import net.labymod.api.util.bounds.ModifyReason;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@AutoWidget
@SettingWidget
@Link("modules.lss")
public class TransporterModulesWidget extends FlexibleContentWidget {

    private static final ModifyReason FLEX_BOUNDS_VERTICAL = ModifyReason.of("flexBoundsVertical");

    private void updateHeight() {
        this.bounds().setHeight(ModuleManager.getInstance().getTransporterModuleWidgetList().size() * 42, FLEX_BOUNDS_VERTICAL);
    }

    @Override
    protected void updateContentBounds() {
        super.updateContentBounds();
        updateHeight();
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        for (TransporterModuleWidget widget : ModuleManager.getInstance().getTransporterModuleWidgetList()) {
            widget.addId("module");
            this.addFlexibleContent(widget);
        }
    }

    @Override
    public void postStyleSheetLoad() {
        super.postStyleSheetLoad();
        updateHeight();
    }

    @SettingElement(
            extended = true
    )
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ModuleWidget {

        /**
         * The displayName of the module
         *
         * @return the displayName
         */
        String displayName() default "";

        /**
         * The description of the module.
         *
         * @return the moduleDescription
         */
        String moduleDescription() default "";
    }


    @SettingFactory
    public static class Factory implements WidgetFactory<ModuleWidget, TransporterModulesWidget> {

        public Factory() {
        }

        @Override
        public TransporterModulesWidget[] create(Setting setting, ModuleWidget annotation, SettingInfo<?> info, SettingAccessor accessor) {

            TransporterModulesWidget widget = new TransporterModulesWidget();

            return new TransporterModulesWidget[]{widget};
        }

        @Override
        public Class<?>[] types() {
            return PrimitiveHelper.BOOLEAN;
        }
    }
}
