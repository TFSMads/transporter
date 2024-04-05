package ml.volder.unikapi.api.minecraft;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;

import java.net.SocketAddress;
import java.util.Map;

public interface MinecraftAPI {
    boolean isInGame();
    boolean isSingleplayer();

    boolean isF3MenuOpen();
    String filterAllowedCharacters(String inputString);
    boolean isAllowedCharacter(char character);
    String translateLanguageKey(String translateKey);

    void openMainMenu();

    /**
     * @return Returns true if minecraft version is prior to version 1.13
     */
    boolean isLegacy();

    Map<String, Integer> getScoreBoardLines();
    String getScoreBoardTitle();

    ApiProvider<MinecraftAPI> apiProvider = new ApiProvider<>("MinecraftAPI");

    static MinecraftAPI getAPI() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.api.minecraft.impl", ApiReferenceStorage::getMinecraftAPI, MinecraftAPI.class);
    }
}
