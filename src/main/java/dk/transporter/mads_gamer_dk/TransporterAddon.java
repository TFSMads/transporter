package dk.transporter.mads_gamer_dk;


import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.Items.TransporterItems;
import dk.transporter.mads_gamer_dk.guis.LobbySelecterGui;
import dk.transporter.mads_gamer_dk.guis.TransporterGui;
import dk.transporter.mads_gamer_dk.listeners.JoinListener;
import dk.transporter.mads_gamer_dk.listeners.QuitListener;
import dk.transporter.mads_gamer_dk.listeners.messageReceiveListener;
import dk.transporter.mads_gamer_dk.messageSendingSettings.messageSettings;
import dk.transporter.mads_gamer_dk.modules.AutoTransporterModule;
import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.ingamegui.Module;
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

    public static boolean connectedToSuperawesome;

    private messageSettings MessageSettings;

    private Integer timer = 0;

    private boolean executeCommands;

    private boolean secondClickReset;

    private boolean autoTransporer;

    private int executeState;

    private int delay;

    private Integer keyBind;

    private Integer autoTransporterKeyBind;

    private Integer transporterMenuKeyBind;

    private Integer lobbySelecterKeybind;

    private Integer autoTransporterDelay;

    private Integer autoTransporterTimer = 0;

    public static TransporterAddon addon;

    private boolean isInSaLobby;

    public void setisInSaLobby(boolean isinlobby){
        this.isInSaLobby = isinlobby;
    }

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

    public String getServerString(Integer server){
        if(server == 1){ return server1; }else if(server == 2){  return server2; }else if(server == 3){ return server3; }else if(server == 4){ return server4; }else if(server == 5){ return server5; }else if(server == 6){ return server6; }else if(server == 7){ return server7; }
        return "larmelobby";
    }

    @Override
    public void onEnable() {
        items = new Items();
        addon = this;
        System.out.println("TransporterAddon Enabled!");
        this.getApi().registerForgeListener(this);

        this.getApi().getEventManager().register((MessageReceiveEvent)new messageReceiveListener());

        this.getApi().registerModule((Module)new AutoTransporterModule(this));
        this.getApi().getEventManager().registerOnJoin((Consumer) new JoinListener());
        this.getApi().getEventManager().registerOnQuit((Consumer) new QuitListener());
    }

    public static TransporterAddon getAddon(){
        return addon;
    }

    @Override
    public void loadConfig() {

        this.delay = getConfig().has( "delay" ) ? getConfig().get( "delay" ).getAsInt() : 45;

        this.autoTransporterDelay = getConfig().has( "autoTransporterDelay" ) ? getConfig().get( "autoTransporterDelay" ).getAsInt() : 30;

        this.secondClickReset = getConfig().has( "secondClickReset" ) ? getConfig().get( "secondClickReset" ).getAsBoolean() : false;

        this.autoTransporer = getConfig().has( "autoTransporer" ) ? getConfig().get( "autoTransporer" ).getAsBoolean() : false;



        this.MessageSettings = (this.getConfig().has("Beskeder") ? messageSettings.valueOf(this.getConfig().get("Beskeder").getAsString()) : messageSettings.getDefaultAction());

        updateMessageSettings();

        this.keyBind = getConfig().has( "keyBind" ) ? getConfig().get( "keyBind" ).getAsInt() : Keyboard.KEY_R;

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

        //TransporterItems items[] = TransporterItems.values();
        //for(TransporterItems item : items) {
        //   Boolean bool = getConfig().has( item.toString() ) ? getConfig().get( item.toString()  ).getAsBoolean() : true;
        //   System.out.println(bool);
        //    this.items.setItem(item.toString(),bool);
        // }
    }

    private Boolean getItemConfig(String item){
        return getConfig().has( item ) ? getConfig().get( item  ).getAsBoolean() : true;
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings ) {


        subSettings.add( new SliderElement( "Delay (Ticks)", this, new ControlElement.IconData( Material.WATCH ), "delay", this.delay ).setRange( 40, 100 ) );

        subSettings.add( new BooleanElement( "Second Click Reset", this, new ControlElement.IconData( "labymod/textures/settings/settings/afecdistancedetection.png" ), "secondClickReset", this.secondClickReset ) );

        KeyElement keyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), keyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );keyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );keyBind = accepted;configSave(); }});

        KeyElement AutoTransporterKeyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.WOOD_BUTTON ), autoTransporterKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );autoTransporterKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );autoTransporterKeyBind = accepted;configSave(); }});


        subSettings.add( keyElement );

        KeyElement transporterMenuKeyElement = new KeyElement( "Transporter Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), transporterMenuKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );transporterMenuKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );transporterMenuKeyBind = accepted;configSave(); }});

        subSettings.add( transporterMenuKeyElement );




        final DropDownMenu<messageSettings> uploadServiceDropDownMenu = (DropDownMenu<messageSettings>)new DropDownMenu("Beskeder", 0, 0, 0, 0).fill((Object[])messageSettings.values());final DropDownElement<messageSettings> uploadServiceDropDownElement = (DropDownElement<messageSettings>)new DropDownElement("Beskeder", (DropDownMenu)uploadServiceDropDownMenu);uploadServiceDropDownMenu.setSelected(this.MessageSettings);uploadServiceDropDownElement.setChangeListener(Message_Settings -> { this.MessageSettings = Message_Settings;this.getConfig().addProperty("Beskeder", Message_Settings.name());this.saveConfig();updateMessageSettings(); });

        subSettings.add((SettingsElement)uploadServiceDropDownElement);





        subSettings.add(new HeaderElement(ModColor.cl("a") + "Server Selecter"));

        KeyElement lobbySelecterKeyElement = new KeyElement( "Lobby Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), lobbySelecterKeybind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted < 0 ) { System.out.println( "Set new key to NONE" );lobbySelecterKeybind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );lobbySelecterKeybind = accepted;configSave(); }});

        subSettings.add( lobbySelecterKeyElement );


        StringElement serverElement1 = new StringElement( "Server 1." , new ControlElement.IconData( Material.PAPER ), server1, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server1 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement1 );


        StringElement serverElement2 = new StringElement( "Server 2." , new ControlElement.IconData( Material.PAPER ), server2, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server2 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement2 );


        StringElement serverElement3 = new StringElement( "Server 3." , new ControlElement.IconData( Material.PAPER ), server3, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server3 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement3 );


        StringElement serverElement4 = new StringElement( "Server 4." , new ControlElement.IconData( Material.PAPER ), server4, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server4 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement4 );


        StringElement serverElement5 = new StringElement( "Server 5." , new ControlElement.IconData( Material.PAPER ), server5, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server5 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement5 );


        StringElement serverElement6 = new StringElement( "Server 6." , new ControlElement.IconData( Material.PAPER ), server6, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server6 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement6 );


        StringElement serverElement7 = new StringElement( "Server 7." , new ControlElement.IconData( Material.PAPER ), server7, new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                server7 = accepted;
                serverConfigSave();
            }
        });

        subSettings.add( serverElement7 );



        subSettings.add(new HeaderElement(ModColor.cl("a") + "Auto Transporter"));

        subSettings.add( new BooleanElement( "Auto Transporter", this, new ControlElement.IconData( "labymod/textures/settings/settings/autotext.png" ), "autoTransporer", this.autoTransporer ) );

        subSettings.add( AutoTransporterKeyElement );

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

        subSettings.add( numberElement );

        subSettings.add(new HeaderElement(ModColor.cl("c") + "Items"));

        TransporterItems items[] = TransporterItems.values();
        for(TransporterItems item : items) {
            Boolean bool = getItemConfig(item.toString());
            ControlElement.IconData iconData = this.items.getIconData(item);
            String name = this.items.getName(item);
            subSettings.add( new BooleanElement( name, this, iconData, item.toString(), bool ));
        }



    }

    private void configSave(){
        getConfig().addProperty("keyBind", this.keyBind);
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


    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {

        if(!isInSaLobby){executeCommands = false; LabyMod.getInstance().displayMessageInChat(ModColor.cl("c") + "Din transporter cycle er blevet stoppet."); isInSaLobby = true; return;}

        if(!connectedToSuperawesome){ return; }

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
            timer++;
            if (timer >= delay) {
                timer = 0;
                TransporterItems items[] = TransporterItems.values();
                if(items[executeState] != null){
                    System.out.println(getItemConfig(items[executeState].toString()));
                    while (!getItemConfig(items[executeState].toString())){
                        System.out.println("WHILTE LOOP:" + getItemConfig(items[executeState].toString()) + " " + executeState);
                        executeState++;
                        if (executeState >= 35){
                            break;
                        }

                    }
                    if (executeState < 35){
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put " + items[executeState].toString());
                    }
                    executeState++;
                    while (!getItemConfig(items[executeState].toString())){
                        System.out.println("WHILTE LOOP:" + getItemConfig(items[executeState].toString()) + " " + executeState);
                        executeState++;
                        if (executeState >= 35){
                            break;
                        }

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
        if(!connectedToSuperawesome){ return; }
        //System.out.println(Keyboard.getEventKey() + keyBind);

        if(autoTransporterKeyBind != null) {
            if (Keyboard.isKeyDown(keyBind)) {
                if (executeCommands) {
                    if (!secondClickReset) {
                        LabyMod.getInstance().displayMessageInChat(ModColor.cl("c") + "Du er allerede igang med at putte items i din transporter!");
                        return;
                    }
                }
                timer = 0;
                executeCommands = true;
                executeState = 0;
            }
        }if(autoTransporterKeyBind != null) {
            if(Keyboard.isKeyDown(autoTransporterKeyBind)) {
                autoTransporer = !autoTransporer;
                configSave();
            }
        }if(transporterMenuKeyBind != null) {
            if (Keyboard.isKeyDown(transporterMenuKeyBind)) {
                Minecraft.getMinecraft().displayGuiScreen(new TransporterGui(addon, items));
            }

        }if(lobbySelecterKeybind != null){
           if(Keyboard.isKeyDown(lobbySelecterKeybind)){
                Minecraft.getMinecraft().displayGuiScreen(new LobbySelecterGui(addon));
           }
        }



    }

}


