package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.event.events.clienttickevent.ClientTickEvent;
import ml.volder.unikapi.event.events.serverswitchevent.ServerSwitchEvent;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;

import java.util.HashMap;
import java.util.Map;

public class AutoTransporter extends SimpleModule implements Listener {

    //private boolean onlyActiveInLobby = true;
    private boolean isEnabled;
    private TransporterAddon addon;
    //private int delay = 50;
    private Key toggleKey = Key.O; // Default key = P

    //private boolean useTransporterPutMine = true;

    //private int timer = 0;

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
    public void fillSettings(SettingRegistryAccessor subSettings) {
        /*subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.onlyActiveInLobby = b),
                        getDataManager(),
                        "onlyActiveInLobby",
                        true))
                .id("onlyActiveInLobby")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 4, 1))
                .build()
        );*/

        /*subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SliderWidget.class,
                        new TransporterAction<Float>(v -> delay = v != null ? v.intValue() : delay),
                        getDataManager(),
                        "autoTransporterDelay",
                        80F).range(20, 150))
                .id("autoTransporterDelay")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 1))
                .build()
        );*/

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        KeybindWidget.class,
                        new TransporterAction<net.labymod.api.client.gui.screen.key.Key>(key -> toggleKey = Laby4KeyMapper.convert(key)),
                        getDataManager(),
                        "autoTransporterKeybind",
                        net.labymod.api.client.gui.screen.key.Key.O))
                .id("autoTransporterKeybind")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 5))
                .build()
        );

        /*subSettings.add(TransporterSettingElementFactory.Builder.begin()

                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.useTransporterPutMine = b),
                        getDataManager(),
                        "useTransporterPutMine",
                        true))
                .id("useTransporterPutMine")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 5, 1))
                .build()
        );*/
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        //hasTransporterData = hasConfigEntry("hasTransporterData") ? getConfigEntry("hasTransporterData", Boolean.class) : false;
    }

    /*@EventHandler
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
    }*/

    @Deprecated(forRemoval = true)
    private void executeAutoTransporterPutItemMethod() {
        Map<String, Integer> itemAmountMap = new HashMap<>();

        for (Item item: addon.getTransporterItemManager().getItemList())
            itemAmountMap.put(item.getModernType(), InventoryAPI.getAPI().getAmount(item.getMaterial(), item.getItemDamage()));


        int maxAmount = -1;
        String itemWithMost = "";
        for (Map.Entry<String, Integer> item : itemAmountMap.entrySet())
            if(item.getValue() > maxAmount){
                itemWithMost = item.getKey();
                maxAmount = item.getValue();
            }

        if(maxAmount > 0 && itemWithMost.length() > 0){
            PlayerAPI.getAPI().sendCommand("transporter put " + itemWithMost);
            //timer = 0;
        }
    }

    @Deprecated(forRemoval = true)
    private void executeAutoTransporterPutMineMethod() {
        int itemAmount = 0;

        for (Item item: addon.getTransporterItemManager().getItemList())
            itemAmount += InventoryAPI.getAPI().getAmount(item.getMaterial(), item.getItemDamage());
        if (itemAmount < 1)
            return;
        PlayerAPI.getAPI().sendCommand("transporter put all");
        //timer = 0;
    }

    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(!TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        if(toggleKey == null)
            return;
        if (InputAPI.getAPI().isKeyDown(toggleKey) && !PlayerAPI.getAPI().hasOpenScreen())
            this.toggle();
    }

    @EventHandler
    public void onMessage(ClientMessageEvent event) {
        if(event.getCleanMessage().equals("Slår auto-transporter til! (all)") || event.getCleanMessage().equals("Slår auto-transporter til! (mine)")) {
            isEnabled = true;
        } else if (event.getCleanMessage().equals("Slår auto-transporter fra!")) {
            isEnabled = false;
        }
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        isEnabled = false;
    }

    public void toggle(){
        isEnabled = !isEnabled;
        PlayerAPI.getAPI().sendCommand("autotransporter");
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }


    public boolean isEnabled() {
        return isEnabled;
    }
}
