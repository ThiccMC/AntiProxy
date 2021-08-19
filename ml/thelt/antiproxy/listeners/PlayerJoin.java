package ml.thelt.antiproxy.listeners;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.api.IPapi;
import ml.thelt.antiproxy.api.ProxyCheck;
import ml.thelt.antiproxy.lib.BlacklistIP;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.lib.WhitelistIP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoin implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);

    ProxyCheck proxyCheck = new ProxyCheck();
    IPapi iplocation = new IPapi();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (plugin.getConfig().getBoolean("enable") == true) {
            Player p = event.getPlayer();
            String playerIP = p.getAddress().getHostString();

            if (playerIP.equals("127.0.0.1")) {
                String location = "Localhost";
                staffNotify(p, playerIP, location, "Localhost", "&2");
                savePlayer(p, playerIP);
                return;
            }

            String location = iplocation.ipLoc(playerIP);
            if (p.hasPermission("antiproxy.admin") || p.isOp()) {
                savePlayer(p, playerIP);
                staffNotify(p, playerIP, location, "Staff - Canceled IP Checking", "&b");
                return;
            }

            String whitelisted = WhitelistIP.getWhitelist(playerIP);
            if (whitelisted.equals("true")) {
                staffNotify(p, playerIP, location, "OK - Connected with Good IP (Cache)", "&a");
                return;
            }

            String blacklisted = BlacklistIP.getBlacklist(playerIP);
            if (blacklisted.equals("true")) {
                p.kickPlayer(Util.chat("&cUsing Proxy/VPN is not allowed!\n\n&cIf you think your IP was detected as Proxy/VPN by accident,\n&cplease join &6https://qtpc.tech/discord &cand contact a staff member!"));
                staffNotify(p, playerIP, location, "Bad - Kicked due to Bad IP (Cache)", "&c");
                return;
            }

            String proxy = proxyCheck.ProxyCheck(playerIP);
            if (proxy.equals("no")) {
                staffNotify(p, playerIP, location, "OK - Connected with Good IP", "&a");
                savePlayer(p, playerIP);
            } else {
                p.kickPlayer(Util.chat("&cUsing Proxy/VPN is not allowed!"));
                blacklistIP(p, playerIP);
                staffNotify(p, playerIP, location, "Bad - Kicked due to Bad IP", "&c");
            }
        }
    }

    public void staffNotify(Player p, String ip, String location, String status, String statusColor) {
        System.out.println(Util.chat("&e"+ p.getDisplayName() + " connected! &6[ IP: &e" + ip + " &6- Location: &e"+ location + " &6- Status: " + statusColor + status + " &6]"));
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("antiproxy.admin") || staff.isOp()) {
                if (plugin.getConfig().getBoolean("staff." + staff.getDisplayName() + ".notification") == true) {
                    staff.sendMessage(Util.chat(statusColor + "&m-----------------------------------"));
                    staff.sendMessage(Util.chat("&a&l" + p.getDisplayName() + " &e&lconnected!"));
                    staff.sendMessage(Util.chat("&6IP: &e" + ip));
                    staff.sendMessage(Util.chat("&6Location: &e" + location));
                    staff.sendMessage(Util.chat("&6Status: " + statusColor + status));
                    staff.sendMessage(Util.chat(statusColor + "&m-----------------------------------"));
                }
            }
        }
    }

    public void savePlayer(Player p, String ip) {
        if (!ip.equals("127.0.0.1")) {
            WhitelistIP.saveWhitelist(ip);
        }
        if (p.hasPermission("antiproxy.admin") || p.isOp()) {
            if (!plugin.getConfig().contains("staff." + p.getDisplayName() + ".notification")) {
                plugin.getConfig().set("staff." + p.getDisplayName() + ".notification",true);
                plugin.saveConfig();
                plugin.reloadConfig();
            }
        }
    }

    public void blacklistIP(Player p, String ip) {
        BlacklistIP.saveBlacklist(ip);
    }
}
