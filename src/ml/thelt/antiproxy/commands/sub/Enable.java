package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Enable {
    private Main plugin;
    public Enable(Main plugin) {
        this.plugin = plugin;
    }

    public void info(CommandSender sender) {
        List<String> list = plugin.getMessages().getStringList("messages.usage.enable");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
        }
    }

    public void set(CommandSender sender, String bol) {
        if (bol.equals(String.valueOf(plugin.getConfig().getBoolean("enable")))) {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.alreadystatus"), plugin)));
        } else {
            if (bol.equals("true")) {
                plugin.getConfig().set("enable", true);
                plugin.saveConfig();
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.newstatus.plugin"), plugin)));
            } else if (bol.equals("false")) {
                plugin.getConfig().set("enable", false);
                plugin.saveConfig();
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.newstatus.plugin"), plugin)));
            } else {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.invalidusage"), plugin)));
            }
        }
    }
}
