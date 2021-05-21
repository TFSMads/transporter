package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

public class QuitListener implements Consumer<ServerData>
{
    public void accept(final ServerData serverData) {
        TransporterAddon.connectedToSuperawesome = false;
    }
}