package dk.transporter.mads_gamer_dk.guis.getItemsGui;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.settings.SettingsCategory;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.ListContainerElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

import java.util.ArrayList;

public class SettingElements {
    
    public ArrayList<SettingsCategory> getCategories(TransporterAddon addon) {
        ArrayList<SettingsCategory> settingsCategories = new ArrayList();
        settingsCategories.add(getSettings(addon));
        return settingsCategories;
    }

    private SettingsCategory getSettings(TransporterAddon addon) {
        SettingsCategory mainCategory = new SettingsCategory("Indstillinger");

        /*
         * Er aktiv element.
         * dette element styrer om auto get er aktiv
         */

        mainCategory.addSetting(new BooleanElement( "Er aktiv", addon, new ControlElement.IconData( Material.REDSTONE_TORCH_ON ), "autoGetIsActive", addon.autoGetIsActive ));

        /*
         * Valgte item element
         * dette element styrer hvilken item auto get tager fra din transporter.
         */

        ListContainerElement itemSelectionElement = new ListContainerElement(ModColor.cl("7") + "Valgte Item", new ControlElement.IconData(addon.getItems().getItemByID(addon.getItems().getIdBySaName(addon.autoGetItem)).getMaterial()));

        itemSelectionElement.getSubSettings().add(new BooleanElement( "Er aktiv", addon, new ControlElement.IconData( Material.REDSTONE_TORCH_ON ), "autoGetIsActive", addon.autoGetIsActive ));

        mainCategory.addSetting(itemSelectionElement);

        /*
         * Minimum antal
         * dette element styrer hvor lidt af en item du skal have før addonet tager mere ud af din transporter
         */

        NumberElement valueElement = new NumberElement( "Maximum antal items af valgte item, før der tages mere", new ControlElement.IconData( Material.REDSTONE_COMPARATOR ) , addon.autoGetMinimum );

        valueElement.addCallback( new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                addon.autoGetMinimum = accepted;
                addon.getConfig().addProperty("autoGetMinimum", accepted);
            }
        } );

        mainCategory.addSetting(valueElement);

        return mainCategory;
    }
}
