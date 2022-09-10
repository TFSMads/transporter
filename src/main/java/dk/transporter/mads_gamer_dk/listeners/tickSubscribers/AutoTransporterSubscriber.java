package dk.transporter.mads_gamer_dk.listeners.tickSubscribers;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.GetAmountOfItemInInventory;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

public class AutoTransporterSubscriber implements ITickSubscriber{
    int timer = 0;

    public static int delay = 40;

    public static void setDelay(int delay){
        AutoTransporterSubscriber.delay = delay;
    }


    @Override
    public void onTick() {
        if(!TransporterAddon.getInstance().getAutoTransporter() || !TransporterAddon.getInstance().isEnabled() || !TransporterAddon.getInstance().isValidVersion())
            return;
        timer++;
        Map<String, Integer> itemAmountMap = new HashMap<>();

        if(!(timer >= delay))
            return;

        for (Item item: TransporterAddon.getInstance().getItems().getAllItems())
            itemAmountMap.put(item.getSaName(),GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory, item.getItemDamage(), item.getInventoryItem()));

        int maxAmount = -1;
        String itemWithMost = "";
        for (Map.Entry<String, Integer> item : itemAmountMap.entrySet())
            if(item.getValue() > maxAmount){
                itemWithMost = item.getKey();
                maxAmount = item.getValue();
            }


        if(maxAmount > 0 && itemWithMost.length() > 0){
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put " + itemWithMost);
            timer = 0;
        }


    }
}
