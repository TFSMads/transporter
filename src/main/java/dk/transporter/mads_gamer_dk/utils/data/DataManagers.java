package dk.transporter.mads_gamer_dk.utils.data;

import com.google.gson.JsonObject;
import net.labymod.addon.AddonLoader;

import java.io.File;

public class DataManagers {

    private JsonObject mcmmoData;
    private DataManager<Data> mcmmoDataManager;

    private JsonObject serverData;
    private DataManager<Data> serverDataManager;


    public DataManagers(){
        this.mcmmoDataManager = new DataManager<Data>(new File(AddonLoader.getConfigDirectory() + "\\TransporterAddon", "mcmmoData.json"), Data.class);
        this.mcmmoData = (this.mcmmoDataManager.getSettings()).getData();

        this.serverDataManager = new DataManager<Data>(new File(AddonLoader.getConfigDirectory() + "\\TransporterAddon", "serverData.json"), Data.class);
        this.serverData = (this.serverDataManager.getSettings()).getData();
    }

    public void saveMCMMOData() {
        if (this.mcmmoDataManager != null) {
            this.mcmmoDataManager.save();
        }

    }

    public JsonObject getMCMMOData() {
        return this.mcmmoData;
    }

    public void saveServerData() {
        if (this.serverDataManager != null) {
            this.serverDataManager.save();
        }

    }

    public JsonObject getServerData() {
        return this.serverData;
    }

}
