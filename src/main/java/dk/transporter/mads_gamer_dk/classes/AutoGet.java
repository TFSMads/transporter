package dk.transporter.mads_gamer_dk.classes;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.GetAmountOfItemInInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class AutoGet {

    private int timer = 0;
    private TransporterAddon addon;

    public AutoGet(TransporterAddon addon) {
        this.addon = addon;
    }


    public void onTick(){
        timer++;
        if(timer >= 45){
            timer = 0;
            get();
        }
    }

    private void get(){
        Item item = addon.items.getItemByID(addon.items.getIdBySaName(addon.autoGetItem));
        if(GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory, item.getItemDamage(), item.getInventoryItem()) < addon.autoGetMinimum && item.getAmount() > 0 && !addon.getAutoTransporter()){
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter get " + addon.autoGetItem);
        }
    }

}
