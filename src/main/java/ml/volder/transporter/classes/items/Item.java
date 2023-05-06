package ml.volder.transporter.classes.items;

import ml.volder.transporter.TransporterAddon;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.types.Material;

import java.util.UUID;

public class Item {

    @Deprecated
    private String registryName;
    private Integer itemDamage;
    private Integer sellValue;
    private String displayName;
    private String commandName;
    private String chatName;
    private String transporterInfoName;
    private Integer amountInTransporter;
    private Material material;
    private boolean isAutoTransporterEnabled = true;

    private UUID currentPlayerData;

    public Item(String registryName, Integer itemDamage, Integer sellValue, String displayName, String commandName, String chatName, String transporterInfoName, Material material){
        this.registryName = registryName;
        this.itemDamage = itemDamage;
        this.sellValue = sellValue;
        this.displayName = displayName;
        this.commandName = commandName;
        this.chatName = chatName;
        this.transporterInfoName = transporterInfoName;
        this.material = material;
    }

    public void setSellValue(Integer sellValue) {
        this.sellValue = sellValue;
    }

    public void setAmountInTransporter(Integer amountInTransporter) {
        this.amountInTransporter = amountInTransporter;
        if(TransporterAddon.getInstance().getTransporterItemManager().getDataManager() != null){
            TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().addProperty("amount." + this.getChatName(), this.amountInTransporter);
            TransporterAddon.getInstance().getTransporterItemManager().getDataManager().save();
        }
    }

    public Integer getAmountInTransporter() {
        if(currentPlayerData == null || !currentPlayerData.equals(PlayerAPI.getAPI().getUUID()))
            loadData();
        return amountInTransporter;
    }

    /**
     * @apiNote RegistryName er blevet tilføjet til UnikAPI Materials. Benyt det istedet for den her UnikAPI da disse registry names kun virker med legacy item (før 1.13)
     */
    @Deprecated
    public String getRegistryName() {
        return registryName;
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
        return displayName;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getChatName() {
        return chatName;
    }

    public String getTransporterInfoName() {
        return transporterInfoName;
    }

    public Material getMaterial() {
        return material;
    }

    private void loadData() {
        if(PlayerAPI.getAPI().getUUID() == null)
            return;
        UUID uuid = PlayerAPI.getAPI().getUUID();
        currentPlayerData = uuid;
        this.amountInTransporter = TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().has("amount." + this.getChatName())
                ? TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getSettings().getData().get("amount." + this.getChatName()).getAsInt()
                : 0;
    }

    /**
     * @return True if this item should be put in transporter by Auto Transporter
     */
    public boolean isAutoTransporterEnabled() {
        return isAutoTransporterEnabled;
    }

    /**
     * @param autoTransporterEnabled Whether this item should be put in transporter by Auto Transporter.
     */
    public void setAutoTransporterEnabled(boolean autoTransporterEnabled) {
        isAutoTransporterEnabled = autoTransporterEnabled;
    }
}
