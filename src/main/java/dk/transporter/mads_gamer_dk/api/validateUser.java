package dk.transporter.mads_gamer_dk.api;


import dk.transporter.mads_gamer_dk.utils.GetUUID;


import static dk.transporter.mads_gamer_dk.api.getUsers.getUsersJson;

public class validateUser {

    public static boolean isSubscriber() throws Exception {
        return getUsersJson();
    }

}
