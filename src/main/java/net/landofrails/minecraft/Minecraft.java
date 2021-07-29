package net.landofrails.minecraft;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Minecraft {

    private static Minecraft minecraft;

    private Minecraft() {

    }

    public static Minecraft getInstance() {
        if (minecraft == null)
            minecraft = new Minecraft();
        return minecraft;
    }

    public boolean login(String email, String password) {

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://authserver.mojang.com/authenticate");

            StringEntity stringEntity = new StringEntity(getHttpEntity(email, password).toString(), "application/json", "UTF-8"
            );
            httppost.setEntity(stringEntity);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());

            if (response.getStatusLine().getStatusCode() != 200)
                return false;

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    Reader reader = new InputStreamReader(instream, "UTF-8");
                    JsonObject result = new Gson().fromJson(reader, JsonObject.class);

                    System.out.println(result.toString());

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    private JsonObject getHttpEntity(String email, String password) {

        JsonObject object = new JsonObject();
        JsonObject agent = new JsonObject();

        agent.addProperty("name", "Minecraft");
        agent.addProperty("version", 1);

        object.add("agent", agent);
        object.addProperty("username", email);
        object.addProperty("password", password);
        object.addProperty("requestUser", true);

        return object;
    }

}
