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

public class ProxyCheck { ;
    private static Plugin plugin = Main.getPlugin(Main.class);

    public static String[] get(String ip) {
        checkAsync();
        String[] result = new String[2];
        result[0] = "no";
        result[1] = null;
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
            result[0] = json.getAsJsonObject(ip).get("proxy").getAsString();
            result[1] = json.getAsJsonObject(ip).get("type").getAsString();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    private static final void checkAsync() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Attempted to execute a database operation from the server thread!");
        }
    }
}
