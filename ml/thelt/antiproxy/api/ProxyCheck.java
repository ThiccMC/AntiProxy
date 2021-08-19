package ml.thelt.antiproxy.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProxyCheck {
    private Plugin plugin = Main.getPlugin(Main.class);

    public String ProxyCheck(String ip) {
        try {
            URL url = new URL("https://proxycheck.io/v2/"+ ip + "?&key=" + plugin.getConfig().getString("api-key.proxycheck")+ "&vpn=1&asn=1");
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
            String proxy = json.getAsJsonObject(ip).get("proxy").getAsString();
            return proxy;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "no";
    }
}
