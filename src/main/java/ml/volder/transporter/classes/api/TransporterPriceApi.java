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

            // Parse JSON response
            JsonElement jsonElement = JsonParser.parseString(response.toString());
            return jsonElement.getAsJsonObject();
        } catch (Exception e) {
            UnikAPI.LOGGER.debug("Failed to get JSON from URL: " + stringURL, Logger.DEBUG_LEVEL.MEDIUM);
        }
        return null;
    }

    private String getUrl() {
        if(url == null) {
            JsonObject jsonObject = getJsonFromUrl("https://raw.githubusercontent.com/TransporterAddon/TransporterPriceAPI/main/info.json");

            String url = (jsonObject != null && jsonObject.get("url") != null) ? jsonObject.get("url").getAsString() : null;

            if(url != null) {
                this.url = url;
            } else {
                this.url = "https://transporter-price-api-479a42778d13.herokuapp.com";
            }

            UnikAPI.LOGGER.debug("Transporter Price API URL: " + url, Logger.DEBUG_LEVEL.MEDIUM);
        }
        return url;
    }

    public int getSellValueFromPriceServer(String itemName) {
        JsonObject jsonObject = getJsonFromUrl(getUrl() + "/items/" + itemName);
        if(jsonObject == null) {
            return 0;
        }
        return jsonObject.get("itemValue") == null ? 0 : jsonObject.get("itemValue").getAsInt();
    }

}
