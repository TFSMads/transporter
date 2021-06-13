package dk.transporter.mads_gamer_dk.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dk.transporter.mads_gamer_dk.utils.GetUUID;

import java.io.IOException;

import static dk.transporter.mads_gamer_dk.api.getUsers.getUsersJson;

public class validateUser {

    public static boolean isSubscriber(String user) throws Exception {

        String uuid = GetUUID.getUUID(user);

        JsonObject users = getUsersJson();

        JsonArray usersElement = users.get("users").getAsJsonArray();

        for(JsonElement jsonElement : usersElement){
            if(jsonElement.getAsString().equals(uuid)){
                return true;
            }

        }
        return false;

    }

}
