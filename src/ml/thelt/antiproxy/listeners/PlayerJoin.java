package ml.thelt.antiproxy.listeners;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.api.IPapi;
import ml.thelt.antiproxy.api.ProxyCheck;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.List;

public class PlayerJoin implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private final DatabaseHandler db;

    public PlayerJoin(DatabaseHandler db) {
        this.db = db;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (plugin.getConfig().getBoolean("enable")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                // Get event player and player's IP
                Player p = event.getPlayer();
                String ip = p.getAddress().getHostString();

                // Check if IP is localhost
                if (ip.equals("127.0.0.1")) {
                    String location = "Localhost";
                    String operation = plugin.getConfig().getString("messages.staffnotify.connected");
                    String status = plugin.getConfig().getString("messages.staffnotify.good");
                    notifyStaff(p, ip, location, operation, status, "localhost");
                    return;
                }

                // Get Player's location - ip-api.com
                String[] location = IPapi.get(ip);
                if (p.hasPermission("antiproxy.admin") || p.isOp()) {
                    String operation = plugin.getConfig().getString("messages.staffnotify.connected");
                    String status = plugin.getConfig().getString("messages.staffnotify.good");
                    notifyStaff(p, ip, String.join(", ", location), operation, status, "Staff");
                    return;
                }

                // Get database
                DatabaseHandler.GetInternetProtocol dbip;
                try {
                    dbip = db.getIP(ip);
                } catch (SQLException e) {
                    System.out.println(Util.chat("&cError while getting data from database!"));
                    e.printStackTrace();
                    return;
                }

                if (dbip != null) {
                    Boolean ip_status = dbip.status;
                    String proxy_type = dbip.proxy_type;
                    if (ip_status) {
                        String operation = plugin.getConfig().getString("messages.staffnotify.connected");
                        String status = plugin.getConfig().getString("messages.staffnotify.good");
                        notifyStaff(p, ip, String.join(", ", location), operation, status, proxy_type);
                    } else {
                        String operation = plugin.getConfig().getString("messages.staffnotify.kicked");
                        String status = plugin.getConfig().getString("messages.staffnotify.bad");
                        if (dbip.proxy_type.equals("Blacklisted")) {
                            List<String> list = plugin.getConfig().getStringList("messages.staffnotify.banmessage");
                            String[] array = list.toArray(new String[0]);
                            kick(p, Util.chat(String.join("\n", array)));
                            notifyStaff(p, ip, String.join(", ", location), operation, status, proxy_type);
                        } else {
                            List<String> list = plugin.getConfig().getStringList("messages.staffnotify.kickmessage");
                            String[] array = list.toArray(new String[0]);
                            kick(p, Util.chat(String.join("\n", array)));
                            notifyStaff(p, ip, String.join(", ", location), operation, status, proxy_type);
                        }
                    }
                } else {
                    String[] proxy = ProxyCheck.get(ip);
                    String operation = "";
                    String status = "";

                    if (proxy[0].equals("no")) {
                        operation = plugin.getConfig().getString("messages.staffnotify.connected");
                        status = plugin.getConfig().getString("messages.staffnotify.good");
                        saveIP(ip, proxy[1], true);
                        notifyStaff(p, ip, String.join(", ", location), operation, status, proxy[1]);
                    } else {
                        operation = plugin.getConfig().getString("messages.staffnotify.kicked");
                        status = plugin.getConfig().getString("messages.staffnotify.bad");
                        List<String> list = plugin.getConfig().getStringList("messages.staffnotify.kickmessage");
                        String[] array = list.toArray(new String[0]);
                        kick(p, Util.chat(String.join("\n", array)));
                        saveIP(ip, proxy[1], false);
                        notifyStaff(p, ip, String.join(", ", location), operation, status, proxy[1]);
                    }
                }
            });
        }
    }

    public void notifyStaff(Player p, String ip, String location, String operation, String status, String proxy_type) {
        consoleLog(p, ip, location, operation, status, proxy_type);
        List<String> list = plugin.getConfig().getStringList("messages.staffnotify.notify");
        String[] array = list.toArray(new String[0]);
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("antiproxy.admin") || staff.isOp()) {
                if (plugin.getConfig().contains("staff." + staff.getName() + ".notify")) {
                    if (plugin.getConfig().getBoolean("staff." + staff.getName() + ".notify")) {
                        PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(staff);
                        if (session.isLoggedIn()) {
                            for (String msg: array) {
                                if (!plugin.getConfig().getBoolean("check-location")) {
                                    if (!msg.contains("%location%")) {
                                        staff.sendMessage(Util.chat(Placeholders.get(msg, ip, null, p.getDisplayName(), location, operation, status, proxy_type)));
                                    }
                                } else {
                                    staff.sendMessage(Util.chat(Placeholders.get(msg, ip, null, p.getDisplayName(), location, operation, status, proxy_type)));
                                }
                            }
                        }
                    }
                } else {
                    plugin.getConfig().set("staff." + staff.getName() + ".notify", true);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(staff);
                    if (session.isLoggedIn()) {
                        for (String msg: array) {
                            if (!plugin.getConfig().getBoolean("check-location")) {
                                if (!msg.contains("%location%")) {
                                    staff.sendMessage(Util.chat(Placeholders.get(msg, ip, null, p.getDisplayName(), location, operation, status, proxy_type)));
                                }
                            } else {
                                staff.sendMessage(Util.chat(Placeholders.get(msg, ip, null, p.getDisplayName(), location, operation, status, proxy_type)));
                            }
                        }
                    }
                }
            }
        }
    }

    public void consoleLog(Player p, String ip, String location, String operation, String status, String proxy_type) {
        List<String> list = plugin.getConfig().getStringList("messages.staffnotify.console");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            System.out.println(Util.chat(Placeholders.get(msg, ip, null, p.getDisplayName(), location, operation, status, proxy_type)));
        }
    }

    public void saveIP(String ip, String proxy_type, Boolean status) {
        if (!ip.equals("127.0.0.1")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    db.addIP(ip, proxy_type, status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void kick(Player p, String reason) {
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.kickPlayer(Util.chat(reason));
            }
        });
    }
}
