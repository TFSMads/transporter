package dk.transporter.mads_gamer_dk.modules;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.ModColor;
import net.minecraft.util.ResourceLocation;

public class TransporterVærdiModule extends SimpleModule
{
    private String DisplayValue;

    TransporterAddon addon;

    public TransporterVærdiModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Transporter Værdi";
    }

    public String getDisplayValue() {
        Integer value = 0;
        Integer add = 0;
        for(Item i : addon.getItems().getAllItems()){
            if(i.getAmount() > 0 && i.getValue() > 0){
                add = (i.getAmount()/6400)*i.getValue();
                if(add > 0){
                    value = add+value;
                }
            }
        }
        DisplayValue = value.toString();
        return DisplayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(new ResourceLocation("transporter/textures/icons/Birch_Log.png"));
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
