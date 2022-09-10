package dk.transporter.mads_gamer_dk.modules.mcmmo;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class Woodcutting extends SimpleModule {
    TransporterAddon addon;

    public Woodcutting(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Woodcutting Level";
    }

    public String getDisplayValue() {
        String displayValue = addon.getSkills().getSkill("Woodcutting").getLevel().toString();
        return displayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.DIAMOND_AXE);
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Dit woodcutting level.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_MCMMO;
    }

    public String getControlName() {
        return "Woodcutting Level";
    }

    public boolean isShown() {
        return true;
    }
}
