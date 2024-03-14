package ml.volder.transporter.modules.mcmmomodule.skills;

import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.event.events.clientmessageevent.ClientMessageEvent;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.widgets.ModuleSystem;

import java.time.Instant;

public class AbilitySkill extends SimpleSkill {

    protected String activatedMessage;
    protected long lastActivated = -1;

    protected double powerUpDuration = 0;

    public AbilitySkill(String skillId, String activatedMessage, DataManager<Data> dataManager) {
        super(skillId, dataManager);
        this.activatedMessage = activatedMessage;
    }

    @Override
    public void updateLevel(int level) {
        super.updateLevel(level);
    }

    private void activatePowerUp() {
        lastActivated = Instant.now().getEpochSecond();
    }

    @Override
    public void onChatMessageReceive(ClientMessageEvent event) {
        if(event.getCleanMessage().equals(activatedMessage)) {
            powerUpDuration = (Math.floor((double) level / 50)) + 2;
            activatePowerUp();
        }
    }

    private boolean isActive() {
        return lastActivated != -1 && Instant.now().getEpochSecond() - lastActivated < powerUpDuration;
    }

    private boolean isReady() {
        return lastActivated == -1 || Instant.now().getEpochSecond() - lastActivated > powerUpDuration + 239;
    }

    @Override
    public void registerModules(Object category, Object powerupCategory) {
        registerModules(category);
        ModuleSystem.registerModule(skillId + "-powerup", skillId + " Ability", false, powerupCategory, icon, s -> {
            if (noLevelData)
                return "Ingen data (Skriv /mcstats)";
            if (isActive())
                return ModColor.GREEN + "Aktiv " + ModColor.GRAY + "(" + (powerUpDuration - (Instant.now().getEpochSecond() - lastActivated)) + " Sekunder)";
            if (isReady())
                return ModColor.GREEN + "Klar!";
            return ModColor.RED + "Klar om " + ((powerUpDuration + 239) - (Instant.now().getEpochSecond() - lastActivated)) + " Sekunder!";
        });
    }
}
