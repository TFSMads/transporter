package dk.transporter.mads_gamer_dk.mcmmo;

import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.Module;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.ingamegui.moduletypes.SimpleTextModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

import java.util.Collection;

public class Skills {
    private Skill[] allSkills = new Skill[14];
    private TransporterAddon addon;
    private JsonObject data;

    public Skills(TransporterAddon addon){
        this.addon = addon;
        this.data = addon.getDataManagers().getMCMMOData();
        allSkills[0] = new Skill("PowerLevel", data.has( "PowerLevel" ) ? data.get( "PowerLevel" ).getAsInt() : 0, null, new ControlElement.IconData(Material.PAPER));
        allSkills[1] = new Skill("Excavation", data.has( "Excavation" ) ? data.get( "Excavation" ).getAsInt() : 0, "GIGA DRILL BREAKER", new ControlElement.IconData(Material.DIAMOND_SPADE));
        allSkills[2] = new Skill("Woodcutting", data.has( "Woodcutting" ) ? data.get( "Woodcutting" ).getAsInt() : 0, "TREE FELLER", new ControlElement.IconData(Material.DIAMOND_AXE));
        allSkills[3] = new Skill("Repair", data.has( "Repair" ) ? data.get( "Repair" ).getAsInt() : 0, null, new ControlElement.IconData(Material.ANVIL));
        allSkills[4] = new Skill("Acrobatics", data.has( "Acrobatics" ) ? data.get( "Acrobatics" ).getAsInt() : 0, null, new ControlElement.IconData(Material.CHAINMAIL_BOOTS));
        allSkills[5] = new Skill("Alchemy", data.has( "Alchemy" ) ? data.get( "Alchemy" ).getAsInt() : 0,null, new ControlElement.IconData(Material.BLAZE_ROD));
        allSkills[6] = new Skill("Archery", data.has( "Archery" ) ? data.get( "Archery" ).getAsInt() : 0, null, new ControlElement.IconData(Material.BOW));
        allSkills[7] = new Skill("Axes", data.has( "Axes" ) ? data.get( "Axes" ).getAsInt() : 0, null, new ControlElement.IconData(Material.IRON_AXE));
        allSkills[8] = new Skill("Fishing", data.has( "Fishing" ) ? data.get( "Fishing" ).getAsInt() : 0, null, new ControlElement.IconData(Material.FISHING_ROD));
        allSkills[9] = new Skill("Herbalism", data.has( "Herbalism" ) ? data.get( "Herbalism" ).getAsInt() : 0, null, new ControlElement.IconData(Material.SPIDER_EYE));
        allSkills[10] = new Skill("Mining", data.has( "Mining" ) ? data.get( "Mining" ).getAsInt() : 0, "SUPER BREAKER", new ControlElement.IconData(Material.DIAMOND_PICKAXE));
        allSkills[11] = new Skill("Swords", data.has( "Swords" ) ? data.get( "Swords" ).getAsInt() : 0, "SERRATED STRIKES", new ControlElement.IconData(Material.DIAMOND_SWORD));
        allSkills[12] = new Skill("Taming", data.has( "Taming" ) ? data.get( "Taming" ).getAsInt() : 0, null, new ControlElement.IconData(Material.SADDLE));
        allSkills[13] = new Skill("Unarmed", data.has( "Unarmed" ) ? data.get( "Unarmed" ).getAsInt() : 0, null, new ControlElement.IconData(Material.STICK));
    }

    public void activatePowerUp(String name){
        for (Skill skill: allSkills) {
            if (name.equals(skill.getPowerUpName())){
                skill.activatePowerup();
            }
        }
    }

    public void saveLevels(){
        updatePowerLevel();
        //updatePowerUpLength();
        for (Skill skill: allSkills) {
            data.addProperty(skill.getSkillName(), skill.getLevel());
        }
        addon.getDataManagers().saveMCMMOData();
    }

    public void setLevel(String skillName, Integer level){
        for (Skill skill: allSkills) {
            if(skill.getSkillName().equals(skillName)){
                skill.setLevel(level);
            }
        }
        saveLevels();
        updatePowerLevel();
        //updatePowerUpLength();
    }

    public Skill getSkill(String skillName){
        for (Skill skill: allSkills) {
            if(skill.getSkillName().equals(skillName)){
                return skill;
            }
        }
        return null;
    }

    public void checkScoreboard(){
        Collection<ScoreObjective> objectives = Minecraft.getMinecraft().theWorld.getScoreboard().getScoreObjectives();
        for (ScoreObjective x: objectives) {
            if(!(x.getName().equals("mcmmo_sidebar"))){
                System.out.println("INVALID SCOREBOARD NAME" + x.getName());
                return;
            }
        }
        Collection<Score> scores = Minecraft.getMinecraft().theWorld.getScoreboard().getScores();
        for (Score score : scores) {
            if(score.getPlayerName().equals("§6Power Level")){
                allSkills[0].setLevel(score.getScorePoints());
            }
            String name = score.getPlayerName().replace("§a", "");
            for (Skill skill: allSkills) {
                if(skill.getSkillName().equals(name)){
                    skill.setLevel(score.getScorePoints());
                }
            }
        }
        saveLevels();
        //updatePowerUpLength();
    }

    public void updatePowerLevel(){
        Integer powerLevel = 0;
        for (Skill skill: allSkills) {
            if(!skill.getSkillName().equals("PowerLevel")){
                powerLevel = powerLevel + skill.getLevel();
            }
        }
        allSkills[0].setLevel(powerLevel);
    }


    public void registerGuiModules(){//TODO

    }
}
