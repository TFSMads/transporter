package dk.transporter.mads_gamer_dk.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.utils.GetUUID;


import static dk.transporter.mads_gamer_dk.api.getUsers.getUsersJson;

public class validateUser {

    public static boolean isSubscriber(String user) throws Exception {

        String uuid = GetUUID.getUUID(user);

        JsonObject users = getUsersJson();

        JsonArray usersElement = users.get("users-1.3").getAsJsonArray();

        for(JsonElement jsonElement : usersElement){
            if(jsonElement.getAsString().equals(uuid)){
                return true;
            }

        }
        return false;

    }

}
