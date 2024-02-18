package ml.volder.transporter.classes.items;


import com.google.gson.JsonObject;
import ml.volder.transporter.updater.UpdateManager;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.types.Material;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager {
    private List<Item> itemList = new ArrayList<>();

    public ItemManager() {
        loadItemsFromCSV();
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
                if(line.startsWith("id"))
                    line = br.readLine();
                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                try {
                    String[] attributes = line.split(",");
                    String legacyType = attributes[2];
                    //Extra number after : in legacyType if present
                    int itemDamage = legacyType.contains(":") ? Integer.parseInt(legacyType.split(":")[1]) : 0;
                    // Get Material enum from enum string
                    Material material = Material.valueOf(attributes[4]);
                    Item item = new Item(Integer.parseInt(attributes[0]), attributes[1], attributes[2], attributes[3], itemDamage, material);
                    // adding item into ArrayList
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

    public Item getItemByName(String name) {
        AtomicReference<Item> returnItem = new AtomicReference<>();
        itemList.forEach(item -> {
            if(item.getName().equals(name))
                returnItem.set(item);
        });
        return returnItem.get();
    }

    private DataManager<Data> dataManager;
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

    public void loadPriceConfig(JsonObject priceConfig) {
        for (String key : priceConfig.keySet()) {
            Item item = getItemByName(key);
            if(item != null && priceConfig.has(key)) {
                item.setSellValue(priceConfig.get(key).getAsInt());
            }
        }
    }
}
