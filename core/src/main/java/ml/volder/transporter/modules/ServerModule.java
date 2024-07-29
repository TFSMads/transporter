package ml.volder.transporter.modules;

import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.event.events.serverswitchevent.ServerSwitchEvent;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerModule extends SimpleModule implements Listener {
    public ServerModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        return this;
    }

    @Override
    public SimpleModule enable() {
        registerModules();
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void fillSettings(SettingRegistryAccessor subSettings) {

    }

    public String getCurrentServer() {
        return currentServer;
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    private String currentServer;
    private boolean cancelNextServerCommand = false;

    @EventHandler
    public void onMessage(ClientMessageEvent event) {
        if (!isFeatureActive())
            return;
        final Pattern pattern = Pattern.compile("^Du er lige nu forbundet til ([A-Za-z]+), brug /server <navn> for at joine en anden server.$");
        final Matcher matcher = pattern.matcher(event.getCleanMessage());
        if (matcher.find()) {
            currentServer = matcher.group(1);
            if (cancelNextServerCommand) {
                cancelNextServerCommand = false;
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (!isFeatureActive())
            return;
        if(event.getSwitchType() == ServerSwitchEvent.SWITCH_TYPE.LEAVE){
            currentServer = null;
            return;
        }
        currentServer = null;
        new Timer("updateServerStatus").schedule(new TimerTask() {
            @Override
            public void run() {
                cancelNextServerCommand = true;
                PlayerAPI.getAPI().sendCommand("server");
            }
        }, 100L);
    }

    private void registerModules() {
        ServerModule instance = ModuleManager.getInstance().getModule(ServerModule.class);
        ModuleSystem.registerModule("server", "Server", false, GuiModulesModule.getModuleRegistry().getOtherCategory(), Material.PAPER, s -> instance.isFeatureActive ? instance.currentServer == null ? "Opdatere!" : instance.currentServer : "Server Tracker featuren er ikke aktiv!");
    }
}