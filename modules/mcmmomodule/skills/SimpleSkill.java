package ml.volder.transporter.modules.mcmmomodule.skills;

import ml.volder.transporter.modules.mcmmomodule.McmmoSkill;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.widgets.ModuleSystem;

public class SimpleSkill implements McmmoSkill {

    protected DataManager<Data> dataManager;
    protected int level = 0;
    protected boolean noLevelData = false;
    protected String skillId;
    protected Material icon = Material.PAPER;


    public SimpleSkill(String skillId, DataManager<Data> dataManager) {
        this.dataManager = dataManager;
        this.skillId = skillId;
        loadSkill();
    }

    private void loadSkill() {
        level = dataManager.getSettings().getData().has("skill." + skillId + ".level") ? dataManager.getSettings().getData().get("skill." + skillId + ".level").getAsInt() : -1;
        if(level == -1) {
            level = 1;
            noLevelData = true;
        }
    }

    @Override
    public void updateLevel(int level) {
        noLevelData = false;
        this.level = level;
        dataManager.getSettings().getData().addProperty("skill." + skillId + ".level", level);
        dataManager.save();
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getId() {
        return skillId;
    }

    public SimpleSkill setIcon(Material material) {
        this.icon = material;
        return this;
    }

    @Override
    public void registerModules(Object category) {
        ModuleSystem.registerModule(skillId + "-level", skillId + " Level", false, category, icon, s -> {
            if (noLevelData)
                return "Ingen data (Skriv /mcstats)";
            return String.valueOf(level);
        });
    }
}
