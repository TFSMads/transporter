package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.autoget.AutoGetMenu;
import ml.volder.transporter.modules.autoget.SelectItemMenu;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.event.events.clienttickevent.ClientTickEvent;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

public class AutoGetModule extends SimpleModule implements Listener {

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
        selectItemElement.setAdvancedButtonCallback(aBoolean -> PlayerAPI.getAPI().openGuiScreen(new SelectItemMenu(PlayerAPI.getAPI().getCurrentScreen())));
        selectItemElement.getSubSettings().add(new HeaderElement(""));
        selectItemElement.setOpenSubSettings(false);
        AutoGetMenu.addSetting(selectItemElement);
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        minimumAmount = getDataManager().getSettings().getData().has("minimumAmount") ? getDataManager().getSettings().getData().get("minimumAmount").getAsInt() : 64;
        selectedItem = addon.getTransporterItemManager().getItemByName(getDataManager().getSettings().getData().has("selectedItem") ? getDataManager().getSettings().getData().get("selectedItem").getAsString() : "dirt");
    }

    @EventHandler
    public void onTick(ClientTickEvent tickEvent) {
        if(!TransporterAddon.isEnabled() || !this.isEnabled || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        timer++;

        if(!(timer >= delay))
            return;

        if(InventoryAPI.getAPI().getAmount(selectedItem.getMaterial(), selectedItem.getItemDamage()) < minimumAmount) {
            PlayerAPI.getAPI().sendCommand("transporter get " + selectedItem.getName());
            timer = 0;
        }
    }

    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(openKey == null || openKey.equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(openKey) && !PlayerAPI.getAPI().hasOpenScreen())
            PlayerAPI.getAPI().openGuiScreen(new AutoGetMenu(this, null));
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void fillSettings(Settings subSettings) {

        BooleanElement onlyActiveInLobbyElement  = new BooleanElement(
                "Kun aktiv i lobbyer!",
                getDataManager(),
                "onlyActiveInLobby",
                new ControlElement.IconData(Material.DIODE),
                true
        );
        this.onlyActiveInLobby = onlyActiveInLobbyElement.getCurrentValue();
        onlyActiveInLobbyElement.addCallback(b -> this.onlyActiveInLobby = b);
        subSettings.add(onlyActiveInLobbyElement);

        SliderElement sliderElement = new SliderElement("Delay (Ticks)", getDataManager(), "autoGetDelay", new ControlElement.IconData(Material.WATCH), 125);
        sliderElement.setRange(20, 500);
        this.delay = sliderElement.getCurrentValue();
        sliderElement.addCallback(integer -> this.delay = integer);

        subSettings.add(sliderElement);

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "autoGetKeybind", false, openKey);
        this.openKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.openKey = key);

        subSettings.add(keyElement);
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void selectItem(Item item) {
        this.selectedItem = item;
        getDataManager().getSettings().getData().addProperty("selectedItem", selectedItem.getName());
        getDataManager().save();
        ControlElement.IconData iconData = new ControlElement.IconData(selectedItem.getMaterial());
        iconData.setItemDamage(selectedItem.getItemDamage());
        selectItemElement.setIconData(iconData);
    }
}
