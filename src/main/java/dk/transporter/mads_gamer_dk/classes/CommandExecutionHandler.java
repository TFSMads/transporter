package dk.transporter.mads_gamer_dk.classes;

import dk.transporter.mads_gamer_dk.TransporterAddon;

import java.util.Timer;

public class CommandExecutionHandler {

    private TransporterAddon addon;
    private Integer executionCheckInterval;

    private Integer lastCommandExecution;

    public CommandExecutionHandler(TransporterAddon addon, int inverval){
        this.addon = addon;
        this.executionCheckInterval = inverval;
    }

    private void startCommandExecutionCheck(){
        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 5000);
    }

    public Integer getLastCommandExecution() {
        return lastCommandExecution;
    }

    public void setLastCommandExecution(Integer lastCommandExecution) {
        this.lastCommandExecution = lastCommandExecution;
    }


}
