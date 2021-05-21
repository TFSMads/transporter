package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.labymod.utils.ServerData;


public class JoinListener implements Consumer<ServerData>
{

    public void accept(final ServerData serverData) {
        if(serverData.getIp().toLowerCase().contains("superawesome")){
            TransporterAddon.connectedToSuperawesome = true;
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("a") + " Tilslutet superawesome, aktivere...");
        }else{
            TransporterAddon.connectedToSuperawesome = false;
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Tilslutet " + serverData.getIp() + ", deaktivere...");
        }
    }
}
