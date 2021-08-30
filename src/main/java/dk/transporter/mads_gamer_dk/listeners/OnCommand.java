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

            case "/server limbo":
            case "/hub":
                addon.setCurrentServer("limbo");
                break;
            case "/server larmelobby":
                addon.setCurrentServer("larmelobby");
                break;
            case "/server shoppylobby":
                addon.setCurrentServer("shoppylobby");
                break;
            case "/server byggelobby":
                addon.setCurrentServer("byggelobby");
                break;
            case "/server maskinrummet":
                addon.setCurrentServer("maskinrummet");
                break;
            case "/server maskinrummetlight":
                addon.setCurrentServer("maskinrummetlight");
                break;
            case "/server creepylobby":
                addon.setCurrentServer("creepylobby");
                break;
            default:
                if(s.contains("/server ")){
                    addon.setCurrentServer("Ukendt");
                }
        }
        System.out.println("Server: " + addon.getCurrentServer());
        return false;


    }
}
