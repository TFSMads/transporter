package dk.transporter.mads_gamer_dk.listeners.tickSubscribers;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;

public class SaveTimerSubscriber implements ITickSubscriber{
    private int saveTimer;

    @Override
    public void onTick(){
        saveTimer++;
        if(saveTimer >= 1000){
            saveTimer = 0;
            for(Item i : TransporterAddon.getInstance().getItems().getAllItems()){

                TransporterAddon.getInstance().getConfig().addProperty(i.getItem().toString()+"-Amount", i.getAmount());

            }
            TransporterAddon.getInstance().saveConfig();
        }
    }
}
