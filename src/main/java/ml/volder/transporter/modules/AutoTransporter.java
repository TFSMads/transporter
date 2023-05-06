package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.*;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AutoTransporter extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    private boolean isEnabled;
    private TransporterAddon addon;
    private int delay = 50;
    private Key toggleKey = Key.P; // Default key = P

    private int timer = 0;

    public AutoTransporter(String moduleName, TransporterAddon addon) {
        super(moduleName);
        EventManager.registerEvents(this);
        this.addon = addon;
        fillSettings();
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

        Map<String, Integer> itemAmountMap = new HashMap<>();

        for (Item item: addon.getTransporterItemManager().getItemList())
            if(item.isAutoTransporterEnabled())
                itemAmountMap.put(item.getCommandName(), InventoryAPI.getAPI().getAmount(item.getMaterial(), item.getItemDamage()));


        int maxAmount = -1;
        String itemWithMost = "";
        for (Map.Entry<String, Integer> item : itemAmountMap.entrySet())
            if(item.getValue() > maxAmount){
                itemWithMost = item.getKey();
                maxAmount = item.getValue();
            }

        if(maxAmount > 0 && itemWithMost.length() > 0){
            PlayerAPI.getAPI().sendCommand("transporter put " + itemWithMost);
            timer = 0;
        }
    }

    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(toggleKey == null)
            return;
        if (InputAPI.getAPI().isKeyDown(toggleKey) && !PlayerAPI.getAPI().hasOpenScreen())
            this.toggle();
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void enable(){
        isEnabled = true;
    }
    public void disable(){
        isEnabled = false;
    }
    public void toggle(){
        isEnabled = !isEnabled;
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Auto Transporter", "En feature til at putte item i din transporter for dig.", ModTextures.MISC_HEAD_QUESTION, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isActive) {
                isFeatureActive = isActive;
                setConfigEntry("isFeatureActive", isFeatureActive);
            }
        });
        moduleElement.setActive(isFeatureActive);

        Settings subSettings = moduleElement.getSubSettings();

        SliderElement sliderElement = new SliderElement("Delay (Ticks)", getDataManager(), "autoTransporterDelay", new ControlElement.IconData(Material.WATCH), 40);
        sliderElement.setRange(20, 100);
        this.delay = sliderElement.getCurrentValue();
        sliderElement.addCallback(integer -> this.delay = integer);

        subSettings.add(sliderElement);

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "autoTransporterKeybind", false, toggleKey);
        this.toggleKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.toggleKey = key);

        subSettings.add(keyElement);

        ListContainerElement autoTransporterItems = new ListContainerElement("Items", new ControlElement.IconData(Material.CHEST));

        autoTransporterItems.getSubSettings().add(new HeaderElement("Vælg de items der skal gemmes i din transporter."));

        addon.getTransporterItemManager().getItemList().forEach(item -> {
            ControlElement.IconData iconData = new ControlElement.IconData(item.getMaterial());
            iconData.setItemDamage(item.getItemDamage());
            BooleanElement booleanElement = new BooleanElement(item.getDisplayName(), getDataManager(), "items." + item.getChatName(), iconData, true);
            booleanElement.addCallback(item::setAutoTransporterEnabled);
            item.setAutoTransporterEnabled(booleanElement.getCurrentValue());
            NumberElement numberElement = new NumberElement("Værdi (EMs)", getDataManager(), "items." + item.getChatName() + ".value", new ControlElement.IconData(Material.EMERALD), item.getSellValue());
            item.setSellValue(numberElement.getCurrentValue());
            numberElement.addCallback(integer -> item.setSellValue(integer));
            booleanElement.getSubSettings().add(numberElement);
            autoTransporterItems.getSubSettings().add(booleanElement);
        });

        subSettings.add(autoTransporterItems);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
