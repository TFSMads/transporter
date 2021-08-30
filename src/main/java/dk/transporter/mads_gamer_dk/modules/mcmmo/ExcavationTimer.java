package dk.transporter.mads_gamer_dk.modules.mcmmo;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.mcmmo.Skill;
import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class ExcavationTimer extends SimpleModule
{

    TransporterAddon addon;

    public ExcavationTimer(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Excavation Timer";
    }

    public String getDisplayValue() {

        Skill skill = addon.getSkills().getSkill("Excavation");

        String displayValue = "Fejl";

        if(skill.getPowerUpActivated()){
            double powerUpLength = skill.getPowerUpLength();
            Integer timeStamp = skill.getPowerUpActivatedTimestamp();

            if(UnixTimestampOfNow.getTime() - timeStamp > powerUpLength){
                skill.deactivatePowerup();
            }
            displayValue = "Giga drill aktiv (" + (powerUpLength - (UnixTimestampOfNow.getTime() - timeStamp)) + ")";
        } else{
            Integer timeStamp = skill.getPowerUpActivatedTimestamp();
            if(UnixTimestampOfNow.getTime() - timeStamp > 346){
                displayValue = "Giga drill er klar til brug.";
            }else{
                displayValue = "Giga drill er klar om " + (346 - (UnixTimestampOfNow.getTime() - timeStamp)) + " Sekunder.";
            }
        }



        return displayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.WATCH);
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Din excavation timer.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_MCMMO;
    }

    public String getControlName() {
        return "Excavation Timer";
    }

    public boolean isShown() {
        return true;
    }
}
