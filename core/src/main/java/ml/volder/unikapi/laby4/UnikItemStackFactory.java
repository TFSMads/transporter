package ml.volder.unikapi.laby4;

import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.client.world.item.ItemStackFactory;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface UnikItemStackFactory extends ItemStackFactory {

  @Override
  default ItemStack create(ResourceLocation location, int count) {
    return create(location, count, 0);
  }

  default ItemStack create(String namespace, String path, int count, int itemDamage) {
    return create(ResourceLocation.create(namespace, path), count, itemDamage);
  }

  ItemStack create(ResourceLocation location, int count, int itemDamage);

}
