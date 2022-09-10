package dk.transporter.mads_gamer_dk.modules.mcmmo;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.mcmmo.Skill;
import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class WoodcuttingTimer extends SimpleModule {
    TransporterAddon addon;

    public WoodcuttingTimer(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Woodcutting Timer";
    }

    public String getDisplayValue() {

        Skill skill = addon.getSkills().getSkill("Woodcutting");

        String displayValue = "Fejl";

        if(skill.getPowerUpActivated()){
            double powerUpLength = skill.getPowerUpLength();
            Integer timeStamp = skill.getPowerUpActivatedTimestamp();

            if(UnixTimestampOfNow.getTime() - timeStamp > powerUpLength){
                skill.deactivatePowerup();
            }
            displayValue = "Tree Feller aktiv (" + (powerUpLength - (UnixTimestampOfNow.getTime() - timeStamp)) + ")";
        } else{
            Integer timeStamp = skill.getPowerUpActivatedTimestamp();
            if(UnixTimestampOfNow.getTime() - timeStamp > skill.getPowerUpLength() + 239 ){
                displayValue = "Tree Feller er klar til brug.";
            }else{
                displayValue = "Tree Feller er klar om " + (skill.getPowerUpLength() + 239 - (UnixTimestampOfNow.getTime() - timeStamp)) + " Sekunder.";
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
        return "Din tree feller timer.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_MCMMO;
    }

    public String getControlName() {
        return "Woodcutting Timer";
    }

    public boolean isShown() {
        return true;
    }
}
