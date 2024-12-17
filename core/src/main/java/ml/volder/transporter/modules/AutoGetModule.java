package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.autoget.AutoGetMenu;
import ml.volder.transporter.modules.autoget.SelectItemMenu;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import ml.volder.unikapi.types.Material;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import org.jetbrains.annotations.NotNull;

public class AutoGetModule extends SimpleModule {

    private boolean onlyActiveInLobby = true;
    private boolean isEnabled;
    private int minimumAmount;
    private Item selectedItem;
    private TransporterAddon addon;
    private int delay = 50;
    private Key openKey = Key.N;

    private int timer = 0;

    private ListContainerElement selectItemElement;

    public AutoGetModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
        this.addon = TransporterAddon.getInstance();
    }

    @Override
    public SimpleModule init() {
        isEnabled = false;
        return this;
    }

    @Override
    public SimpleModule enable() {
        //Fill auto get menu
        BooleanElement booleanElement = new BooleanElement("Er aktiv",  getDataManager(), null, new ControlElement.IconData(Material.REDSTONE_TORCH), isEnabled);
        booleanElement.addCallback(aBoolean -> {
            isEnabled = aBoolean;
        });
        AutoGetMenu.addSetting(booleanElement);

        NumberElement numberElement = new NumberElement("Maximum antal af valgte item, før der tages mere.", getDataManager(), "minimumAmount", new ControlElement.IconData(Material.DIODE), minimumAmount);
        numberElement.addCallback(integer -> minimumAmount = integer);
        AutoGetMenu.addSetting(numberElement);

        ControlElement.IconData iconData = new ControlElement.IconData(selectedItem.getMaterial());
        iconData.setItemDamage(selectedItem.getItemDamage());
        selectItemElement = new ListContainerElement("Valgte item", iconData);
        selectItemElement.setAdvancedButtonCallback(aBoolean -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new SelectItemMenu(Laby.labyAPI().minecraft().minecraftWindow().currentScreen())));
        selectItemElement.getSubSettings().add(new HeaderElement(""));
        selectItemElement.setOpenSubSettings(false);
        AutoGetMenu.addSetting(selectItemElement);
        Laby.labyAPI().eventBus().registerListener(this);
        return this;
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        minimumAmount = getDataManager().getSettings().getData().has("minimumAmount") ? getDataManager().getSettings().getData().get("minimumAmount").getAsInt() : 64;
        selectedItem = addon.getTransporterItemManager().getItemByType(getDataManager().getSettings().getData().has("selectedItem") ? getDataManager().getSettings().getData().get("selectedItem").getAsString() : "dirt");
    }

    @Subscribe
    public void onTick(@NotNull GameTickEvent event){
        if(event.phase() == Phase.POST)
            return;
        if(!TransporterAddon.isEnabled() || !this.isEnabled || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        timer++;

        if(!(timer >= delay))
            return;

        if(InventoryAPI.getAPI().getAmount(selectedItem.getMaterial(), selectedItem.getItemDamage()) < minimumAmount) {
            PlayerAPI.getAPI().sendCommand("transporter get " + selectedItem.getModernType());
            timer = 0;
        }
    }

    @Subscribe
    public void onKeyPress(KeyEvent event){
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(openKey == null || openKey.equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(openKey) && !Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened())
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new AutoGetMenu(this, null));
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

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
                        SliderWidget.class,
                        new TransporterAction<Float>(v -> delay = v != null ? v.intValue() : delay),
                        getDataManager(),
                        "autoGetDelay",
                        125F).range(20, 500))
                .id("autoGetDelay")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 1))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        KeybindWidget.class,
                        new TransporterAction<net.labymod.api.client.gui.screen.key.Key>(key -> openKey = Laby4KeyMapper.convert(key)),
                        getDataManager(),
                        "autoGetKeybind",
                        net.labymod.api.client.gui.screen.key.Key.N))
                .id("autoGetKeybind")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 5))
                .build()
        );
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void selectItem(Item item) {
        this.selectedItem = item;
        getDataManager().getSettings().getData().addProperty("selectedItem", selectedItem.getModernType());
        getDataManager().save();
        ControlElement.IconData iconData = new ControlElement.IconData(selectedItem.getMaterial());
        iconData.setItemDamage(selectedItem.getItemDamage());
        selectItemElement.setIconData(iconData);
    }
}
