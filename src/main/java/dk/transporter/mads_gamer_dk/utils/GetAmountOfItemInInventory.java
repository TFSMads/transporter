package dk.transporter.mads_gamer_dk.utils;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GetAmountOfItemInInventory {

    public static Integer getAmountOfItem(InventoryPlayer inventory, Integer itemDamage, Item mcItem){
        Integer itemAmount = 0;
        for(ItemStack item : inventory.mainInventory){
            if (item != null){
                Integer iDamage = item.getItemDamage();

                Item mitem = item.getItem();
                if( iDamage == itemDamage && mitem == mcItem){
                    itemAmount = itemAmount + item.stackSize;
                }
            }

        }
        return itemAmount;
    }
}
