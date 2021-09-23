package ml.thelt.antiproxy.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.api.sub.Funkemunky;
import ml.thelt.antiproxy.api.sub.IPHub;
import ml.thelt.antiproxy.api.sub.ProxyCheck;
import ml.thelt.antiproxy.api.sub.VPNApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Checker { ;
    private static Plugin plugin = Main.getPlugin(Main.class);

    public static int get(String ip) {
        checkAsync();

        int total_score = 0;
        int api_count = 0;

        if (plugin.getConfig().getBoolean("api.proxycheck.enable")) {
            String result = ProxyCheck.check(ip);
            if (result != null) {
                if (result.equals("yes")) {
                    total_score = total_score + 100;
                    System.out.print("ProxyCheck: 100");
                }
                api_count = api_count + 1;
            }
        }

        if (plugin.getConfig().getBoolean("api.vpnapi.enable")) {
            String result = VPNApi.check(ip);
            if (result != null) {
                if (result.equals("true")) {
                    total_score = total_score + 100;
                    System.out.print("VPNApi: 100");
                }
                api_count = api_count + 1;
            }
        }

        if (plugin.getConfig().getBoolean("api.funkemunky.enable")) {
            String result = Funkemunky.check(ip);
            if (result != null) {
                if (result.equals("true")) {
                    total_score = total_score + 100;
                    System.out.print("Funkemunky: 100");
                }
                api_count = api_count + 1;
            }
        }

        if (plugin.getConfig().getBoolean("api.iphub.enable")) {
            String result = IPHub.check(ip);
            if (result != null) {
                if (result.equals("true")) {
                    total_score = total_score + 100;
                    System.out.print("IpHub: 100");
                } else if (result.equals("n/a")) {
                    total_score = total_score + 50;
                    System.out.print("IpHub: 50");
                }
                api_count = api_count + 1;
            }
        }

        total_score = total_score / api_count;

        return total_score;
    }

    private static final void checkAsync() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Attempted to execute a HTTP request operation from the server thread!");
        }
    }
}
