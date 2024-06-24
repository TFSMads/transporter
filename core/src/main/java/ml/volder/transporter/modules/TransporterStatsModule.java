package ml.volder.transporter.modules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.events.ItemAmountUpdatedEvent;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventHandler;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.Listener;
import ml.volder.unikapi.guisystem.elements.Settings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;

public class TransporterStatsModule extends SimpleModule implements Listener {
  //TODO not implemented yet

  public TransporterStatsModule(ModuleManager.ModuleInfo moduleInfo) {
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

  }

  @EventHandler
  public void onItemAmountUpdate(ItemAmountUpdatedEvent event) {
    if(PlayerAPI.getAPI().getUUID() == null)
        return;
    //Copy itemdata file to current date file in stats folder
    File itemDataFile = TransporterAddon.getInstance().getTransporterItemManager().getDataManager().getFile();
    File statsFolder = new File(UnikAPI.getPlayerDataFolder(), "transporterHistory");

    ZoneId z = ZoneId.of("Europe/Paris");
    LocalDate today = LocalDate.now( z );
    String date = today.getDayOfMonth() + "-" + today.getMonthValue() + "-" + today.getYear();

    File statsFile = new File(statsFolder, date + ".json");

    if(!statsFolder.exists())
        statsFolder.mkdirs();

    try {
      Files.copy(itemDataFile.toPath(), statsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ignored) { }
  }
}
