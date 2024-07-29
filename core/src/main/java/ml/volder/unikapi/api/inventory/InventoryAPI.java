package ml.volder.unikapi.api.inventory;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.types.Material;

public interface InventoryAPI {
    int getAmount(Material material, int itemDamage);

    ApiProvider<InventoryAPI> apiProvider = new ApiProvider<>("InventoryAPI");



    static InventoryAPI getAPI() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.api.inventory.impl", ApiReferenceStorage::getInventoryAPI, InventoryAPI.class);
    }
}
