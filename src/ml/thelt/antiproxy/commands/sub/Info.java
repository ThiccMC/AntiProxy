package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Info {
    private Main plugin;
    public Info(Main plugin) {
        this.plugin = plugin;
    }

    public void run(CommandSender sender) {
        List<String> list = plugin.getMessages().getStringList("messages.info");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
        }
    }
}
