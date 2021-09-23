package ml.thelt.antiproxy.commands;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.HoverText;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player implements CommandExecutor {
    private Main plugin;
    public Player(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!sender.hasPermission("antiproxy.admin") || !sender.isOp()) {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.noperm"), plugin)));
            return true;
        }

        if (!plugin.getConfig().getBoolean("enable")) {
            if (sender instanceof org.bukkit.entity.Player) {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.isdisable"), plugin)));
                return true;
            }
        }

        if (args.length == 0) {

        }

        if (args.length == 1) {
            org.bukkit.entity.Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                List<String> list = plugin.getMessages().getStringList("messages.player.check");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    Pattern pattern = Pattern.compile("#(.*?)#");
                    Matcher matcher = pattern.matcher(msg);
                    if (matcher.find()) {
                        HoverText.getHover(msg, matcher.group(0), plugin, sender);
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(msg, plugin, "player_info", target.getDisplayName(), target.getName(), target.getAddress().getHostString())));
                    }
                }
            } else {

            }
        }

        if (args.length == 2) {
            org.bukkit.entity.Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                List<String> list = plugin.getMessages().getStringList("messages.player.option");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg, plugin, "player_option", target.getDisplayName(), target.getAddress().getHostString())));
                }
            } else {

            }
        }
        return true;
    }
}
