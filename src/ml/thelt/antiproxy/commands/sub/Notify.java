package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Notify {
    private Main plugin;
    public Notify(Main plugin) {
        this.plugin = plugin;
    }

    public void info(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.playeronly"), plugin)));
            return;
        }

        Player p = (Player) sender;

        if (!plugin.getConfig().contains("staff." + p.getName() + ".notify")) {
            plugin.getConfig().set("staff." + p.getName() + ".notify", true);
        }

        List<String> list = plugin.getConfig().getStringList("messages.usage.notify");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            p.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
        }
    }

    public void set(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.playeronly"), plugin)));
            return;
        }

        Player p = (Player) sender;

        if (args[1].equalsIgnoreCase("true")) {
            plugin.getConfig().set("staff." + p.getName() + ".notify", true);
            plugin.saveConfig();
            plugin.reloadConfig();
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.newstatus.notify"), plugin, "notify", "true")));
        } else if (args[1].equalsIgnoreCase("false")) {
            plugin.getConfig().set("staff." + p.getName() + ".notify", false);
            plugin.saveConfig();
            plugin.reloadConfig();
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.newstatus.notify"), plugin, "notify", "false")));
        } else {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.invalidusage"), plugin)));
        }
    }
}
