package ml.volder.unikapi.api;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.inventory.InventoryAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.event.EventImpl;
import ml.volder.unikapi.keysystem.KeyMapper;
import ml.volder.unikapi.wrappers.guibutton.IGuiButtonImpl;
import ml.volder.unikapi.wrappers.guiscreen.IGuiScreenImpl;

public interface ApiReferenceStorage {
    DrawAPI getDrawAPI();
    InputAPI getInputAPI();
    InventoryAPI getInventoryAPI();
    MinecraftAPI getMinecraftAPI();
    PlayerAPI getPlayerAPI();

    Class<? extends EventImpl> getVersionedOpenSignEvent();
    Class<? extends EventImpl> getVersionedMainMenuOpenEvent();

    KeyMapper getVersionedKeyMapper();

    Class<? extends IGuiButtonImpl> getVersionedGuiButton();
    Class<? extends IGuiScreenImpl> getVersionedGuiScreen();
}
