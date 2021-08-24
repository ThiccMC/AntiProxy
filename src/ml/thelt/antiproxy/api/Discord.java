package ml.thelt.antiproxy.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Discord {
    public static Plugin plugin = Main.getPlugin(Main.class);

    public static void getDiscordTag(String id, Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String tag = null;
            try {
                URL url = new URL("https://api.thelt.ml/v2/discord/getUser?id=" + id);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
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
                tag = json.get("tag").getAsString();
            }
            catch (Exception e) {
                System.out.println(e);
            }
            String finaltag = tag;
            Bukkit.getScheduler().runTask(plugin, () -> callback.onRetrieve(finaltag));
        });
    }

    public interface Callback {
        void onRetrieve(String result);
    }
}
