package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.mcmmo.Skills;
import net.labymod.api.events.MessageSendEvent;

import java.util.Timer;
import java.util.TimerTask;

public class OnCommand implements MessageSendEvent {

    private TransporterAddon addon;
    private Skills skills;

    public OnCommand(TransporterAddon addon,Skills skills){
        this.addon = addon;
        this.skills = skills;
    }

    @Override
    public boolean onSend(String s) {
        if(s.charAt(0) == '/')
            this.addon.setTimer(this.addon.getTimer()-100);
        switch (s){
            case "/mcstats":
                new Timer("Timer").schedule(new TimerTask() {public void run() { skills.checkScoreboard(); }}, 1000L);
                break;
        }
        return false;


    }
}
