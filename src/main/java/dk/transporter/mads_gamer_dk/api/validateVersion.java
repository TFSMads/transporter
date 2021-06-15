package dk.transporter.mads_gamer_dk.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class validateVersion {

    public static boolean isValidVersion(String version) throws Exception {

        JsonObject versions = getVersionControl.getVestionControlJson();

        JsonArray usersElement = versions.get("versions").getAsJsonArray();

        for(JsonElement jsonElement : usersElement){
            if(jsonElement.getAsString().equals(version)){
                return true;
            }

        }
        return false;

    }

}
