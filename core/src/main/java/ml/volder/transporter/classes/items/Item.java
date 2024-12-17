package ml.volder.transporter.classes.items;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.api.TransporterPriceApi;
import ml.volder.transporter.events.ItemAmountUpdatedEvent;
import ml.volder.transporter.messaging.PluginMessageHandler;
import ml.volder.transporter.messaging.channels.GetChannel;
import ml.volder.transporter.messaging.channels.PutChannel;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.types.Material;
import net.labymod.api.Laby;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Item {

    private DataManager<Data> dataManagerSettings;

    private Material material;

    private Integer sellValue;
    private Integer amountInTransporter;

    private boolean autoUpdateSellValue = true;

    private UUID currentPlayerData;

    public Item(String type, String legacy_type){
        this.material = Material.create("minecraft", type, legacy_type);
        loadData();
        this.dataManagerSettings = DataManager.getOrCreateDataManager("%common%/settings.json");
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
      Laby.fireEvent(new ItemAmountUpdatedEvent());
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

    /**
     * Send get payload to server
     *
     * @param amount amount to get from transporter
     */
    public void get(Integer amount) {
        if(dataManagerSettings.getBoolean("useTransporterPackets")) {
            PluginMessageHandler.getChannel(GetChannel.class).sendPayload(this.getModernType(), amount);
        } else {
            PlayerAPI.getAPI().sendCommand("transporter get " + this.getModernType() + " " + amount);
        }
    }

    /**
     * Send put payload to server
     *
     * @param amount amount to put in transporter
     */
    public void put(Integer amount) {
        if(dataManagerSettings.getBoolean("useTransporterPackets")) {
            PluginMessageHandler.getChannel(PutChannel.class).sendPayload(this.getModernType(), amount);
        } else {
            PlayerAPI.getAPI().sendCommand("transporter put " + this.getModernType() + " " + amount);
        }
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
