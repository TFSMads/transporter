package ml.volder.unikapi.loader.laby4;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.labymod.api.Laby;
import net.labymod.api.configuration.settings.type.SettingElement;
import net.labymod.api.localization.Internationalization;

public class UnikSetting extends SettingElement {

  Consumer<UnikRootSettingRegistry> initializeCallback;
  UnikRootSettingRegistry registry;

  public UnikSetting(Consumer<UnikRootSettingRegistry> initializeCallback, UnikRootSettingRegistry registry) {
    super("unikapi-settings-loader", null, "", new String[0]);
    addTranslation();
    this.registry = registry;
    this.initializeCallback = initializeCallback;
  }

  private boolean addTranslation() {
    try {
      Internationalization internationalization = Laby.references().internationalization();
      Field field = internationalization.getClass().getDeclaredField("fallbackTranslations");
      field.setAccessible(true);
      Object fieldValue = field.get(internationalization);
      if(fieldValue instanceof Map) {
        Map<String, String> map = (Map<String, String>) fieldValue;
        map.put("unikapi-undefined.name", "");
        return true;
      }
      return false;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      return false;
    }
  }

  @Override
  public String getTranslationKey() {
    addTranslation();
    return "unikapi-undefined";
  }

  @Override
  public String[] getSearchTags() {
    return new String[0];
  }

  @Override
  public boolean isElement() {
    return true;
  }

  @Override
  public SettingElement asElement() {
    initializeCallback.accept(registry);
    return super.asElement();
  }
}
