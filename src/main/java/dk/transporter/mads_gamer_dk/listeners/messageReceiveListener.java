package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.IsNumeric;
import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;
import net.labymod.api.events.MessageReceiveEvent;
import net.minecraft.client.Minecraft;

public class messageReceiveListener implements MessageReceiveEvent {

    public static Integer message = 2;

    private TransporterAddon addon;

    private Items items;

    public messageReceiveListener(Items items, TransporterAddon addon){
        this.addon = addon;
        this.items = items;
    }
    public boolean onReceive(final String msg, final String clean){

        if(message == 0){ return false;}
        String[] splited = clean.split("\\s+");
        Integer index = 0;
        for(String s : splited){
            System.out.println("string : "+s+" index : "+index);
            index++;
        }


        if(IsNumeric.isNumeric(splited[0])){
            for(Item i : items.getAllItems()){
                if(splited[1].equals(i.getSaName())){
                    i.setAmount(Integer.parseInt(splited[0]));
                }
            }
        }

        if(splited[0].equals("miningrigv2") && splited[1].equals("loaded.") && splited[2].equals("Paste") && splited[3].equals("it") && splited[4].equals("with")){
            addon.getTimers().setLastUsed(UnixTimestampOfNow.getTime());
        }

        if(splited[0].equals("Du") && splited[1].equals("har") && splited[2].equals("ikke") && splited[3].equals("noget") && splited[5].equals("at") && splited[6].equals("putte") && splited[7].equals("i")){
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getManglerPutMessage(splited[4]), false);
                return true;
            }
        }else if(splited[0].equals("Gemmer") && splited[3].equals("i") && splited[4].equals("din") && splited[5].equals("transporter.") && splited[6].equals("Du") && splited[7].equals("har") && splited[8].equals("i") && splited[9].equals("alt") && splited[12].equals("i") && splited[13].equals("den.")){
            items.getItemByID(items.getIdBySaName(splited[2])).setAmount(Integer.parseInt(splited[10]));
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getPutMessage(splited[2],Integer.parseInt(splited[1]),Integer.parseInt(splited[10])), false);
                return true;
            }
        }else if(splited[0].equals("Du") && splited[1].equals("har") && splited[2].equals("taget") && splited[5].equals("fra")  && splited[6].equals("din") && splited[7].equals("transporter.") && splited[8].equals("Du") && splited[9].equals("har") && splited[14].equals("tilbage") && splited[15].equals("i") && splited[16].equals("den.")){
            items.getItemByID(items.getIdBySaName(splited[4])).setAmount(Integer.parseInt(splited[10]));
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getGetMessage(splited[4],Integer.parseInt(splited[3]),Integer.parseInt(splited[10])), false);
                return true;
            }
        }else if(splited[0].equals("Du") && splited[1].equals("har") && splited[2].equals("ikke") && splited[3].equals("noget") && splited[5].equals("du") && splited[6].equals("kan") && splited[7].equals("tage") && splited[8].equals("fra") && splited[9].equals("din") && splited[10].equals("transporter")){
            if(message == 1) {
                return true;
            }else if(message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getManglerGetMessage(splited[4]), false);
                return true;
            }
        }else if(clean.equals("Der er 2 sekunders cooldown på transporteren")) {
            if (message == 1) {
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getDelayMessage(), false);
                return true;
            } else if (message == 2) {
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getDelayMessage(), false);
                return true;
            }
        }

        return false;
    }
}
