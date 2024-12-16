package ml.volder.transporter.modules.mcmmomodule;

import ml.volder.transporter.modules.mcmmomodule.skills.AbilitySkill;
import ml.volder.transporter.modules.mcmmomodule.skills.SimpleSkill;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class McmmoManager {
    List<McmmoSkill> mcmmoSkills = new ArrayList<>();
    DataManager<Data> dataManager;

    public McmmoManager(DataManager<Data> dataManager) {
        this.dataManager = dataManager;
    }

    public void init() {
        mcmmoSkills.add(new AbilitySkill("Excavation", "§a**GIGA DRILL BREAKER ACTIVATED**", dataManager).setIcon(Material.DIAMOND_SHOVEL));
        mcmmoSkills.add(new AbilitySkill("Woodcutting", "§a**TREE FELLER ACTIVATED**",dataManager).setIcon(Material.DIAMOND_AXE));
        mcmmoSkills.add(new SimpleSkill("Repair", dataManager).setIcon(Material.ANVIL));
        mcmmoSkills.add(new SimpleSkill("Acrobatics", dataManager).setIcon(Material.LEATHER_BOOTS));
        mcmmoSkills.add(new SimpleSkill("Alchemy", dataManager).setIcon(Material.BREWING_STAND));
        mcmmoSkills.add(new SimpleSkill("Archery", dataManager).setIcon(Material.BOW));
        mcmmoSkills.add(new SimpleSkill("Axes", dataManager).setIcon(Material.DIAMOND_AXE));
        mcmmoSkills.add(new SimpleSkill("Fishing", dataManager).setIcon(Material.FISHING_ROD));
        mcmmoSkills.add(new SimpleSkill("Herbalism", dataManager).setIcon(Material.SPIDER_EYE));
        mcmmoSkills.add(new AbilitySkill("Mining", "§a**SUPER BREAKER ACTIVATED**",dataManager).setIcon(Material.DIAMOND_PICKAXE));
        mcmmoSkills.add(new SimpleSkill("Swords", dataManager).setIcon(Material.DIAMOND_SWORD));
        mcmmoSkills.add(new SimpleSkill("Taming", dataManager).setIcon(Material.SADDLE));
        mcmmoSkills.add(new SimpleSkill("Unarmed", dataManager).setIcon(Material.STICK));
    }

    public McmmoSkill getSkillById(String id) {
        for (McmmoSkill mcmmoSkill : mcmmoSkills) {
            if(mcmmoSkill.getId().equals(id))
                return mcmmoSkill;
        }
        return null;
    }

    public void onMessageReceive(String clean) {
        final Pattern pattern = Pattern.compile("§l([a-zA-Z]+) increased to §r§a§l([^)]*)§r§f");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            String numString = matcher.group(2).replace(",", "");
            int num = Integer.parseInt(numString);
            McmmoSkill mcmmoSkill = getSkillById(matcher.group(1));
            if(mcmmoSkill != null)
                mcmmoSkill.updateLevel(num);
        }

        for (McmmoSkill mcmmoSkill : mcmmoSkills)
            mcmmoSkill.onChatMessageReceive(clean);
    }

    public void registerModules() {
        Object category = ModuleSystem.registerCategory("McMMO Skill - Levels", Material.PAPER, "Widgets der viser dit level i forskellige McMMO Skills");
        Object powerUpCategory = ModuleSystem.registerCategory("McMMO - Abilities", Material.WATCH, "Widgets der viser en timer for de forskellige McMMO Abilities");
        for (McmmoSkill mcmmoSkill : mcmmoSkills) {
            mcmmoSkill.registerModules(category, powerUpCategory);
        }
    }
}
