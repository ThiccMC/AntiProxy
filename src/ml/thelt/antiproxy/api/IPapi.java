package ml.thelt.antiproxy.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPapi {
    public static Plugin plugin = Main.getPlugin(Main.class);
    public static String[] get(String ip) {
        checkAsync();

        String[] location = new String[3];
        try {
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
                
            String line = "";
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder res = new StringBuilder();
            while ((line=br.readLine()) != null) {
                res.append(line);
            }
            br.close();
            JsonObject json = new JsonParser().parse(res.toString()).getAsJsonObject();
            location[0] = json.get("regionName").getAsString();
            location[1] = json.get("city").getAsString();
            location[2] = json.get("country").getAsString();
            return location;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private static final void checkAsync() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Attempted to execute a database operation from the server thread!");
        }
    }
}