package dk.transporter.mads_gamer_dk;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.Items.TransporterItems;
import dk.transporter.mads_gamer_dk.classes.MiningRigTimers;
import dk.transporter.mads_gamer_dk.guis.serverSelector.ServerSelecterGui;
import dk.transporter.mads_gamer_dk.guis.TransporterGui;
import dk.transporter.mads_gamer_dk.listeners.*;
import dk.transporter.mads_gamer_dk.mcmmo.Skills;
import dk.transporter.mads_gamer_dk.messageSendingSettings.messageSettings;
import dk.transporter.mads_gamer_dk.modules.AutoTransporterModule;
import dk.transporter.mads_gamer_dk.modules.RegisterModules;
import dk.transporter.mads_gamer_dk.settingelements.DescribedBooleanElement;
import dk.transporter.mads_gamer_dk.utils.GetAmountOfItemInInventory;
import dk.transporter.mads_gamer_dk.utils.MessageHandler;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.ingamegui.Module;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;


public class TransporterAddon  extends LabyModAddon {

    public static final ModuleCategory CATEGORY_TRANSPORTERITEMS = new ModuleCategory("Transporter Items", true, new ControlElement.IconData("transporter/textures/icons/Items.png"));

    public static final ModuleCategory CATEGORY_TRANSPORTERITEMSVÆRDI = new ModuleCategory("Transporter Værdier", true, new ControlElement.IconData(Material.EMERALD));

    public static final ModuleCategory CATEGORY_TRANSPORTERADDON = new ModuleCategory("Transporter Addon", true, new ControlElement.IconData(Material.SIGN));

    public static final ModuleCategory CATEGORY_MCMMO = new ModuleCategory("Mcmmo", true, new ControlElement.IconData(Material.DIAMOND_SPADE));

    private boolean isEnabled;

    private boolean isValidVersion;

    public static boolean connectedToSuperawesome;

    private MiningRigTimers timers;

    private messageSettings MessageSettings;

    private Integer timer = 0;

    private boolean executeCommands;

    private boolean autoTransporter;

    private int executeState;

    private int delay;

    private Integer autoTransporterKeyBind;

    private Integer transporterMenuKeyBind;

    private Integer lobbySelecterKeybind;

    private TransporterAddon addon;

    private boolean isInSaLobby;


    public Integer getTimer(){
        return this.timer;
    }

    public void setTimer(Integer timer){
        this.timer = timer;
    }

    public Items items;

    private String server1;
    private String server2;
    private String server3;
    private String server4;
    private String server5;
    private String server6;
    private String server7;

    private DataManagers dataManagers;

    private MessageHandler messages;

    private Integer antalKrævet;

    private Boolean checkItems;

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    private String currentServer;




    public boolean getAutoTransporter() {
        return autoTransporter;
    }

    public void setAutoTransporter(boolean autoTransporter) {
        this.autoTransporter = autoTransporter;
    }



    public MessageHandler getMessages(){
        return this.messages;
    }

    public String getServerString(Integer server){
        if(server == 1){ return server1; }else if(server == 2){  return server2; }else if(server == 3){ return server3; }else if(server == 4){ return server4; }else if(server == 5){ return server5; }else if(server == 6){ return server6; }else if(server == 7){ return server7; }
        return "larmelobby";
    }

