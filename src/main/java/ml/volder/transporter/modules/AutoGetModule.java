package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.*;
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
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

import java.util.function.Consumer;

public class AutoGetModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    private boolean isEnabled;
    private int minimumAmount;
    private Item selectedItem;
    private TransporterAddon addon;
    private int delay = 50;
    private Key openKey = Key.N;

    private int timer = 0;

    private ListContainerElement selectItemElement;

    public AutoGetModule(String moduleName, TransporterAddon addon) {
        super(moduleName);
        EventManager.registerEvents(this);
        this.addon = addon;
        fillSettings();

        isEnabled = false;
        minimumAmount = getDataManager().getSettings().getData().has("minimumAmount") ? getDataManager().getSettings().getData().get("minimumAmount").getAsInt() : 64;
        selectedItem = addon.getTransporterItemManager().getItemByChatName(getDataManager().getSettings().getData().has("selectedItem") ? getDataManager().getSettings().getData().get("selectedItem").getAsString() : "dirt");

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

    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    @EventHandler
    public void onTick(ClientTickEvent tickEvent) {
        if(!TransporterAddon.isEnabled() || !this.isEnabled || !this.isFeatureActive)
            return;
        timer++;

        if(!(timer >= delay))
            return;

        if(InventoryAPI.getAPI().getAmount(selectedItem.getMaterial(), selectedItem.getItemDamage()) < minimumAmount) {
            PlayerAPI.getAPI().sendCommand("transporter get " + selectedItem.getCommandName());
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

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Auto Get", "En feature til at automatisk tage ting fra din transporter.", ModTextures.MISC_HEAD_QUESTION, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isActive) {
                isFeatureActive = isActive;
                setConfigEntry("isFeatureActive", isFeatureActive);
            }
        });
        moduleElement.setActive(isFeatureActive);

        Settings subSettings = moduleElement.getSubSettings();

        SliderElement sliderElement = new SliderElement("Delay (Ticks)", getDataManager(), "autoGetDelay", new ControlElement.IconData(Material.WATCH), 50);
        sliderElement.setRange(20, 100);
        this.delay = sliderElement.getCurrentValue();
        sliderElement.addCallback(integer -> this.delay = integer);

        subSettings.add(sliderElement);

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "autoGetKeybind", false, openKey);
        this.openKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.openKey = key);

        subSettings.add(keyElement);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void selectItem(Item item) {
        this.selectedItem = item;
        getDataManager().getSettings().getData().addProperty("selectedItem", selectedItem.getChatName());
        getDataManager().save();
        ControlElement.IconData iconData = new ControlElement.IconData(selectedItem.getMaterial());
        iconData.setItemDamage(selectedItem.getItemDamage());
        selectItemElement.setIconData(iconData);
    }
}
