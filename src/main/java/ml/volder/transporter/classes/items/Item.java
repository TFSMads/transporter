package ml.volder.transporter.classes.items;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.api.TransporterPriceApi;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.types.Material;

import java.util.UUID;
import java.util.function.Consumer;

public class Item {

    private String name;
    private String legacy_type;
    private String modern_type;
    private int id;

    private Integer itemDamage;
    private Integer sellValue;
    private Integer amountInTransporter;
    private Material material;
    private boolean autoUpdateSellValue = true;

    private UUID currentPlayerData;

    public Item(int id, String name, String legacy_type, String modern_type, int itemDamage, Material material){
        this.id = id;
        this.name = name;
        this.legacy_type = legacy_type;
        this.modern_type = modern_type;
        this.itemDamage = itemDamage;
        this.material = material;
    }

    public void setSellValue(Integer sellValue) {
        this.sellValue = sellValue;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().addProperty("sellValue." + this.getName(), this.sellValue);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().save();
        }
    }

    public void setAmountInTransporter(Integer amountInTransporter) {
        this.amountInTransporter = amountInTransporter < 0 ? 0 : amountInTransporter;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManager() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().addProperty("amount." + this.getName(), this.amountInTransporter);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManager().save();
        }
    }

    public Integer getAmountInTransporter() {
        if(currentPlayerData == null || !currentPlayerData.equals(PlayerAPI.getAPI().getUUID()))
            loadData();
        return amountInTransporter < 0 ? 0 : amountInTransporter;
    }

    public Integer getItemDamage() {
        return itemDamage;
    }

    public Integer getSellValue() {
        if(sellValue == null)
            return 0;
        return sellValue;
    }

    public String getDisplayName() {
        //TODO tilføj et display navn i transporter-items.csv. Der ser bedre ud end name.
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getName() {
        return name;
    }

    public String getLegacyType() {
        return legacy_type;
    }

    public String getModernType() {
        return modern_type;
    }

    public int getSaId() {
        return id;
    }




    public Material getMaterial() {
        return material;
    }

    private void loadData() {
        this.sellValue = TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().has("sellValue." + this.getName())
                ? TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().get("sellValue." + this.getName()).getAsInt()
                : 0;
        this.autoUpdateSellValue = !TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().has("autoUpdateSellValue." + this.getName()) || TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().get("autoUpdateSellValue." + this.getName()).getAsBoolean();
        if(PlayerAPI.getAPI().getUUID() == null)
            return;
        UUID uuid = PlayerAPI.getAPI().getUUID();
        currentPlayerData = uuid;
        this.amountInTransporter = TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().has("amount." + this.getName())
                ? TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().get("amount." + this.getName()).getAsInt()
                : 0;
    }

    private int getSellValueFromPriceServer() {
        return TransporterPriceApi.getInstance().getSellValueFromPriceServer(this.getName());
    }


    public void updateSellValueFromPriceServer() {
        updateSellValueFromPriceServer(null);
    }

    public void updateSellValueFromPriceServer(Consumer<Integer> consumer) {
        Thread taskThread = new Thread(() -> {
            int value = this.getSellValueFromPriceServer();
            this.setSellValue(value);
            if(consumer != null)
                consumer.accept(value);
        });
        taskThread.start();
    }

    public boolean getAutoUpdateSellValue() {
        return autoUpdateSellValue;
    }

    public void setAutoUpdateSellValue(boolean autoUpdateSellValue) {
        this.autoUpdateSellValue = autoUpdateSellValue;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().addProperty("autoUpdateSellValue." + this.getName(), this.autoUpdateSellValue);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().save();
        }
    }

}
