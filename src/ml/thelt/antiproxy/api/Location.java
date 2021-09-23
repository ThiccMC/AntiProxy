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

public class Location {
    public static Plugin plugin = Main.getPlugin(Main.class);
    public static String get(String ip) {
        checkAsync();

        try {
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            String line = "";
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder res = new StringBuilder();
            while ((line=br.readLine()) != null) {
                res.append(line);
            }
            br.close();
            JsonObject json = new JsonParser().parse(res.toString()).getAsJsonObject();

            String location = formatting(json);
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String formatting(JsonObject json) {
        String conf_format = plugin.getConfig().getString("location.format");
        if (conf_format.endsWith(":")) {
            conf_format.substring(0, conf_format.length() - 1);
        }
        conf_format = conf_format.replace("%country%", json.get("country").getAsString());
        conf_format = conf_format.replace("%countryCode%", json.get("countryCode").getAsString());
        conf_format = conf_format.replace("%region%", json.get("region").getAsString());
        conf_format = conf_format.replace("%regionName%", json.get("regionName").getAsString());
        conf_format = conf_format.replace("%city%", json.get("city").getAsString());
        conf_format = conf_format.replace("%timezone%", json.get("timezone").getAsString());
        conf_format = conf_format.replace("%isp%", json.get("isp").getAsString());

        return conf_format;
    }

    private static final void checkAsync() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Attempted to execute a HTTP request operation from the server thread!");
        }
    }
}