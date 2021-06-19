package dk.transporter.mads_gamer_dk.modules.items;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class BlazerodModule extends SimpleModule
{
    private String DisplayValue;

    TransporterAddon addon;

    public BlazerodModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Blaze Rod";
    }

    public String getDisplayValue() {
        DisplayValue = addon.getItems().getItemByID(21).getAmount().toString();
        return DisplayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.BLAZE_ROD);
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Hvor mange blaze rods du har i din transporter.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_TRANSPORTERITEMS;
    }

    public String getControlName() {
        return "Blazerod";
    }

    public boolean isShown() {
        return true;
    }
}
