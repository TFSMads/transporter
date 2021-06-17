package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.api.events.MessageSendEvent;

public class OnCommand implements MessageSendEvent {

    TransporterAddon addon;

    public OnCommand(TransporterAddon addon){
        this.addon = addon;
    }

    @Override
    public boolean onSend(String s) {
        if(s.charAt(0) == '/')
            this.addon.setTimer(this.addon.getTimer()-100);
        return false;
    }
}
