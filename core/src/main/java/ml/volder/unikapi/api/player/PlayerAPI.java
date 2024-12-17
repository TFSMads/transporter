package ml.volder.unikapi.api.player;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;

import java.util.UUID;

public interface PlayerAPI {
    void sendCommand(String command);

    UUID getUUID();
    void displayChatMessage(String message);
    void displayActionBarMessage(String message);

    ApiProvider<PlayerAPI> apiProvider = new ApiProvider<>("PlayerAPI");

    static PlayerAPI getAPI() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.api.player.impl", ApiReferenceStorage::getPlayerAPI, PlayerAPI.class);
    }
}
