package dk.transporter.mads_gamer_dk;


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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.lwjgl.input.Keyboard;

import java.util.List;

public class TransporterAddon  extends LabyModAddon {

    public static boolean connectedToSuperawesome;

    public static messageSettings MessageSettings;

    private Integer timer = 0;

    private boolean executeCommands;

    private boolean secondClickReset;

    private boolean autoTransporer;

    private int executeState;

    private int delay;

    private boolean sand;
    private boolean sand1;
    private boolean stone;
    private boolean cobblestone;
    private boolean stonebrick;
    private boolean dirt;
    private boolean grass;
    private boolean charcoal;
    private boolean coal;
    private boolean ironore;
    private boolean goldore;
    private boolean ironingot;
    private boolean goldingot;
    private boolean bone;
    private boolean glowstonedust;
    private boolean glowstone;
    private boolean lapislazuli;
    private boolean quartz;
    private boolean redstone;
    private boolean diamond;
    private boolean obsidian;
    private boolean blazerod;
    private boolean enderpearl;
    private boolean book;
    private boolean sugarcane;
    private boolean leather;
    private boolean sprucelog;
    private boolean oaklog;
    private boolean birchlog;
    private boolean junglelog;
    private boolean slimeball;
    private boolean glass;
    private boolean chest;
    private boolean trappedchest;
    private boolean hopper;


    private Integer keyBind;

    private Integer autoTransporterKeyBind;

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

