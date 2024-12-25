package ml.volder.transporter.modules.mcmmomodule.skills;

import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.widgets.ModuleSystem;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

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
    public void onChatMessageReceive(String clean) {
        if(clean.equals(activatedMessage)) {
            powerUpDuration = (Math.floor((double) level / 50)) + 42;
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
                return Component.text("Ingen data (Skriv /mcstats)");
            if (isActive())
                return Component.text("Aktiv ").color(NamedTextColor.GREEN).append(Component.text( "(" + (powerUpDuration - (Instant.now().getEpochSecond() - lastActivated)) + " Sekunder)").color(NamedTextColor.GRAY));
            if (isReady())
                return Component.text("Klar!").color(NamedTextColor.GREEN);
            return Component.text("Klar om " + ((powerUpDuration + 239) - (Instant.now().getEpochSecond() - lastActivated)) + " Sekunder!").color(NamedTextColor.RED);
        });
    }
}
