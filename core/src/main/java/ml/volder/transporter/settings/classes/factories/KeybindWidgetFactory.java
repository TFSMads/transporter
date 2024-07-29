package ml.volder.transporter.settings.classes.factories;

import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.mapper.KeyMapper;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;

public class KeybindWidgetFactory implements TransporterWidgetFactory<Key> {
    @Override
    public Widget create(TransporterAction<Key> transporterAction, DataManager<Data> dataManager, String configEntryName, Key defaultValue) {
        Key initialValue = dataManager.getSettings().getData().has(configEntryName)
                    ? dataManager.getSettings().getData().get(configEntryName).getAsString() != null
                        ? KeyMapper.getKey(dataManager.getSettings().getData().get(configEntryName).getAsString())
                        : defaultValue
                    : defaultValue;

        transporterAction.andAfter(accepted -> {
            dataManager.getSettings().getData().addProperty(configEntryName, accepted.getName());
            dataManager.save();
        });

        transporterAction.setInitialValue(initialValue);

        KeybindWidget widget = new KeybindWidget(transporterAction);

        widget.key(initialValue);

        return widget;
    }

    @Override
    public Class<Key> getActionType() {
        return Key.class;
    }
}