    @Override
    public void onEnable() {
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

        this.autoTransporterKeyBind = getConfig().has( "autoTransporterKeyBind" ) ? getConfig().get( "autoTransporterKeyBind" ).getAsInt() : Keyboard.KEY_P;

        this.sand = getConfig().has( "sand" ) ? getConfig().get( "sand" ).getAsBoolean() : true;
        this.sand1 = getConfig().has( "sand1" ) ? getConfig().get( "sand1" ).getAsBoolean() : true;
        this.stone = getConfig().has( "stone" ) ? getConfig().get( "stone" ).getAsBoolean() : true;
        this.cobblestone = getConfig().has( "cobblestone" ) ? getConfig().get( "cobblestone" ).getAsBoolean() : true;
        this.stonebrick = getConfig().has( "stonebrick" ) ? getConfig().get( "stonebrick" ).getAsBoolean() : true;
        this.dirt = getConfig().has( "dirt" ) ? getConfig().get( "dirt" ).getAsBoolean() : true;
        this.grass = getConfig().has( "grass" ) ? getConfig().get( "grass" ).getAsBoolean() : true;
        this.charcoal = getConfig().has( "charcoal" ) ? getConfig().get( "charcoal" ).getAsBoolean() : true;
        this.coal = getConfig().has( "coal" ) ? getConfig().get( "coal" ).getAsBoolean() : true;
        this.ironore = getConfig().has( "ironore" ) ? getConfig().get( "ironore" ).getAsBoolean() : true;
        this.goldore = getConfig().has( "goldore" ) ? getConfig().get( "goldore" ).getAsBoolean() : true;
        this.ironingot = getConfig().has( "ironingot" ) ? getConfig().get( "ironingot" ).getAsBoolean() : true;
        this.goldingot = getConfig().has( "goldingot" ) ? getConfig().get( "goldingot" ).getAsBoolean() : true;
        this.bone = getConfig().has( "bone" ) ? getConfig().get( "bone" ).getAsBoolean() : true;
        this.glowstonedust = getConfig().has( "glowstonedust" ) ? getConfig().get( "glowstonedust" ).getAsBoolean() : true;
        this.glowstone = getConfig().has( "glowstone" ) ? getConfig().get( "glowstone" ).getAsBoolean() : true;
        this.lapislazuli = getConfig().has( "lapislazuli" ) ? getConfig().get( "lapislazuli" ).getAsBoolean() : true;
        this.quartz = getConfig().has( "quartz" ) ? getConfig().get( "quartz" ).getAsBoolean() : true;
        this.redstone = getConfig().has( "redstone" ) ? getConfig().get( "redstone" ).getAsBoolean() : true;
        this.diamond = getConfig().has( "diamond" ) ? getConfig().get( "diamond" ).getAsBoolean() : true;
        this.obsidian = getConfig().has( "obsidian" ) ? getConfig().get( "obsidian" ).getAsBoolean() : true;
        this.blazerod = getConfig().has( "blazerod" ) ? getConfig().get( "blazerod" ).getAsBoolean() : true;
        this.enderpearl = getConfig().has( "enderpearl" ) ? getConfig().get( "enderpearl" ).getAsBoolean() : true;
        this.book = getConfig().has( "book" ) ? getConfig().get( "book" ).getAsBoolean() : true;
        this.sugarcane = getConfig().has( "sugarcane" ) ? getConfig().get( "sugarcane" ).getAsBoolean() : true;
        this.leather = getConfig().has( "leather" ) ? getConfig().get( "leather" ).getAsBoolean() : true;
        this.sprucelog = getConfig().has( "sprucelog" ) ? getConfig().get( "sprucelog" ).getAsBoolean() : true;
        this.oaklog = getConfig().has( "oaklog" ) ? getConfig().get( "oaklog" ).getAsBoolean() : true;
        this.birchlog = getConfig().has( "birchlog" ) ? getConfig().get( "birchlog" ).getAsBoolean() : true;
        this.junglelog = getConfig().has( "junglelog" ) ? getConfig().get( "junglelog" ).getAsBoolean() : true;
        this.slimeball = getConfig().has( "slimeball" ) ? getConfig().get( "slimeball" ).getAsBoolean() : true;
        this.glass = getConfig().has( "glass" ) ? getConfig().get( "glass" ).getAsBoolean() : true;
        this.chest = getConfig().has( "chest" ) ? getConfig().get( "chest" ).getAsBoolean() : true;
        this.trappedchest = getConfig().has( "trappedchest" ) ? getConfig().get( "trappedchest" ).getAsBoolean() : true;
        this.hopper = getConfig().has( "hopper" ) ? getConfig().get( "hopper" ).getAsBoolean() : true;

    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings ) {


        subSettings.add( new SliderElement( "Delay (Ticks)", this, new ControlElement.IconData( Material.WATCH ), "delay", this.delay ).setRange( 40, 100 ) );

        subSettings.add( new BooleanElement( "Second Click Reset", this, new ControlElement.IconData( "labymod/textures/settings/settings/afecdistancedetection.png" ), "secondClickReset", this.secondClickReset ) );

        KeyElement keyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), keyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted == -1 ) { System.out.println( "Set new key to NONE" );keyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );keyBind = accepted;configSave(); }});

        KeyElement AutoTransporterKeyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.WOOD_BUTTON ), autoTransporterKeyBind, new Consumer<Integer>() {@Override public void accept( Integer accepted ) { if ( accepted == -1 ) { System.out.println( "Set new key to NONE" );autoTransporterKeyBind = -1;configSave();return; }System.out.println( "Set new key to " + Keyboard.getKeyName( accepted ) );autoTransporterKeyBind = accepted;configSave(); }});

        keyElement.setConfigEntryName("keyBind");

        keyElement.setConfigEntryName("autoTransporterKeyBind");

        subSettings.add( keyElement );

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

        subSettings.add( new BooleanElement( "Sand", this, new ControlElement.IconData( Material.SAND ), "sand", this.sand ) );
        subSettings.add( new BooleanElement( "Redsand", this, new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/RedSand.png") ), "sand1", this.sand1 ) );
        subSettings.add( new BooleanElement( "Stone", this, new ControlElement.IconData( Material.STONE ), "stone", this.stone ) );
        subSettings.add( new BooleanElement( "Cobblestone", this, new ControlElement.IconData( Material.COBBLESTONE ), "cobblestone", this.cobblestone ) );
        subSettings.add( new BooleanElement( "Stonebrick", this, new ControlElement.IconData( Material.SMOOTH_BRICK ), "stonebrick", this.stonebrick ) );
        subSettings.add( new BooleanElement( "Dirt", this, new ControlElement.IconData( Material.DIRT ), "dirt", this.dirt ) );
        subSettings.add( new BooleanElement( "Grass", this, new ControlElement.IconData( Material.GRASS ), "grass", this.grass ) );
        subSettings.add( new BooleanElement( "Charcoal", this, new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/charcoal.png")), "charcoal", this.charcoal ) );
        subSettings.add( new BooleanElement( "Coal", this, new ControlElement.IconData( Material.COAL), "coal", this.coal ) );
        subSettings.add( new BooleanElement( "Ironore", this, new ControlElement.IconData( Material.IRON_ORE), "ironore", this.ironore ) );
        subSettings.add( new BooleanElement( "Goldore", this, new ControlElement.IconData( Material.GOLD_ORE), "goldore", this.goldore ) );
        subSettings.add( new BooleanElement( "Ironingot", this, new ControlElement.IconData( Material.IRON_INGOT), "ironingot", this.ironingot ) );
        subSettings.add( new BooleanElement( "Goldingot", this, new ControlElement.IconData( Material.GOLD_INGOT), "goldingot", this.goldingot ) );
        subSettings.add( new BooleanElement( "Bone", this, new ControlElement.IconData( Material.BONE), "bone", this.bone ) );
        subSettings.add( new BooleanElement( "Glowstonedust", this, new ControlElement.IconData( Material.GLOWSTONE_DUST), "glowstonedust", this.glowstonedust ) );
        subSettings.add( new BooleanElement( "Glowstone", this, new ControlElement.IconData( Material.GLOWSTONE), "glowstone", this.glowstone ) );
        subSettings.add( new BooleanElement( "Lapislazuli", this, new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/blue_dye.png")), "lapislazuli", this.lapislazuli ) );
        subSettings.add( new BooleanElement( "Quartz", this, new ControlElement.IconData( Material.QUARTZ), "quartz", this.quartz ) );
        subSettings.add( new BooleanElement( "Redstone", this, new ControlElement.IconData( Material.REDSTONE), "redstone", this.redstone ) );
        subSettings.add( new BooleanElement( "Diamond", this, new ControlElement.IconData( Material.DIAMOND), "diamond", this.diamond ) );
        subSettings.add( new BooleanElement( "Obsidian", this, new ControlElement.IconData( Material.OBSIDIAN), "obsidian", this.obsidian ) );
        subSettings.add( new BooleanElement( "Blazerod", this, new ControlElement.IconData( Material.BLAZE_ROD), "blazerod", this.blazerod ) );
        subSettings.add( new BooleanElement( "Enderpearl", this, new ControlElement.IconData( Material.ENDER_PEARL), "enderpearl", this.enderpearl ) );
        subSettings.add( new BooleanElement( "Book", this, new ControlElement.IconData( Material.BOOK), "book", this.book ) );
        subSettings.add( new BooleanElement( "Sugarcane", this, new ControlElement.IconData( Material.SUGAR_CANE), "sugarcane", this.sugarcane ) );
        subSettings.add( new BooleanElement( "Leather", this, new ControlElement.IconData( Material.LEATHER), "leather", this.leather ) );
        subSettings.add( new BooleanElement( "Sprucelog", this, new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Spruce_Log.png")), "sprucelog", this.sprucelog ) );
        subSettings.add( new BooleanElement( "Oaklog", this, new ControlElement.IconData( Material.LOG), "oaklog", this.oaklog ) );
        subSettings.add( new BooleanElement( "Birchlog", this, new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Birch_Log.png")), "birchlog", this.birchlog ) );
        subSettings.add( new BooleanElement( "junglelog", this, new ControlElement.IconData( new ResourceLocation("transporter/textures/icons/Jungle_Log.png")), "junglelog", this.junglelog ) );
        subSettings.add( new BooleanElement( "Slimeball", this, new ControlElement.IconData( Material.SLIME_BALL), "slimeball", this.slimeball ) );
        subSettings.add( new BooleanElement( "Glass", this, new ControlElement.IconData( Material.GLASS), "glass", this.glass ) );
        subSettings.add( new BooleanElement( "Chest", this, new ControlElement.IconData( Material.CHEST), "chest", this.chest ) );
        subSettings.add( new BooleanElement( "Trappedchest", this, new ControlElement.IconData( Material.TRAPPED_CHEST), "trappedchest", this.trappedchest ) );
        subSettings.add( new BooleanElement( "Hopper", this, new ControlElement.IconData( Material.HOPPER), "hopper", this.hopper ) );

    }

    private void configSave(){
        getConfig().addProperty("keyBind", this.keyBind);
        getConfig().addProperty("autoTransporterKeyBind", this.autoTransporterKeyBind);
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
                if (executeState == 0) {
                    executeState++;
                    if(sand) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put sand");
                        return;
                    }
                }
                if (executeState == 1) {
                    executeState++;
                    if(sand1) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put sand:1");
                        return;
                    }
                }
                if (executeState == 2) {
                    executeState++;
                    if(stone) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put stone");
                        return;
                    }
                }
                if (executeState == 3) {
                    executeState++;
                    if(cobblestone) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put cobblestone");
                        return;
                    }
                }
                if (executeState == 4) {
                    executeState++;
                    if(stonebrick) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put stonebrick");
                        return;
                    }
                }
                if (executeState == 5) {
                    executeState++;
                    if(dirt) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put dirt");
                        return;
                    }
                }
                if (executeState == 6) {
                    executeState++;
                    if(grass) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put grass");
                        return;
                    }
                }
                if (executeState == 7) {
                    executeState++;
                    if(charcoal) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put charcoal");
                        return;
                    }
                }
                if (executeState == 8) {
                    executeState++;
                    if(coal) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put coal");
                        return;
                    }
                }
                if (executeState == 9) {
                    executeState++;
                    if(ironore) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put ironore");
                        return;
                    }
                }
                if (executeState == 10) {
                    executeState++;
                    if(goldore) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put goldore");
                        return;
                    }
                }
                if (executeState == 11) {
                    executeState++;
                    if(ironingot) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put ironingot");
                        return;
                    }
                }
                if (executeState == 12) {
                    executeState++;
                    if(goldingot) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put goldingot");
                        return;
                    }
                }
                if (executeState == 13) {
                    executeState++;
                    if(bone) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put bone");
                        return;
                    }
                }
                if (executeState == 14) {
                    executeState++;
                    if(glowstonedust) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put glowstonedust");
                        return;
                    }
                }
                if (executeState == 15) {
                    executeState++;
                    if(glowstone) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put glowstone");
                        return;
                    }
                }
                if (executeState == 16) {
                    executeState++;
                    if(lapislazuli) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put lapislazuli");
                        return;
                    }
                }
                if (executeState == 17) {
                    executeState++;
                    if(quartz) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put quartz");
                        return;
                    }
                }
                if (executeState == 18) {
                    executeState++;
                    if(redstone) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put redstone");
                        return;
                    }
                }
                if (executeState == 19) {
                    executeState++;
                    if(diamond) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put diamond");
                        return;
                    }
                }
                if (executeState == 20) {
                    executeState++;
                    if(obsidian) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put obsidian");
                        return;
                    }
                }
                if (executeState == 21) {
                    executeState++;
                    if(blazerod) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put blazerod");
                        return;
                    }
                }
                if (executeState == 22) {
                    executeState++;
                    if(enderpearl) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put enderpearl");
                        return;
                    }
                }
                if (executeState == 23) {
                    executeState++;
                    if(book) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put book");
                        return;
                    }
                }
                if (executeState == 24) {
                    executeState++;
                    if(sugarcane) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put sugarcane");
                        return;
                    }
                }
                if (executeState == 25) {
                    executeState++;
                    if(leather) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put leather");
                        return;
                    }
                }
                if (executeState == 26) {
                    executeState++;
                    if(sprucelog) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put sprucelog");
                        return;
                    }
                }
                if (executeState == 27) {
                    executeState++;
                    if(oaklog) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put oaklog");
                        return;
                    }
                }
                if (executeState == 28) {
                    executeState++;
                    if(birchlog) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put birchlog");
                        return;
                    }
                }
                if (executeState == 29) {
                    executeState++;
                    if(junglelog) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put junglelog");
                        return;
                    }
                }
                if (executeState == 30) {
                    executeState++;
                    if(slimeball) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put slimeball");
                        return;
                    }
                }
                if (executeState == 31) {
                    executeState++;
                    if(glass) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put glass");
                        return;
                    }
                }
                if (executeState == 32) {
                    executeState++;
                    if(chest) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put chest");
                        return;
                    }
                }
                if (executeState == 33) {
                    executeState++;
                    if(trappedchest) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put trappedchest");
                        return;
                    }
                }
                if (executeState == 34) {
                    executeState++;
                    if(hopper) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put hopper");
                        return;
                    }
                }
                if (executeState == 35) {
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
        }else if(Keyboard.isKeyDown(Keyboard.KEY_L)){
            //TransporterGui.draw();
        }



    }

}


