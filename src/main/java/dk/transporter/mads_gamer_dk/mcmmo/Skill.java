package dk.transporter.mads_gamer_dk.mcmmo;

import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;
import net.labymod.settings.elements.ControlElement;

public class Skill {

    private String skillName;
    private Integer level;
    private ControlElement.IconData iconData;

    private String powerUpName;
    private Boolean powerUpActivated;
    private Integer powerUpActivatedTimestamp;

    public Skill(String skillName, Integer level, String powerUpName, ControlElement.IconData iconData){
        this.skillName = skillName;
        this.level = level;
        this.powerUpName = powerUpName;
        this.powerUpActivated = false;
        this.powerUpActivatedTimestamp = 0;
        this.iconData = iconData;
    }

    public void activatePowerup(){
        System.out.println("POWER UP ACTIVATED " + powerUpName);
        this.powerUpActivated = true;
        this.powerUpActivatedTimestamp = UnixTimestampOfNow.getTime();
    }

    public void deactivatePowerup(){
        this.powerUpActivated = false;
    }
    public Integer getPowerUpActivatedTimestamp() {
        return powerUpActivatedTimestamp;
    }

    public Boolean getPowerUpActivated() {
        if(powerUpActivated){
            return true;
        }else{
            return false;
        }
    }

    public ControlElement.IconData getIconData(){
        return this.iconData;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public double getPowerUpLength() {
        double powerUpLength = Math.floor((Double.parseDouble(level.toString()))/50);
        return powerUpLength + 2;
    }

    public String getPowerUpName() {
        return powerUpName;
    }

    public void setPowerUpName(String powerUpName) {
        this.powerUpName = powerUpName;
    }


}
