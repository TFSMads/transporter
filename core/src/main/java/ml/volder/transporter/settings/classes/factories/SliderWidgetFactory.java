package ml.volder.transporter.settings.classes.factories;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;

public class SliderWidgetFactory implements TransporterWidgetFactory<Float> {
    @Override
    public Widget create(TransporterAction<Float> transporterAction, DataManager<Data> dataManager, String configEntryName, Float defaultValue) {
        float initialValue = dataManager.getSettings().getData().has(configEntryName) ? dataManager.getSettings().getData().get(configEntryName).getAsFloat() : defaultValue;

        transporterAction.andAfter((accepted) -> {
            dataManager.getSettings().getData().addProperty(configEntryName, accepted);
            dataManager.save();
        });

        transporterAction.setInitialValue(initialValue);

        SliderWidget widget = new SliderWidget(1.0F, transporterAction);

        widget.setValue(initialValue);

        return widget;
    }

    @Override
    public Class<Float> getActionType() {
        return Float.class;
    }
}
