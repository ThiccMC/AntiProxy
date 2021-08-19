package ml.thelt.antiproxy.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPapi {
    public String ipLoc(String ip) {
        try {
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String line = "";
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder res = new StringBuilder();
            while ((line=br.readLine()) != null) {
                res.append(line);
            }
            br.close();
            JsonObject json = new JsonParser().parse(res.toString()).getAsJsonObject();
            String location = new String();
            location = json.get("country").getAsString();
            return location;
        } catch (Exception e) {
            System.out.println(e);
        }
        String location = new String();
        location = "null";
        return location;
    }
}
