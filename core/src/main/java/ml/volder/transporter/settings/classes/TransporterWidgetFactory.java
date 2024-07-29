package ml.volder.transporter.settings.classes;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.factories.*;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;

import java.util.HashMap;
import java.util.Map;

public interface TransporterWidgetFactory<T> {

    Map<Class<? extends Widget>, TransporterWidgetFactory<?>> widgetFactories = new HashMap<>() {{
        put(SwitchWidget.class, new SwitchWidgetFactory());
        put(TextFieldWidget.class, new TextFieldWidgetFactory());
        put(SliderWidget.class, new SliderWidgetFactory());
        put(KeybindWidget.class, new KeybindWidgetFactory());
        put(DropdownWidget.class, new DropdownWidgetFactory<>());
    }} ;

    static void registerWidgetFactory(Class<? extends Widget> widgetClass, TransporterWidgetFactory<?> widgetFactory) {
        widgetFactories.put(widgetClass, widgetFactory);
    }

    @SuppressWarnings("unchecked")
    static <T extends Widget> T createWidget(Class<T> widgetClass, TransporterAction<?> transporterAction, DataManager<Data> dataManager, String configEntryName, Object defaultValue) {
        TransporterWidgetFactory<?> widgetFactory = widgetFactories.get(widgetClass);
        if(widgetFactory == null)
            return null;
        return (T) widgetFactory.createWidget(transporterAction, dataManager, configEntryName, defaultValue);
    }

    @SuppressWarnings("unchecked")
    default Widget createWidget(TransporterAction<?> transporterAction, DataManager<Data> dataManager, String configEntryName, Object defaultValue) {
        Class<?> clazz = getActionType();

        if(!defaultValue.getClass().isAssignableFrom(clazz))
            return null;
        if(!transporterAction.getActionType().isAssignableFrom(clazz))
            return null;

        return create((TransporterAction<T>) transporterAction, dataManager, configEntryName, (T) defaultValue);
    }

    Widget create(TransporterAction<T> transporterAction, DataManager<Data> dataManager, String configEntryName, T defaultValue);

    Class<?> getActionType();
}
