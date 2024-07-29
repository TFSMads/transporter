package ml.volder.unikapi.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import org.jetbrains.annotations.NotNull;

public class UnikHudWidgetCategory extends HudWidgetCategory {

    private final String title;
    private final String description;

    public UnikHudWidgetCategory(@NotNull String id, String title, String description) {
      super(id);
      this.title = title;
      this.description = description;
    }

    @NotNull
    @Override
    public Component title() {
      return Component.text(title)
          .decorate(TextDecoration.BOLD);
    }

    @NotNull
    @Override
    public Component description() {
      return Component.text(description);
    }
}
