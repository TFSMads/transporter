package ml.volder.transporter.settings.classes.factories;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;

public class SwitchWidgetFactory implements TransporterWidgetFactory<Boolean> {
    @Override
    public Widget create(TransporterAction<Boolean> transporterAction, DataManager<Data> dataManager, String configEntryName, Boolean defaultValue) {

        boolean initialValue = dataManager.getSettings().getData().has(configEntryName) ? dataManager.getSettings().getData().get(configEntryName).getAsBoolean() : defaultValue;

        transporterAction.setInitialValue(initialValue);

        transporterAction.andAfter((accepted) -> {
            dataManager.getSettings().getData().addProperty(configEntryName, accepted);
            dataManager.save();
        });
        SwitchWidget widget = SwitchWidget.create(transporterAction);

        widget.setValue(initialValue);

        return widget;
    }

    @Override
    public Class<Boolean> getActionType() {
        return Boolean.class;
    }
}
