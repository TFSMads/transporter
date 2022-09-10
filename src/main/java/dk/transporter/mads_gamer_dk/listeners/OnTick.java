package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.listeners.tickSubscribers.AutoGetSubscriber;
import dk.transporter.mads_gamer_dk.listeners.tickSubscribers.AutoTransporterSubscriber;
import dk.transporter.mads_gamer_dk.listeners.tickSubscribers.ITickSubscriber;
import dk.transporter.mads_gamer_dk.listeners.tickSubscribers.SaveTimerSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class OnTick {

    List<ITickSubscriber> tickSubscriberList = new ArrayList<>();

    public OnTick(){
        tickSubscriberList.add(new SaveTimerSubscriber());
        tickSubscriberList.add(new AutoTransporterSubscriber());
        tickSubscriberList.add(new AutoGetSubscriber());
    }
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        try{
            tickSubscriberList.forEach(ITickSubscriber::onTick);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
