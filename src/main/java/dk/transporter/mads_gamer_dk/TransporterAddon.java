package dk.transporter.mads_gamer_dk;

import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.classes.MiningRigTimers;
import dk.transporter.mads_gamer_dk.guis.TransporterInfoGui.TransporterGuiOld;
import dk.transporter.mads_gamer_dk.guis.TransporterInfoGui.TransporterInfoGui;
import dk.transporter.mads_gamer_dk.guis.getItemsGui.GetItemsConfigGui;
import dk.transporter.mads_gamer_dk.guis.serverSelector.ServerSelecterGui;
import dk.transporter.mads_gamer_dk.listeners.*;
import dk.transporter.mads_gamer_dk.mcmmo.Skills;
import dk.transporter.mads_gamer_dk.messageSendingSettings.messageSettings;
import dk.transporter.mads_gamer_dk.modules.AutoTransporterModule;
import dk.transporter.mads_gamer_dk.modules.RegisterModules;
import dk.transporter.mads_gamer_dk.utils.ConfigHandler;
import dk.transporter.mads_gamer_dk.utils.MessageHandler;
import dk.transporter.mads_gamer_dk.utils.TaDrawUtils;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.accountmanager.storage.StorageType;
import net.labymod.accountmanager.storage.account.Account;
import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.ingamegui.Module;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public class TransporterAddon extends LabyModAddon {

    public static final ModuleCategory CATEGORY_TRANSPORTERITEMS = new ModuleCategory("Transporter Items", true, new ControlElement.IconData("transporter/textures/icons/Items.png"));

    public static final ModuleCategory CATEGORY_TRANSPORTERITEMSVÆRDI = new ModuleCategory("Transporter Værdier", true, new ControlElement.IconData(Material.EMERALD));

    public static final ModuleCategory CATEGORY_TRANSPORTERADDON = new ModuleCategory("Transporter Addon", true, new ControlElement.IconData(Material.SIGN));

    public static final ModuleCategory CATEGORY_MCMMO = new ModuleCategory("Mcmmo", true, new ControlElement.IconData(Material.DIAMOND_SPADE));

    private boolean isEnabled;

    public boolean isValidVersion() {
        return isValidVersion;
    }

    private boolean isValidVersion;

    public static boolean connectedToSuperawesome;

    private MiningRigTimers timers;

    public messageSettings MessageSettings;

    private Integer timer = 0;

    private boolean autoTransporter;

    public Integer autoTransporterKeyBind;

    public Integer transporterMenuKeyBind;

    public Integer autoGetMenuKeyBind;

    public Integer lobbySelecterKeybind;

    private static TransporterAddon addon;

    public Integer getTimer(){
        return this.timer;
    }

    public void setTimer(Integer timer){
        this.timer = timer;
    }

    public Items items;

    private DataManagers dataManagers;

    private MessageHandler messages;

    public Integer antalKrævet;


    public boolean isSignToolsGuiOpen = false;

    /*
     * Auto get variables
     */

    public Boolean autoGetIsActive = false;
    public String autoGetItem;
    public int autoGetMinimum;

    private TaDrawUtils drawUtils;

    public static TaDrawUtils getDrawUtils() {
        return addon.drawUtils;
    }

    /*
     * Static Method for getting instance of addon
     */

    public static TransporterAddon getInstance(){
        return addon;
    }

    /*
     * Signtool varible and getter and setter
     */


    public boolean getAutoTransporter() {
        return autoTransporter;
    }

    public MessageHandler getMessages(){
        return this.messages;
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

    public void setValidVersion(boolean validVersion,JoinListener listener) {
        if(listener == this.joinListener) {
            isValidVersion = validVersion;
        }
    }


    public boolean newMenu = true;

    private JoinListener joinListener;

    public Skills getSkills() {
        return skills;
    }

    private Skills skills;

    private boolean showUpdateScreen = false;

    @Override
    public void onEnable() {

        /*try {
            showUpdateScreen = UpdateChecker.CheckUpdate();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }*/

        addon = this;
        this.drawUtils = new TaDrawUtils();

        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

        //Skift til microsoft account gennem labymod account manager hvis kørt i debug mode.
        if(isDebug){
            Account[] profiles = LabyMod.getInstance().getAccountManager().getAccounts();

            for (Account acc: profiles) {
                if(acc.getUsername().equals("Mads_Gamer_DK")){
                    if(!acc.isAccessTokenExpired() && acc.getStorageType() == StorageType.EXTERNAL){
                        System.out.println("Changing session: " + acc.getUsername() + " Storage: " + acc.getStorageType().name());
                        try {
                            LabyMod.getInstance().setSession(acc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        dataManagers = new DataManagers();

        skills = new Skills(this);

        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERITEMS);
        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERITEMSVÆRDI);
        ModuleCategoryRegistry.loadCategory(CATEGORY_TRANSPORTERADDON);
        ModuleCategoryRegistry.loadCategory(CATEGORY_MCMMO);

        isValidVersion = false;
        isEnabled = false;
        items = new Items();
        System.out.println("TransporterAddon Enabled!");
        this.getApi().registerForgeListener(this);
        this.getApi().registerForgeListener(new OnTick());
        this.getApi().registerForgeListener(new GuiListener(this));

        this.getApi().getEventManager().register((MessageSendEvent)new OnCommand(this, skills));

        this.getApi().getEventManager().register((MessageReceiveEvent)new messageReceiveListener(items, addon,skills));

        this.getApi().registerModule((Module)new AutoTransporterModule(this));

        this.joinListener = new JoinListener(this);

        this.getApi().getEventManager().registerOnJoin((Consumer<net.labymod.utils.ServerData>) this.joinListener);
        this.getApi().getEventManager().registerOnQuit((Consumer<net.labymod.utils.ServerData>) new QuitListener());

        messages = new MessageHandler();
        timers = new MiningRigTimers();

        RegisterModules.registerAllModules(this);

    }

    @Override
    public void loadConfig() {
        ConfigHandler.loadConfig(this,getConfig());
    }

    public Boolean getItemConfig(String item){
        return getConfig().has( item ) ? getConfig().get( item  ).getAsBoolean() : true;
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings ) {
        ConfigHandler.fillSettings(subSettings,this);
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
                        autoTransporter = false;
                    }else{
                        autoTransporter = true;
                    }
                }
            }
            if (transporterMenuKeyBind >= 0) {
                if (Keyboard.isKeyDown(transporterMenuKeyBind)) {
                    if(newMenu){
                        Minecraft.getMinecraft().displayGuiScreen(new TransporterInfoGui(this));
                    }else{
                        Minecraft.getMinecraft().displayGuiScreen(new TransporterGuiOld(addon, items));
                    }

                }

            }
            if (lobbySelecterKeybind >= 0) {
                if (Keyboard.isKeyDown(lobbySelecterKeybind)) {
                    Minecraft.getMinecraft().displayGuiScreen(new ServerSelecterGui(addon, dataManagers));
                }
            }
            if (autoGetMenuKeyBind >= 0) {
                if (Keyboard.isKeyDown(autoGetMenuKeyBind)) {
                    Minecraft.getMinecraft().displayGuiScreen(new GetItemsConfigGui(null, this));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public DataManagers getDataManagers() {
        return dataManagers;
    }
}


