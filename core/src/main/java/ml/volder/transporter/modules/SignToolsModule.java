package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.signtoolsmodule.SignBuffer;
import ml.volder.transporter.modules.signtoolsmodule.SignGui;
import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.action.TransporterAction;
import ml.volder.transporter.settings.classes.TransporterSettingElementFactory;
import ml.volder.transporter.settings.classes.TransporterWidgetFactory;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.configuration.settings.type.SettingHeader;

import java.util.Timer;
import java.util.TimerTask;

public class SignToolsModule extends SimpleModule implements Listener {

    private static Key pasteKey = Key.V;
    private static Key copyKey = Key.C;

    private int placeDelay = 1000;

    private boolean openSignEditor = true;

    public SignToolsModule(ModuleManager.ModuleInfo moduleInfo) {
        super(moduleInfo);
    }

    @Override
    public SimpleModule init() {
        return this;
    }

    @Override
    public SimpleModule enable() {
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void fillSettings(SettingRegistryAccessor subSettings) {
        subSettings.add(new SettingHeader(
                "header",
                true,
                "",
                "header"
        ));

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        KeybindWidget.class,
                        new TransporterAction<net.labymod.api.client.gui.screen.key.Key>(key -> pasteKey = Laby4KeyMapper.convert(key)),
                        getDataManager(),
                        "pasteKey",
                        net.labymod.api.client.gui.screen.key.Key.V))
                .id("pasteKey")
                .icon(Icon.sprite16(ModTextures.COMMON_ICONS, 4, 5))
                .build()
        );

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        KeybindWidget.class,
                        new TransporterAction<net.labymod.api.client.gui.screen.key.Key>(key -> copyKey = Laby4KeyMapper.convert(key)),
                        getDataManager(),
                        "copyKey",
                        net.labymod.api.client.gui.screen.key.Key.C))
                .id("copyKey")
                .icon(Icon.sprite16(ModTextures.COMMON_ICONS, 3, 5))
                .build()
        );

        subSettings.add(new SettingHeader(
                "header2",
                true,
                "",
                "header2"
        ));

        subSettings.add(TransporterSettingElementFactory.Builder.begin()
                .addWidget(TransporterWidgetFactory.createWidget(
                        SwitchWidget.class,
                        new TransporterAction<Boolean>((b) -> this.openSignEditor = b),
                        getDataManager(),
                        "openSignEditor",
                        true))
                .id("openSignEditor")
                .icon(Icon.sprite16(ModTextures.COMMON_ICONS, 6, 5))
                .build()
        );


    }

    boolean isSendingUpdatePacket = false;

    @EventHandler
    public void onSignOpen(OpenSignEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(!openSignEditor) {
            if (isSendingUpdatePacket) {
                PlayerAPI.getAPI().displayChatMessage(ModColor.RED + "Du placere skilte for hurtigt!");
                event.setCancelled(true);
                return;
            }
            WrappedTileEntitySign tileEntitySign = event.getTileEntitySign();
            SignBuffer signText = SignGui.getBufferedText();
            tileEntitySign.setLine(0, signText == null ? "" : signText.getLine(1));
            tileEntitySign.setLine(1, signText == null ? "" : signText.getLine(2));
            tileEntitySign.setLine(2, signText == null ? "" : signText.getLine(3));
            tileEntitySign.setLine(3, signText == null ? "" : signText.getLine(4));
            isSendingUpdatePacket = true;
            new Timer("sendSignUpdate").schedule(new TimerTask() {
                @Override
                public void run() {
                    tileEntitySign.sendUpdatePacket();
                    tileEntitySign.setEditable(true);
                    isSendingUpdatePacket = false;
                }
            }, placeDelay);
            event.setCancelled(true);
            return;
        }
        event.setScreen(new SignGui(getDataManager(), event.getTileEntitySign()));
    }

    public static Key getPasteKey() {
        return pasteKey;
    }

    public static Key getCopyKey() {
        return copyKey;
    }
}
