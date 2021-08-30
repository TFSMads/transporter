package dk.transporter.mads_gamer_dk.utils.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;


public class DataHandler {
    public static void Write(String filePath) throws Exception {
        System.out.println("Write to file: " + filePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(new FileReader(filePath));
        System.out.println(object);
        object.put("test", 0);
        FileWriter fileWrite = new FileWriter(filePath);
        fileWrite.write(gson.toJson(object));
        fileWrite.close();
    }
}
