package ml.volder.unikapi.types;

import java.util.HashMap;
import java.util.Map;

public class Material {

  //modern items
  public static final Material REDSTONE_TORCH = new Material("minecraft", "redstone_torch", "redstone_torch");
  public static final Material DIODE = new Material("minecraft", "repeater", "repeater");
  public static final Material WATCH = new Material("minecraft", "clock", "clock");
  public static final Material EMERALD = new Material("minecraft", "emerald", "emerald");
  public static final Material EMERALD_BLOCK = new Material("minecraft", "emerald_block", "emerald_block");
  public static final Material PAINTING = new Material("minecraft", "painting", "painting");
  public static final Material PAPER = new Material("minecraft", "paper", "paper");
  public static final Material REDSTONE_LAMP = new Material("minecraft", "redstone_lamp", "redstone_lamp");
  public static final Material OAK_BUTTON = new Material("minecraft", "oak_button", "wooden_button");
  public static final Material OAK_SIGN = new Material("minecraft", "oak_sign", "sign");
  public static final Material BARRIER = new Material("minecraft", "barrier", "barrier");
  public static final Material SAND = new Material("minecraft", "sand", "sand");
  public static final Material RED_SAND = new Material("minecraft", "red_sand", "sand:1");
  public static final Material STONE = new Material("minecraft", "stone", "stone");
  public static final Material COBBLESTONE = new Material("minecraft", "cobblestone", "cobblestone");
  public static final Material STONE_BRICK = new Material("minecraft", "stone_bricks", "stonebrick");
  public static final Material DIRT = new Material("minecraft", "dirt", "dirt");
  public static final Material GRASS = new Material("minecraft", "grass_block", "grass");
  public static final Material COAL = new Material("minecraft", "coal", "coal");
  public static final Material CHARCOAL = new Material("minecraft", "charcoal", "coal:1");
  public static final Material IRON_ORE = new Material("minecraft", "iron_ore", "iron_ore");
  public static final Material GOLD_ORE = new Material("minecraft", "gold_ore", "gold_ore");
  public static final Material IRON_INGOT = new Material("minecraft", "iron_ingot", "iron_ingot");
  public static final Material GOLD_INGOT = new Material("minecraft", "gold_ingot", "gold_ingot");
  public static final Material BONE = new Material("minecraft", "bone", "bone");
  public static final Material GLOWSTONE_DUST = new Material("minecraft", "glowstone_dust", "glowstone_dust");
  public static final Material GLOWSTONE = new Material("minecraft", "glowstone", "glowstone");
  public static final Material LAPIS_LAZULI = new Material("minecraft", "lapis_lazuli", "dye:4");
  public static final Material QUARTZ = new Material("minecraft", "quartz", "quartz");
  public static final Material REDSTONE = new Material("minecraft", "redstone", "redstone");
  public static final Material DIAMOND = new Material("minecraft", "diamond", "diamond");
  public static final Material OBSIDIAN = new Material("minecraft", "obsidian", "obsidian");
  public static final Material BLAZE_ROD = new Material("minecraft", "blaze_rod", "blaze_rod");
  public static final Material ENDER_PEARL = new Material("minecraft", "ender_pearl", "ender_pearl");
  public static final Material BOOK = new Material("minecraft", "book", "book");
  public static final Material SUGAR_CANE = new Material("minecraft", "sugar_cane", "reeds");
  public static final Material LEATHER = new Material("minecraft", "leather", "leather");
  public static final Material OAK_LOG = new Material("minecraft", "oak_log", "log");
  public static final Material SPRUCE_LOG = new Material("minecraft", "spruce_log", "log:1");
  public static final Material BIRCH_LOG = new Material("minecraft", "birch_log", "log:2");
  public static final Material JUNGLE_LOG = new Material("minecraft", "jungle_log", "log:3");
  public static final Material SLIME_BALL = new Material("minecraft", "slime_ball", "slime_ball");
  public static final Material CHEST = new Material("minecraft", "chest", "chest");
  public static final Material TRAPPED_CHEST = new Material("minecraft", "trapped_chest", "trapped_chest");
  public static final Material HOPPER = new Material("minecraft", "hopper", "hopper");



  public static final Material WHITE_WOOL = new Material("minecraft", "white_wool", "wool");
  public static final Material ORANGE_WOOL = new Material("minecraft", "orange_wool", "wool:1");
  public static final Material MAGENTA_WOOL = new Material("minecraft", "magenta_wool", "wool:2");
  public static final Material LIGHT_BLUE_WOOL = new Material("minecraft", "light_blue_wool", "wool:3");
  public static final Material YELLOW_WOOL = new Material("minecraft", "yellow_wool", "wool:4");
  public static final Material LIME_WOOL = new Material("minecraft", "lime_wool", "wool:5");
  public static final Material PINK_WOOL = new Material("minecraft", "pink_wool", "wool:6");
  public static final Material GRAY_WOOL = new Material("minecraft", "gray_wool", "wool:7");
  public static final Material LIGHT_GRAY_WOOL = new Material("minecraft", "light_gray_wool", "wool:8");
  public static final Material CYAN_WOOL = new Material("minecraft", "cyan_wool", "wool:9");
  public static final Material PURPLE_WOOL = new Material("minecraft", "purple_wool", "wool:10");
  public static final Material BLUE_WOOL = new Material("minecraft", "blue_wool", "wool:11");
  public static final Material BROWN_WOOL = new Material("minecraft", "brown_wool", "wool:12");
  public static final Material GREEN_WOOL = new Material("minecraft", "green_wool", "wool:13");
  public static final Material RED_WOOL = new Material("minecraft", "red_wool", "wool:14");
  public static final Material BLACK_WOOL = new Material("minecraft", "black_wool", "wool:15");


