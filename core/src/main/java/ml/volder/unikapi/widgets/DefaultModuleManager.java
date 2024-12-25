package ml.volder.unikapi.widgets;

import ml.volder.unikapi.types.Material;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;

import java.util.function.Function;

public class DefaultModuleManager implements ModuleManager {

  int categoryId = 0;
  @Override
  public Object registerCategory(String title, Material material, String description) {
    HudWidgetCategory category = new UnikHudWidgetCategory("unikapi-category-" + categoryId, title, description);
    Laby.labyAPI().hudWidgetRegistry().categoryRegistry().register(category);
    categoryId += 1;
    return category;
  }

  @Override
  public void registerModule(String key, String defaultPrefix, boolean defaultIsEnabled, Object category, Material icon, Function<String, Component> getDisplayValue) {
    if(!(category instanceof HudWidgetCategory))
      return;
    Laby.labyAPI().hudWidgetRegistry().register(new UnikHudWidget(key, (HudWidgetCategory) category, defaultPrefix, icon,getDisplayValue));
  }

  @Override
  public void openEditor() {
    ScreenNavigationElement navigationElement = Laby.references().navigationRegistry().getById("labymod") instanceof ScreenNavigationElement ? (ScreenNavigationElement) Laby.references().navigationRegistry().getById("labymod") : null;
    if(navigationElement == null || navigationElement.getScreen() == null || !(navigationElement.getScreen() instanceof TabbedActivity))
      return;
    TabbedActivity tabbedActivity = (TabbedActivity) navigationElement.getScreen();
    tabbedActivity.switchTab("widgets");
    Laby.labyAPI().minecraft().minecraftWindow().displayScreen(tabbedActivity);
  }
}
