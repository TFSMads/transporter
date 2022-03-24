package dk.transporter.mads_gamer_dk.modules.values;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class BlazerodValueModule extends SimpleModule
{
    private String DisplayValue;

    TransporterAddon addon;

    public BlazerodValueModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Blazerod";
    }

    public String getDisplayValue() {
        Double amount = Double.parseDouble(addon.getItems().getItemByID(21).getAmount().toString());
        Double value = Double.parseDouble(addon.getItems().getItemByID(21).getValue().toString());
        Double r = (amount/6400)*value;

        String r2 = Double.toString(Math.floor(r));

        DisplayValue = r2;
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
        return "Værdien af de blazerods du har i din transporter.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_TRANSPORTERITEMSVÆRDI;
    }

    public String getControlName() {
        return "Blazerod";
    }

    public boolean isShown() {
        return true;
    }
}