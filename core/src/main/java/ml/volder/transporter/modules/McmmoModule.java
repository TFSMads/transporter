package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.mcmmomodule.McmmoManager;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.Laby;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ActionBarReceiveEvent;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class McmmoModule extends SimpleModule implements Listener {
    private McmmoManager mcmmoManager;

    public McmmoModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        this.mcmmoManager = new McmmoManager(getDataManager());
        mcmmoManager.init();
        return this;
    }

    @Override
    public SimpleModule enable() {
        mcmmoManager.registerModules();
        Laby.labyAPI().eventBus().registerListener(this);
        return this;
    }

    @Override
    public void fillSettings(SettingRegistryAccessor subSettings) {

    }

    @Subscribe
    public void onChatMessage(ChatReceiveEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        mcmmoManager.onMessageReceive(event.chatMessage().getPlainText());
    }

    @Subscribe
    public void onActionBarMessage(ActionBarReceiveEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive || event.getMessage() == null || event.phase() != Phase.PRE)
            return;
        String message = event.getMessage().toString().replace("literal{", "");
        message = message.substring(0, message.length() - 1);
        UnikAPI.LOGGER.debug("Action bar: " + message, Logger.DEBUG_LEVEL.LOWEST);
        mcmmoManager.onMessageReceive(message);
    }

    @Subscribe
    public void onMessage(@NotNull ChatMessageSendEvent event){
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(!event.getMessage().equals("/mcstats"))
            return;
        new Timer("mcstatsCheckScoreboard").schedule(new TimerTask() {
            @Override
            public void run() {
                if(!MinecraftAPI.getAPI().getScoreBoardTitle().equals("mcMMO Stats"))
                    return;
                Map<String, Integer> scoreboardLines = MinecraftAPI.getAPI().getScoreBoardLines();
                for (Map.Entry<String, Integer> entry: scoreboardLines.entrySet()) {
                    mcmmoManager.getSkillById(entry.getKey().replace("§a", "")).updateLevel(entry.getValue());
                }
            }
        }, 1000L);
    }

    public boolean isFeatureActive() {
        return isFeatureActive;
    }
}
