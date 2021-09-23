package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class API {
    private Main plugin;
    public API(Main plugin) {
        this.plugin = plugin;
    }

    public void info(CommandSender sender) {
        List<String> list = plugin.getMessages().getStringList("messages.api");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
        }
    }

    public void run(CommandSender sender, String[] args) {
        if (args[1].equalsIgnoreCase("set")) {
            if (args[2].equalsIgnoreCase("proxycheck") || args[2].equalsIgnoreCase("vpnapi") || args[2].equalsIgnoreCase("iphub")) {
                setKey(args[2].toLowerCase(), args[3]);
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.api.changed"), plugin, "API_Key", args[2].toLowerCase())));
            } else if (args[2].equalsIgnoreCase("funkemunky")) {
                sender.sendMessage(Util.chat(Placeholders.get("%prefix% &eThis API does not require a key!", plugin)));
            }
        }

        if (args[1].equalsIgnoreCase("enable")) {
            String provider = args[2];
            if (provider.equalsIgnoreCase("proxycheck") || provider.equalsIgnoreCase("vpnapi") || provider.equalsIgnoreCase("funkemunky") || provider.equalsIgnoreCase("iphub")) {
                if (args[3].equalsIgnoreCase("true")) {
                    setStatus(provider.toLowerCase(), true);
                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.api.newstatus"), plugin, "API_Status", args[3], provider)));
                }
                if (args[3].equalsIgnoreCase("false")) {
                    setStatus(provider.toLowerCase(), false);
                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.api.newstatus"), plugin, "API_Status", args[3], provider)));
                }
            } else {
                sender.sendMessage(Util.chat(Placeholders.get("%prefix% &eInvalid provider!", plugin)));
            }
        }
    }

    public void setStatus(String provider, Boolean status) {
        plugin.getConfig().set("api." + provider + ".enable", status);
        plugin.saveConfig();
        plugin.reloadConfig();
    }

    public void setKey(String api, String key) {
        plugin.getConfig().set("api." + api + ".key", key);
        plugin.saveConfig();
        plugin.reloadConfig();
    }
}
