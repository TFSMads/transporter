package dk.transporter.mads_gamer_dk.settingelements;

import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;

public class DescribedBooleanElement extends BooleanElement {
    public DescribedBooleanElement(String displayName, LabyModAddon addon, ControlElement.IconData iconData, String attributeName, boolean defaultValue, String description) {
        super(displayName, addon, iconData, attributeName, defaultValue);
        this.setDescriptionText(description);
    }
}
