package dk.transporter.mads_gamer_dk.modules.Timers;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class MiningRigModule extends SimpleModule {
    private String DisplayValue;

    TransporterAddon addon;

    public MiningRigModule(final TransporterAddon addon) {
        this.addon = addon;
    }

    public String getDisplayName() {
        return "Mining Rig Timer";
    }

    public String getDisplayValue() {
        Integer delay = 900 - (UnixTimestampOfNow.getTime() - addon.getTimers().getLastUsed());
        if(delay < 0){
            delay = 0;
        }
        DisplayValue = delay.toString() + " Sekunder";
        if(delay == 1) DisplayValue = delay.toString() + " Sekundt";
        return DisplayValue;
    }

    public String getDefaultValue() {
        return "0";
    }

    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.WATCH);
    }

    public void loadSettings() {
    }

    public String getSettingName() {
        return null;
    }

    public String getDescription() {
        return "Hvor lang tid til du kan bruge en mining rig.";
    }

    public int getSortingId() {
        return 200;
    }

    public ModuleCategory getCategory() {
        return TransporterAddon.CATEGORY_TRANSPORTERADDON;
    }

    public String getControlName() {
        return "Mining Rig Timer";
    }

    public boolean isShown() {
        return true;
    }
}
