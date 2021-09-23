package ml.thelt.antiproxy.api.sub;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPHub {
    private static Plugin plugin = Main.getPlugin(Main.class);

    public static String check(String ip) {
        try {
            URL url = new URL("https://v2.api.iphub.info/ip/" + ip);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("X-Key", plugin.getConfig().getString("api.iphub.key"));

            String line = "";
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder res = new StringBuilder();
            while ((line=br.readLine()) != null) {
                res.append(line);
            }
            br.close();

            JsonObject json = new JsonParser().parse(res.toString()).getAsJsonObject();
            String result = json.get("block").getAsString();
            if (result.equals("0")) {
                result = "false";
            }
            if (result.equals("1")) {
                result = "true";
            }
            if (result.equals("2")) {
                result = "n/a";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
