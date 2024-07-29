package ml.volder.transporter.settings.classes;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.configuration.settings.type.SettingElement;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class TransporterSettingElementFactory {

    public static SettingElement create(String id, Icon icon, String customTranslation, String[] searchTags, Widget[] widgets) {
        if(id == null || id.isEmpty())
            id = randomId();
        SettingElement element = new SettingElement(id, icon, customTranslation, searchTags);
        if(widgets != null)
            element.setWidgets(widgets);
        return element;
    }
    public static SettingElement create(String id, Icon icon, String customTranslation, String[] searchTags) {
        return create(id, icon, customTranslation, searchTags, new Widget[]{});
    }

    public static SettingElement create(String id, Icon icon) {
        return create(id, icon, null, null);
    }

    public static SettingElement create(String id) {
        return create(id, (Icon) null);
    }

    public static SettingElement create(String id, Icon icon, Widget[] widgets) {
        return create(id, icon, null, null, widgets);
    }

    public static SettingElement create(String id, Widget[] widgets) {
        return create(id, null, widgets);
    }

    public static SettingElement create(Builder builder) {
        return create(builder.id, builder.icon, builder.customTranslation, builder.searchTags, builder.widgets.toArray(new Widget[0]));
    }

    private static String randomId() {
        byte[] array = new byte[8]; // length is bounded by 8
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public static class Builder {
        private String id;
        private Icon icon;
        String customTranslation;
        String[] searchTags;

        List<Widget> widgets = new ArrayList<>();

        public static Builder begin() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder icon(Icon icon) {
            this.icon = icon;
            return this;
        }

        public Builder customTranslation(String customTranslation) {
            this.customTranslation = customTranslation;
            return this;
        }

        public Builder searchTags(String[] searchTags) {
            this.searchTags = searchTags;
            return this;
        }

        public Builder addWidget(Widget widget) {
            widgets.add(widget);
            return this;
        }

        public Builder addWidgets(Widget[] widgets) {
            this.widgets.addAll(List.of(widgets));
            return this;
        }

        public Builder addWidgets(Collection<Widget> widgets) {
            this.widgets.addAll(widgets);
            return this;
        }

        public SettingElement build() {
            return TransporterSettingElementFactory.create(this);
        }
    }
}
