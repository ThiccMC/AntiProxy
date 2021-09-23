package ml.thelt.antiproxy.api.sub;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProxyCheck {
    private static Plugin plugin = Main.getPlugin(Main.class);

    public static String check(String ip) {
        try {
            URL url = new URL("https://proxycheck.io/v2/"+ ip + "?&key=" + plugin.getConfig().getString("api-key.proxycheck")+ "&vpn=1&asn=1");
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
            String result = json.getAsJsonObject(ip).get("proxy").getAsString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
