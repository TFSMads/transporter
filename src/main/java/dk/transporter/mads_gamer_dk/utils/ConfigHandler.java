package dk.transporter.mads_gamer_dk.utils;

import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.Items.TransporterItems;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.listeners.messageReceiveListener;
import dk.transporter.mads_gamer_dk.listeners.tickSubscribers.AutoTransporterSubscriber;
import dk.transporter.mads_gamer_dk.messageSendingSettings.messageSettings;
import dk.transporter.mads_gamer_dk.settingelements.DescribedBooleanElement;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ConfigHandler {
    public static void loadConfig(TransporterAddon addon, JsonObject config){
        AutoTransporterSubscriber.setDelay(config.has( "delay" ) ? config.get( "delay" ).getAsInt() : 45);

        addon.MessageSettings = (config.has("Beskeder") ? messageSettings.valueOf(config.get("Beskeder").getAsString()) : messageSettings.getDefaultAction());

        addon.transporterMenuKeyBind = config.has( "transporterMenuKeyBind" ) ? config.get( "transporterMenuKeyBind" ).getAsInt() : Keyboard.KEY_L;

        addon.autoGetMenuKeyBind = config.has( "autoGetMenuKeyBind" ) ? config.get( "autoGetMenuKeyBind" ).getAsInt() : Keyboard.KEY_N;

        addon.autoTransporterKeyBind = config.has( "autoTransporterKeyBind" ) ? config.get( "autoTransporterKeyBind" ).getAsInt() : Keyboard.KEY_P;

        addon.lobbySelecterKeybind = config.has( "lobbySelecterKeybind" ) ? config.get( "lobbySelecterKeybind" ).getAsInt() : Keyboard.KEY_Y;

        addon.getMessages().setMessageById(0, config.has( "putMessage" ) ? config.get( "putMessage" ).getAsString() : "&bGemmer &3%antal% %item% &bi din transporter. &7(&3%total%&7)");
        addon.getMessages().setMessageById(1, config.has( "getMessage" ) ? config.get( "getMessage" ).getAsString() : "&bTager &3%antal% %item% &bfra din transporter. &7(&3%total%&7)");
        addon.getMessages().setMessageById(2, config.has( "putManglerMessage" ) ? config.get( "putManglerMessage" ).getAsString() : "&bDu har ikke noget &3%item%");
        addon.getMessages().setMessageById(3, config.has( "getManglerMessage" ) ? config.get( "getManglerMessage" ).getAsString() : "&bDu har ikke noget &3%item% &bi din transporter.");
        addon.getMessages().setMessageById(4, config.has( "delayMessage" ) ? config.get( "delayMessage" ).getAsString() : "&cDer er 2 sekunders cooldown på transporteren.");

        addon.antalKrævet = config.has( "antalKrævet" ) ? config.get( "antalKrævet" ).getAsInt() : 1;
        addon.newMenu = config.has( "newMenu" ) ? config.get( "newMenu" ).getAsBoolean() : true;


        /*
         * Auto get data
         */

        addon.autoGetIsActive = config.has( "autoGetIsActive" ) ? config.get( "autoGetIsActive" ).getAsBoolean() : true;
        addon.autoGetItem = config.has( "autoGetItem" ) ? config.get( "autoGetItem" ).getAsString() : "dirt";
        addon.autoGetMinimum = config.has( "autoGetMinimum" ) ? config.get( "autoGetMinimum" ).getAsInt() : 64;

        TransporterItems items[] = TransporterItems.values();
        for(TransporterItems item : items) {
            addon.items.getItemByID(addon.items.getId(item)).setAntalKrævet(config.has( item.toString() + "-Required" ) ? config.get( item.toString() + "-Required").getAsInt() : 1);
            addon.items.getItemByID(addon.items.getId(item)).setValue(config.has( item.toString() + "-Value" ) ? config.get( item.toString() + "-Value").getAsInt() : addon.items.getItemByID(addon.items.getId(item)).getValue());
            addon.items.getItemByID(addon.items.getId(item)).setAmount(config.has( item.toString() + "-Amount" ) ? config.get( item.toString() + "-Amount").getAsInt() : addon.items.getItemByID(addon.items.getId(item)).getAmount());
        }
    }

    public static void fillSettings(List<SettingsElement> subSettings, TransporterAddon addon){
        KeyElement AutoTransporterKeyElement = new KeyElement( "Keybind", new ControlElement.IconData( Material.WOOD_BUTTON ), addon.autoTransporterKeyBind, accepted -> {
            addon.autoTransporterKeyBind = accepted;
            if ( accepted < 0 )
                addon.autoTransporterKeyBind = -1;
            addon.getConfig().addProperty("autoTransporterKeyBind", addon.autoTransporterKeyBind);
        });

        KeyElement transporterMenuKeyElement = new KeyElement( "Transporter Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), addon.transporterMenuKeyBind, accepted -> {
            addon.transporterMenuKeyBind = accepted;
            if ( accepted < 0 )
                addon.transporterMenuKeyBind = -1;
            addon.getConfig().addProperty("transporterMenuKeyBind", addon.transporterMenuKeyBind);
        });

        subSettings.add( transporterMenuKeyElement );

        KeyElement autoGetMenuKeyElement = new KeyElement( "Auto Get Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), addon.autoGetMenuKeyBind, accepted -> {
            addon.autoGetMenuKeyBind = accepted;
            if ( accepted < 0 )
                addon.autoGetMenuKeyBind = -1;
            addon.getConfig().addProperty("autoGetMenuKeyBind", addon.autoGetMenuKeyBind);
        });

        subSettings.add( autoGetMenuKeyElement );

        subSettings.add( new BooleanElement( "Brug nye Transporter menu", addon, new ControlElement.IconData( Material.PAINTING ), "newMenu", addon.newMenu ) );

        subSettings.add(new HeaderElement(ModColor.cl("a") + " "));



        ListContainerElement listMessages = new ListContainerElement(ModColor.cl("7") + "Beskeder", new ControlElement.IconData(Material.PAPER));


        final DropDownMenu<messageSettings> uploadServiceDropDownMenu = (DropDownMenu<messageSettings>)new DropDownMenu("Beskeder", 0, 0, 0, 0).fill((Object[])messageSettings.values());
        final DropDownElement<messageSettings> uploadServiceDropDownElement = new DropDownElement<messageSettings>("Beskeder", (DropDownMenu<messageSettings>)uploadServiceDropDownMenu);
        uploadServiceDropDownMenu.setSelected(addon.MessageSettings);
        uploadServiceDropDownElement.setChangeListener(Message_Settings -> {
            addon.MessageSettings = Message_Settings;
            addon.getConfig().addProperty("Beskeder", Message_Settings.name());
            addon.saveConfig();
            messageReceiveListener.message = addon.MessageSettings.getId();
        });

        listMessages.getSubSettings().add((SettingsElement)uploadServiceDropDownElement);


        StringElement customChat1 = new StringElement( "Transporter put besked" , new ControlElement.IconData( Material.PAPER ), addon.getMessages().getMessageById(0), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.getMessages().setMessageById(0,accepted);
                addon.getConfig().addProperty("putMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat1 );

        StringElement customChat2 = new StringElement( "Transporter get besked" , new ControlElement.IconData( Material.PAPER ), addon.getMessages().getMessageById(1), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.getMessages().setMessageById(1,accepted);
                addon.getConfig().addProperty("getMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat2 );

        StringElement customChat3 = new StringElement( "Put mangler besked" , new ControlElement.IconData( Material.PAPER ), addon.getMessages().getMessageById(2), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.getMessages().setMessageById(2,accepted);
                addon.getConfig().addProperty("putManglerMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat3 );

        StringElement customChat4= new StringElement( "Get mangler besked" , new ControlElement.IconData( Material.PAPER ), addon.getMessages().getMessageById(3), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.getMessages().setMessageById(3,accepted);
                addon.getConfig().addProperty("getManglerMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat4 );

        StringElement customChat5 = new StringElement( "Delay besked" , new ControlElement.IconData( Material.PAPER ), addon.getMessages().getMessageById(4), new Consumer<String>() {
            @Override
            public void accept( String accepted ) {
                addon.getMessages().setMessageById(4,accepted);
                addon.getConfig().addProperty("delayMessage", accepted);
            }
        });

        listMessages.getSubSettings().add( customChat5 );


        ListContainerElement listServerSelector = new ListContainerElement(ModColor.cl("2") + "Server Selector", new ControlElement.IconData(Material.COMPASS));


        listServerSelector.getSubSettings().add(new HeaderElement(ModColor.cl("2") + "Server Selecter"));

        KeyElement lobbySelecterKeyElement = new KeyElement( "Server Menu Keybind", new ControlElement.IconData( Material.STONE_BUTTON ), addon.lobbySelecterKeybind, accepted -> {
            addon.lobbySelecterKeybind = accepted;
            if ( accepted < 0 )
                addon.lobbySelecterKeybind = -1;
            addon.getConfig().addProperty("lobbySelecterKeybind", addon.lobbySelecterKeybind);
        });

        listServerSelector.getSubSettings().add( lobbySelecterKeyElement );


        ListContainerElement listAutoTransporter = new ListContainerElement(ModColor.cl("a") + "Auto Transporter", new ControlElement.IconData(Material.REDSTONE_COMPARATOR));


        listAutoTransporter.getSubSettings().add(new HeaderElement(ModColor.cl("a") + "Auto Transporter"));


        listAutoTransporter.getSubSettings().add(new SliderElement( "Delay (Ticks)", addon, new ControlElement.IconData( Material.WATCH ), "delay", AutoTransporterSubscriber.delay ).setRange( 40, 100 ) );


        listAutoTransporter.getSubSettings().add( AutoTransporterKeyElement );


        ListContainerElement listItems = new ListContainerElement(ModColor.cl("f") + "Items", new ControlElement.IconData(Material.IRON_INGOT));

        listItems.getSubSettings().add(new HeaderElement(ModColor.cl("a") + ModColor.cl("l") + "ITEMS"));
        listItems.getSubSettings().add(new HeaderElement(ModColor.cl("f") + "Vælg de items du vil putte i din transporter."));

        TransporterItems items[] = TransporterItems.values();

        listItems.getSubSettings().add( new SliderElement( "Antal Krævet.", addon, new ControlElement.IconData( Material.DETECTOR_RAIL ), "requiredAll", addon.antalKrævet ).setRange( 0, 64 ).addCallback(accepted -> {
            addon.getConfig().addProperty("antalKrævet", accepted);
            for(TransporterItems item : items) {
                addon.antalKrævet = accepted;
                addon.getConfig().addProperty(item.toString() + "-Required", accepted);
                addon.items.getItemByID(addon.items.getId(item)).setAntalKrævet(accepted);
            }
        }));

        listItems.getSubSettings().add(new HeaderElement(ModColor.cl("a") + " "));


        for(TransporterItems item : items) {
            Boolean bool = addon.getItemConfig(item.toString());
            ControlElement.IconData iconData = addon.items.getIconData(item);
            String name = addon.items.getName(item);
            Integer antalKrævet = addon.items.getItemByID(addon.items.getId(item)).getAntalKrævet();

            DescribedBooleanElement itemElement = new DescribedBooleanElement(name, addon, iconData, item.toString(), bool, "Slå denne til for at den putter " + name + " i din transporter.");
            itemElement.getSubSettings().add( new SliderElement( "Antal " + name + " krævet", addon, new ControlElement.IconData( Material.DETECTOR_RAIL ), item.toString() + "-Required", antalKrævet).setRange( 0, 64 ).addCallback(new Consumer<Integer>() {
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
                    addon.getConfig().addProperty(item.toString() + "-Value", accepted);
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
}
