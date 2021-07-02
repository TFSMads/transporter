package dk.transporter.mads_gamer_dk.modules;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.Round;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.minecraft.util.ResourceLocation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static java.lang.Math.floor;

public class TransporterValueModule extends SimpleModule
{
    private String DisplayValue;

    TransporterAddon addon;

    public TransporterValueModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Transporter Værdi";
    }

    public String getDisplayValue() {
        Double value = 0d;
        Double add = 0d;
        for(Item i : addon.getItems().getAllItems()){
            if(i.getAmount() > 0 && i.getValue() > 0){
                add = ((i.getAmount().doubleValue())/6400)*(i.getValue().doubleValue());
                if(add > 0){
                    value = add+value;
                }
            }
        }

        DisplayValue = Round.round(value, 2) + " Ems";
        return DisplayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.EMERALD);
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Hvor meget din transporter er værd.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_EXTERNAL_SERVICES;
    }

    public String getControlName() {
        return "Transporter Værdi";
    }

    public boolean isShown() {
        return true;
    }
}
