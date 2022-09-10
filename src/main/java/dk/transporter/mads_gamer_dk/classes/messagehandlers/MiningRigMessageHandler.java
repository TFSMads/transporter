package dk.transporter.mads_gamer_dk.classes.messagehandlers;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;

public class MiningRigMessageHandler implements IMessageHandler{

    private final TransporterAddon addon;

    public MiningRigMessageHandler(TransporterAddon addon){
        this.addon = addon;
    }
    @Override
    public boolean messageReceived(String msg, String clean) {
        if(clean.equals("miningrigv2 loaded. Paste it with //paste")){
            addon.getTimers().setLastUsed(UnixTimestampOfNow.getTime());
        }
        return false;
    }
}
