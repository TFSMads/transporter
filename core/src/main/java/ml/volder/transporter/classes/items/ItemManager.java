package ml.volder.transporter.classes.items;


import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.Laby;
import net.labymod.api.client.world.item.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager {
    private List<Item> itemList = new ArrayList<>();

    public void loadItems() {
        loadItemsFromCSV();
      updateItemSellValues();
    }

    public void updateItemSellValues() {
        itemList.forEach(item -> {
            if(item.getAutoUpdateSellValue())
                item.updateSellValueFromPriceServer();
        });
    }

    public void reloadItemsFromCSV() {
        UnikAPI.LOGGER.info("Reloading items from CSV");
        loadItemsFromCSV();
    }

    private void loadItemsFromCSV() {
        // Get file from common resources
        File file = new File(UnikAPI.getCommonDataFolder(), "transporter-items.csv");
        // Check if file exists
        if(!file.exists()) {
            UnikAPI.LOGGER.warning("Failed to load items from CSV: File does not exist");
            return;
        }
        // Get input stream from file
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            UnikAPI.LOGGER.warning("Failed to load items from CSV: Could not get input stream from file");
            return;
        }

        itemList.clear();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            // read the first line from the text file
            String line = br.readLine();
            // loop until all lines are read
            while (line != null) {
                //skip title line
                if(line.startsWith("type"))
                    line = br.readLine();
                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                try {
                    String[] attributes = line.split(",");
                    String modernType = attributes[0];
                    String legacyType = attributes[1];
                    //Check if item is supported by client version
                    ItemStack itemStack;
                    if(MinecraftAPI.getAPI().isLegacy()) {
                      itemStack = Laby.references().itemStackFactory().create("minecraft", legacyType.split(":")[0], 1);
                      int itemDamage = legacyType.contains(":") ? Integer.parseInt(legacyType.split(":")[1]) : 0;
                      itemStack.setLegacyItemData(itemDamage);
                    }else {
                      itemStack = Laby.references().itemStackFactory().create("minecraft", modernType, 1);
                    }

                    UnikAPI.LOGGER.debug(modernType + ": " + itemStack.getAsItem().getIdentifier().getPath() + " - ");
                    if(
                        Objects.equals(itemStack.getAsItem().getIdentifier().getPath(), "apple") && !Objects.equals(modernType, "apple")
                        || MinecraftAPI.getAPI().isLegacy()
                            && modernType.equals("dragon_head")
                            || modernType.endsWith("bed") && !modernType.equals("white_bed")
                            || modernType.startsWith("potted_")
                            || modernType.endsWith("wall_skull")
                    ) {
                        UnikAPI.LOGGER.info("Item is not supported by client version: " + modernType);
                        line = br.readLine();
                        continue;
                    }

                    if(modernType.equals("white_bed")) {
                      modernType = "bed";
                    }
                    if(modernType.equals("grass_block")) {
                      modernType = "grass";
                    }


                    Item item = new Item(modernType, legacyType);
                    itemList.add(item);
                    line = br.readLine();
                }catch (Exception e) {
                    UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
                    UnikAPI.LOGGER.info("Failed to load item: " + line);
                    line = br.readLine();
                }

        }
        } catch (IOException e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.INFO, e);
            throw new RuntimeException(e);
        }
    }


    public List<Item> getItemList(){
        return this.itemList;
    }

    public Item getItemByDisplayName(String name) {
        AtomicReference<Item> returnItem = new AtomicReference<>();
        itemList.forEach(item -> {
            if(item.getDisplayName().equals(name))
                returnItem.set(item);
        });
        return returnItem.get();
    }

  public Item getItemByType(String name) {
    AtomicReference<Item> returnItem = new AtomicReference<>();
    itemList.forEach(item -> {
      if(item.getModernType().equals(name))
        returnItem.set(item);
    });
    return returnItem.get();
  }

    private DataManager<Data> dataManager;
    private DataManager<Data> globalDataManager;

    private UUID dataManagerUUID;

    private void initDataManager() {
        if(UnikAPI.getPlayerDataFolder() == null)
            return;
        this.dataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getPlayerDataFolder(), "itemData.json"));
        dataManagerUUID = PlayerAPI.getAPI().getUUID();
    }

    public DataManager<Data> getDataManager() {
        if(dataManager == null || PlayerAPI.getAPI().getUUID() == null || !dataManagerUUID.equals(PlayerAPI.getAPI().getUUID()))
            initDataManager();
        return dataManager;
    }

    private void initDataManagerGlobal() {
        if(UnikAPI.getCommonDataFolder() == null)
            return;
        this.globalDataManager = DataManager.getOrCreateDataManager(new File(UnikAPI.getCommonDataFolder(), "itemData.json"));
    }

    public DataManager<Data> getDataManagerGlobal() {
        if(globalDataManager == null)
            initDataManagerGlobal();
        return globalDataManager;
    }
}
