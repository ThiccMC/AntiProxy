package ml.thelt.antiproxy.commands;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.commands.sub.*;
import ml.thelt.antiproxy.commands.sub.Stats;
import ml.thelt.antiproxy.lib.*;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AntiProxy implements CommandExecutor {
    private Main plugin;
    private final DatabaseHandler db;

    public AntiProxy(Main plugin, DatabaseHandler db) {
        this.db = db;
        this.plugin = plugin;
    }

    /* Command lists:
     * ap help
     * ap player [ Player ]
     * ap whitelist [ ... ]
     * ap blacklist [ ... ]
     * ap stats
     * ap api [ ... ]
     * ap enable [ true / false ]
     * ap notify [ true / false ]
     * ap reload
     */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!sender.hasPermission("antiproxy.admin") || !sender.isOp()) {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.noperm"), plugin)));
            return true;
        }

        if (!plugin.getConfig().getBoolean("enable")) {
            if (sender instanceof Player) {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.isdisable"), plugin)));
                return true;
            }
        }

        if (args.length == 0) {
            Info inf = new Info(plugin);
            inf.run(sender);
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                Help help = new Help(plugin);
                help.run(sender);
            }

            if (args[0].equalsIgnoreCase("whitelist")) {
                Whitelist wl = new Whitelist(plugin);
                wl.info(sender);
            }

            if (args[0].equalsIgnoreCase("blacklist")) {
                Blacklist bl = new Blacklist(plugin);
                bl.info(sender);
            }

            if (args[0].equalsIgnoreCase("stats")) {
                sender.sendMessage(Util.chat("&cThis command will be available soon!"));
            }

            if (args[0].equalsIgnoreCase("api")) {
                API api = new API(plugin);
                api.info(sender);
            }

            if (args[0].equalsIgnoreCase("enable")) {
                Enable en = new Enable(plugin);
                en.info(sender);
            }

            if (args[0].equalsIgnoreCase("notify")) {
                Notify not = new Notify(plugin);
                not.info(sender);
            }

            if (args[0].equalsIgnoreCase("clear")) {
                ClearData clear = new ClearData(plugin);
                clear.info(sender);
            }

            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.saveMessagesFile();
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.reloaded"), plugin)));
            }

            if (args[0].equalsIgnoreCase("test")) {
                Stats stats = new Stats(plugin);
                stats.run(sender);
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable")) {
                Enable en = new Enable(plugin);
                en.set(sender, args[1]);
            }

            if (args[0].equalsIgnoreCase("whitelist")) {
                Whitelist wl = new Whitelist(plugin);
                wl.sb(sender, args);
            }

            if (args[0].equalsIgnoreCase("blacklist")) {
                Blacklist bl = new Blacklist(plugin);
                bl.sb(sender, args);
            }

            if (args[0].equalsIgnoreCase("notify")) {
                Notify not = new Notify(plugin);
                not.set(sender, args);
            }

            if (args[0].equalsIgnoreCase("clear")) {
                ClearData clear = new ClearData(plugin);
                clear.run(sender, args, db);
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("whitelist")) {
                Whitelist wl = new Whitelist(plugin);
                wl.operation(sender, args, db);
            }

            if (args[0].equalsIgnoreCase("blacklist")) {
                Blacklist bl = new Blacklist(plugin);
                bl.operation(sender, args, db);
            }

            if (args[0].equalsIgnoreCase("api")) {
                API api = new API(plugin);
                api.info(sender);
            }
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("api")) {
                API api = new API(plugin);
                api.run(sender, args);
            }
        }
        return true;
    }
}
