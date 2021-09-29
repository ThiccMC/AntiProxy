package ml.thelt.antiproxy.listeners;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.api.Location;
import ml.thelt.antiproxy.api.Checker;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.List;

public class PlayerJoin implements Listener {
    private final DatabaseHandler db;
    private Main plugin;

    public PlayerJoin(Main plugin, DatabaseHandler db) {
        this.plugin = plugin;
        this.db = db;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("enable")) {
            return;
        }

        long start = System.nanoTime();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Player p = event.getPlayer();
            String ip = p.getAddress().getHostString();
            String playerAction;

            /**
             * staff_notify() and log_console() functions
             * @param p : Event player (Player)
             * @param playerAction : Action for player (String)
             * @param ip : Player's IP (String)
             * @param proxy_type : Player's IP type (String)
             * @param location : Player's location (String)
             * @param score : Proxy score (Integer)
             * @param start: debug (time)
            **/

            if (ip.equals("127.0.0.1") || ip.startsWith("192.168.")) {
                playerAction = plugin.getMessages().getString("messages.staffnotify.connected");
                staff_notify(p, playerAction, ip, "localhost" ,"localhost", 0, start);
                log_console(p, playerAction, ip, "localhost", "localhost", 0, start);
                return;
            }

            String location = Location.get(ip);

            if (p.hasPermission("antiproxy.admin")) {
                playerAction = plugin.getMessages().getString("messages.staffnotify.connected");
                staff_notify(p, playerAction, ip, "Staff IP", location, 0, start);
                log_console(p, playerAction, ip, "Staff IP", location, 0, start);
                return;
            }

            if (p.hasPermission("antiproxy.bypass")) {
                playerAction = plugin.getMessages().getString("messages.staffnotify.connected");
                staff_notify(p, playerAction, ip, "Bypass Permission", location, 0, start);
                log_console(p, playerAction, ip, "Bypass Permission", location, 0, start);
                return;
            }

            DatabaseHandler.GetInternetProtocol database_ip;
            try {
                database_ip = db.getIP(ip);
            } catch (SQLException e) {
                System.out.println(Util.chat("&cError while getting data from database!"));
                e.printStackTrace();
                return;
            }

            if (database_ip != null) {
                boolean ip_status = database_ip.status;
                String ip_type = database_ip.ip_type;
                int score = database_ip.score;
                if (ip_status) {
                    playerAction = plugin.getMessages().getString("messages.staffnotify.connected");
                    staff_notify(p, playerAction, ip, ip_type, location, score, start);
                    log_console(p, playerAction, ip, ip_type, location, score, start);
                } else {
                    String reason = "";
                    playerAction = plugin.getMessages().getString("messages.staffnotify.kicked");
                    if (ip_type.equals("VPN/Proxy")) { reason = "messages.staffnotify.kickmessage"; }
                    if (ip_type.equals("Blacklisted")) { reason = "messages.staffnotify.banmessage"; }
                    playerKick(p, reason);
                    staff_notify(p, playerAction, ip, ip_type, location, score, start);
                    log_console(p, playerAction, ip, ip_type, location, score, start);
                }
            } else {
                int score = Checker.get(ip);
                if (score >= plugin.getConfig().getInt("score.kick.at")) {
                    saveIP(ip, score, "VPN/Proxy", false);
                    playerAction = plugin.getMessages().getString("messages.staffnotify.kicked");
                    playerKick(p, "messages.staffnotify.kickmessage");
                    staff_notify(p, playerAction, ip, "VPN/Proxy", location, score, start);
                    log_console(p, playerAction, ip, "VPN/Proxy", location, score, start);
                } else {
                    saveIP(ip, score, "Player IP", true);
                    playerAction = plugin.getMessages().getString("messages.staffnotify.connected");
                    staff_notify(p, playerAction, ip, "Player IP",location, score, start);
                    log_console(p, playerAction, ip, "Player IP", location, score, start);
                }
            }
        });
    }

    public void staff_notify(Player p, String playerAction, String ip, String proxy_type, String location, int score, long time) {
        NotifyStaffPlayers.run(p, playerAction, ip, proxy_type, location, score, time, plugin);
    }

    public void log_console(Player p, String playerAction, String ip, String proxy_type, String location, int score, long time) {
        List<String> list = plugin.getMessages().getStringList("messages.staffnotify.console");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            System.out.println(Util.chat(Placeholders.get(msg, plugin, "player_connect", p.getDisplayName(), playerAction, ip, proxy_type, location, String.valueOf(score))));
        }
        if (plugin.getConfig().getBoolean("debug")) {
            long elapsedTime = System.nanoTime() - time;
            long milliseconds = elapsedTime / 1000000;
            System.out.println(Util.chat("&8[Debug] &7It took " + milliseconds + "ms to check player's IP."));
        }
    }

    public void saveIP(String ip, int score, String ip_type, Boolean status) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                db.addIP(ip, score, ip_type, status);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void playerKick(Player p, String path) {
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                List<String> list = plugin.getMessages().getStringList(path);
                String[] array = list.toArray(new String[0]);
                for (String reason: array) {
                    p.kickPlayer(Util.chat(reason));
                }
            }
        });
    }
}
