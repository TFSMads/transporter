package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.gui.elements.*;
import ml.volder.transporter.modules.messagemodule.*;
import ml.volder.transporter.modules.serverlistmodule.ServerSelecterGui;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.types.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MessagesModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    private String itemRegex = "[0-9A-Za-z:_]+";
    private Map<String, String> messagesMap = new HashMap<>();
    private MessageModes messageMode = MessageModes.ACTIONBAR_MESSAGES;
    private List<IMessageHandler> messageHandlers = new ArrayList<>();

    public MessagesModule(String moduleName) {
        super(moduleName);
        EventManager.registerEvents(this);
        fillSettings();
        messageHandlers.add(new TransporterFailedHandler(this));
        messageHandlers.add(new TransporterGetMessageHandler(this));
        messageHandlers.add(new TransporterPutMessageHandler(this));
        messageHandlers.add(new TransporterInfoHandler(this));
    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    @EventHandler
    public void onChatMessage(ClientMessageEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        AtomicBoolean isCancelled = new AtomicBoolean(false);
        messageHandlers.forEach(iMessageHandler -> {
            isCancelled.set(iMessageHandler.messageReceived(event.getMessage(), event.getCleanMessage()) ? true : isCancelled.get());
        });
        event.setCancelled(isCancelled.get());
    }

    private void open() {
        if(PlayerAPI.getAPI().hasOpenScreen())
            return;
        PlayerAPI.getAPI().openGuiScreen(new ServerSelecterGui(getDataManager()));
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Beskeder", "En feature der benytter beskeder i chatten til at samle data omkring din transporter.", ModTextures.MISC_HEAD_QUESTION, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isActive) {
                isFeatureActive = isActive;
                setConfigEntry("isFeatureActive", isFeatureActive);
            }
        });
        moduleElement.setActive(isFeatureActive);
        Settings subSettings = moduleElement.getSubSettings();

        DropDownMenu<MessageModes> dropDownMenu = new DropDownMenu<>("", 0, 0, 0, 0);
        dropDownMenu.fill(MessageModes.values());
        DropDownElement<MessageModes> dropDownElement = new DropDownElement<>("Hvor skal beskeder sendes?", "selectedChatMode", dropDownMenu, new ControlElement.IconData(Material.PAPER), (DropDownElement.DropDownLoadValue<MessageModes>) value -> {
            if(value.equals("NO_MESSAGES")) {
                return MessageModes.NO_MESSAGES;
            }else if(value.equals("CHAT_MESSAGES")) {
                return MessageModes.CHAT_MESSAGES;
            }else if(value.equals("ACTIONBAR_MESSAGES")) {
                return MessageModes.ACTIONBAR_MESSAGES;
            }
            return MessageModes.ACTIONBAR_MESSAGES;
        }, getDataManager());
        this.messageMode = (MessageModes) dropDownElement.getDropDownMenu().getSelected();
        dropDownElement.setCallback(mode -> MessagesModule.this.messageMode = mode);

        subSettings.add(dropDownElement);


        StringElement stringElementGetSuccess = new StringElement("Besked - Get Success",
                "getSuccess",
                new ControlElement.IconData(Material.PAPER),
                "&bTager &3%antal% %item% &bfra din transporter. &7(&3%total%&7)",
                getDataManager());
        stringElementGetSuccess.addCallback(value -> messagesMap.put(stringElementGetSuccess.getConfigEntryName(), value));
        messagesMap.put(stringElementGetSuccess.getConfigEntryName(), stringElementGetSuccess.getCurrentValue());
        subSettings.add(stringElementGetSuccess);

        StringElement stringElementGetFailed = new StringElement("Besked - Get Failed",
                "getFailed",
                new ControlElement.IconData(Material.PAPER),
                "&cDu har ikke noget af denne item i din transporter.",
                getDataManager());
        stringElementGetFailed.addCallback(value -> messagesMap.put(stringElementGetFailed.getConfigEntryName(), value));
        messagesMap.put(stringElementGetFailed.getConfigEntryName(), stringElementGetFailed.getCurrentValue());
        subSettings.add(stringElementGetFailed);

        StringElement stringElementGetInventoryFull = new StringElement("Besked - Get Inventory Fyldt",
                "getFull",
                new ControlElement.IconData(Material.PAPER),
                "&cDit inventory er fyldt!",
                getDataManager());
        stringElementGetInventoryFull.addCallback(value -> messagesMap.put(stringElementGetInventoryFull.getConfigEntryName(), value));
        messagesMap.put(stringElementGetInventoryFull.getConfigEntryName(), stringElementGetInventoryFull.getCurrentValue());
        subSettings.add(stringElementGetInventoryFull);

        StringElement stringElementPutSuccess = new StringElement("Besked - Put Success",
                "putSuccess",
                new ControlElement.IconData(Material.PAPER),
                "&bGemmer &3%antal% %item% &bi din transporter. &7(&3%total%&7)",
                getDataManager());
        stringElementPutSuccess.addCallback(value -> messagesMap.put(stringElementPutSuccess.getConfigEntryName(), value));
        messagesMap.put(stringElementPutSuccess.getConfigEntryName(), stringElementPutSuccess.getCurrentValue());
        subSettings.add(stringElementPutSuccess);

        StringElement stringElementPutFailed = new StringElement("Besked - Put Failed",
                "putFailed",
                new ControlElement.IconData(Material.PAPER),
                "&bDu har ikke noget &3%item%",
                getDataManager());
        stringElementPutFailed.addCallback(value -> messagesMap.put(stringElementPutFailed.getConfigEntryName(), value));
        messagesMap.put(stringElementPutFailed.getConfigEntryName(), stringElementPutFailed.getCurrentValue());
        subSettings.add(stringElementPutFailed);

        StringElement stringElementCooldown = new StringElement("Besked - Cooldown",
                "cooldown",
                new ControlElement.IconData(Material.PAPER),
                "&cDer er 2 sekunders cooldown på transporteren.",
                getDataManager());
        stringElementCooldown.addCallback(value -> messagesMap.put(stringElementCooldown.getConfigEntryName(), value));
        messagesMap.put(stringElementCooldown.getConfigEntryName(), stringElementCooldown.getCurrentValue());
        subSettings.add(stringElementCooldown);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public String getItemRegex() {
        return itemRegex;
    }

    public String getRawMessage(String key) {
        return messagesMap.getOrDefault(key, "");
    }

    public String getMessage(String message, String item, String antal, String total){

        message = message.replace('&','§');

        message = message.replace("%item%", item != null ? item : "%item%");

        message = message.replace("%antal%", antal != null ? antal : "%antal%");

        message = message.replace("%total%", total != null ? total : "%total%");

        return message;

    }

    public MessageModes getMessageMode() {
        return messageMode;
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }
}