package dk.transporter.mads_gamer_dk.api;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class getUsers {



    public static JsonObject getUsersJson() throws IOException {
        String sURL = "https://tfsmads.github.io/Data/Users.json";
        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();



        StringWriter writer = new StringWriter();
        IOUtils.copy((InputStream) request.getContent(), writer, StandardCharsets.UTF_8.name());
        String json = writer.toString();

        //System.out.println("JSON STRING: " + json);

        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        //System.out.println("JSON OBJECT: " + jsonObject);

        return jsonObject;

    }


}
