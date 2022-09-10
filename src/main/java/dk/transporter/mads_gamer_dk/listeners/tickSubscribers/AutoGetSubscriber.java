package dk.transporter.mads_gamer_dk.listeners.tickSubscribers;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.GetAmountOfItemInInventory;
import net.minecraft.client.Minecraft;

public class AutoGetSubscriber implements ITickSubscriber{
    private int timer = 0;
    @Override
    public void onTick() {
        if(!TransporterAddon.getInstance().autoGetIsActive || !TransporterAddon.getInstance().isEnabled() || !TransporterAddon.getInstance().isValidVersion())
            return;
        timer++;
        if(timer >= AutoTransporterSubscriber.delay){
            timer = 0;
            get();
        }
    }

    private void get(){
        Item item = TransporterAddon.getInstance().items.getItemByID(TransporterAddon.getInstance().items.getIdBySaName(TransporterAddon.getInstance().autoGetItem));
        if(GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory, item.getItemDamage(), item.getInventoryItem()) < TransporterAddon.getInstance().autoGetMinimum && item.getAmount() > 0 && !TransporterAddon.getInstance().getAutoTransporter()){
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter get " + TransporterAddon.getInstance().autoGetItem);
        }
    }
}
