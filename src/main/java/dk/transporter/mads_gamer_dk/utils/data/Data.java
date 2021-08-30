package dk.transporter.mads_gamer_dk.utils.data;

import com.google.gson.JsonObject;

public class Data {
    private JsonObject data = new JsonObject();

    public Data() {
        this.data = new JsonObject();
        System.out.println("CONFIG: " + this.data);
    }

    public JsonObject getData() {
        return this.data;
    }
}