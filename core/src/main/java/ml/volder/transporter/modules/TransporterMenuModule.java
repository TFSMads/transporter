package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.transportermenumodule.TransporterMenu;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.Settings;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransporterMenuModule extends SimpleModule implements Listener {

  private List<Item> activeItems = new ArrayList<>();
    private boolean onlyActiveInLobby = true;
    private int withdrawAmount = 64;

    private Key openKey = Key.K;

    public TransporterMenuModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        return this;
    }

    @Override
    public SimpleModule enable() {
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void fillSettings(SettingRegistryAccessor subSettings) {
        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.onlyActiveInLobby = b),
                        getDataManager(),
                        "onlyActiveInLobby",
                        true))
                .id("onlyActiveInLobby")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 4, 1))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        KeybindWidget.class,
                        new TransporterAction<net.labymod.api.client.gui.screen.key.Key>(key -> openKey = Laby4KeyMapper.convert(key)),
                        getDataManager(),
                        "transporterMenuKeybind",
                        net.labymod.api.client.gui.screen.key.Key.K))
                .id("transporterMenuKeybind")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 5))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SliderWidget.class,
                        new TransporterAction<Float>(v -> withdrawAmount = v != null ? v.intValue() : withdrawAmount),
                        getDataManager(),
                        "withdrawAmount",
                        -1F).range(-1, 2304))
                .id("withdrawAmount")
                .icon(Icon.sprite32(ModTextures.FLINT_ICONS, 2, 1))
                .build()
        );
    }

    @Override
  public void loadConfig() {
    super.loadConfig();
    if(getDataManager() != null){
      for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
        if(getDataManager().getSettings().getData().has(item.getModernType())){
          if(getDataManager().getSettings().getData().get(item.getModernType()).getAsBoolean()){
            activeItems.add(item);
          }
        }
      }
      if(activeItems.size() == 0) {
        List<String> defaultItems = Arrays.asList("stone","cobblestone","diamond_ore","diamond_block","gold_block","iron_block","iron_ore","gold_ore","red_sand","sand","oak_log","spruce_log","dirt","grass_block","birch_log","glass","white_wool","chest","crafting_table","emerald_block","beacon","redstone","trapped_chest","sticky_piston","piston","hopper","comparator","diamond","emerald","iron_ingot","gold_ingot","lapis_lazuli","quartz","coal","bone","bone_meal","item_frame","cooked_beef","glowstone","obsidian","glowstone_dust","slime_ball","spruce_planks","spruce_sapling","oak_sapling","leather","sugar_cane","torch","blue_stained_glass","white_stained_glass","cocoa_beans","grass","oak_planks","jukebox","repeater");
        for(String item : defaultItems){
          Item i = TransporterAddon.getInstance().getTransporterItemManager().getItemByType(item);
          if(i != null){
            addActiveItem(i);
          }
        }
      }
    }
  }

    public void fillSettings(Settings subSettings) {
        //onlyActiveInLobby: Kun aktiv i lobbyer!
        //transporterMenuKeybind: Åben menu
        //withdrawAmount: Antal items der skal trækkes ud
    }


    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        if(openKey == null || openKey.equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(openKey))
            this.open();
    }

    private void open() {
        if(PlayerAPI.getAPI().hasOpenScreen())
            return;
        PlayerAPI.getAPI().openGuiScreen(new TransporterMenu());
    }

    public void addActiveItem(Item item) {
        activeItems.add(item);
        if(getDataManager() != null){
          getDataManager().getSettings().getData().addProperty(item.getModernType(), true);
          getDataManager().save();
        }
    }

    public void removeActiveItem(Item item) {
        activeItems.remove(item);
        if(getDataManager() != null){
          getDataManager().getSettings().getData().addProperty(item.getModernType(), false);
          getDataManager().save();
        }
    }

    public boolean isActiveItem(Item item) {
        return activeItems.contains(item);
    }

    public List<Item> getActiveItems() {
      return activeItems;
    }

    public int getWithdrawAmount() {
        return withdrawAmount;
    }
}
