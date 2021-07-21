package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.api.validateUser;
import dk.transporter.mads_gamer_dk.api.validateVersion;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;


public class JoinListener implements Consumer<ServerData>
{
    private final TransporterAddon addon;

    public JoinListener(TransporterAddon addon){
        this.addon = addon;
    }

    public void accept(final ServerData serverData) {
        try {
            Boolean isVV = validateVersion.isValidVersion("2.0");
            addon.setValidVersion(isVV, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Boolean isVU = validateUser.isSubscriber();
            addon.setEnabled(isVU, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!addon.isEnabled()){
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Deaktivere transporter addon. Du har ikke købt adgang!");


            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Køb adgang på vores discord https://discord.gg/YnAUwqXQwv");
        }else{
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("a") + " Aktivere transporter addon.");
        }
        if(serverData.getIp().toLowerCase().contains("superawesome")){
            TransporterAddon.connectedToSuperawesome = true;
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("a") + " Tilslutet superawesome, aktivere...");
        }else{
            TransporterAddon.connectedToSuperawesome = false;
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Tilslutet " + serverData.getIp() + ", deaktivere...");
        }

        if(!addon.isValidVersion()){
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Din nuværende version af transporter addon er udløbet, download den seneste version fra vores discord. https://discord.gg/YnAUwqXQwv ");
        }
    }
}
