package ml.volder.unikapi.api.player;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.UUID;

public interface PlayerAPI {
    void sendCommand(String command);
    void openGuiScreen(WrappedGuiScreen screen);
    /**
     * Returns current WrappedGuiScreen if current guiscreen is apart of the WrappedGuiScreen implementaion.
     * NOTE: if the current screen isn't a WrappedGuiScreen null will be returned.
     *
     * @return current WrappedGuiScreen
     */
    WrappedGuiScreen getCurrentScreen();

    /**
     * Returns true if the current guiscreen is null.
     *
     * @return true if no guiscreen is open.
     */
    boolean hasOpenScreen();


    UUID getUUID();
    void displayChatMessage(String message);
    void displayActionBarMessage(String message);

    ApiProvider<PlayerAPI> apiProvider = new ApiProvider<>("PlayerAPI");

    static PlayerAPI getAPI() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.api.player.impl", ApiReferenceStorage::getPlayerAPI, PlayerAPI.class);
    }
}
