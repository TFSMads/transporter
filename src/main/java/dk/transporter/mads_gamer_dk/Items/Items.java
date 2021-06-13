package dk.transporter.mads_gamer_dk.Items;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.settings.LabyModAddonsGui;
import net.labymod.settings.LabyModSettingsGui;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Items {

    private TransporterAddon addon;

    private Item[] allItems = new Item[35];




    public Items() {
        try {

            allItems[0] = new Item(TransporterItems.SAND, Material.SAND, 0, "Sand", Blocks.sand);
            allItems[1] = new Item(TransporterItems.SAND1, Material.SAND, 0, "Redsand",Blocks.sand);
            allItems[2] = new Item(TransporterItems.STONE, Material.STONE, 0, "Stone", Blocks.stone);
            allItems[3] = new Item(TransporterItems.COBBLESTONE, Material.COBBLESTONE, 0, "Cobblestone",Blocks.cobblestone);
            allItems[4] = new Item(TransporterItems.STONEBRICK, Material.SMOOTH_BRICK, 0, "Stonebrick",Blocks.stonebrick);
            allItems[5] = new Item(TransporterItems.DIRT, Material.DIRT, 0, "Dirt",Blocks.dirt);
            allItems[6] = new Item(TransporterItems.GRASS, Material.GRASS, 0, "Grass",Blocks.grass);
            allItems[7] = new Item(TransporterItems.CHARCOAL, Material.COAL, 0, "Charcoal", net.minecraft.init.Items.coal);
            allItems[8] = new Item(TransporterItems.COAL, Material.COAL, 0, "Coal", net.minecraft.init.Items.coal);
            allItems[9] = new Item(TransporterItems.IRONORE, Material.IRON_ORE, 0, "Ironore",Blocks.iron_ore);
            allItems[10] = new Item(TransporterItems.GOLDORE, Material.GOLD_ORE, 0, "Goldore",Blocks.gold_ore);
            allItems[11] = new Item(TransporterItems.IRONINGOT, Material.IRON_INGOT, 0, "Iron Ingot", net.minecraft.init.Items.iron_ingot);
            allItems[12] = new Item(TransporterItems.GOLDINGOT, Material.GOLD_INGOT, 0, "Gold Ingot", net.minecraft.init.Items.gold_ingot);
            allItems[13] = new Item(TransporterItems.BONE, Material.BONE, 0, "Bone", net.minecraft.init.Items.bone);
            allItems[14] = new Item(TransporterItems.GLOWSTONEDUST, Material.GLOWSTONE_DUST, 0, "Glowstonedust", net.minecraft.init.Items.glowstone_dust);
            allItems[15] = new Item(TransporterItems.GLOWSTONE, Material.GLOWSTONE, 0, "Glowstone",Blocks.glowstone);
            allItems[16] = new Item(TransporterItems.LAPISLAZULI, Material.INK_SACK, 0, "Lapislazuli", net.minecraft.init.Items.dye);
            allItems[17] = new Item(TransporterItems.QUARTZ, Material.QUARTZ, 0, "Quartz", net.minecraft.init.Items.quartz);
            allItems[18] = new Item(TransporterItems.REDSTONE, Material.REDSTONE, 0, "Redstone", net.minecraft.init.Items.redstone);
            allItems[19] = new Item(TransporterItems.DIAMOND, Material.DIAMOND, 0, "Diamond", net.minecraft.init.Items.diamond);
            allItems[20] = new Item(TransporterItems.OBSIDIAN, Material.OBSIDIAN, 0, "Obsidian",Blocks.obsidian);
            allItems[21] = new Item(TransporterItems.BLAZEROD, Material.BLAZE_ROD, 0, "Blaze Rod", net.minecraft.init.Items.blaze_rod);
            allItems[22] = new Item(TransporterItems.ENDERPEARL, Material.ENDER_PEARL, 0, "Ender Pearl", net.minecraft.init.Items.ender_pearl);
            allItems[23] = new Item(TransporterItems.BOOK, Material.BOOK, 0, "Book", net.minecraft.init.Items.book);
            allItems[24] = new Item(TransporterItems.SUGARCANE, Material.SUGAR_CANE, 0, "Sugar Cane", net.minecraft.init.Items.sugar);
            allItems[25] = new Item(TransporterItems.LEATHER, Material.LEATHER, 0, "Leather", net.minecraft.init.Items.leather);
            allItems[26] = new Item(TransporterItems.SPRUCELOG, Material.LOG, 0, "Spruce Log",Blocks.log);
            allItems[27] = new Item(TransporterItems.OAKLOG, Material.LOG, 0, "Oak Log",Blocks.log);
            allItems[28] = new Item(TransporterItems.BIRCHLOG, Material.LOG, 0, "Birch Log",Blocks.log);
            allItems[29] = new Item(TransporterItems.JUNGLELOG, Material.LOG, 0, "Jungle Log",Blocks.log);
            allItems[30] = new Item(TransporterItems.SLIMEBALL, Material.SLIME_BALL, 0, "Slime Ball",Blocks.log);
            allItems[31] = new Item(TransporterItems.GLASS, Material.GLASS, 0, "Glass",Blocks.glass);
            allItems[32] = new Item(TransporterItems.CHEST, Material.CHEST, 0, "Chest",Blocks.chest);
            allItems[33] = new Item(TransporterItems.TRAPPEDCHEST, Material.TRAPPED_CHEST, 0, "Trapped Chest",Blocks.trapped_chest);
            allItems[34] = new Item(TransporterItems.HOPPER, Material.HOPPER, 0, "Hopper",Blocks.hopper);
        }catch (Exception e){
            System.out.println(e);
        }
        addon = TransporterAddon.getAddon();
        //TransporterItems items[] = TransporterItems.values();
        //for(TransporterItems item : items) {
        //    map.put(item.toString(), false);
        //}
    }

    //public void setItem(String item, Boolean bool){
    //    this.map.put(item, bool);
    //}

    //public boolean getItem(String item){
    //    return this.map.get(item);
    //}

    public Material getMaterial(TransporterItems item){
        for(Item i : allItems) {
            if(i.getItem().toString() == item.toString()){
                return i.getMaterial();
            }
        }
        return Material.BARRIER;
    }

    public Item getItemByID(Integer id){
        return allItems[id];
    }

    public ControlElement.IconData getIconData(TransporterItems item){
        Material material = getMaterial(item);
        ControlElement.IconData iconData = new ControlElement.IconData( material );
        if(item.equals(TransporterItems.SAND1)){
            iconData = new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/RedSand.png") );
        } else if(item.equals(TransporterItems.CHARCOAL)){
            iconData = new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/charcoal.png"));
        }else if(item.equals(TransporterItems.LAPISLAZULI)){
            iconData = new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/blue_dye.png"));
        }else if(item.equals(TransporterItems.SPRUCELOG)){
            iconData = new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Spruce_Log.png"));
        }else if(item.equals(TransporterItems.BIRCHLOG)){
            iconData = new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Birch_Log.png"));
        }else if(item.equals(TransporterItems.JUNGLELOG)){
            iconData = new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Jungle_Log.png"));
        }
        return iconData;
    }

    public String getName(TransporterItems item){
        for(Item i : allItems) {
            if(i.getItem().toString() == item.toString()){
                return i.getName();
            }
        }
        return "Fejl";
    }



}
