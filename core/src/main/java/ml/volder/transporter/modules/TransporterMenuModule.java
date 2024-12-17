package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.transportermenumodule.TransporterMenu;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

import java.util.*;
import java.util.stream.Collectors;

public class TransporterMenuModule extends SimpleModule {

    private Map<Item, Integer> activeItems = new HashMap<>();
    private boolean onlyActiveInLobby = true;
    private int withdrawAmount = 64;

    private Key openKey = Key.K;

    public TransporterMenuModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        return this;
    }

    @Override
    public SimpleModule enable() {
        Laby.labyAPI().eventBus().registerListener(this);
        return this;
    }

    @Override
    public void fillSettings(SettingRegistryAccessor subSettings) {
        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.onlyActiveInLobby = b),
                        getDataManager(),
                        "onlyActiveInLobby",
                        true))
                .id("onlyActiveInLobby")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS_1, 4, 1))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        KeybindWidget.class,
                        new TransporterAction<net.labymod.api.client.gui.screen.key.Key>(key -> openKey = Laby4KeyMapper.convert(key)),
                        getDataManager(),
                        "transporterMenuKeybind",
                        net.labymod.api.client.gui.screen.key.Key.K))
                .id("transporterMenuKeybind")
                .icon(Icon.sprite16(ModTextures.SETTINGS_ICONS, 1, 5))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SliderWidget.class,
                        new TransporterAction<Float>(v -> withdrawAmount = v != null ? v.intValue() : withdrawAmount),
                        getDataManager(),
                        "withdrawAmount",
                        -1F).range(-1, 2304))
                .id("withdrawAmount")
                .icon(Icon.sprite32(ModTextures.FLINT_ICONS, 2, 1))
                .build()
        );
    }

    @Override
  public void loadConfig() {
    super.loadConfig();
    if(getDataManager() != null){

        //Config adapter - Convert old config to new config
        if(!getDataManager().getSettings().getData().has("configVersion") || getDataManager().getSettings().getData().get("configVersion").getAsInt() < 1){
            int slot = 0;

            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
                if(getDataManager().getSettings().getData().has(item.getModernType())){
                    try {
                        if(getDataManager().getSettings().getData().get(item.getModernType()).getAsBoolean()){
                            getDataManager().getSettings().getData().addProperty(item.getModernType(), slot);
                            slot++;
                        } else if (!(getDataManager().getSettings().getData().get(item.getModernType()).getAsInt() >= 0)) {
                            getDataManager().getSettings().getData().remove(item.getModernType());
                        }
                    } catch (Exception ignored) {
                        getDataManager().getSettings().getData().remove(item.getModernType());
                    }

                }
            }
            getDataManager().getSettings().getData().addProperty("configVersion", 1);
            getDataManager().save();
        }

        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
            if(getDataManager().getSettings().getData().has(item.getModernType())){
                if(getDataManager().getSettings().getData().get(item.getModernType()).getAsInt() >= 0){
                    activeItems.put(item, getDataManager().getSettings().getData().get(item.getModernType()).getAsInt());
                }
            }
        }
        if(activeItems.isEmpty()) {
            List<String> defaultItems = Arrays.asList("stone","cobblestone","diamond_ore","diamond_block","gold_block","iron_block","iron_ore","gold_ore","red_sand","sand","oak_log","spruce_log","dirt","grass_block","birch_log","glass","white_wool","chest","crafting_table","emerald_block","beacon","redstone","trapped_chest","sticky_piston","piston","hopper","comparator","diamond","emerald","iron_ingot","gold_ingot","lapis_lazuli","quartz","coal","bone","bone_meal","item_frame","cooked_beef","glowstone","obsidian","glowstone_dust","slime_ball","spruce_planks","spruce_sapling","oak_sapling","leather","sugar_cane","torch","blue_stained_glass","white_stained_glass","cocoa_beans","grass","oak_planks","jukebox","repeater");
            for(String item : defaultItems) {
                Item i = TransporterAddon.getInstance().getTransporterItemManager().getItemByType(item);
                if(i != null){
                    addActiveItem(i);
                }
            }
        }

        adjustSlots();
        saveActiveItems();
    }
  }


    @Subscribe
    public void onKeyPress(KeyEvent event){
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(onlyActiveInLobby && !TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return;
        if(openKey == null || openKey.equals(Key.NONE))
            return;
        if (InputAPI.getAPI().isKeyDown(openKey))
            this.open();
    }

    private void open() {
        if(Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened())
            return;
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new TransporterMenu());
    }

    public void swap(int slot, int slot2) {
        Item item = activeItems.entrySet().stream().filter(e -> e.getValue() == slot).map(Map.Entry::getKey).findFirst().orElse(null);
        Item item2 = activeItems.entrySet().stream().filter(e -> e.getValue() == slot2).map(Map.Entry::getKey).findFirst().orElse(null);
        if(item != null && item2 != null){
            activeItems.put(item, slot2);
            activeItems.put(item2, slot);
            saveActiveItems();
        }
    }

    /**
     * Adds an item to the active items list without saving it to the config,
     * and ensures that the item is not already in the list aswell as each slot only being used once.
     */
    private void internalAddActiveItem(Item item) {
        //write
        if(activeItems.containsKey(item))
            return;
        int slot = 0;
        while(activeItems.containsValue(slot)) {
            slot++;
        }
        activeItems.put(item, slot);
        adjustSlots();
    }

    private void adjustSlots() {
        List<Map.Entry<Item, Integer>> sortedEntries = new ArrayList<>(activeItems.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue()); //Sortere liste efter slot

        Map<Item, Integer> slotMap = new HashMap<>();

        int slot = 0;
        for (Map.Entry<Item, Integer> entry : sortedEntries) {
            while (slotMap.containsValue(slot)) {
                slot++; // Move to the next slot
            }
            slotMap.put(entry.getKey(), slot);
            slot++;
        }

        activeItems = slotMap;
    }

    private void saveActiveItems() {
        if(getDataManager() != null){
            for (Map.Entry<Item, Integer> entry : activeItems.entrySet()) {
                getDataManager().getSettings().getData().addProperty(entry.getKey().getModernType(), entry.getValue());
            }
            getDataManager().save();
        }
    }

    public void addActiveItem(Item item) {
        internalAddActiveItem(item);
        saveActiveItems();
    }

    public void removeActiveItem(Item item) {
        activeItems.remove(item);
        adjustSlots();
        if(getDataManager() != null){
            getDataManager().getSettings().getData().remove(item.getModernType());
            saveActiveItems();
        }
    }

    public boolean isActiveItem(Item item) {
        return activeItems.containsKey(item);
    }

    // Get the slot of an item
    public int getSlot(Item item) {
        return activeItems.getOrDefault(item, -1);
    }

    // Has slot
    public boolean hasSlot(int slot) {
        return activeItems.containsValue(slot);
    }

    /**
     * @return List of active items sorted by slot
     */
    public List<Item> getActiveItems() {
        return activeItems.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public int getWithdrawAmount() {
        return withdrawAmount;
    }
}
