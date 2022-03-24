package dk.transporter.mads_gamer_dk.modules.values;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.minecraft.util.ResourceLocation;

public class SprucelogValueModule extends SimpleModule
{
    private String DisplayValue;

    TransporterAddon addon;

    public SprucelogValueModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Sprucelog";
    }

    public String getDisplayValue() {
        Double amount = Double.parseDouble(addon.getItems().getItemByID(26).getAmount().toString());
        Double value = Double.parseDouble(addon.getItems().getItemByID(26).getValue().toString());
        Double r = (amount/6400)*value;

        String r2 = Double.toString(Math.floor(r));

        DisplayValue = r2;
        return DisplayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Spruce_Log.png"));
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Værdien af det sprucelog du har i din transporter.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_TRANSPORTERITEMSVÆRDI;
    }

    public String getControlName() {
        return "Sprucelog";
    }

    public boolean isShown() {
        return true;
    }
}