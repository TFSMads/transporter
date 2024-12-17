package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.events.TransporterChannelRegisteredEvent;
import ml.volder.transporter.messaging.PluginMessageHandler;
import ml.volder.transporter.messaging.channels.BalanceChannel;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BalanceModule extends SimpleModule{
    private DataManager<Data> dataManagerSettings;
    private TransporterAddon addon;

    public BalanceModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
        this.addon = TransporterAddon.getInstance();
        this.dataManagerSettings = DataManager.getOrCreateDataManager("%common%/settings.json");
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
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.updateOnJoin = b),
                        getDataManager(),
                        "updateAtJoin",
                        true))
                .id("updateAtJoin")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 0, 4))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.updateInterval = b),
                        getDataManager(),
                        "updateInterval",
                        true))
                .id("updateInterval")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 0, 7))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SliderWidget.class,
                        new TransporterAction<Float>(v -> updateIntervalSeconds = v != null ? v.intValue() : updateIntervalSeconds),
                        getDataManager(),
                        "intervalUpdate",
                        80F).steps(10F).range(10F, 1000F))
                .id("intervalUpdate")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 6))
                .build()
        );
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        balance = hasConfigEntry("currentBalance") ? getDataManager().getSettings().getData().get("currentBalance").getAsBigDecimal() : BigDecimal.valueOf(0);
        loadMessageRegexFromCsv();
    }

    private final Map<String, ACTION>  messagesMap = new HashMap<>();

    public void reloadMessagesFromCSV() {
        UnikAPI.LOGGER.info("Reloading balance messages from CSV");
        loadMessageRegexFromCsv();
    }

    private void loadMessageRegexFromCsv() {
        // Get file from common resources
        File file = new File(UnikAPI.getCommonDataFolder(), "transporter-balance-messages.csv");
        // Check if file exists
        if(!file.exists()) {
            UnikAPI.LOGGER.warning("Failed to load balance messages from CSV: File does not exist");
            return;
        }
        // Get input stream from file
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            UnikAPI.LOGGER.warning("Failed to load balance messages from CSV: Could not get input stream from file");
            return;
        }
        messagesMap.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while (line != null) {
                if(line.startsWith("message_regex"))
                    line = br.readLine();

                try {
                    String[] attributes = line.split(";");
                    messagesMap.put(attributes[0], ACTION.valueOf(attributes[1]));
                    line = br.readLine();
                }catch (Exception e) {
                    UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
                    UnikAPI.LOGGER.info("Failed to load message: " + line);
                    line = br.readLine();
                }
            }
        } catch (IOException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            throw new RuntimeException(e);
        }
    }

    public boolean isFeatureActive() {
        return isFeatureActive && ModuleManager.getInstance().getModule(ServerModule.class).isFeatureActive();
    }

    private boolean updateOnJoin = true;
    private boolean updateInterval = false;
    private boolean cancelNextBalanceCommand = false;
    private int updateIntervalSeconds = 60;

    private BigDecimal balance = BigDecimal.valueOf(0);

    @Subscribe
    public void onMessage(ChatReceiveEvent event) {
        if (!isFeatureActive() || !TransporterAddon.isEnabled())
            return;
        if(matchBalCommandMessage(event.chatMessage().getPlainText()))
            event.setCancelled(true);
        matchMessage(event.chatMessage().getPlainText());
    }

    private void matchMessage(String clean) {
        for (Map.Entry<String, ACTION> entry : messagesMap.entrySet()) {
            String regex = entry.getKey();
            ACTION action = entry.getValue();
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(clean);
            if (matcher.find()) {
                BigDecimal amount = null;
                if (hasGroup(matcher, "amountf1")) {
                    amount = new BigDecimal(matcher.group("amountf1").replace(",", ""));
                } else if (hasGroup(matcher, "amountf2")) {
                    amount = new BigDecimal(matcher.group("amountf2"));
                } else if (hasGroup(matcher, "amountf3")) {
                    amount = new BigDecimal(matcher.group("amountf3").replace(".", "").replace(",","."));
                }
                switch (action) {
                    case ADD:
                        updateBalance(balance.add(amount));
                        break;
                    case REMOVE:
                        updateBalance(balance.subtract(amount));
                        break;
                    case SET:
                        updateBalance(amount);
                        break;
                }
            }
        }
    }

    private boolean hasGroup(Matcher matcher, String string) {
        try {
            matcher.group(string);
            return true;
        }catch (Exception ignored) {
            return false;
        }
    }


    private boolean matchBalCommandMessage(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Money] Balance: ([0-9,.]+) Emeralds$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            if (cancelNextBalanceCommand) {
                cancelNextBalanceCommand = false;
                return true;
            }
        }
        return false;
    }

    @Subscribe
    public void onTransporterChannelRegister(TransporterChannelRegisteredEvent event) {
        if (!isFeatureActive() || !updateOnJoin || !TransporterAddon.isEnabled())
            return;
        PluginMessageHandler.getChannel(BalanceChannel.class).sendPayload();
    }

    private int timer = 0;
    @Subscribe
    public void onTick(@NotNull GameTickEvent event){
        if(event.phase() == Phase.POST)
            return;
        if (!isFeatureActive() || !updateInterval)
            return;
        timer+=1;
        if(timer/20 >= updateIntervalSeconds) {
            if(dataManagerSettings.getBoolean("useTransporterPackets")) {
                PluginMessageHandler.getChannel(BalanceChannel.class).sendPayload();
            } else {
                if(TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer())){
                    cancelNextBalanceCommand = true;
                    PlayerAPI.getAPI().sendCommand("bal");
                }
            }
            timer = 0;

        }
    }

    public void updateBalance(BigDecimal newBalance) {
        this.balance = newBalance;
        getDataManager().getSettings().getData().addProperty("currentBalance", balance);
        getDataManager().save();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    private enum ACTION {
        ADD,
        REMOVE,
        SET
    }
}
