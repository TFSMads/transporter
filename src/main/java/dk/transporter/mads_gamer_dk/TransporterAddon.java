package dk.transporter.mads_gamer_dk;


import com.sun.org.apache.xpath.internal.operations.Bool;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.Items.TransporterItems;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.CommandEvent;
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

        KeyElement keyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), keyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted == -1 ) { System.out.println( "Set new key to NONE" );keyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );keyBind = accepted;configSave(); }});

        KeyElement AutoTransporterKeyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.WOOD_BUTTON ), autoTransporterKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted == -1 ) { System.out.println( "Set new key to NONE" );autoTransporterKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );autoTransporterKeyBind = accepted;configSave(); }});

        keyElement.setConfigEntryName("keyBind");

        AutoTransporterKeyElement.setConfigEntryName("autoTransporterKeyBind");

        subSettings.add( keyElement );

        KeyElement transporterMenuKeyElement = new KeyElement( "Transporter Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), transporterMenuKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted == -1 ) { System.out.println( "Set new key to NONE" );keyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );keyBind = accepted;configSave(); }});

        transporterMenuKeyElement.setConfigEntryName("transporterMenuKeyElement");


        subSettings.add( transporterMenuKeyElement );

        final DropDownMenu<messageSettings> uploadServiceDropDownMenu = (DropDownMenu<messageSettings>)new DropDownMenu("Beskeder", 0, 0, 0, 0).fill((Object[])messageSettings.values());final DropDownElement<messageSettings> uploadServiceDropDownElement = (DropDownElement<messageSettings>)new DropDownElement("Beskeder", (DropDownMenu)uploadServiceDropDownMenu);uploadServiceDropDownMenu.setSelected(this.MessageSettings);uploadServiceDropDownElement.setChangeListener(Message_Settings -> { this.MessageSettings = Message_Settings;this.getConfig().addProperty("Beskeder", Message_Settings.name());this.saveConfig();updateMessageSettings(); });

        subSettings.add((SettingsElement)uploadServiceDropDownElement);

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
        getConfig().addProperty("autoTransporterKeyBind", this.autoTransporterKeyBind);
        getConfig().addProperty("transporterMenuKeyBind", this.transporterMenuKeyBind);
        getConfig().addProperty("autoTransporer", this.autoTransporer);
        getConfig().addProperty("autoTransporterDelay", this.autoTransporterDelay);
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
        if (keyBind == null || keyBind == -1) {
            return;
        }

        if (Keyboard.isKeyDown(keyBind)) {
            if(executeCommands) {
                if (!secondClickReset) {
                    LabyMod.getInstance().displayMessageInChat(ModColor.cl("c") + "Du er allerede igang med at putte items i din transporter!");
                    return;
                }
            }
            timer = 0;
            executeCommands = true;
            executeState = 0;
        }else if(Keyboard.isKeyDown(autoTransporterKeyBind)){
            autoTransporer = !autoTransporer;
            configSave();
        }else if(Keyboard.isKeyDown(transporterMenuKeyBind)){
            Minecraft.getMinecraft().displayGuiScreen(new TransporterGui(addon,items));
        }



    }

}


