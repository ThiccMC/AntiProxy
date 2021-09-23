package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class ClearData {
    private Main plugin;
    public ClearData(Main plugin) {
        this.plugin = plugin;
    }

    public void info(CommandSender sender) {
        List<String> list = plugin.getMessages().getStringList("messages.cleardata");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
        }
    }

    public void run(CommandSender sender, String[] args, DatabaseHandler db) {
        if (args[1].equalsIgnoreCase("player_ip")) {
            clearList("Player IP", db);
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }

        if (args[1].equalsIgnoreCase("good_ip")) {
            clearData(true, db);
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }

        if (args[1].equalsIgnoreCase("proxy_ip")) {
            clearList("VPN/Proxy", db);
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }

        if (args[1].equalsIgnoreCase("bad_ip")) {
            clearData(false, db);
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }

        if (args[1].equalsIgnoreCase("ip_database")) {
            clearData(true, db);
            clearData(false, db);
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }

        if (args[1].equalsIgnoreCase("staff")) {
            plugin.getConfig().set("staff", "");
            plugin.saveConfig();
            plugin.reloadConfig();
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }

        if (args[1].equalsIgnoreCase("all")) {
            if (sender instanceof Player) {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.consoleonly"), plugin)));
                return;
            }
            clearData(true, db);
            clearData(false, db);
            plugin.getConfig().set("staff", "");
            plugin.saveConfig();
            plugin.reloadConfig();
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.cleardata.done"), plugin, "clear_data", args[1])));
        }
    }

    public void clearList(String ip_type, DatabaseHandler db) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                db.clearList(ip_type);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void clearData(Boolean status, DatabaseHandler db) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                db.clearData(status);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
