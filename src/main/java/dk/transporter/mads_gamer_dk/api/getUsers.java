package dk.transporter.mads_gamer_dk.api;

import com.google.gson.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class getUsers {



    public static Boolean getUsersJson() throws IOException {

        String sURL = "https://transporter-4c63c-default-rtdb.europe-west1.firebasedatabase.app/Users/" + Minecraft.getMinecraft().thePlayer.getUniqueID() + ".json";
        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();



        StringWriter writer = new StringWriter();
        IOUtils.copy((InputStream) request.getContent(), writer, StandardCharsets.UTF_8.name());
        String out = writer.toString();

        Boolean bool = Boolean.valueOf(out);

        return bool;

    }


}
