package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.transporter.modules.mcmmomodule.McmmoManager;
import ml.volder.transporter.modules.messagemodule.*;
import ml.volder.transporter.modules.serverlistmodule.ServerSelecterGui;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.event.events.sendmessageevent.SendMessageEvent;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.types.Material;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void fillSettings(Settings subSettings) {

    }

    @EventHandler
    public void onChatMessage(ClientMessageEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        mcmmoManager.onMessageReceive(event);
    }

    @EventHandler
    public void onSendMessage(SendMessageEvent event) {
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
