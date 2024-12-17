package ml.volder.unikapi.api;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.input.impl.Laby4InputAPI;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.inventory.impl.Laby4InventoryAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.minecraft.impl.Laby4MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.api.player.impl.Laby4PlayerAPI;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.event.events.mainmenuopenevent.impl.Laby4MainMenuOpenEvent;
import ml.volder.unikapi.event.events.opensignevent.impl.Laby4OpenSignEvent;
import ml.volder.unikapi.keysystem.KeyMapper;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import ml.volder.unikapi.wrappers.guibutton.IGuiButtonImpl;
import ml.volder.unikapi.wrappers.guibutton.impl.Laby4GuiButton;

public class ApiReferenceStorageLaby4 implements ApiReferenceStorage{

    private static ApiReferenceStorage instance;

    public static ApiReferenceStorage getInstance() {
        if(instance == null)
            instance = new ApiReferenceStorageLaby4();
        return instance;
    }

    @Override
    public DrawAPI getDrawAPI() {
        return Laby4DrawAPI.getAPI();
    }

    @Override
    public InputAPI getInputAPI() {
        return Laby4InputAPI.getAPI();
    }

    @Override
    public InventoryAPI getInventoryAPI() {
        return Laby4InventoryAPI.getAPI();
    }

    @Override
    public MinecraftAPI getMinecraftAPI() {
        return Laby4MinecraftAPI.getAPI();
    }

    @Override
    public PlayerAPI getPlayerAPI() {
        return Laby4PlayerAPI.getAPI();
    }

    @Override
    public Class<? extends EventImpl> getVersionedOpenSignEvent() {
        return Laby4OpenSignEvent.class;
    }

  @Override
  public Class<? extends EventImpl> getVersionedMainMenuOpenEvent() {
    return Laby4MainMenuOpenEvent.class;
  }

  @Override
    public KeyMapper getVersionedKeyMapper() {
        return Laby4KeyMapper.getAPI();
    }

    @Override
    public Class<? extends IGuiButtonImpl> getVersionedGuiButton() {
        return Laby4GuiButton.class;
    }
}
