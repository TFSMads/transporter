package ml.volder.transporter.modules;

import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.jetbrains.annotations.NotNull;

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
        Laby.labyAPI().eventBus().registerListener(this);
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

    @Subscribe
    public void onMessage(ChatReceiveEvent event) {
        if (!isFeatureActive())
            return;
        final Pattern pattern = Pattern.compile("^Du er lige nu forbundet til ([A-Za-z]+), brug /server <navn> for at joine en anden server.$");
        final Matcher matcher = pattern.matcher(event.chatMessage().getPlainText());
        if (matcher.find()) {
            currentServer = matcher.group(1);
            if (cancelNextServerCommand) {
                cancelNextServerCommand = false;
                event.setCancelled(true);
            }
        }

    }

    public void onServerSwitch() {
        if (!isFeatureActive())
            return;
        currentServer = null;
        new Timer("updateServerStatus").schedule(new TimerTask() {
            @Override
            public void run() {
                cancelNextServerCommand = true;
                PlayerAPI.getAPI().sendCommand("server");
            }
        }, 100L);
    }

    @Subscribe
    public void onDisconnect(@NotNull ServerDisconnectEvent event){
        if (!isFeatureActive())
            return;
        currentServer = null;
    }

    @Subscribe
    public void onSubServerSwitch(@NotNull SubServerSwitchEvent event){
        onServerSwitch();
    }

    @Subscribe
    public void onJoin(@NotNull ServerJoinEvent event){
        onServerSwitch();
    }

    private void registerModules() {
        ServerModule instance = ModuleManager.getInstance().getModule(ServerModule.class);
        ModuleSystem.registerModule("server", "Server", false, GuiModulesModule.getModuleRegistry().getOtherCategory(), Material.PAPER, s -> instance.isFeatureActive ? instance.currentServer == null ? "Opdatere!" : instance.currentServer.substring(0, 1).toUpperCase() + currentServer.substring(1) : "Server Tracker featuren er ikke aktiv!");
    }
}