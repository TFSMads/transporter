package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.TransporterModulesMenu;
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
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BalanceModule extends SimpleModule implements Listener {
    private TransporterAddon addon;

    public BalanceModule(ModuleManager.ModuleInfo moduleInfo) {
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
        HeaderElement headerElement = new HeaderElement("Vælg de servere hvor din balance skal opdateres!");
        subSettings.add(headerElement);

        BooleanElement booleanElement = new BooleanElement(
                "Opdatere ved join",
                getDataManager(),
                "updateAtJoin",
                new ControlElement.IconData(Material.DIODE),
                true
        );
        updateOnJoin = booleanElement.getCurrentValue();
        booleanElement.addCallback(b -> updateOnJoin = b);
        subSettings.add(booleanElement);

        BooleanElement booleanElement2 = new BooleanElement(
                "Opdatere i interval",
                getDataManager(),
                "updateInterval",
                new ControlElement.IconData(Material.DIODE),
                false
        );
        updateInterval = booleanElement2.getCurrentValue();
        booleanElement2.addCallback(b -> updateInterval = b);
        subSettings.add(booleanElement2);

        NumberElement numberElement = new NumberElement(
                "Update Interval (Sekunder)", getDataManager(),
                "intervalUpdate",
                new ControlElement.IconData(Material.REDSTONE_TORCH),
                60
        );
        numberElement.setMinValue(10);
        updateIntervalSeconds = numberElement.getCurrentValue();
        numberElement.addCallback(i -> updateIntervalSeconds = i);
        subSettings.add(numberElement);
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        balance = hasConfigEntry("currentBalance") ? getDataManager().getSettings().getData().get("currentBalance").getAsBigDecimal() : BigDecimal.valueOf(0);
    }

    public boolean isFeatureActive() {
        return isFeatureActive && ModuleManager.getInstance().getModule(ServerModule.class).isFeatureActive();
    }

    private boolean updateOnJoin = true;
    private boolean updateInterval = false;
    private boolean cancelNextBalanceCommand = false;
    private int updateIntervalSeconds = 60;

    private BigDecimal balance = BigDecimal.valueOf(0);

    @EventHandler
    public void onMessage(ClientMessageEvent event) {
        if (!isFeatureActive() || !TransporterAddon.isEnabled())
            return;
        if(matchBalCommandMessage(event.getCleanMessage()))
            event.setCancelled(true);
        matchPaySendCommandMessage(event.getCleanMessage());
        matchReceiveCommandMessage(event.getCleanMessage());
        matchPlaceAfgift(event.getCleanMessage());
        matchBreakPayback(event.getCleanMessage());
        matchShopBuy(event.getCleanMessage());
        matchShopBuyOther(event.getCleanMessage());
        matchShopSell(event.getCleanMessage());
        matchShopSellOther(event.getCleanMessage());
    }

    private boolean matchBalCommandMessage(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Money] Balance: ([0-9,.]+) Emeralds$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = new BigDecimal(matcher.group(1).replace(".", "").replace(",","."));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
            if (cancelNextBalanceCommand) {
                cancelNextBalanceCommand = false;
                return true;
            }
        }
        return false;
    }

    private void matchPaySendCommandMessage(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Money] [0-9A-Za-z_]+ has sent you ([0-9.,]+) Emerald\\.$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.add(new BigDecimal(matcher.group(1).replace(".", "").replace(",",".")));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchReceiveCommandMessage(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Money] You have sent ([0-9.,]+) Emerald to [0-9A-Za-z_]+\\.$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.subtract(new BigDecimal(matcher.group(1).replace(".", "").replace(",",".")));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchPlaceAfgift(String clean) {
        final Pattern pattern = Pattern.compile("^\\Du har betalt ([0-9.]+) ems for at sætte [A-Za-z_\\s]+$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.subtract(new BigDecimal(matcher.group(1)));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchBreakPayback(String clean) {
        final Pattern pattern = Pattern.compile("^\\Du har fået ([0-9.]+) ems for at fjerne [A-Za-z_\\s]+$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.add(new BigDecimal(matcher.group(1)));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchShopBuy(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Shop] [0-9A-Za-z_]+ bought [0-9]+ [A-Za-z0-9_:\\s#]+ for ([0-9.,]+) Emeralder from you\\.$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.add(new BigDecimal(matcher.group(1).replace(".","").replace(",", ".")));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchShopBuyOther(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Shop] You bought [0-9]+ [A-Za-z0-9_:\\s#]+ from [0-9A-Za-z_]+ for ([0-9.,]+) Emeralder\\.$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.subtract(new BigDecimal(matcher.group(1).replace(".","").replace(",", ".")));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchShopSell(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Shop] [0-9A-Za-z_]+ sold [0-9]+ [A-Za-z0-9_:\\s#]+ for ([0-9.,]+) Emeralder to you\\.$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.subtract(new BigDecimal(matcher.group(1).replace(".","").replace(",", ".")));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    private void matchShopSellOther(String clean) {
        final Pattern pattern = Pattern.compile("^\\[Shop] You sold [0-9]+ [A-Za-z0-9_:\\s#]+ to [0-9A-Za-z_]+ for ([0-9.,]+) Emeralder\\.$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            BigDecimal amount = balance;
            try {
                amount = amount.add(new BigDecimal(matcher.group(1).replace(".","").replace(",", ".")));
            } catch (NumberFormatException ignored) {}
            updateBalance(amount);
        }
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (!isFeatureActive() || !updateOnJoin || !TransporterAddon.isEnabled())
            return;
        if(event.getSwitchType() == ServerSwitchEvent.SWITCH_TYPE.LEAVE)
            return;
        new Timer("updateBalance").schedule(new TimerTask() {
            @Override
            public void run() {
                if(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer() == null)
                    return;
                if(TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer())) {
                    cancelNextBalanceCommand = true;
                    PlayerAPI.getAPI().sendCommand("bal");
                }
            }
        }, 1000L);
        new Timer("updateCancelVar").schedule(new TimerTask() {
            @Override
            public void run() {
                cancelNextBalanceCommand = false;
            }
        }, 1500L);
    }

    private int timer = 0;
    @EventHandler
    public void onTick(ClientTickEvent event) {
        if (!isFeatureActive() || !updateInterval)
            return;
        timer+=1;
        if(timer/20 >= updateIntervalSeconds) {
            if(TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer())){
                cancelNextBalanceCommand = true;
                PlayerAPI.getAPI().sendCommand("bal");
            }
            timer = 0;
        }
    }

    private void updateBalance(BigDecimal newBalance) {
        this.balance = newBalance;
        getDataManager().getSettings().getData().addProperty("currentBalance", balance);
        getDataManager().save();
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
