package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.messagemodule.*;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.configuration.settings.type.SettingElement;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

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
        messageHandlers.add(new TransporterMiscellaneousMessageHandler(this));
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
        Laby.labyAPI().eventBus().registerListener(this);
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
                        DropdownWidget.class,
                        new TransporterAction<MessageModes>((v) -> this.messageMode = v),
                        getDataManager(),
                        "selectedChatMode",
                        MessageModes.ACTIONBAR_MESSAGES))
                .id("selectedChatMode")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 4, 4))
                .build()
        );

        subSettings.add(createMessageSetting(
                "getSuccess",
                "&bTager &3%antal% %item% &bfra din transporter. &7(&3%total%&7)",
                Icon.sprite8(ModTextures.WIDGET_EDITOR_ICONS, 4, 0)
        ));

        subSettings.add(createMessageSetting(
                "getFailed",
                "&cDu har ikke noget af denne item i din transporter.",
                Icon.sprite8(ModTextures.WIDGET_EDITOR_ICONS, 4, 0)
        ));

        subSettings.add(createMessageSetting(
                "getFull",
                "&cDit inventory er fyldt!",
                Icon.sprite8(ModTextures.WIDGET_EDITOR_ICONS, 4, 0)
        ));

        subSettings.add(createMessageSetting(
                "putSuccess",
                "&bGemmer &3%antal% %item% &bi din transporter. &7(&3%total%&7)",
                Icon.sprite8(ModTextures.WIDGET_EDITOR_ICONS, 4, 1)
        ));

        subSettings.add(createMessageSetting(
                "putFailed",
                "&bDu har ikke noget &3%item%",
                Icon.sprite8(ModTextures.WIDGET_EDITOR_ICONS, 4, 1)
        ));

        subSettings.add(createMessageSetting(
                "sendSuccess",
                "&bDu sendte &3%antal% %item% &btil %spiller%. &7(&3%total%&7)",
                Icon.sprite16(ModTextures.COMMON_ICONS, 3, 0)
        ));

        subSettings.add(createMessageSetting(
                "modtagSuccess",
                "&bDu modtog &3%antal% %item% &bfra %spiller%. &7(&3%total%&7)",
                Icon.sprite16(ModTextures.COMMON_ICONS, 3, 1)
        ));

        subSettings.add(createMessageSetting(
                "putAllMessage",
                "&bGemte &3%antal% &bitems i din transporter!",
                Icon.sprite8(ModTextures.WIDGET_EDITOR_ICONS, 4, 1)
        ));

        subSettings.add(createMessageSetting(
                "autoTransporterOn",
                "&aAuto-transporter er nu aktiv!",
                Icon.sprite16(ModTextures.COMMON_ICONS, 1, 0)
        ));

        subSettings.add(createMessageSetting(
                "autoTransporterOff",
                "&cAuto-transporter er nu inaktiv!",
                Icon.sprite16(ModTextures.COMMON_ICONS, 4, 6)
        ));

        subSettings.add(createMessageSetting(
                "sendOffline",
                "&cDenne spiller er ikke online!",
                Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 2, 1)
        ));

        subSettings.add(createMessageSetting(
                "sendSelf",
                "&cDu kan ikke sende til dig selv!",
                Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 2, 1)
        ));

        subSettings.add(createMessageSetting(
                "commandDelay",
                "&cVent! Du skriver kommandoer for hurtigt.",
                Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 6)
        ));
    }

    private SettingElement createMessageSetting(String id, String defaultValue, Icon icon) {
        return TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        TextFieldWidget.class,
                        new TransporterAction<String>((v) -> messagesMap.put(id, v)),
                        getDataManager(),
                        id,
                        defaultValue))
                .id(id)
                .icon(icon)
                .build();
    }

    @Subscribe
    public void onChatMessage(ChatReceiveEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        AtomicBoolean isCancelled = new AtomicBoolean(false);
        messageHandlers.forEach(iMessageHandler -> {
            boolean result = iMessageHandler.messageReceived(event.chatMessage().getFormattedText(), event.chatMessage().getPlainText());
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

    public LatestTitle LAST_TITLE = LatestTitle.TRANSPORTER_INFO;

    public enum LatestTitle {
        TRANSPORTER_INFO,
        TRANSPORTER_PUT_MINE
    }
}
