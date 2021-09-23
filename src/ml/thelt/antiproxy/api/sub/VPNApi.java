package ml.thelt.antiproxy.api.sub;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VPNApi {
    private static Plugin plugin = Main.getPlugin(Main.class);

    public static String check(String ip) {
        try {
            URL url = new URL("https://vpnapi.io/api/"+ ip + "?key=" + plugin.getConfig().getString("api.vpnapi.key"));
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
            String[] result = new String[3];
            result[0] = json.getAsJsonObject("security").get("vpn").getAsString();
            result[1] = json.getAsJsonObject("security").get("proxy").getAsString();
            result[2] = json.getAsJsonObject("security").get("tor").getAsString();
            if (result[0].equals("true") || result[1].equals("true") || result[2].equals("true")) {
                return "true";
            }
            return "false";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
