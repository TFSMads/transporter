package dk.transporter.mads_gamer_dk;

import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.Items.TransporterItems;
import dk.transporter.mads_gamer_dk.guis.LobbySelecterGui;
import dk.transporter.mads_gamer_dk.guis.TransporterGui;
import dk.transporter.mads_gamer_dk.listeners.JoinListener;
import dk.transporter.mads_gamer_dk.listeners.OnCommand;
import dk.transporter.mads_gamer_dk.listeners.QuitListener;
import dk.transporter.mads_gamer_dk.listeners.messageReceiveListener;
import dk.transporter.mads_gamer_dk.messageSendingSettings.messageSettings;
import dk.transporter.mads_gamer_dk.modules.AutoTransporterModule;
import dk.transporter.mads_gamer_dk.modules.TransporterValueModule;
import dk.transporter.mads_gamer_dk.modules.items.*;
import dk.transporter.mads_gamer_dk.modules.values.*;
import dk.transporter.mads_gamer_dk.settingelements.DescribedBooleanElement;
import dk.transporter.mads_gamer_dk.utils.GetAmountOfItemInInventory;
import dk.transporter.mads_gamer_dk.utils.MessageHandler;
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

    public static boolean isEnabled;

    public static boolean isValidVersion;

    public static boolean connectedToSuperawesome;

    private messageSettings MessageSettings;

    private Integer timer = 0;

    private boolean executeCommands;

    private boolean autoTransporer;

    private int executeState;

    private int delay;

    private Integer autoTransporterKeyBind;

    private Integer transporterMenuKeyBind;

    private Integer lobbySelecterKeybind;

    private Integer disableTransporterKeyBind;

    private Integer autoTransporterDelay;

    private Integer autoTransporterTimer = 0;

    public static TransporterAddon addon;

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


    private MessageHandler messages;

    private Integer antalKrævet;

    private Boolean checkItems;


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



    @Override
    public void onEnable() {
        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERITEMS);
        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERITEMSVÆRDI);

        isValidVersion = false;
        isEnabled = false;
        items = new Items();
        addon = this;
        System.out.println("TransporterAddon Enabled!");
        this.getApi().registerForgeListener(this);

        this.getApi().getEventManager().register((MessageSendEvent)new OnCommand(this));

        this.getApi().getEventManager().register((MessageReceiveEvent)new messageReceiveListener(items));

        this.getApi().registerModule((Module)new AutoTransporterModule(this));
        this.getApi().getEventManager().registerOnJoin((Consumer) new JoinListener());
        this.getApi().getEventManager().registerOnQuit((Consumer) new QuitListener());

        messages = new MessageHandler();


        this.getApi().registerModule((Module)new TransporterValueModule(this));

        this.getApi().registerModule((Module)new SandModule(this));
        this.getApi().registerModule((Module)new RedSandModule(this));
        this.getApi().registerModule((Module)new StoneModule(this));
        this.getApi().registerModule((Module)new CobblestoneModule(this));
        this.getApi().registerModule((Module)new StonebrickModule(this));
        this.getApi().registerModule((Module)new DirtModule(this));
        this.getApi().registerModule((Module)new GrassModule(this));
        this.getApi().registerModule((Module)new CharcoalModule(this));
        this.getApi().registerModule((Module)new CoalModule(this));
        this.getApi().registerModule((Module)new IronoreModule(this));
        this.getApi().registerModule((Module)new GoldoreModule(this));
        this.getApi().registerModule((Module)new IroningotModule(this));
        this.getApi().registerModule((Module)new GoldingotModule(this));
        this.getApi().registerModule((Module)new BoneModule(this));
        this.getApi().registerModule((Module)new GlowstonedustModule(this));
        this.getApi().registerModule((Module)new GlowstoneModule(this));
        this.getApi().registerModule((Module)new LapislazuliModule(this));
        this.getApi().registerModule((Module)new QuartzModule(this));
        this.getApi().registerModule((Module)new RedstoneModule(this));
        this.getApi().registerModule((Module)new DiamondModule(this));
        this.getApi().registerModule((Module)new ObsidianModule(this));
        this.getApi().registerModule((Module)new BlazerodModule(this));
        this.getApi().registerModule((Module)new EnderpearlModule(this));
        this.getApi().registerModule((Module)new BookModule(this));
        this.getApi().registerModule((Module)new SugarcaneModule(this));
        this.getApi().registerModule((Module)new LeatherModule(this));
        this.getApi().registerModule((Module)new SprucelogModule(this));
        this.getApi().registerModule((Module)new OaklogModule(this));
        this.getApi().registerModule((Module)new BirchlogModule(this));
        this.getApi().registerModule((Module)new JunglelogModule(this));
        this.getApi().registerModule((Module)new SlimeballModule(this));
        this.getApi().registerModule((Module)new GlassModule(this));
        this.getApi().registerModule((Module)new ChestModule(this));
        this.getApi().registerModule((Module)new TrappedchestModule(this));
        this.getApi().registerModule((Module)new HopperModule(this));


        this.getApi().registerModule((Module)new SandValueModule(this));
        this.getApi().registerModule((Module)new RedSandValueModule(this));
        this.getApi().registerModule((Module)new StoneValueModule(this));
        this.getApi().registerModule((Module)new CobblestoneValueModule(this));
        this.getApi().registerModule((Module)new StonebrickValueModule(this));
        this.getApi().registerModule((Module)new DirtValueModule(this));
        this.getApi().registerModule((Module)new GrassValueModule(this));
        this.getApi().registerModule((Module)new CharcoalValueModule(this));
        this.getApi().registerModule((Module)new CoalValueModule(this));
        this.getApi().registerModule((Module)new IronoreValueModule(this));
        this.getApi().registerModule((Module)new GoldoreValueModule(this));
        this.getApi().registerModule((Module)new IroningotValueModule(this));
        this.getApi().registerModule((Module)new GoldingotValueModule(this));
        this.getApi().registerModule((Module)new BoneValueModule(this));
        this.getApi().registerModule((Module)new GlowstonedustValueModule(this));
        this.getApi().registerModule((Module)new GlowstoneValueModule(this));
        this.getApi().registerModule((Module)new LapislazuliValueModule(this));
        this.getApi().registerModule((Module)new QuartzValueModule(this));
        this.getApi().registerModule((Module)new RedstoneValueModule(this));
        this.getApi().registerModule((Module)new DiamondValueModule(this));
        this.getApi().registerModule((Module)new ObsidianValueModule(this));
        this.getApi().registerModule((Module)new BlazerodValueModule(this));
        this.getApi().registerModule((Module)new EnderpearlValueModule(this));
        this.getApi().registerModule((Module)new BookValueModule(this));
        this.getApi().registerModule((Module)new SugarcaneValueModule(this));
        this.getApi().registerModule((Module)new LeatherValueModule(this));
        this.getApi().registerModule((Module)new SprucelogValueModule(this));
        this.getApi().registerModule((Module)new OaklogValueModule(this));
        this.getApi().registerModule((Module)new BirchlogValueModule(this));
        this.getApi().registerModule((Module)new JunglelogValueModule(this));
        this.getApi().registerModule((Module)new SlimeballValueModule(this));
        this.getApi().registerModule((Module)new GlassValueModule(this));
        this.getApi().registerModule((Module)new ChestValueModule(this));
        this.getApi().registerModule((Module)new TrappedchestValueModule(this));
        this.getApi().registerModule((Module)new HopperValueModule(this));

    }

    public static TransporterAddon getAddon(){
        return addon;
    }

    @Override
    public void loadConfig() {

        this.delay = getConfig().has( "delay" ) ? getConfig().get( "delay" ).getAsInt() : 45;

        this.autoTransporterDelay = getConfig().has( "autoTransporterDelay" ) ? getConfig().get( "autoTransporterDelay" ).getAsInt() : 30;

        this.autoTransporer = getConfig().has( "autoTransporer" ) ? getConfig().get( "autoTransporer" ).getAsBoolean() : false;



        this.MessageSettings = (this.getConfig().has("Beskeder") ? messageSettings.valueOf(this.getConfig().get("Beskeder").getAsString()) : messageSettings.getDefaultAction());

        updateMessageSettings();

        this.transporterMenuKeyBind = getConfig().has( "transporterMenuKeyBind" ) ? getConfig().get( "transporterMenuKeyBind" ).getAsInt() : Keyboard.KEY_L;

        this.autoTransporterKeyBind = getConfig().has( "autoTransporterKeyBind" ) ? getConfig().get( "autoTransporterKeyBind" ).getAsInt() : Keyboard.KEY_P;

        this.lobbySelecterKeybind = getConfig().has( "lobbySelecterKeybind" ) ? getConfig().get( "lobbySelecterKeybind" ).getAsInt() : Keyboard.KEY_Y;

        this.disableTransporterKeyBind = getConfig().has( "disableAutoTransporterKeyBind" ) ? getConfig().get( "disableAutoTransporterKeyBind" ).getAsInt() : Keyboard.KEY_J;

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


        subSettings.add( new SliderElement( "Delay (Ticks)", this, new ControlElement.IconData( Material.WATCH ), "delay", this.delay ).setRange( 40, 100 ) );

        KeyElement keyElement = new KeyElement( "Slå Transporter Fra", new ControlElement.IconData( Material.STONE_BUTTON ), disableTransporterKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );disableTransporterKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );disableTransporterKeyBind = accepted;configSave(); }});

        KeyElement AutoTransporterKeyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.WOOD_BUTTON ), autoTransporterKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );autoTransporterKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );autoTransporterKeyBind = accepted;configSave(); }});


        subSettings.add( keyElement );

        KeyElement transporterMenuKeyElement = new KeyElement( "Transporter Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), transporterMenuKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );transporterMenuKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );transporterMenuKeyBind = accepted;configSave(); }});

        subSettings.add( transporterMenuKeyElement );


        subSettings.add(new HeaderElement(ModColor.cl("a") + " "));



        ListContainerElement listMessages = new ListContainerElement(ModColor.cl("7") + "Beskeder", new ControlElement.IconData(Material.PAPER));


        final DropDownMenu<messageSettings> uploadServiceDropDownMenu = (DropDownMenu<messageSettings>)new DropDownMenu("Beskeder", 0, 0, 0, 0).fill((Object[])messageSettings.values());final DropDownElement<messageSettings> uploadServiceDropDownElement = (DropDownElement<messageSettings>)new DropDownElement("Beskeder", (DropDownMenu)uploadServiceDropDownMenu);uploadServiceDropDownMenu.setSelected(this.MessageSettings);uploadServiceDropDownElement.setChangeListener(Message_Settings -> { this.MessageSettings = Message_Settings;this.getConfig().addProperty("Beskeder", Message_Settings.name());this.saveConfig();updateMessageSettings(); });

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

        listAutoTransporter.getSubSettings().add( new BooleanElement( "Auto Transporter", this, new ControlElement.IconData( "labymod/textures/settings/settings/autotext.png" ), "autoTransporer", this.autoTransporer ) );

        listAutoTransporter.getSubSettings().add( AutoTransporterKeyElement );

        NumberElement numberElement = new NumberElement( "Delay (Sekunder)",
                new ControlElement.IconData( Material.WATCH ) , (autoTransporterDelay/20)  );

        numberElement.addCallback( new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                System.out.println( "New number: " + accepted );
                autoTransporterDelay = accepted * 20;
                configSave();
            }
        } );

        listAutoTransporter.getSubSettings().add( numberElement );



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
        getConfig().addProperty("autoTransporer", this.autoTransporer);
        getConfig().addProperty("autoTransporterDelay", this.autoTransporterDelay);
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
    public void onTick(final TickEvent.ClientTickEvent event) {
        saveTimer++;
        if(saveTimer >= 1000){
            saveTimer = 0;
            for(Item i : items.getAllItems()){

                getConfig().addProperty(i.getItem().toString()+"-Amount", i.getAmount());

            }
            saveConfig();
        }

        //System.out.println("Current Time: " + System.currentTimeMillis());
        if(!isInSaLobby){executeCommands = false; LabyMod.getInstance().displayMessageInChat(ModColor.cl("c") + "Din transporter cycle er blevet stoppet."); isInSaLobby = true; return;}

        if(!connectedToSuperawesome){ return; }
        if(!isEnabled){ return; }
        if(!isValidVersion){ return; }

        if(autoTransporer){
            autoTransporterTimer++;
            if(autoTransporterTimer > autoTransporterDelay){
                if(!executeCommands) {
                    executeCommands = true;
                    timer = 0;
                    executeState = 0;
                    autoTransporterTimer = 0;
                }
            }
        }

        if (executeCommands) {
            if (executeState >= 35) {
                executeCommands = false;
            }
            timer++;
            //System.out.println("TIMER: " + timer);
            if (timer >= delay) {
                timer = 0;
                TransporterItems items[] = TransporterItems.values();
                if(items[executeState] != null){
                    //System.out.println(getItemConfig(items[executeState].toString()));
                    while (!getItemConfig(items[executeState].toString()) || GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory, this.items.getItemByID(executeState).getItemDamage(), this.items.getItemByID(executeState).getInventoryItem()) < this.items.getItemByID(executeState).getAntalKrævet()){
                        //System.out.println("WHILTE LOOP:" + getItemConfig(items[executeState].toString()) + " " + executeState);
                        executeState++;
                        if (executeState >= 35){
                            break;
                        }

                    }
                    //Integer itemAmount = GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory,0, Item.getItemById(12));
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
                            //System.out.println("WHILTE LOOP2:" + getItemConfig(items[executeState].toString()) + " " + executeState);
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
            //System.out.println("VAlID VERSION: " + isValidVersion + " ENABLED: " + isEnabled + " Connected to SuperAwesome: " + connectedToSuperawesome);
            if (!connectedToSuperawesome) {
                return;
            }
            if (!isEnabled) {
                return;
            }
            if(!isValidVersion){
                return;
            }
            //System.out.println(Keyboard.getEventKey() + keyBind);


            if (autoTransporterKeyBind >= 0) {
                if (Keyboard.isKeyDown(autoTransporterKeyBind)) {
                    autoTransporer = !autoTransporer;
                    getConfig().addProperty("autoTransporer", this.autoTransporer);
                }
            }
            if (transporterMenuKeyBind >= 0) {
                if (Keyboard.isKeyDown(transporterMenuKeyBind)) {
                    Minecraft.getMinecraft().displayGuiScreen(new TransporterGui(addon, items));
                }

            }
            if (lobbySelecterKeybind >= 0) {
                if (Keyboard.isKeyDown(lobbySelecterKeybind)) {
                    Minecraft.getMinecraft().displayGuiScreen(new LobbySelecterGui(addon));
                }
            }
            if (disableTransporterKeyBind >= 0) {
                if (Keyboard.isKeyDown(disableTransporterKeyBind)) {
                    if (autoTransporer) {
                        executeCommands = false;
                        autoTransporer = false;
                        getConfig().addProperty("autoTransporer", this.autoTransporer);
                        LabyMod.getInstance().displayMessageInChat(ModColor.cl("c") + "Din transporter cycle er blevet stoppet.");
                    }
                }
            }

            //TEST KEY
            //if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)){
            //    Integer itemAmount = GetAmountOfItemInInventory.getAmountOfItem(Minecraft.getMinecraft().thePlayer.inventory,0, Item.getItemById(12));
            //    System.out.println("Du har " + itemAmount + " sand!");
            //}


        } catch (Exception e) {
            System.out.println(e);
        }

    }
}


