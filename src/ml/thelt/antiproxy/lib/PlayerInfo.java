package ml.thelt.antiproxy.lib;

import dev.nicho.rolesync.db.DatabaseHandler;
import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.api.Discord;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.sql.SQLException;

public class PlayerInfo {
    public static Plugin plugin = Main.getPlugin(Main.class);
    private static Chat chat = null;
    private static DatabaseHandler db = null;

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static String[] getPlayerInfo(Player p) throws SQLException {
        if (p.isOnline()) {
            String[] info = new String[3];
            /* Player: TheLT
             * Rank: [] // if Vault is available
             * IP: 127.0.0.1
             * Location: Localhost
             * Discord: TheLT#0100
             */
            if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
                info[0] = chat.getPlayerPrefix(String.valueOf(p.getWorld()), p);
            }
            info[1] = p.getAddress().getHostString();
            if (plugin.getServer().getPluginManager().getPlugin("DiscordRoleSync") != null) {
                DatabaseHandler.LinkedUserInfo user = db.getLinkedUserInfo(p.getUniqueId().toString());
                if (user != null) {
                    Discord.getDiscordTag(user.discordId, tag -> {
                        info[2] = tag;
                    });
                } else {
                    info[2] = null;
                }
            }
            return info;
        }
        return null;
    }

    public static Chat getChat() {
        return chat;
    }

    public static String getPlayerIP(Player p) {
        return p.getAddress().getHostString();
    }
}
