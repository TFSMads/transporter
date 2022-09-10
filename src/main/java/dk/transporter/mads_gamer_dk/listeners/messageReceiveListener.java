package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.classes.messagehandlers.IMessageHandler;
import dk.transporter.mads_gamer_dk.classes.messagehandlers.McmmoMessageHandler;
import dk.transporter.mads_gamer_dk.classes.messagehandlers.MiningRigMessageHandler;
import dk.transporter.mads_gamer_dk.classes.messagehandlers.TransporterMessageHandler;
import dk.transporter.mads_gamer_dk.mcmmo.Skills;
import dk.transporter.mads_gamer_dk.utils.IsNumeric;
import dk.transporter.mads_gamer_dk.utils.UnixTimestampOfNow;
import net.labymod.api.events.MessageReceiveEvent;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class messageReceiveListener implements MessageReceiveEvent {

    public static Integer message = 2;

    private TransporterAddon addon;
    private Skills skills;

    private Items items;

    private List<IMessageHandler> handlers;

    public messageReceiveListener(Items items, TransporterAddon addon, Skills skills){
        this.addon = addon;
        this.items = items;
        this.skills = skills;
        handlers = new ArrayList<>();
        handlers.add(new McmmoMessageHandler(skills));
        handlers.add(new TransporterMessageHandler(items, addon));
        handlers.add(new MiningRigMessageHandler(addon));
    }

    public boolean onReceive(final String msg, final String clean){

        AtomicBoolean returnValue = new AtomicBoolean(false);

        handlers.forEach(iMessageHandler -> {
            if(iMessageHandler.messageReceived(msg,clean)){
                returnValue.set(true);
            }
        });

        return returnValue.get();
    }
}
