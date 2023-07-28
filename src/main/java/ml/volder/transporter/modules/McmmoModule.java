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
    private boolean isFeatureActive;
    private McmmoManager mcmmoManager;
    private static McmmoModule instance;

    public McmmoModule(String moduleName) {
        super(moduleName);
        instance = this;
        EventManager.registerEvents(this);
        fillSettings();
        this.mcmmoManager = new McmmoManager(getDataManager());
        mcmmoManager.init();
    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
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

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("McMMO", "En feature der tilføjer moduler der viser nogen af dine McMMO stats!", ModTextures.MISC_HEAD_QUESTION, isActive -> {
            isFeatureActive = isActive;
            setConfigEntry("isFeatureActive", isFeatureActive);
        });
        moduleElement.setActive(isFeatureActive);
        TransporterModulesMenu.addSetting(moduleElement);
    }


    public boolean isFeatureActive() {
        return isFeatureActive;
    }

    public static void registerModules() {
        if(instance == null)
            return;
        instance.mcmmoManager.registerModules();
    }
}
