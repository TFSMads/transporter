package ml.volder.transporter.settings.accesors;

import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.AbstractSetting;

import java.util.List;

public interface SettingRegistryAccessor {
    void add(AbstractSetting setting);
    void addAll(List<Setting> settings);
    boolean reset();
}
