package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.labymod.utils.ServerData;


public class JoinListener implements Consumer<ServerData>
{
    private final TransporterAddon addon;

    public JoinListener(TransporterAddon addon){
        this.addon = addon;
    }

    public void accept(final ServerData serverData) {

        System.out.println("Switch server");

        System.out.println("Server:" + serverData);

        addon.setValidVersion(true, this);
        addon.setEnabled(true, this);

        if(serverData.getIp().toLowerCase().contains("superawesome")){
            TransporterAddon.connectedToSuperawesome = true;
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("a") + " Tilslutet superawesome, aktivere...");
        }else{
            TransporterAddon.connectedToSuperawesome = false;
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Tilslutet " + serverData.getIp() + ", deaktivere...");
        }
    }
}
