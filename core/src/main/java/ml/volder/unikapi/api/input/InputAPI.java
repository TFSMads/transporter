package ml.volder.unikapi.api.input;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;

public interface InputAPI {
    boolean isKeyDown(Key key);
    int getEventDWheel();
    boolean isCtrlKeyDown();
    boolean isShiftKeyDown();
    boolean isAltKeyDown();
    String getKeyName(Key key);
    void enableRepeatEvents(boolean enable);

    ApiProvider<InputAPI> apiProvider = new ApiProvider<>("InputAPI");

    static InputAPI getAPI() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.api.input.impl", ApiReferenceStorage::getInputAPI, InputAPI.class);
    }
}
