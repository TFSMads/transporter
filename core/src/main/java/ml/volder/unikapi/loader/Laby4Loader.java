package ml.volder.unikapi.loader;

import ml.volder.transporter.settings.TransporterAddonConfig;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiReferenceStorageLaby4;
import ml.volder.unikapi.event.events.mainmenuopenevent.impl.Laby4MainMenuOpenEvent;
import ml.volder.unikapi.logger.Laby4Logger;
import ml.volder.unikapi.widgets.Laby4ModuleManager;
import ml.volder.unikapi.widgets.ModuleSystem;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
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
    ModuleSystem.setModuleManager(new Laby4ModuleManager());
    Loader.onEnable();
    Laby.labyAPI().eventBus().registerListener(this);
    Laby4MainMenuOpenEvent.checkMainMenu();

    this.registerSettingCategory();
  }

  @Override
  protected Class<TransporterAddonConfig> configurationClass() {
    return TransporterAddonConfig.class;
  }
}
