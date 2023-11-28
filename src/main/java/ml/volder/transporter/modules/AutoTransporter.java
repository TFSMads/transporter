package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.event.events.clienttickevent.ClientTickEvent;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoTransporter extends SimpleModule implements Listener {

    private boolean onlyActiveInLobby = true;
    private boolean hasTransporterData;
    private boolean isEnabled;
    private TransporterAddon addon;
    private int delay = 50;
    private Key toggleKey = Key.P; // Default key = P

    private boolean useTransporterPutMine = true;

    private int timer = 0;

    public AutoTransporter(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
        this.addon = TransporterAddon.getInstance();
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

        SliderElement sliderElement = new SliderElement("Delay (Ticks)", getDataManager(), "autoTransporterDelay", new ControlElement.IconData(Material.WATCH), 125);
        sliderElement.setRange(20, 150);
        this.delay = sliderElement.getCurrentValue();
        sliderElement.addCallback(integer -> this.delay = integer);

        subSettings.add(sliderElement);

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "autoTransporterKeybind", false, toggleKey);
        this.toggleKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.toggleKey = key);

        subSettings.add(keyElement);

        BooleanElement useTransporterPutMineElement = new BooleanElement("Brug '/transporter put mine'", getDataManager(), "useTransporterPutMine", new ControlElement.IconData(Material.DIODE), true);
        this.useTransporterPutMine = useTransporterPutMineElement.getCurrentValue();
        useTransporterPutMineElement.addCallback(b -> this.useTransporterPutMine = b);
        subSettings.add(useTransporterPutMineElement);

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
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        hasTransporterData = hasConfigEntry("hasTransporterData") ? getConfigEntry("hasTransporterData", Boolean.class) : false;
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

        if(useTransporterPutMine)
            executeAutoTransporterPutMineMethod();
        else
            executeAutoTransporterPutItemMethod();
    }

    boolean cancelTransporterInfoMessages = false;
    @EventHandler
    public void onMessage(ClientMessageEvent event) {
        if(!cancelTransporterInfoMessages)
            return;
        if(event.getCleanMessage().equals("Du har følgende i din transporter:")){
            event.setCancelled(true);
            return;
        }
        final Pattern pattern = Pattern.compile("^ - ([A-Za-z0-9:_]+) ([0-9]+)$");
        final Matcher matcher = pattern.matcher(event.getCleanMessage());
        if (matcher.find()) {
            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
                if(matcher.group(1).equals(item.getTransporterInfoName())){
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private void executeAutoTransporterPutItemMethod() {
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

    boolean executeTransporterInfo = false;
    private void executeAutoTransporterPutMineMethod() {
        int itemAmount = 0;

        for (Item item: addon.getTransporterItemManager().getItemList())
            if(item.isAutoTransporterEnabled())
                itemAmount += InventoryAPI.getAPI().getAmount(item.getMaterial(), item.getItemDamage());
        if (itemAmount < 1)
            return;
        if (executeTransporterInfo && itemAmount < 256) {
            cancelTransporterInfoMessages = true;
            PlayerAPI.getAPI().sendCommand("transporter info");
            executeTransporterInfo = false;
            new Timer("changeCancelInfoMessage").schedule(new TimerTask() {
                @Override
                public void run() {
                    cancelTransporterInfoMessages = false;
                }
            }, 1000L);
        } else {
            PlayerAPI.getAPI().sendCommand("transporter put mine");
            executeTransporterInfo = true;
        }
        timer = 0;

    }

    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        if(toggleKey == null)
            return;
        if (InputAPI.getAPI().isKeyDown(toggleKey) && !PlayerAPI.getAPI().hasOpenScreen())
            this.toggle();
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void toggle(){
        isEnabled = !isEnabled;
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public boolean hasTransporterData() {
        return hasTransporterData;
    }

    public void transporterInfoSet() {
        if(hasTransporterData)
            return;
        hasTransporterData = true;
        getDataManager().getSettings().getData().addProperty("hasTransporterData", hasTransporterData);
        getDataManager().save();
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
