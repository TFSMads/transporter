package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.event.events.serverswitchevent.ServerSwitchEvent;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.ModuleElement;
import ml.volder.unikapi.guisystem.elements.Settings;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;
    private TransporterAddon addon;

    public ServerModule(String moduleName, TransporterAddon addon) {
        super(moduleName);
        EventManager.registerEvents(this);
        this.addon = addon;
        instance = this;
        fillSettings();
    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }


    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Server Tracker", "En feature der tracker hvilken server du er forbundet til.", ModTextures.MISC_HEAD_QUESTION, isActive -> {
            isFeatureActive = isActive;
            setConfigEntry("isFeatureActive", isFeatureActive);
        });
        moduleElement.setActive(isFeatureActive);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    private static ServerModule instance;

    public static String getCurrentServer() {
        return instance.currentServer;
    }

    public static boolean isActive() {
        return instance.isFeatureActive();
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

    public static void registerModules(Object otherCategory) {
        ModuleSystem.registerModule("server", "Server", false, otherCategory, Material.PAPER, s -> instance.isFeatureActive ? instance.currentServer == null ? "Opdatere!" : instance.currentServer : "Server Tracker featuren er ikke aktiv!");
    }
}