package ml.volder.transporter.classes.items;



import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.types.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager {
    private List<Item> itemList = new ArrayList<>();

    public ItemManager() {
        itemList.add(new Item(
                "minecraft:sand",
                0,
                800,
                "Sand",
                "sand",
                "sand:0",
                "sand",
                Material.SAND
        ));
        itemList.add(new Item(
                "minecraft:sand",
                1,
                800,
                "Redsand",
                "sand:1",
                "sand:1",
                "sand:1",
                Material.RED_SAND
        ));
        itemList.add(new Item(
                "minecraft:stone",
                0,
                0,
                "Stone",
                "stone",
                "stone",
                "stone",
                Material.STONE
        ));
        itemList.add(new Item(
                "minecraft:cobblestone",
                0,
                0,
                "Cobblestone",
                "cobblestone",
                "cobblestone",
                "cobblestone",
                Material.COBBLESTONE
        ));
        itemList.add(new Item(
                "minecraft:stonebrick",
                0,
                0,
                "Stonebrick",
                "smooth_brick",
                "smooth_brick",
                "smooth_brick",
                Material.STONE_BRICK
        ));
        itemList.add(new Item(
                "minecraft:dirt",
                0,
                0,
                "Dirt",
                "dirt",
                "dirt",
                "dirt",
                Material.DIRT
        ));
        itemList.add(new Item(
                "minecraft:grass",
                0,
                0,
                "Grass",
                "grass",
                "grass",
                "grass", //Unchecked
                Material.GRASS
        ));
        itemList.add(new Item(
                "minecraft:coal",
                1,
                0,
                "Charcoal",
                "charcoal",
                "coal:1",
                "charcoal",
                Material.COAL
        ));
        itemList.add(new Item(
                "minecraft:coal",
                0,
                0,
                "Coal",
                "coal",
                "coal:0",
                "coal",
                Material.CHARCOAL
        ));
        itemList.add(new Item(
                "minecraft:iron_ore",
                0,
                0,
                "Iron Ore",
                "ironore",
                "iron_ore",
                "ironore",
                Material.IRON_ORE
        ));
        itemList.add(new Item(
                "minecraft:gold_ore",
                0,
                0,
                "Gold Ore",
                "goldore",
                "gold_ore",
                "goldore",
                Material.GOLD_ORE
        ));
        itemList.add(new Item(
                "minecraft:iron_ingot",
                0,
                0,
                "Iron Ingot",
                "ironingot",
                "iron_ingot",
                "ironingot",
                Material.IRON_INGOT
        ));
        itemList.add(new Item(
                "minecraft:gold_ingot",
                0,
                0,
                "Gold Ingot",
                "goldingot",
                "gold_ingot",
                "goldingot",
                Material.GOLD_INGOT
        ));
        itemList.add(new Item(
                "minecraft:bone",
                0,
                0,
                "Bone",
                "bone",
                "bone",
                "bone",
                Material.BONE
        ));
        itemList.add(new Item(
                "minecraft:glowstone_dust",
                0,
                0,
                "Glowstone Dust",
                "glowstonedust",
                "glowstone_dust",
                "glowstonedust",
                Material.GLOWSTONE_DUST
        ));
        itemList.add(new Item(
                "minecraft:glowstone",
                0,
                0,
                "Glowstone",
                "glowstone",
                "glowstone",
                "glowstone",
                Material.GLOWSTONE
        ));
        itemList.add(new Item(
                "minecraft:dye",
                4,
                0,
                "Lapis Lazuli",
                "lapislazuli",
                "ink_sack:4",
                "lapislazuli",
                Material.LAPIS_LAZULI
        ));
        itemList.add(new Item(
                "minecraft:quartz",
                0,
                0,
                "Quartz",
                "quartz",
                "quartz",
                "quartz",
                Material.QUARTZ
        ));
        itemList.add(new Item(
                "minecraft:redstone",
                0,
                0,
                "Redstone",
                "redstone",
                "redstone",
                "redstone",
                Material.REDSTONE
        ));
        itemList.add(new Item(
                "minecraft:diamond",
                0,
                0,
                "Diamond",
                "diamond",
                "diamond",
                "diamond",
                Material.DIAMOND
        ));
        itemList.add(new Item(
                "minecraft:obsidian",
                0,
                0,
                "Obsidian",
                "obsidian",
                "obsidian",
                "obsidian",
                Material.OBSIDIAN
        ));
        itemList.add(new Item(
                "minecraft:blaze_rod",
                0,
                0,
                "Blaze Rod",
                "blazerod",
                "blaze_rod", //Unchecked
                "blazerod", //Unchecked
                Material.BLAZE_ROD
        ));
        itemList.add(new Item(
                "minecraft:ender_pearl",
                0,
                0,
                "Ender Pearl",
                "enderpearl",
                "ender_pearl", //Unchecked
                "enderpearl", //Unchecked
                Material.ENDER_PEARL
        ));
        itemList.add(new Item(
                "minecraft:book",
                0,
                0,
                "Book",
                "book",
                "book", //Unchecked
                "book", //Unchecked
                Material.BOOK
        ));
        itemList.add(new Item(
                "minecraft:reeds",
                0,
                0,
                "Sugarcane",
                "sugarcane",
                "sugarcane", //Unchecked
                "sugarcane", //Unchecked
                Material.SUGAR_CANE
        ));
        itemList.add(new Item(
                "minecraft:leather",
                0,
                0,
                "Leather",
                "leather",
                "leather",
                "leather",
                Material.LEATHER
        ));
        itemList.add(new Item(
                "minecraft:log",
                0,
                0,
                "Oak Log",
                "oaklog",
                "log:0",
                "oaklog",
                Material.OAK_LOG
        ));
        itemList.add(new Item(
                "minecraft:log",
                1,
                0,
                "Spruce Log",
                "sprucelog",
                "log:1",
                "sprucelog",
                Material.SPRUCE_LOG
        ));
        itemList.add(new Item(
                "minecraft:log",
                2,
                0,
                "Birch Log",
                "birchlog",
                "log:2",
                "birchlog",
                Material.BIRCH_LOG
        ));
        itemList.add(new Item(
                "minecraft:log",
                3,
                0,
                "Jungle Log",
                "junglelog",
                "log:3",
                "junglelog",
                Material.JUNGLE_LOG
        ));
        itemList.add(new Item(
                "minecraft:slime_ball",
                0,
                0,
                "Slimeball",
                "slimeball",
                "slime_ball",
                "slimeball",
                Material.SLIME_BALL
        ));
        itemList.add(new Item(
                "minecraft:chest",
                0,
                0,
                "Chest",
                "chest",
                "chest",
                "chest",
                Material.CHEST
        ));
        itemList.add(new Item(
                "minecraft:trapped_chest",
                0,
                0,
                "Trapped Chest",
                "trappedchest",
                "trapped_chest",
                "trappedchest",
                Material.TRAPPED_CHEST
        ));
        itemList.add(new Item(
                "minecraft:hopper",
                0,
                0,
                "Hopper",
                "hopper",
                "hopper",
                "hopper",
                Material.HOPPER
        ));
        for (int i = 0; i < 16; i++) {
            itemList.add(new Item(
                    "minecraft:wool",
                    i,
                    0,
                    "Wool:" + i,
                    "wool:" + i,
                    "wool:" + i,
                    "wool:" + + i,
                    getColored(Material.WOOL, i)
            ));
        }

        for (int i = 0; i < 16; i++) {
            itemList.add(new Item(
                    "minecraft:stained_glass",
                    i,
                    0,
                    "Stained Glass:" + i,
                    "stained_glass:" + i,
                    "stained_glass:" + i,
                    "stained_glass:" + + i,
                    getColored(Material.STAINED_GLASS, i)
            ));
        }
    }

    private Material getColored(Material material, int itemDamage) {
        switch (itemDamage) {
            case 1:
                return material == Material.STAINED_GLASS ? Material.ORANGE_GLASS : material == Material.WOOL ? Material.ORANGE_WOOL : null;
            case 2:
                return material == Material.STAINED_GLASS ? Material.MAGENTA_GLASS : material == Material.WOOL ? Material.MAGENTA_WOOL : null;
            case 3:
                return material == Material.STAINED_GLASS ? Material.LIGHT_BLUE_GLASS : material == Material.WOOL ? Material.LIGHT_BLUE_WOOL : null;
            case 4:
                return material == Material.STAINED_GLASS ? Material.YELLOW_GLASS : material == Material.WOOL ? Material.YELLOW_WOOL : null;
            case 5:
                return material == Material.STAINED_GLASS ? Material.LIME_GLASS : material == Material.WOOL ? Material.LIME_WOOL : null;
            case 6:
                return material == Material.STAINED_GLASS ? Material.PINK_GLASS : material == Material.WOOL ? Material.PINK_WOOL : null;
            case 7:
                return material == Material.STAINED_GLASS ? Material.GRAY_GLASS : material == Material.WOOL ? Material.GRAY_WOOL : null;
            case 8:
                return material == Material.STAINED_GLASS ? Material.LIGHT_GRAY_GLASS : material == Material.WOOL ? Material.LIGHT_GRAY_WOOL : null;
            case 9:
                return material == Material.STAINED_GLASS ? Material.CYAN_GLASS : material == Material.WOOL ? Material.CYAN_WOOL : null;
            case 10:
                return material == Material.STAINED_GLASS ? Material.PURPLE_GLASS : material == Material.WOOL ? Material.PURPLE_WOOL : null;
            case 11:
                return material == Material.STAINED_GLASS ? Material.BLUE_GLASS : material == Material.WOOL ? Material.BLUE_WOOL : null;
            case 12:
                return material == Material.STAINED_GLASS ? Material.BROWN_GLASS : material == Material.WOOL ? Material.BROWN_WOOL : null;
            case 13:
                return material == Material.STAINED_GLASS ? Material.GREEN_GLASS : material == Material.WOOL ? Material.GREEN_WOOL : null;
            case 14:
                return material == Material.STAINED_GLASS ? Material.RED_GLASS : material == Material.WOOL ? Material.RED_WOOL : null;
            case 15:
                return material == Material.STAINED_GLASS ? Material.BLACK_GLASS : material == Material.WOOL ? Material.BLACK_WOOL : null;
            default:
                return material == Material.STAINED_GLASS ? Material.WHITE_GLASS : material == Material.WOOL ? Material.WHITE_WOOL : null;
        }
    }

    public List<Item> getItemList(){
        return this.itemList;
    }

    public Item getItemByChatName(String chatName) {
        AtomicReference<Item> returnItem = new AtomicReference<>();
        itemList.forEach(item -> {
            if(item.getChatName().equals(chatName))
                returnItem.set(item);
        });
        return returnItem.get();
    }

    private DataManager<Data> dataManager;
    private UUID dataManagerUUID;

    private void initDataManager() {
        if(TransporterAddon.getInstance().getPlayerDataFolder() == null)
            return;
        this.dataManager = new DataManager<Data>(new File(TransporterAddon.getInstance().getPlayerDataFolder(), "itemData.json"), Data.class);
        dataManagerUUID = PlayerAPI.getAPI().getUUID();
    }

    public DataManager<Data> getDataManager() {
        if(dataManager == null || PlayerAPI.getAPI().getUUID() == null || !dataManagerUUID.equals(PlayerAPI.getAPI().getUUID()))
            initDataManager();
        return dataManager;
    }
}