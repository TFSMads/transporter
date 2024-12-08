package ml.volder.unikapi.api.inventory.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.types.Material;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4InventoryAPI implements InventoryAPI {

  private static Laby4InventoryAPI instance;

  public static Laby4InventoryAPI getAPI() {
    if(instance == null)
      instance = new Laby4InventoryAPI();
    return instance;
  }

  @Override
  public int getAmount(Material material, int itemDamage) {
    int amount = 0;
    for (int slot = 0; slot < 36; slot++) {
      ItemStack itemStack = Laby.labyAPI().minecraft().getClientPlayer().inventory().itemStackAt(slot);

      if(itemStack == null
          || itemStack.isAir()
          || itemStack.getAsItem() == null
          || itemStack.getAsItem().getIdentifier() == null
          || !itemStack.getAsItem().getIdentifier().equals(ResourceLocation.create(material.getNamespace(), material.getPath(MinecraftAPI.getAPI()
          .isLegacy(), false)))
          || (MinecraftAPI.getAPI().isLegacy() && itemStack.getCurrentDamageValue() != itemDamage)
          || !itemStack.getDisplayName().toString().startsWith("translation")
      )
        continue;
      amount += itemStack.getSize();
    }
    return amount;
  }

}
