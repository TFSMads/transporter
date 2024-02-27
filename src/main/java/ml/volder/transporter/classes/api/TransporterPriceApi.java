package ml.volder.transporter.classes.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransporterPriceApi {

    private static TransporterPriceApi instance;
    private String url;

    public static TransporterPriceApi getInstance() {
        if(instance == null) {
            instance = new TransporterPriceApi();
        }
        return instance;
    }
    private JsonObject getJsonFromUrl(String stringURL) {
        try {
            // URL of the JSON endpoint
            URL url = new URL(stringURL);

            // Create HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            //JsonElement jsonElement = JsonParser.parseString(response.toString());
            return new JsonParser().parse(response.toString()).getAsJsonObject();
        } catch (Exception e) {
            UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.FINER, e);
            UnikAPI.LOGGER.debug("Failed to get JSON from URL: " + stringURL, Logger.DEBUG_LEVEL.MEDIUM);
        }
        return null;
    }

    private String getUrl() {
        return "https://transporter-price-api-479a42778d13.herokuapp.com";

    }

    public int getSellValueFromPriceServer(String itemName) {
        UnikAPI.LOGGER.debug("Hent item pris: " + itemName, Logger.DEBUG_LEVEL.LOW);
        JsonObject jsonObject = getJsonFromUrl(getUrl() + "/items/" + itemName);
        UnikAPI.LOGGER.debug("Hent item pris (response): " + jsonObject, Logger.DEBUG_LEVEL.TESTING);
        if(jsonObject == null) {
            return 0;
        }
        return jsonObject.get("itemValue") == null ? 0 : jsonObject.get("itemValue").getAsInt();
    }

}
