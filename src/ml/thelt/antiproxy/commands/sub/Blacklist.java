package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.Placeholders;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.List;

public class Blacklist {
    private Main plugin;
    public Blacklist(Main plugin) {
        this.plugin = plugin;
    }

    public void info(CommandSender sender) {
        List<String> list = plugin.getMessages().getStringList("messages.blacklist");
        String[] array = list.toArray(new String[0]);
        for (String msg: array) {
            sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
        }
    }

    public void sb(CommandSender sender, String[] args) {
        if (args[1].equalsIgnoreCase("clear")) {
            List<String> list = plugin.getMessages().getStringList("messages.confirm.clearblacklist");
            String[] array = list.toArray(new String[0]);
            for (String msg: array) {
                sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
            }
        } else {
            List<String> list = plugin.getMessages().getStringList("messages.blacklist");
            String[] array = list.toArray(new String[0]);
            for (String msg: array) {
                sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
            }
        }
    }

    public void operation(CommandSender sender, String[] args, DatabaseHandler db) {
        if (args[1].equalsIgnoreCase("add")) {
            boolean correct = Util.validIP(args[2]);
            if (correct) {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    DatabaseHandler.GetInternetProtocol dbip;
                    try {
                        dbip = db.getIP(args[2]);
                        if (dbip != null) {
                            if (!dbip.status) {
                                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.already.butwhitelist"), plugin, "IP", args[2])));
                            } else {
                                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.already.blacklisted"), plugin, "IP", args[2])));
                            }
                        } else {
                            try {
                                db.addIP(args[2], 100, "Blacklisted", false);
                                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.blacklist.add"), plugin, "IP", args[2])));
                            } catch (SQLException e) {
                                sender.sendMessage(Util.chat("&cSomething went wrong while adding to blacklist!"));
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println(Util.chat("&cError while getting data from database!"));
                        e.printStackTrace();
                    }
                });
            } else {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.blacklist.invalid"), plugin)));
            }
        } else if (args[1].equalsIgnoreCase("remove")) {
            boolean correct = Util.validIP(args[2]);
            if (correct) {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    DatabaseHandler.GetInternetProtocol dbip;
                    try {
                        dbip = db.getIP(args[2]);
                        if (dbip == null) {
                            sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.blacklist.notfound"), plugin, "IP", args[2])));
                        } else {
                            if (dbip.status) {
                                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.already.butwhitelist"), plugin, "IP", args[2])));
                            } else {
                                try {
                                    db.removeIP(args[2]);
                                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.blacklist.remove"), plugin, "IP", args[2])));
                                } catch (SQLException e) {
                                    sender.sendMessage(Util.chat("&cSomething went wrong while removing from blacklist!"));
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println(Util.chat("&cError while getting data from database!"));
                        e.printStackTrace();
                    }
                });
            } else {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.blacklist.invalid"), plugin)));
            }
        } else if (args[1].equalsIgnoreCase("clear")) {
            if (args[2].equalsIgnoreCase("confirm")) {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    DatabaseHandler.GetInternetProtocol dbip;
                    try {
                        db.clearList("Blacklisted");
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getMessages().getString("messages.success.blacklist.clear"), plugin)));
                    } catch (SQLException e) {
                        sender.sendMessage(Util.chat("&cSomething went wrong!"));
                        e.printStackTrace();
                    }
                });
            } else {
                List<String> list = plugin.getMessages().getStringList("messages.confirm.clearblacklist");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
                }
            }
        } else {
            List<String> list = plugin.getMessages().getStringList("messages.blacklist");
            String[] array = list.toArray(new String[0]);
            for (String msg: array) {
                sender.sendMessage(Util.chat(Placeholders.get(msg, plugin)));
            }
        }
    }
}
