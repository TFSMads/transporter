package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.messagemodule.*;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.types.Material;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessagesModule extends SimpleModule implements Listener {
    private Map<String, String> messagesMap = new HashMap<>();
    private Map<String, String> messageRegexMap = new HashMap<>();
    private MessageModes messageMode = MessageModes.ACTIONBAR_MESSAGES;
    private List<IMessageHandler> messageHandlers = new ArrayList<>();

    private boolean onlyActiveInLobby = true;

    public MessagesModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        loadMessageRegexFromCsv();

        messageHandlers.add(new TransporterFailedHandler(this));
        messageHandlers.add(new TransporterGetMessageHandler(this));
        messageHandlers.add(new TransporterPutMessageHandler(this));
        messageHandlers.add(new TransporterInfoHandler(this));
        messageHandlers.add(new TransporterSendMessageHandler(this));
        return this;
    }

    public String getRegexByMessageId(String messageId) {
        return messageRegexMap.getOrDefault(messageId, "");
    }

    public void reloadMessagesFromCSV() {
        UnikAPI.LOGGER.info("Reloading messages from CSV");
        loadMessageRegexFromCsv();
    }

    private void loadMessageRegexFromCsv() {

        // Get file from common resources
        File file = new File(UnikAPI.getCommonDataFolder(), "transporter-messages.csv");
        // Check if file exists
        if(!file.exists()) {
            UnikAPI.LOGGER.warning("Failed to load messages from CSV: File does not exist");
            return;
        }
        // Get input stream from file
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            UnikAPI.LOGGER.warning("Failed to load messages from CSV: Could not get input stream from file");
            return;
        }

        this.messageRegexMap.clear();
        Map<String, String> messageRegexMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while (line != null) {
                if(line.startsWith("message_id"))
                    line = br.readLine();

                try {
                    String[] attributes = line.split(",");
                    messageRegexMap.put(attributes[0], attributes[1]);
                    line = br.readLine();
                }catch (Exception e) {
                    UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
                    UnikAPI.LOGGER.debug("Failed to load message: " + line);
                    line = br.readLine();
                }
            }
        } catch (IOException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            throw new RuntimeException(e);
        }

        messageRegexMap.forEach((key, value) -> {
            //find text between % and % and replace with regex if present in messageRegexMap
            while(value.contains("%")) {
                String regexKey = value.substring(value.indexOf("%") + 1, value.indexOf("%", value.indexOf("%") + 1));
                value = value.replace("%" + regexKey + "%", messageRegexMap.getOrDefault(regexKey, ""));
            }
            this.messageRegexMap.put(key, value);
        });
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

        DropDownMenu<MessageModes> dropDownMenu = new DropDownMenu<>("", 0, 0, 0, 0);
        dropDownMenu.fill(MessageModes.values());
        DropDownElement<MessageModes> dropDownElement = new DropDownElement<>("Hvor skal beskeder sendes?", "selectedChatMode", dropDownMenu, new ControlElement.IconData(Material.PAPER), value -> {
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

        StringElement stringElementSendSuccess = new StringElement("Besked - Send Success",
                "sendSuccess",
                new ControlElement.IconData(Material.PAPER),
                "&bDu sendte &3%antal% %item% &btil %spiller%. &7(&3%total%&7)",
                getDataManager());
        stringElementSendSuccess.addCallback(value -> messagesMap.put(stringElementSendSuccess.getConfigEntryName(), value));
        messagesMap.put(stringElementSendSuccess.getConfigEntryName(), stringElementSendSuccess.getCurrentValue());
        subSettings.add(stringElementSendSuccess);

        StringElement stringElementModtag = new StringElement("Besked - Modtag",
                "modtagSuccess",
                new ControlElement.IconData(Material.PAPER),
                "&bDu modtog &3%antal% %item% &bfra %spiller%. &7(&3%total%&7)",
                getDataManager());
        stringElementModtag.addCallback(value -> messagesMap.put(stringElementModtag.getConfigEntryName(), value));
        messagesMap.put(stringElementModtag.getConfigEntryName(), stringElementModtag.getCurrentValue());
        subSettings.add(stringElementModtag);

        StringElement stringElementOffline = new StringElement("Besked - Send Offline",
                "sendOffline",
                new ControlElement.IconData(Material.PAPER),
                "&cDenne spiller er ikke online!",
                getDataManager());
        stringElementOffline.addCallback(value -> messagesMap.put(stringElementOffline.getConfigEntryName(), value));
        messagesMap.put(stringElementOffline.getConfigEntryName(), stringElementOffline.getCurrentValue());
        subSettings.add(stringElementOffline);

        StringElement stringElementSendSelf = new StringElement("Besked - Send Dig Selv",
                "sendSelf",
                new ControlElement.IconData(Material.PAPER),
                "&cDu kanne ikke sende til dig selv!",
                getDataManager());
        stringElementSendSelf.addCallback(value -> messagesMap.put(stringElementSendSelf.getConfigEntryName(), value));
        messagesMap.put(stringElementSendSelf.getConfigEntryName(), stringElementSendSelf.getCurrentValue());
        subSettings.add(stringElementSendSelf);

        StringElement stringElementToFast = new StringElement("Besked - Wait",
                "commandDelay",
                new ControlElement.IconData(Material.PAPER),
                "&cVent! Du skriver kommandoer for hurtigt.",
                getDataManager());
        stringElementToFast.addCallback(value -> messagesMap.put(stringElementToFast.getConfigEntryName(), value));
        messagesMap.put(stringElementToFast.getConfigEntryName(), stringElementToFast.getCurrentValue());
        subSettings.add(stringElementToFast);
    }

    @EventHandler
    public void onChatMessage(ClientMessageEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        AtomicBoolean isCancelled = new AtomicBoolean(false);
        messageHandlers.forEach(iMessageHandler -> {
            boolean result = iMessageHandler.messageReceived(event.getMessage(), event.getCleanMessage());
            isCancelled.set(result || isCancelled.get());
        });
        if(isCancelled.get())
            event.setCancelled(isCancelled.get());
    }

    public String getRawMessage(String key) {
        return messagesMap.getOrDefault(key, "");
    }

    public String getMessage(String message, String item, String antal, String total, String spiller){

        try {
            antal = FormatingUtils.formatNumber(Integer.parseInt(antal));
        }catch (Exception ignored){}
        try {
            total = FormatingUtils.formatNumber(Integer.parseInt(total));
        }catch (Exception ignored){}

        message = message.replace('&','\u00a7');

        message = message.replace("%item%", item != null ? item : "%item%");

        message = message.replace("%antal%", antal != null ? antal : "%antal%");

        message = message.replace("%total%", total != null ? total : "%total%");

        message = message.replace("%spiller%", spiller != null ? spiller : "%spiller%");

        return message;

    }

    public String getMessage(String message, String item, String antal, String total){
        return getMessage(message, item, antal, total, null);
    }

    public MessageModes getMessageMode() {
        return messageMode;
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }
}