  public static final Material WHITE_GLASS = new Material("minecraft", "white_stained_glass", "stained_glass");
  public static final Material ORANGE_GLASS = new Material("minecraft", "orange_stained_glass", "stained_glass:1");
  public static final Material MAGENTA_GLASS = new Material("minecraft", "magenta_stained_glass", "stained_glass:2");
  public static final Material LIGHT_BLUE_GLASS = new Material("minecraft", "light_blue_stained_glass", "stained_glass:3");
  public static final Material YELLOW_GLASS = new Material("minecraft", "yellow_stained_glass", "stained_glass:4");
  public static final Material LIME_GLASS = new Material("minecraft", "lime_stained_glass", "stained_glass:5");
  public static final Material PINK_GLASS = new Material("minecraft", "pink_stained_glass", "stained_glass:6");
  public static final Material GRAY_GLASS = new Material("minecraft", "gray_stained_glass", "stained_glass:7");
  public static final Material LIGHT_GRAY_GLASS = new Material("minecraft", "light_gray_stained_glass", "stained_glass:8");
  public static final Material CYAN_GLASS = new Material("minecraft", "cyan_stained_glass", "stained_glass:9");
  public static final Material PURPLE_GLASS = new Material("minecraft", "purple_stained_glass", "stained_glass:10");
  public static final Material BLUE_GLASS = new Material("minecraft", "blue_stained_glass", "stained_glass:11");
  public static final Material BROWN_GLASS = new Material("minecraft", "brown_stained_glass", "stained_glass:12");
  public static final Material GREEN_GLASS = new Material("minecraft", "green_stained_glass", "stained_glass:13");
  public static final Material RED_GLASS = new Material("minecraft", "red_stained_glass", "stained_glass:14");
  public static final Material BLACK_GLASS = new Material("minecraft", "black_stained_glass", "stained_glass:15");

  public static final Material DIAMOND_SHOVEL = new Material("minecraft", "diamond_shovel", "diamond_shovel");
  public static final Material DIAMOND_AXE = new Material("minecraft", "diamond_axe", "diamond_axe");
  public static final Material ANVIL = new Material("minecraft", "anvil", "anvil");
  public static final Material LEATHER_BOOTS = new Material("minecraft", "leather_boots", "leather_boots");
  public static final Material BREWING_STAND = new Material("minecraft", "brewing_stand", "brewing_stand");
  public static final Material BOW = new Material("minecraft", "bow", "bow");
  public static final Material FISHING_ROD = new Material("minecraft", "fishing_rod", "fishing_rod");
  public static final Material SPIDER_EYE = new Material("minecraft", "spider_eye", "spider_eye");
  public static final Material DIAMOND_PICKAXE = new Material("minecraft", "diamond_pickaxe", "diamond_pickaxe");
  public static final Material DIAMOND_SWORD = new Material("minecraft", "diamond_sword", "diamond_sword");
  public static final Material SADDLE = new Material("minecraft", "saddle", "saddle");
  public static final Material STICK = new Material("minecraft", "stick", "stick");

  public static final Material SPRUCE_SAPLING = new Material("minecraft", "spruce_sapling", "sapling:1");
  public static final Material COCAO_BEANS = new Material("minecraft", "cocao_beans", "dye:3");
  public static final Material GLASS = new Material("minecraft", "glass", "glass");

  private final static Map<String, Material> materialCache = new HashMap<>();
  private String namespace;
  private String path;
  private String legacyPath;

  public static Material create(String namespace, String path, String legacyPath) {
    if(!materialCache.containsKey(namespace + ":" + path))
      materialCache.put(namespace + ":" + path, new Material(namespace, path, legacyPath));
    return materialCache.get(namespace + ":" + path);
  }

  private Material(String namespace, String path, String legacyPath) {
    this.namespace = namespace;
    this.path = path;
    this.legacyPath = legacyPath;
  }

  public int getItemDamage(int fallbackValue) {
    try {
      return this.getPath(true).contains(":") ? Integer.parseInt(this.getPath(true).split(":")[1]) : 0;
    }catch (Exception e) {
      return fallbackValue;
    }
  }

  public int getItemDamage() {
    return getItemDamage(0);
  }

  public String getPath() {
    return getPath(false);
  }
  public String getPath(boolean isLegacy) {
    return getPath(isLegacy, true);
  }
  public String getPath(boolean isLegacy, boolean includeItemDamage) {
    if(isLegacy)
      return stripItemDamage(legacyPath, !includeItemDamage);
    return stripItemDamage(path, !includeItemDamage);
  }

  private String stripItemDamage(String path, boolean shouldStrip) {
    if(!shouldStrip)
      return path;
    if(path.contains(":"))
      return path.split(":")[0];
    return path;
  }

  public String getNamespace() {
    return namespace;
  }

}
