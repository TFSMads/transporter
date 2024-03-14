package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.transportermenumodule.TransporterMenu;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.event.events.clientkeypressevent.ClientKeyPressEvent;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.types.Material;

public class TransporterMenuModule extends SimpleModule implements Listener {

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
        EventManager.registerEvents(this);
        return this;
    }

    @Override
    public void fillSettings(Settings subSettings) {

        BooleanElement onlyActiveInLobbyElement  = new BooleanElement(
                "Kun aktiv i lobbyer!",
                getDataManager(),
                "onlyActiveInLobby",
                new ControlElement.IconData(Material.DIODE),
                true
        );
        this.onlyActiveInLobby = onlyActiveInLobbyElement.getCurrentValue();
        onlyActiveInLobbyElement.addCallback(b -> this.onlyActiveInLobby = b);
        subSettings.add(onlyActiveInLobbyElement);

        KeyElement keyElement = new KeyElement("Keybind", new ControlElement.IconData(Material.OAK_BUTTON), getDataManager(), "transporterMenuKeybind", false, openKey);
        this.openKey = keyElement.getCurrentKey();
        keyElement.addCallback(key -> this.openKey = key);
        subSettings.add(keyElement);

        NumberElement numberElement = new NumberElement("Get Antal", getDataManager(), "withdraw.amount", new ControlElement.IconData(Material.PAPER), -1);
        numberElement.setMinValue(-1);
        withdrawAmount = numberElement.getCurrentValue();
        numberElement.addCallback(integer -> withdrawAmount = integer);
        subSettings.add(numberElement);
    }


    @EventHandler
    public void onKeyInput(ClientKeyPressEvent event) {
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
        if(PlayerAPI.getAPI().hasOpenScreen())
            return;
        PlayerAPI.getAPI().openGuiScreen(new TransporterMenu());
    }

    public int getWithdrawAmount() {
        return withdrawAmount;
    }
}
