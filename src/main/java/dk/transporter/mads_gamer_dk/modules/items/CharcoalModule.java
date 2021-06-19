package dk.transporter.mads_gamer_dk.modules.items;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.minecraft.util.ResourceLocation;

public class CharcoalModule extends SimpleModule
{
    private String DisplayValue;

    TransporterAddon addon;

    public CharcoalModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Charcoal";
    }

    public String getDisplayValue() {
        DisplayValue = addon.getItems().getItemByID(7).getAmount().toString();
        return DisplayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(new ResourceLocation("transporter/textures/icons/charcoal.png"));
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Hvor meget Charcoal du har i din transporter.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_TRANSPORTERITEMS;
    }

    public String getControlName() {
        return "Charcoal";
    }

    public boolean isShown() {
        return true;
    }
}