    public Boolean getCheckItems(){
        return this.checkItems;
    }
    public Items getItems(){
        return this.items;
    }
    public MiningRigTimers getTimers() { return timers; }



    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled,JoinListener listener) {
        if(listener == this.joinListener){
            isEnabled = enabled;
        }
    }

    public boolean isValidVersion() {
        return isValidVersion;
    }

    public void setValidVersion(boolean validVersion,JoinListener listener) {
        if(listener == this.joinListener) {
            isValidVersion = validVersion;
        }
    }


    private JoinListener joinListener;

    public Skills getSkills() {
        return skills;
    }

    private Skills skills;

    @Override
    public void onEnable() {

        dataManagers = new DataManagers();

        skills = new Skills(this);


        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERITEMS);
        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERITEMSVÆRDI);
        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERADDON);
        ModuleCategoryRegistry.loadCategory(CATEGORY_MCMMO);

        isValidVersion = false;
        isEnabled = false;
        items = new Items();
        addon = this;
        System.out.println("TransporterAddon Enabled!");
        this.getApi().registerForgeListener(this);

        this.getApi().getEventManager().register((MessageSendEvent)new OnCommand(this, skills));

        this.getApi().getEventManager().register((MessageReceiveEvent)new messageReceiveListener(items, addon,skills));

        this.getApi().registerModule((Module)new AutoTransporterModule(this));

        this.joinListener = new JoinListener(this);

        this.getApi().getEventManager().register(new PluginMessageListener());
        this.getApi().getEventManager().registerOnJoin((Consumer<net.labymod.utils.ServerData>) this.joinListener);
        this.getApi().getEventManager().registerOnQuit((Consumer<net.labymod.utils.ServerData>) new QuitListener());

        messages = new MessageHandler();
        timers = new MiningRigTimers();

        RegisterModules.registerAllModules(this);


    }

    @Override
    public void loadConfig() {

        this.delay = getConfig().has( "delay" ) ? getConfig().get( "delay" ).getAsInt() : 45;

        this.MessageSettings = (this.getConfig().has("Beskeder") ? messageSettings.valueOf(this.getConfig().get("Beskeder").getAsString()) : messageSettings.getDefaultAction());

        updateMessageSettings();

        this.transporterMenuKeyBind = getConfig().has( "transporterMenuKeyBind" ) ? getConfig().get( "transporterMenuKeyBind" ).getAsInt() : Keyboard.KEY_L;

        this.autoTransporterKeyBind = getConfig().has( "autoTransporterKeyBind" ) ? getConfig().get( "autoTransporterKeyBind" ).getAsInt() : Keyboard.KEY_P;

        this.lobbySelecterKeybind = getConfig().has( "lobbySelecterKeybind" ) ? getConfig().get( "lobbySelecterKeybind" ).getAsInt() : Keyboard.KEY_Y;

        this.server1 = getConfig().has( "server1" ) ? getConfig().get( "server1" ).getAsString() : "larmelobby";
        this.server2 = getConfig().has( "server2" ) ? getConfig().get( "server2" ).getAsString() : "shoppylobby";
        this.server3 = getConfig().has( "server3" ) ? getConfig().get( "server3" ).getAsString() : "byggelobby";
        this.server4 = getConfig().has( "server4" ) ? getConfig().get( "server4" ).getAsString() : "maskinrummet";
        this.server5 = getConfig().has( "server5" ) ? getConfig().get( "server5" ).getAsString() : "maskinrummetlight";
        this.server6 = getConfig().has( "server6" ) ? getConfig().get( "server6" ).getAsString() : "creepylobby";
        this.server7 = getConfig().has( "server7" ) ? getConfig().get( "server7" ).getAsString() : "limbo";

        this.messages.setMessageById(0, getConfig().has( "putMessage" ) ? getConfig().get( "putMessage" ).getAsString() : "&bGemmer &3%antal% %item% &bi din transporter. &7(&3%total%&7)");
        this.messages.setMessageById(1, getConfig().has( "getMessage" ) ? getConfig().get( "getMessage" ).getAsString() : "&bTager &3%antal% %item% &bfra din transporter. &7(&3%total%&7)");
        this.messages.setMessageById(2, getConfig().has( "putManglerMessage" ) ? getConfig().get( "putManglerMessage" ).getAsString() : "&bDu har ikke noget &3%item%");
        this.messages.setMessageById(3, getConfig().has( "getManglerMessage" ) ? getConfig().get( "getManglerMessage" ).getAsString() : "&bDu har ikke noget &3%item% &bi din transporter.");
        this.messages.setMessageById(4, getConfig().has( "delayMessage" ) ? getConfig().get( "delayMessage" ).getAsString() : "&cDer er 2 sekunders cooldown på transporteren.");

        this.antalKrævet = getConfig().has( "antalKrævet" ) ? getConfig().get( "antalKrævet" ).getAsInt() : 1;
        this.checkItems = getConfig().has( "checkItems" ) ? getConfig().get( "checkItems" ).getAsBoolean() : true;



        TransporterItems items[] = TransporterItems.values();
        for(TransporterItems item : items) {
            this.items.getItemByID(this.items.getId(item)).setAntalKrævet(getConfig().has( item.toString() + "-Required" ) ? getConfig().get( item.toString() + "-Required").getAsInt() : 1);
            this.items.getItemByID(this.items.getId(item)).setValue(getConfig().has( item.toString() + "-Value" ) ? getConfig().get( item.toString() + "-Value").getAsInt() : this.items.getItemByID(this.items.getId(item)).getValue());
            this.items.getItemByID(this.items.getId(item)).setAmount(getConfig().has( item.toString() + "-Amount" ) ? getConfig().get( item.toString() + "-Amount").getAsInt() : this.items.getItemByID(this.items.getId(item)).getAmount());
        }
    }

    private Boolean getItemConfig(String item){
        return getConfig().has( item ) ? getConfig().get( item  ).getAsBoolean() : true;
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings ) {

        KeyElement AutoTransporterKeyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.WOOD_BUTTON ), autoTransporterKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );autoTransporterKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );autoTransporterKeyBind = accepted;configSave(); }});

        KeyElement transporterMenuKeyElement = new KeyElement( "Transporter Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), transporterMenuKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );transporterMenuKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );transporterMenuKeyBind = accepted;configSave(); }});

        subSettings.add( transporterMenuKeyElement );


        subSettings.add(new HeaderElement(ModColor.cl("a") + " "));



        ListContainerElement listMessages = new ListContainerElement(ModColor.cl("7") + "Beskeder", new ControlElement.IconData(Material.PAPER));


        final DropDownMenu<messageSettings> uploadServiceDropDownMenu = (DropDownMenu<messageSettings>)new DropDownMenu("Beskeder", 0, 0, 0, 0).fill((Object[])messageSettings.values());final DropDownElement<messageSettings> uploadServiceDropDownElement = new DropDownElement<messageSettings>("Beskeder", (DropDownMenu<messageSettings>)uploadServiceDropDownMenu);uploadServiceDropDownMenu.setSelected(this.MessageSettings);uploadServiceDropDownElement.setChangeListener(Message_Settings -> { this.MessageSettings = Message_Settings;this.getConfig().addProperty("Beskeder", Message_Settings.name());this.saveConfig();updateMessageSettings(); });

        listMessages.getSubSettings().add((SettingsElement)uploadServiceDropDownElement);


        StringElement customChat1 = new StringElement( "Transporter put besked" , new ControlElement.IconData( Material.PAPER ), this.messages.getMessageById(0), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.messages.setMessageById(0,accepted);
                getConfig().addProperty("putMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat1 );

        StringElement customChat2 = new StringElement( "Transporter get besked" , new ControlElement.IconData( Material.PAPER ), this.messages.getMessageById(1), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.messages.setMessageById(1,accepted);
                getConfig().addProperty("getMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat2 );

        StringElement customChat3 = new StringElement( "Put mangler besked" , new ControlElement.IconData( Material.PAPER ), this.messages.getMessageById(2), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.messages.setMessageById(2,accepted);
                getConfig().addProperty("putManglerMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat3 );

        StringElement customChat4= new StringElement( "Get mangler besked" , new ControlElement.IconData( Material.PAPER ), this.messages.getMessageById(3), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.messages.setMessageById(3,accepted);
                getConfig().addProperty("getManglerMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat4 );

        StringElement customChat5 = new StringElement( "Delay besked" , new ControlElement.IconData( Material.PAPER ), this.messages.getMessageById(4), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.messages.setMessageById(4,accepted);
                getConfig().addProperty("delayMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat5 );


        ListContainerElement listServerSelector = new ListContainerElement(ModColor.cl("2") + "Server Selector", new ControlElement.IconData(Material.COMPASS));


        listServerSelector.getSubSettings().add(new HeaderElement(ModColor.cl("2") + "Server Selecter"));

        KeyElement lobbySelecterKeyElement = new KeyElement( "Server Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), lobbySelecterKeybind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );lobbySelecterKeybind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );lobbySelecterKeybind = accepted;configSave(); }});

        listServerSelector.getSubSettings().add( lobbySelecterKeyElement );


        StringElement serverElement1 = new StringElement( "Server 1." , new ControlElement.IconData( Material.PAPER ), server1, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server1 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement1 );


        StringElement serverElement2 = new StringElement( "Server 2." , new ControlElement.IconData( Material.PAPER ), server2, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server2 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement2 );


        StringElement serverElement3 = new StringElement( "Server 3." , new ControlElement.IconData( Material.PAPER ), server3, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server3 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement3 );


        StringElement serverElement4 = new StringElement( "Server 4." , new ControlElement.IconData( Material.PAPER ), server4, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server4 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement4 );


        StringElement serverElement5 = new StringElement( "Server 5." , new ControlElement.IconData( Material.PAPER ), server5, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server5 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement5 );


        StringElement serverElement6 = new StringElement( "Server 6." , new ControlElement.IconData( Material.PAPER ), server6, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server6 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement6 );


        StringElement serverElement7 = new StringElement( "Server 7." , new ControlElement.IconData( Material.PAPER ), server7, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server7 = accepted;
                serverConfigSave();
            }
        });

        listServerSelector.getSubSettings().add( serverElement7 );



        ListContainerElement listAutoTransporter = new ListContainerElement(ModColor.cl("a") + "Auto Transporter", new ControlElement.IconData(Material.REDSTONE_COMPARATOR));


        listAutoTransporter.getSubSettings().add(new HeaderElement(ModColor.cl("a") + "Auto Transporter"));


        listAutoTransporter.getSubSettings().add(new SliderElement( "Delay (Ticks)", this, new ControlElement.IconData( Material.WATCH ), "delay", this.delay ).setRange( 40, 100 ) );


        listAutoTransporter.getSubSettings().add( AutoTransporterKeyElement );


        ListContainerElement listItems = new ListContainerElement(ModColor.cl("f") + "Items", new ControlElement.IconData(Material.IRON_INGOT));

        listItems.getSubSettings().add(new HeaderElement(ModColor.cl("a") + ModColor.cl("l") + "ITEMS"));
        listItems.getSubSettings().add(new HeaderElement(ModColor.cl("f") + "Vælg de items du vil putte i din transporter."));

        TransporterItems items[] = TransporterItems.values();








        listItems.getSubSettings().add( new BooleanElement( "Tjek efter items.", this, new ControlElement.IconData( Material.DAYLIGHT_DETECTOR ), "checkItems", this.checkItems ) );

        listItems.getSubSettings().add( new SliderElement( "Antal Krævet.", this, new ControlElement.IconData( Material.DETECTOR_RAIL ), "requiredAll", this.antalKrævet ).setRange( 0, 64 ).addCallback(new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                getConfig().addProperty("antalKrævet", accepted);
                for(TransporterItems item : items) {
                    addon.antalKrævet = accepted;
                    getConfig().addProperty(item.toString() + "-Required", accepted);
                    addon.items.getItemByID(addon.items.getId(item)).setAntalKrævet(accepted);
                }
            }
        } ));

        listItems.getSubSettings().add(new HeaderElement(ModColor.cl("a") + " "));


        for(TransporterItems item : items) {
            Boolean bool = getItemConfig(item.toString());
            ControlElement.IconData iconData = this.items.getIconData(item);
            String name = this.items.getName(item);
            Integer antalKrævet = this.items.getItemByID(this.items.getId(item)).getAntalKrævet();

            DescribedBooleanElement itemElement = new DescribedBooleanElement(name, this, iconData, item.toString(), bool, "Slå denne til for at den putter " + name + " i din transporter.");
            itemElement.getSubSettings().add( new SliderElement( "Antal " + name + " krævet", this, new ControlElement.IconData( Material.DETECTOR_RAIL ), item.toString() + "-Required", antalKrævet).setRange( 0, 64 ).addCallback(new Consumer<Integer>() {
                @Override
                public void accept( Integer accepted ) {
                    addon.items.getItemByID(addon.items.getId(item)).setAntalKrævet(accepted);
                }
            } ));

            NumberElement valueElement = new NumberElement( "Værdi (Ems)",
                    new ControlElement.IconData( Material.EMERALD ) , addon.items.getItemByID(addon.items.getId(item)).getValue() );

            valueElement.addCallback( new Consumer<Integer>() {
                @Override
                public void accept( Integer accepted ) {
                    addon.items.getItemByID(addon.items.getId(item)).setValue(accepted);
                    getConfig().addProperty(item.toString() + "-Value", accepted);
                    System.out.println(accepted);
                }
            } );
            itemElement.getSubSettings().add(valueElement);



            listItems.getSubSettings().add(itemElement);

        }

        subSettings.add(listMessages);
        subSettings.add(listServerSelector);
        subSettings.add(listAutoTransporter);
        subSettings.add(listItems);



    }

    private void configSave(){
        getConfig().addProperty("lobbySelecterKeybind", this.lobbySelecterKeybind);
        getConfig().addProperty("autoTransporterKeyBind", this.autoTransporterKeyBind);
        getConfig().addProperty("transporterMenuKeyBind", this.transporterMenuKeyBind);
    }


    private void serverConfigSave(){
        getConfig().addProperty("server1", this.server1);
        getConfig().addProperty("server2", this.server2);
        getConfig().addProperty("server3", this.server3);
        getConfig().addProperty("server4", this.server4);
        getConfig().addProperty("server5", this.server5);
        getConfig().addProperty("server6", this.server6);
        getConfig().addProperty("server7", this.server7);
    }

    private void updateMessageSettings(){
        System.out.println("[Debug] " + this.MessageSettings);
        if(this.MessageSettings == messageSettings.ACTIONBAR){
            messageReceiveListener.message = 2;
        }else if(this.MessageSettings == messageSettings.INGEN){
            messageReceiveListener.message = 1;
        }else if(this.MessageSettings == messageSettings.CHAT){
            messageReceiveListener.message = 0;
        }
    }

    private Integer saveTimer=0;

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) throws Exception {
        saveTimer++;
        if(saveTimer >= 1000){
            saveTimer = 0;
            for(Item i : items.getAllItems()){

                getConfig().addProperty(i.getItem().toString()+"-Amount", i.getAmount());

            }
            saveConfig();

        }
        if(!isInSaLobby){executeCommands = false; LabyMod.getInstance().displayMessageInChat(ModColor.cl("c") + "Din transporter cycle er blevet stoppet."); isInSaLobby = true; return;}

        if(!connectedToSuperawesome){ return; }
        if(!isEnabled){ return; }
        if(!isValidVersion){ return; }

        if(autoTransporter){
            if(!executeCommands) {
                executeCommands = true;
                timer = 0;
                executeState = 0;
            }
        }

        if (executeCommands) {
            if (executeState >= 35) {
                executeCommands = false;
            }
            timer++;
            if (timer >= delay) {
                timer = 0;
                TransporterItems items[] = TransporterItems.values();
                if(items[executeState] != null){
                    while (!getItemConfig(items[executeState].toString()) || GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory, this.items.getItemByID(executeState).getItemDamage(), this.items.getItemByID(executeState).getInventoryItem(), addon) < this.items.getItemByID(executeState).getAntalKrævet()){
                        executeState++;
                        if (executeState >= 35){
                            break;
                        }

                    }
                    Integer temp = -1;
                    if (executeState <= 34){
                        temp = executeState;
                        if(executeState == 1) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put Sand:1"); }else{
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put " + items[executeState].toString());
                        }

                    }
                    if(executeState < 35) {
                        while (!getItemConfig(items[executeState].toString())) {
                            executeState++;
                            if (executeState >= 35) {
                                break;
                            }
                        }
                    }else{
                        executeCommands = false;
                    }
                    if(temp == executeState){
                        executeState++;
                    }
                }else{
                    executeState++;
                }


                if (executeState >= 35) {
                    executeCommands = false;
                }
            }
        }
    }


    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        try {
            if (!connectedToSuperawesome) {
                return;
            }
            if (!isEnabled) {
                return;
            }
            if(!isValidVersion){
                return;
            }


            if (autoTransporterKeyBind >= 0) {
                if (Keyboard.isKeyDown(autoTransporterKeyBind)) {
                    if (autoTransporter) {
                        executeCommands = false;
                        autoTransporter = false;
                    }else{
                        autoTransporter = true;
                    }
                }
            }
            if (transporterMenuKeyBind >= 0) {
                if (Keyboard.isKeyDown(transporterMenuKeyBind)) {
                    Minecraft.getMinecraft().displayGuiScreen(new TransporterGui(addon, items));
                }

            }
            if (lobbySelecterKeybind >= 0) {
                if (Keyboard.isKeyDown(lobbySelecterKeybind)) {
                    System.out.println("LOBBY");
                    Minecraft.getMinecraft().displayGuiScreen(new ServerSelecterGui(addon, dataManagers));
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
                skills.checkScoreboard();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public DataManagers getDataManagers() {
        return dataManagers;
    }
}


