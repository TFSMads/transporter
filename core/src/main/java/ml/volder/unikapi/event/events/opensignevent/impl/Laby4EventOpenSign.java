package ml.volder.unikapi.event.events.opensignevent.impl;

import ml.volder.core.generated.DefaultReferenceStorage;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.event.events.opensignevent.OpenSignEvent;
import ml.volder.unikapi.loader.Laby4Loader;
import ml.volder.unikapi.utils.ReflectionUtils;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public abstract class Laby4EventOpenSign {
    public void onScreenOpen(ScreenDisplayEvent event, String eventName) {
        if(event.getScreen() == null || event.getScreen().wrap() == null || event.getScreen().wrap().getVersionedScreen() == null)
            return;
        if(isSignScreen(event.getScreen().wrap().getVersionedScreen())){
            WrappedTileEntitySign sign = getSign(event.getScreen().wrap().getVersionedScreen());
            OpenSignEvent openSignEvent = new OpenSignEvent(EventType.PRE, eventName, sign);
            EventManager.callEvent(openSignEvent);
            if(openSignEvent.getNewScreen() != null && openSignEvent.getNewScreen() != null){
                event.setScreen(openSignEvent.getNewScreen());
            }
            if(openSignEvent.isCancelled()) {
                event.setScreen(null);
            }
        }

    }

    public abstract boolean isSignScreen(Object guiScreen);

    public abstract WrappedTileEntitySign getSign(Object guiScreen);

    private static Laby4EventOpenSign instance;
  public static Laby4EventOpenSign getVersionedInstance() {
      if(instance == null){
        DefaultReferenceStorage defaultReferenceStorage = Laby4Loader.referenceStorageAccessorInstance();
        instance = defaultReferenceStorage.laby4EventOpenSign();
      }
      return instance;
  }
}
