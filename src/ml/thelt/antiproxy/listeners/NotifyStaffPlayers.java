package ml.thelt.antiproxy.listeners;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class NotifyStaffPlayers {
    /**
     * @param p : Event player (Player)
     * @param playerAction : Action for player (String)
     * @param ip : Player's IP (String)
     * @param proxy_type : Player's IP type (String)
     * @param location : Player's location (String)
     * @param score : Proxy score (Integer)
     * @param time : debug (long)
     **/

    public static void run(Player p, String playerAction, String ip, String proxy_type, String location, int score, long time, Main plugin) {
        for (Player staff: Bukkit.getOnlinePlayers()) {
            if (!staff.hasPermission("antiproxy.admin")) return;
            if (!plugin.getConfig().contains("staff." + staff.getName() + ".notify")) {
                addStaff(staff, plugin);
            }
            if (!(plugin.getConfig().getBoolean("staff." + staff.getName() + ".notify"))) return;
//            PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(staff);
//            if (!(session.isLoggedIn())) return;
            List<String> list = plugin.getMessages().getStringList("messages.staffnotify.notify");
            String[] array = list.toArray(new String[0]);
            for (String msg: array) {
               if (plugin.getConfig().getBoolean("location.check")) {
                   if (msg.contains("%ip%")) {
                       if (staff.hasPermission("antiproxy.showip") || staff.isOp()) {
                           staff.sendMessage(Util.chat(Placeholders.get(msg, plugin, "player_connect", p.getDisplayName(), playerAction, ip, proxy_type, location, String.valueOf(score))));
                       }
                   } else {
                       staff.sendMessage(Util.chat(Placeholders.get(msg, plugin, "player_connect", p.getDisplayName(), playerAction, ip, proxy_type, location, String.valueOf(score))));
                   }
               } else {
                   if (!msg.contains("%location%")) {
                       if (msg.contains("%ip%")) {
                           if (staff.hasPermission("antiproxy.showip") || staff.isOp()) {
                               staff.sendMessage(Util.chat(Placeholders.get(msg, plugin, "player_connect", p.getDisplayName(), playerAction, ip, proxy_type, location, String.valueOf(score))));
                           }
                       } else {
                           staff.sendMessage(Util.chat(Placeholders.get(msg, plugin, "player_connect", p.getDisplayName(), playerAction, ip, proxy_type, location, String.valueOf(score))));
                       }
                   }
               }
            }

            if (plugin.getConfig().getBoolean("debug")) {
                long elapsedTime = System.nanoTime() - time;
                long milliseconds = elapsedTime / 1000000;
                staff.sendMessage(Util.chat("&b[AntiProxy] &8[Debug] &7It took " + milliseconds + "ms to check player's IP."));
            }
        }
    }

    public static void addStaff(Player p, Main plugin) {
        plugin.getConfig().set("staff." + p.getName() + ".notify", true);
        plugin.saveConfig();
        plugin.reloadConfig();
    }
}
