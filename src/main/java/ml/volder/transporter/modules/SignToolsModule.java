package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.gui.TransporterModulesMenu;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.transporter.modules.signtoolsmodule.SignBuffer;
import ml.volder.transporter.modules.signtoolsmodule.SignGui;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;

public class SignToolsModule extends SimpleModule implements Listener {
    private boolean isFeatureActive;

    private static Key pasteKey = Key.V;
    private static Key copyKey = Key.C;

    private boolean openSignEditor = true;

    public SignToolsModule(String moduleName) {
        super(moduleName);
        EventManager.registerEvents(this);
        fillSettings();
    }

    @Override
    protected void loadConfig() {
        isFeatureActive = hasConfigEntry("isFeatureActive") ? getConfigEntry("isFeatureActive", Boolean.class) : true;
    }

    @EventHandler
    public void onSignOpen(OpenSignEvent event) {
        if(!TransporterAddon.isEnabled() || !this.isFeatureActive)
            return;
        if(!openSignEditor) {
            WrappedTileEntitySign tileEntitySign = event.getTileEntitySign();
            SignBuffer signText = SignGui.getBufferedText();
            tileEntitySign.setLine(0, signText == null ? "" : signText.getLine(1));
            tileEntitySign.setLine(1, signText == null ? "" : signText.getLine(2));
            tileEntitySign.setLine(2, signText == null ? "" : signText.getLine(3));
            tileEntitySign.setLine(3, signText == null ? "" : signText.getLine(4));
            tileEntitySign.sendUpdatePacket();
            tileEntitySign.setEditable(true);
            event.setCancelled(true);
            return;
        }
        event.setScreen(new SignGui(getDataManager(), event.getTileEntitySign()));
    }

    private void fillSettings() {
        ModuleElement moduleElement = new ModuleElement("Sign Tools", "En feature til at forbedre skilte så man kan kopiere og indsætte.", ModTextures.MISC_HEAD_QUESTION, isActive -> {
            isFeatureActive = isActive;
            setConfigEntry("isFeatureActive", isFeatureActive);
        });
        moduleElement.setActive(isFeatureActive);

        Settings subSettings = moduleElement.getSubSettings();

        HeaderElement headerElement = new HeaderElement("Her under kan du vælge hvilke knapper du vil bruge til at indsætte og kopiere skilte. Bemærk du skal holde kontrol knappen inde sammen med knappen for at handlingen udføres.");
        subSettings.add(headerElement);

        KeyElement keyElement = new KeyElement("Indsæt - Knap", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "pasteKey", false, pasteKey);
        pasteKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> pasteKey = key);
        subSettings.add(keyElement);

        keyElement = new KeyElement("Kopier - Knap", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "copyKey", false, copyKey);
        copyKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> copyKey = key);
        subSettings.add(keyElement);

        headerElement = new HeaderElement("Hvis indstillingen under er slået fra vil det text du har kopieret automatisk blive indsat og skiltet placeres med det samme uden at editoren åbenes. ");
        subSettings.add(headerElement);

        BooleanElement booleanElement = new BooleanElement("Åben editor", getDataManager(), "openSignEditor", new ControlElement.IconData(Material.OAK_SIGN), openSignEditor);
        this.openSignEditor = booleanElement.getCurrentValue();
        booleanElement.addCallback(shouldOpen -> this.openSignEditor = shouldOpen);
        subSettings.add(booleanElement);

        TransporterModulesMenu.addSetting(moduleElement);
    }

    public static Key getPasteKey() {
        return pasteKey;
    }

    public static Key getCopyKey() {
        return copyKey;
    }
}
