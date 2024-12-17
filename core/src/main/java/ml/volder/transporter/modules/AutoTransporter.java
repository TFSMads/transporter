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
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.configuration.settings.type.SettingHeader;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AutoTransporter extends SimpleModule {

    //private boolean onlyActiveInLobby = true;
    private boolean isEnabled;
    private TransporterAddon addon;

    private Key toggleKey = Key.O; // Default key = P

    private boolean useAutoTransporterCommand = true;
    private int delay = 50;

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
        Laby.labyAPI().eventBus().registerListener(this);
        return this;
    }

    @Override
    public void fillSettings(SettingRegistryAccessor subSettings) {
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

        subSettings.add(new SettingHeader(
                "header",
                true,
                "",
                "header"
        ));

        subSettings.add(TransporterSettingElementFactory.Builder.begin()

                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.useAutoTransporterCommand = b),
                        getDataManager(),
                        "useAutoTransporterCommand",
                        true))
                .id("useAutoTransporterCommand")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 5, 1))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SliderWidget.class,
                        new TransporterAction<Float>(v -> delay = v != null ? v.intValue() : delay),
                        getDataManager(),
                        "autoTransporterDelay",
                        40F).range(20, 150))
                .id("autoTransporterDelay")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 1))
                .build()
        );
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        //hasTransporterData = hasConfigEntry("hasTransporterData") ? getConfigEntry("hasTransporterData", Boolean.class) : false;
    }

    @Subscribe
    public void onTick(@NotNull GameTickEvent event){
        if(event.phase() == Phase.POST)
            return;
       if(!TransporterAddon.isEnabled() || !this.isEnabled || !this.isFeatureActive)
            return;
        if(!TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        timer++;

        if(!(timer >= delay))
            return;

        if(!useAutoTransporterCommand)
            executeAutoTransporterPutMineMethod();
    }

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

    private void executeAutoTransporterPutMineMethod() {
        int itemAmount = 0;

        for (Item item: addon.getTransporterItemManager().getItemList()) {
            itemAmount += InventoryAPI.getAPI().getAmount(item.getMaterial(), item.getItemDamage());
            if(itemAmount > 0)
                break;
        }

        if (itemAmount < 1)
            return;

        PlayerAPI.getAPI().sendCommand("transporter put all");
        timer = 0;
    }

    @Subscribe
    public void onKeyPress(KeyEvent event){
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(!TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        if(toggleKey == null)
            return;
        if (InputAPI.getAPI().isKeyDown(toggleKey) && !Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened())
            this.toggle();
    }

    @Subscribe
    public void onMessage(ChatReceiveEvent event){
        if(event.chatMessage().getPlainText().equals("Slår auto-transporter til! (all)") || event.chatMessage().getPlainText().equals("Slår auto-transporter til! (mine)")) {
            isEnabled = true;
        } else if (event.chatMessage().getPlainText().equals("Slår auto-transporter fra!")) {
            isEnabled = false;
        }
    }

    public void onServerSwitch() {
        isEnabled = false;
    }

    @Subscribe
    public void onSubServerSwitch(@NotNull SubServerSwitchEvent event){
        onServerSwitch();
    }

    @Subscribe
    public void onDisconnect(@NotNull ServerDisconnectEvent event){
        onServerSwitch();
    }

    @Subscribe
    public void onJoin(@NotNull ServerJoinEvent event){
        onServerSwitch();
    }

    public void toggle(){
        isEnabled = !isEnabled;
        if(useAutoTransporterCommand)
            PlayerAPI.getAPI().sendCommand("autotransporter");
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }


    public boolean isEnabled() {
        return isEnabled;
    }
}
