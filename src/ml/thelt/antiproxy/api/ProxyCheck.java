package ml.thelt.antiproxy.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.nicho.rolesync.db.DatabaseHandler;
import ml.thelt.antiproxy.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class ProxyCheck { ;
    private static Plugin plugin = Main.getPlugin(Main.class);

    public static void get(String ip, Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String[] result = new String[2];
            result[0] = "no";
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
                result[0] = json.getAsJsonObject(ip).get("proxy").getAsString();
                result[1] = json.getAsJsonObject(ip).get("type").getAsString();
            } catch (Exception e) {
                System.out.println(e);
            }
            String[] finalresult = result;
            Bukkit.getScheduler().runTask(plugin, () -> callback.onRetrieve(finalresult));
        });
    }

    public interface Callback {
        void onRetrieve(String[] result);
    }
}
