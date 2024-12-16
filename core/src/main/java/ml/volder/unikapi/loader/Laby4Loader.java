package ml.volder.unikapi.loader;

import ml.volder.transporter.settings.TransporterAddonConfig;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiReferenceStorageLaby4;
import ml.volder.unikapi.event.events.mainmenuopenevent.impl.Laby4MainMenuOpenEvent;
import ml.volder.unikapi.logger.Laby4Logger;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.reference.ReferenceStorageAccessor;

@AddonMain
public class Laby4Loader extends LabyAddon<TransporterAddonConfig> {

  public static <R extends ReferenceStorageAccessor> R referenceStorageAccessorInstance(){
    return instance.referenceStorageAccessor();
  }

  public static String namespace() {
    if(instance == null || instance.addonInfo() == null)
      return "minecraft";
    return instance.addonInfo().getNamespace();
  }

  private static Laby4Loader instance;

  @Override
  protected void enable() {
    instance = this;
    UnikAPI.LOGGER = new Laby4Logger("UnikAPI");
    UnikAPI.initAPI("labymod4", null, "*");
    UnikAPI.registerReferenceStorage(ApiReferenceStorageLaby4.getInstance());
    Loader.onEnable();
    Laby.labyAPI().eventBus().registerListener(this);
    Laby4MainMenuOpenEvent.checkMainMenu();

    this.registerSettingCategory();
  }

  public static void registerCommands(Command... command) {
    if(instance == null)
      return;
    for (Command cmd : command) {
      instance.registerCommand(cmd);
    }
  }

  @Override
  protected Class<TransporterAddonConfig> configurationClass() {
    return TransporterAddonConfig.class;
  }
}
