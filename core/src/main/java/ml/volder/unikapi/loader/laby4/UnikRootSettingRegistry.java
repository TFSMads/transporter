package ml.volder.unikapi.loader.laby4;

import net.labymod.api.client.component.Component;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.RootSettingRegistry;
import net.labymod.api.util.KeyValue;
import java.util.List;
import java.util.function.Consumer;

public class UnikRootSettingRegistry extends RootSettingRegistry {

  protected String displayName;
  Consumer<UnikRootSettingRegistry> initializeCallback;
  UnikSetting unikSetting;

  public UnikRootSettingRegistry(String namespace, String id, Consumer<UnikRootSettingRegistry> initializeCallback) {
    super(namespace, id);
    this.initializeCallback = initializeCallback;
    this.unikSetting = new UnikSetting(initializeCallback, this);
    this.addSetting(unikSetting);
  }

  @Override
  public List<KeyValue<Setting>> getElements() {
    return super.getElements();
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  @Override
  public Component displayName() {
    if(displayName != null){
      Component displayName = Component.empty();

      displayName = displayName.append(
          Component.text(this.displayName)
      );
      return displayName;
    }
    return super.displayName();
  }
}
