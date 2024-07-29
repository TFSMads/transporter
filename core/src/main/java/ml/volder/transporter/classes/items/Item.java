package ml.volder.transporter.classes.items;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.api.TransporterPriceApi;
import ml.volder.transporter.events.ItemAmountUpdatedEvent;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.types.Material;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Item {

    private Material material;

    private Integer sellValue;
    private Integer amountInTransporter;

    private boolean autoUpdateSellValue = true;

    private UUID currentPlayerData;

    public Item(String type, String legacy_type){
        this.material = Material.create("minecraft", type, legacy_type);
        loadData();
    }

    public void setSellValue(Integer sellValue) {
        this.sellValue = sellValue;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().addProperty("sellValue." + this.getModernType(), this.sellValue);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().save();
        }
    }

    public void setAmountInTransporter(Integer amountInTransporter) {
        this.amountInTransporter = amountInTransporter < 0 ? 0 : amountInTransporter;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManager() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().addProperty("amount." + this.getModernType(), this.amountInTransporter);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManager().save();
        }
      EventManager.callEvent(new ItemAmountUpdatedEvent(EventType.POST));
    }

    public Integer getAmountInTransporter() {
        if(currentPlayerData == null || !currentPlayerData.equals(PlayerAPI.getAPI().getUUID()))
            loadData();
        return amountInTransporter < 0 ? 0 : amountInTransporter;
    }

    public Integer getItemDamage() {
        return material.getItemDamage();
    }

    public Integer getSellValue() {
        if(sellValue == null)
            return 0;
        return sellValue;
    }

    private String displayName;

    public String getDisplayName() {
      if(displayName == null) {
        String input = getModernType().replace("_", " ");
        displayName = Arrays.stream(input.split("\\s+"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
            .collect(Collectors.joining(" "));
      }
      return displayName;
    }

    public String getLegacyType() {
        return material.getPath(true);
    }

    public String getModernType() {
        return material.getPath(false);
    }

    public Material getMaterial() {
        return material;
    }

    private void loadData() {
        this.sellValue = TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().has("sellValue." + this.getModernType())
                ? TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().get("sellValue." + this.getModernType()).getAsInt()
                : 0;
        this.autoUpdateSellValue = !TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().has("autoUpdateSellValue." + this.getModernType()) || TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().get("autoUpdateSellValue." + this.getModernType()).getAsBoolean();
        if(PlayerAPI.getAPI().getUUID() == null)
            return;
        UUID uuid = PlayerAPI.getAPI().getUUID();
        currentPlayerData = uuid;
        this.amountInTransporter = TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().has("amount." + this.getModernType())
                ? TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().get("amount." + this.getModernType()).getAsInt()
                : 0;
    }

    private int getSellValueFromPriceServer() {
        return TransporterPriceApi.getInstance().getSellValueFromPriceServer(this.getModernType());
    }


    public void updateSellValueFromPriceServer() {
        updateSellValueFromPriceServer(null);
    }

    public void updateSellValueFromPriceServer(Consumer<Integer> consumer) {
        Thread taskThread = new Thread(() -> {
            try {
                int value = this.getSellValueFromPriceServer();
                this.setSellValue(value);
                if(consumer != null)
                    consumer.accept(value);
            } catch (Exception ignored) {}
        });
        taskThread.start();
    }

    public boolean getAutoUpdateSellValue() {
        return autoUpdateSellValue;
    }

    public void setAutoUpdateSellValue(boolean autoUpdateSellValue) {
        this.autoUpdateSellValue = autoUpdateSellValue;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().getSettings().getData().addProperty("autoUpdateSellValue." + this.getModernType(), this.autoUpdateSellValue);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManagerGlobal().save();
        }
    }

}
