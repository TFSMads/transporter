package dk.transporter.mads_gamer_dk.listeners;

import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;

import static dk.transporter.mads_gamer_dk.TransporterAddon.addon;

public class messageReceiveListener implements MessageReceiveEvent {

    public static Integer message = 2;

    public boolean onReceive(final String msg, final String clean){

        if(message == 0){ return false;}
        String[] splited = clean.split("\\s+");

        System.out.println(splited[0]);

        if(splited[0].equals("Du") && splited[1].equals("har") && splited[2].equals("ikke") && splited[3].equals("noget") && splited[5].equals("at") && splited[6].equals("putte") && splited[7].equals("i")){
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(ModColor.cl("b") + splited[0] + " " + splited[1] + " " + splited[2] + " " + splited[3] + ModColor.cl("3") + " " + splited[4], false);
                return true;
            }
        }else if(splited[0].equals("Gemmer") && splited[3].equals("i") && splited[4].equals("din") && splited[5].equals("transporter.") && splited[6].equals("Du") && splited[7].equals("har") && splited[8].equals("i") && splited[9].equals("alt") && splited[12].equals("i") && splited[13].equals("den.")){
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(ModColor.cl("b") + "Gemmer " + ModColor.cl("3") + splited[1] + " " + splited[2] + ModColor.cl("b") + " i din transporter." + ModColor.cl("7") +"(" + ModColor.cl("3") + splited[10] + ModColor.cl("7") + ")", false);
                return true;
            }
        }else if(splited[0].equals("Du") && splited[1].equals("har") && splited[2].equals("taget") && splited[5].equals("fra")  && splited[6].equals("din") && splited[7].equals("transporter.") && splited[8].equals("Du") && splited[9].equals("har") && splited[14].equals("tilbage") && splited[15].equals("i") && splited[16].equals("den.")){
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(ModColor.cl("b") + "Tager " + ModColor.cl("3") + splited[3] + " " + splited[4] + ModColor.cl("b") + " fra din transporter." + ModColor.cl("7") +"(" + ModColor.cl("3") + splited[10] + ModColor.cl("7") + ")", false);
                return true;
            }
        }else if(splited[0].equals("Du") && splited[1].equals("har") && splited[2].equals("ikke") && splited[3].equals("noget") && splited[5].equals("du") && splited[6].equals("kan") && splited[7].equals("tage") && splited[8].equals("fra") && splited[9].equals("din") && splited[10].equals("transporter")){
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(ModColor.cl("b") + "Du har ikke noget " + ModColor.cl("3") + splited[4] + ModColor.cl("b") + " i din transporter.", false);
                return true;
            }
        }else if(clean.equals("Der er 2 sekunders cooldown på transporteren")){
            if(message == 1) {
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(ModColor.cl("c") + "Der er 2 sekunders cooldown på transporteren", false);
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(ModColor.cl("c") + "Der er 2 sekunders cooldown på transporteren", false);
                return true;
            }
        }else if(splited[0].equals("Unknown")){
            if(addon.getExecuteCommands() == true){
                if (addon.getTimer() == 0 || addon.getTimer() == 1 || addon.getTimer() == 2|| addon.getTimer() == 3|| addon.getTimer() == 4|| addon.getTimer() == 5|| addon.getTimer() == 6|| addon.getTimer() == 7)
                    addon.setisInSaLobby(false);
                return true;
            }

        }else if(splited[0].equals(">>>")){
            addon.setTimer(addon.getTimer() + 100);
            return true;
        }
        return false;
    }
}
