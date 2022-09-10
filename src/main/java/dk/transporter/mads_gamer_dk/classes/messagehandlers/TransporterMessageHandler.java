package dk.transporter.mads_gamer_dk.classes.messagehandlers;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.listeners.messageReceiveListener;
import net.minecraft.client.Minecraft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterMessageHandler implements IMessageHandler{
    private final Items items;
    private final TransporterAddon addon;
    public TransporterMessageHandler(Items items, TransporterAddon addon){
        this.items = items;
        this.addon = addon;
    }
    @Override
    public boolean messageReceived(String msg, String clean) {
        if(checkTransporterInfo(clean))
            return true;
        if(checkTransporterChatMessages(clean))
            return true;
        return false;
    }

    private boolean checkTransporterInfo(String clean){ //Send if match
        final Pattern pattern = Pattern.compile("(^[0-9]+) ([^ ]+)$");
        final Matcher matcher = pattern.matcher(clean);

        if (matcher.find()) {
            for(Item i : items.getAllItems()){
                if(matcher.group(2).equals(i.getSaName())){
                    i.setAmount(Integer.parseInt(matcher.group(1)));
                    return false;
                }
            }
        }
        return false;
    }

    private boolean matchPutManglerMessage(String clean){
        final Pattern pattern = Pattern.compile("^Du har ikke noget ([a-zA-Z]+) at putte i$");
        final Matcher matcher = pattern.matcher(clean);

        if (matcher.find()) {
            if(messageReceiveListener.message == 1) {
                return true;
            }else if(messageReceiveListener.message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getManglerPutMessage(matcher.group(1)), false);
                return true;
            }
        }
        return false;
    }

    private boolean matchPutSuccessMessage(String clean){
        final Pattern pattern = Pattern.compile("^Gemmer ([0-9]+) ([a-zA-Z]+) i din transporter\\. Du har i alt ([0-9]+) ([a-zA-Z]+) i den\\.$");
        final Matcher matcher = pattern.matcher(clean);

        if (matcher.find()) {
            items.getItemByID(items.getIdBySaName(matcher.group(2))).setAmount(Integer.parseInt(matcher.group(3)));
            if(messageReceiveListener.message == 1) {
                return true;
            }else if(messageReceiveListener.message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getPutMessage(matcher.group(2),Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(3))), false);
                return true;
            }
        }
        return false;
    }

    private boolean matchGetManglerMessage(String clean){
        final Pattern pattern = Pattern.compile("^Du har ikke noget ([a-zA-Z]+) du kan tage fra din transporter$");
        final Matcher matcher = pattern.matcher(clean);

        if (matcher.find()) {
            if(messageReceiveListener.message == 1) {
                return true;
            }else if(messageReceiveListener.message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getManglerGetMessage(matcher.group(1)), false);
                return true;
            }
        }
        return false;
    }

    private boolean matchGetSuccessMessage(String clean){
        final Pattern pattern = Pattern.compile("^Du har taget ([0-9]+) ([a-zA-Z]+) fra din transporter\\. Du har ([0-9]+) \\([0-9]+ stacks\\) ([a-zA-Z]+) tilbage i den\\.$");
        final Matcher matcher = pattern.matcher(clean);

        if (matcher.find()) {
            items.getItemByID(items.getIdBySaName(matcher.group(2))).setAmount(Integer.parseInt(matcher.group(3)));
            if(messageReceiveListener.message == 1) {
                return true;
            }else if(messageReceiveListener.message == 2){
                Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getGetMessage(matcher.group(2),Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(3))), false);
                return true;
            }
        }
        return false;
    }

    private boolean matchCooldownMessage(String clean){
        if(clean.equals("Der er 2 sekunders cooldown på transporteren")) {
            Minecraft.getMinecraft().ingameGUI.setRecordPlaying(addon.getMessages().getDelayMessage(), false);
            return true;
        }
        return false;
    }

    private boolean checkTransporterChatMessages(String clean){
        if(messageReceiveListener.message == 0){ return false;}
        if(matchPutManglerMessage(clean) || matchGetManglerMessage(clean) || matchPutSuccessMessage(clean) || matchGetSuccessMessage(clean) || matchCooldownMessage(clean))
            return true;
        return false;
    }
}
