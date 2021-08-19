package ml.thelt.antiproxy.lib;

import org.bukkit.entity.Player;

public class PlayerInfo {
    public static String getPlayerIP(Player p) {
        if (p.isOnline()) {
            return p.getAddress().getHostString();
        }
        return null;
    }
}
