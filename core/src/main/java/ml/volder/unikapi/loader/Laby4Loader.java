package ml.volder.unikapi.loader;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiReferenceStorageLaby4;
import ml.volder.unikapi.event.events.mainmenuopenevent.impl.Laby4MainMenuOpenEvent;
import ml.volder.unikapi.loader.laby4.Laby4AddonConfig;
import ml.volder.unikapi.loader.laby4.UnikRootSettingRegistry;
import ml.volder.unikapi.logger.Laby4Logger;
import ml.volder.unikapi.widgets.Laby4ModuleManager;
import ml.volder.unikapi.widgets.ModuleSystem;
import net.labymod.api.Laby;
import net.labymod.api.addon.AddonService;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.addon.LoadedAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.models.addon.info.InstalledAddonInfo;
import net.labymod.api.reference.ReferenceStorageAccessor;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@AddonMain
public class Laby4Loader extends LabyAddon<Laby4AddonConfig> {

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

    //Redirect addon settings to UnikAPI addon settings
    UnikRootSettingRegistry registry = new UnikRootSettingRegistry(addonInfo().getNamespace(), addonInfo().getNamespace(), unikRootSettingRegistry -> {
      if(Laby.labyAPI().minecraft().minecraftWindow().currentScreen().isActivity() && Laby.labyAPI().minecraft().minecraftWindow().currentScreen().asActivity().getClass().getName().contains("NavigationActivity")) {
        ml.volder.unikapi.AddonMain.callOpenSettings(this);
        changeFromAddonSetting = true;
      }
    });
    registry.setDisplayName(addonInfo().getDisplayName());
    labyAPI().coreSettingRegistry().addSetting(registry);
    Laby4MainMenuOpenEvent.checkMainMenu();
  }

  boolean changeFromAddonSetting = false;
  @Subscribe
  public void onTick(GameTickEvent event) {
    if (changeFromAddonSetting) {
      if(labyAPI().coreSettingRegistry().getById("ingame") != null)
        labyAPI().showSetting(labyAPI().coreSettingRegistry().getById("ingame"));
      ml.volder.unikapi.AddonMain.callOpenSettings(this);
      changeFromAddonSetting = false;
    }
  }

  @Override
  protected Class<Laby4AddonConfig> configurationClass() {
    return Laby4AddonConfig.class;
  }
}
