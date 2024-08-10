package ml.volder.transporter.settings.classes.factories;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.DefaultEntryRenderer;

public class DropdownWidgetFactory<T> implements TransporterWidgetFactory<T> {


    @Override
    @SuppressWarnings("unchecked")
    public Widget createWidget(TransporterAction<?> transporterAction, DataManager<Data> dataManager, String configEntryName, Object defaultValue) {
        try {
            return create((TransporterAction<T>) transporterAction, dataManager, configEntryName, (T) defaultValue);
        } catch (ClassCastException e) {
            return null;
        }

    }

    @Override
    public Widget create(TransporterAction<T> transporterAction, DataManager<Data> dataManager, String configEntryName, T defaultValue) {

        Class<?> clazz = defaultValue.getClass();

        T initialValue = defaultValue;
        if(clazz.isEnum() && dataManager.getSettings().getData().has(configEntryName)) {
            try {
                Object savedEnum = Enum.valueOf(clazz.asSubclass(Enum.class), dataManager.getSettings().getData().get(configEntryName).getAsString());
                initialValue = (T) savedEnum;
            } catch (IllegalArgumentException | ClassCastException ignored) {}
        }

        transporterAction.andAfter(accepted -> {
            dataManager.getSettings().getData().addProperty(configEntryName, accepted.toString());
            dataManager.save();
        });

        transporterAction.setInitialValue(initialValue);

        DropdownWidget<T> widget = new DropdownWidget<>();
        widget.setChangeListener(transporterAction);

        Class<?> type = defaultValue.getClass();


        if (type.isEnum()) {
            Enum[] enumConstants = (Enum[])type.getEnumConstants();
            for (Enum<?> enumConstant : enumConstants) {
                widget.add((T) enumConstant);
            }
        }

        DefaultEntryRenderer<T> entryRenderer = (DefaultEntryRenderer<T>) widget.entryRenderer();
        entryRenderer.setTranslationKeyPrefix("dropdownwidget." + defaultValue.getClass().getSimpleName() + ".entries");

        widget.setSelected(initialValue, true);

        return widget;
    }

    @Override
    public Class<?> getActionType() {
        return Enum.class;
    }
}
