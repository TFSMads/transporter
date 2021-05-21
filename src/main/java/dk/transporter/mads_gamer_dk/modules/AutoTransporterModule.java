package dk.transporter.mads_gamer_dk.modules;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

public class AutoTransporterModule extends SimpleModule
{
    private boolean DisplayValue;

    TransporterAddon addon;

    public AutoTransporterModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "AutoTransporter";
    }

    public String getDisplayValue() {
        DisplayValue = addon.getConfig().has( "autoTransporer" ) ? addon.getConfig().get( "autoTransporer" ).getAsBoolean() : false;
        if(DisplayValue)
            return ModColor.cl("a") + "Til";
        else{
            return ModColor.cl("c") + "Fra";
        }
    }

    public String getDefaultValue() {
        return "false";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData("labymod/textures/settings/settings/autotext.png");
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Se om autotransporter er slået til.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_EXTERNAL_SERVICES;
    }

    public String getControlName() {
        return "AutoTransporter";
    }

    public boolean isShown() {
        return true;
    }
}
