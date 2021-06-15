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

    public void accept(final ServerData serverData) {
        if(!TransporterAddon.isValidVersion){
            try {
                TransporterAddon.isValidVersion = validateVersion.isValidVersion("1.3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!TransporterAddon.isEnabled){
            try {
                TransporterAddon.isEnabled = validateUser.isSubscriber(Minecraft.getMinecraft().thePlayer.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!TransporterAddon.isEnabled){
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

        if(!TransporterAddon.isValidVersion){
            LabyMod.getInstance().displayMessageInChat(ModColor.cl("8") + ModColor.cl("l") + "[ " + ModColor.cl("7") + ModColor.cl("l") +"Transporter " + ModColor.cl("8") + ModColor.cl("l") + "]" + ModColor.cl("c") + " Din nuværende version af transporter addon er udløbet, download den seneste version fra vores discord. https://discord.gg/YnAUwqXQwv ");
        }
    }
}
