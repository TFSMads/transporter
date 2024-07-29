package ml.volder.transporter.settings.classes.factories;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;

public class TextFieldWidgetFactory implements TransporterWidgetFactory<String> {
    @Override
    public Widget create(TransporterAction<String> transporterAction, DataManager<Data> dataManager, String configEntryName, String defaultValue) {
        String initialValue = dataManager.getSettings().getData().has(configEntryName) ? dataManager.getSettings().getData().get(configEntryName).getAsString() : defaultValue;

        transporterAction.andAfter((accepted) -> {
            dataManager.getSettings().getData().addProperty(configEntryName, accepted);
            dataManager.save();
        });

        transporterAction.setInitialValue(initialValue);

        TextFieldWidget widget = new TextFieldWidget();
        widget.updateListener(transporterAction);

        widget.setText(initialValue);

        return widget;
    }

    @Override
    public Class<String> getActionType() {
        return String.class;
    }
}
