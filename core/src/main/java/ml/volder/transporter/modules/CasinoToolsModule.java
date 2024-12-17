package ml.volder.transporter.modules;

import ml.volder.transporter.modules.casinotools.SetMultiCommand;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.loader.Laby4Loader;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.configuration.settings.type.SettingHeader;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CasinoToolsModule extends SimpleModule {

  private SliderWidget payBackMultiSlider;
  private boolean appendPayback = true;
  private float payBackMulti = 2;

  public CasinoToolsModule(ModuleManager.ModuleInfo moduleInfo) {
    super(moduleInfo);
  }

  @Override
  public SimpleModule init() {
    return this;
  }

  @Override
  public SimpleModule enable() {
    Laby4Loader.registerCommands(new SetMultiCommand((multi) -> payBackMultiSlider.setValue(multi,true)));
    Laby.references().eventBus().registerListener(this);
    return this;
  }

  @Override
  public void fillSettings(SettingRegistryAccessor subSettings) {
    subSettings.add(TransporterSettingElementFactory.Builder.begin()
            .addWidget(TransporterWidgetFactory.createWidget(
                    SwitchWidget.class,
                    new TransporterAction<Boolean>((b) -> this.appendPayback = b),
                    getDataManager(),
                    "appendPayback",
                    true))
            .id("appendPayback")
            .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 4, 3))
            .build()
    );

    subSettings.add(new SettingHeader(
            "header",
            true,
            "",
            "header"
    ));


    payBackMultiSlider = TransporterWidgetFactory.createWidget(
            SliderWidget.class,
            new TransporterAction<Float>(v -> payBackMulti = v),
            getDataManager(),
            "payBackMulti",
            2F).range(0F, 10).steps(0.1F);

    subSettings.add(TransporterSettingElementFactory.Builder.begin()
            .addWidget(payBackMultiSlider)
            .id("payBackMulti")
            .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 7, 1))
            .build()
    );
  }

  @Subscribe
  public void onMessage(ChatReceiveEvent event) {
    if(!appendPayback)
      return;

    final Pattern pattern = Pattern.compile("^\\[Money] ([0-9A-Za-z_]+) has sent you ([0-9.]+) Emerald\\.$");
    final Matcher matcher = pattern.matcher(event.chatMessage().getPlainText());
    if (matcher.find()) {
      double amount;
      try {
        amount = Double.parseDouble(matcher.group(2).replace(".", ""));
      } catch (NumberFormatException exception) {
        return;
      }
      int payBackAmount = (int) Math.round(payBackMulti*amount);
      String command = "/pay " + matcher.group(1) + " " + payBackAmount;
      event.chatMessage().edit(
              event.chatMessage().component().append(
                      Component.text(" (" + payBackAmount + ")", NamedTextColor.GRAY)
                              .clickEvent(ClickEvent.suggestCommand(command))
                              .hoverEvent(HoverEvent.showText(Component.text("Klik her for at sende!")))
              ));
    }
  }

}
