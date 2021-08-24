package ml.thelt.antiproxy.commands;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.*;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;


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
     * ap apikey [ API Key ]
     * ap enable [ true / false ]
     * ap notify [ true / false ]
     * ap reload
     */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!sender.hasPermission("antiproxy.admin") || !sender.isOp()) {
            sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.noperm"))));
            return true;
        }

        if (!plugin.getConfig().getBoolean("enable")) {
            if (sender instanceof Player) {
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.isdisable"))));
                return true;
            }
        }

        if (args.length == 0) {
            List<String> list = plugin.getConfig().getStringList("messages.info");
            String[] array = list.toArray(new String[0]);
            for (String msg: array) {
                sender.sendMessage(Util.chat(Placeholders.get(msg)));
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                List<String> list = plugin.getConfig().getStringList("messages.help");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
                }
            } else if (args[0].equalsIgnoreCase("player")) {
//                List<String> list = plugin.getConfig().getStringList("messages.usage.player");
//                String[] array = list.toArray(new String[0]);
//                for (String msg: array) {
//                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
//                }
                sender.sendMessage(Util.chat("&cThis command will be available soon!"));
            } else if (args[0].equalsIgnoreCase("whitelist")) {
                List<String> list = plugin.getConfig().getStringList("messages.whitelist");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
                }
            } else if (args[0].equalsIgnoreCase("blacklist")) {
                List<String> list = plugin.getConfig().getStringList("messages.blacklist");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
                }
            } else if (args[0].equalsIgnoreCase("stats")) {
//                List<String> list = plugin.getConfig().getStringList("messages.stats");
//                String[] array = list.toArray(new String[0]);
//                for (String msg: array) {
//                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
//                }
                sender.sendMessage(Util.chat("&cThis command will be available soon!"));
            } else if (args[0].equalsIgnoreCase("apikey")) {
                List<String> list = plugin.getConfig().getStringList("messages.usage.apikey");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                List<String> list = plugin.getConfig().getStringList("messages.usage.enable");
                String[] array = list.toArray(new String[0]);
                for (String msg: array) {
                    sender.sendMessage(Util.chat(Placeholders.get(msg)));
                }
            } else if (args[0].equalsIgnoreCase("notify")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.playeronly"))));
                    return true;
                }
                List<String> list = plugin.getConfig().getStringList("messages.usage.notify");
                String[] array = list.toArray(new String[0]);
                if (plugin.getConfig().contains("staff." + ((Player) sender).getDisplayName() + ".notify")) {
                    for (String msg: array) {
                        String msgstr = Placeholders.get(msg, null, ((Player) sender).getDisplayName());
                        sender.sendMessage(Util.chat(msgstr));
                    }
                } else {
                    plugin.getConfig().set("staff." + ((Player) sender).getDisplayName() + ".notify", true);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    for (String msg: array) {
                        String msgstr = Placeholders.get(msg, null, ((Player) sender).getDisplayName());
                        sender.sendMessage(Util.chat(msgstr));
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.reloaded"))));
            }
        }


        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable")) {
                Boolean enable = plugin.getConfig().getBoolean("enable");
                if (args[1].equalsIgnoreCase(String.valueOf(enable))) {
                    List<String> list = plugin.getConfig().getStringList("messages.alreadystatus");
                    String[] array = list.toArray(new String[0]);
                    for (String msg: array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                } else {
                    if (args[1].equalsIgnoreCase("true")) {
                        plugin.getConfig().set("enable", true);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.newstatus.plugin"))));
                    } else if (args[1].equalsIgnoreCase("false")) {
                        plugin.getConfig().set("enable", false);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.newstatus.plugin"))));
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.invalidusage"))));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("whitelist")) {
                if (args[1].equalsIgnoreCase("clear")) {
                    List<String> list = plugin.getConfig().getStringList("messages.confirm.clearwhitelist");
                    String[] array = list.toArray(new String[0]);
                    for (String msg: array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                } else {
                    List<String> list = plugin.getConfig().getStringList("messages.whitelist");
                    String[] array = list.toArray(new String[0]);
                    for (String msg: array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("blacklist")) {
                if (args[1].equalsIgnoreCase("clear")) {
                    List<String> list = plugin.getConfig().getStringList("messages.confirm.clearblacklist");
                    String[] array = list.toArray(new String[0]);
                    for (String msg: array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                } else {
                    List<String> list = plugin.getConfig().getStringList("messages.blacklist");
                    String[] array = list.toArray(new String[0]);
                    for (String msg: array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("notify")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("playeronly"))));
                    return true;
                }

                Player p = (Player) sender;
                Boolean notify = plugin.getConfig().getBoolean("staff." + p.getDisplayName() + ".notify");
                if (args[1].equalsIgnoreCase(String.valueOf(notify))) {
                    String msg = plugin.getConfig().getString("messages.already.notifystatus");
                    sender.sendMessage(Util.chat(Placeholders.get(msg, null, p.getDisplayName())));
                } else {
                    if (args[1].equalsIgnoreCase("true")) {
                        plugin.getConfig().set("staff." + p.getDisplayName() + ".notify", true);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.newstatus.notify"),null, p.getDisplayName())));
                    } else if (args[1].equalsIgnoreCase("false")) {
                        plugin.getConfig().set("staff." + p.getDisplayName() + ".notify", false);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.newstatus.notify"), null, p.getDisplayName())));
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.invalidusage"))));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("player")) {
                sender.sendMessage(Util.chat("&cThis command will be available soon!"));
            }

            if (args[0].equalsIgnoreCase("apikey")) {
                String key = args[1];
                plugin.getConfig().set("api-key.proxycheck",key);
                plugin.saveConfig();
                plugin.reloadConfig();
                sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.apikey.changed"))));
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("whitelist")) {
                if (args[1].equalsIgnoreCase("add")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseHandler.GetInternetProtocol dbip;
                            try {
                                dbip = db.getIP(args[2]);
                                if (dbip != null) {
                                    if (dbip.status == false) {
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.already.butblacklist"), args[2])));
                                    } else {
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.already.whitelisted"), args[2])));
                                    }
                                } else {
                                    try {
                                        db.addIP(args[2], "Whitelisted", true);
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.whitelist.add"), args[2])));
                                    } catch (SQLException throwables) {
                                        sender.sendMessage(Util.chat("&cSomething went wrong while adding to whitelist!"));
                                        throwables.printStackTrace();
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println(Util.chat("&cError while getting data from database!"));
                                e.printStackTrace();
                                return;
                            }
                        });
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.whitelist.invalid"))));
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseHandler.GetInternetProtocol dbip;
                            try {
                                dbip = db.getIP(args[2]);
                                if (dbip == null) {
                                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.whitelist.notfound"), args[2])));
                                } else {
                                    if (dbip.status == false) {
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.already.butblacklist"), args[2])));
                                    } else {
                                        try {
                                            db.removeIP(args[2]);
                                            sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.whitelist.remove"), args[2])));
                                        } catch (SQLException throwables) {
                                            sender.sendMessage(Util.chat("&cSomething went wrong while removing from whitelist!"));
                                            throwables.printStackTrace();
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println(Util.chat("&cError while getting data from database!"));
                                e.printStackTrace();
                                return;
                            }
                        });
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.whitelist.invalid"))));
                    }
                } else if (args[1].equalsIgnoreCase("clear")) {
                    if (args[2].equalsIgnoreCase("confirm")) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseHandler.GetInternetProtocol dbip;
                            try {
                                db.clearList(true);
                                sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.whitelist.clear"))));
                            } catch (SQLException throwables) {
                                sender.sendMessage(Util.chat("&cSomething went wrong!"));
                                throwables.printStackTrace();
                            }
                        });
                    } else {
                        List<String> list = plugin.getConfig().getStringList("messages.confirm.clearwhitelist");
                        String[] array = list.toArray(new String[0]);
                        for (String msg: array) {
                            sender.sendMessage(Util.chat(Placeholders.get(msg)));
                        }
                    }
                } else {
                    List<String> list = plugin.getConfig().getStringList("messages.whitelist");
                    String[] array = list.toArray(new String[0]);
                    for (String msg: array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                }
            }


            if (args[0].equalsIgnoreCase("blacklist")) {
                if (args[1].equalsIgnoreCase("add")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseHandler.GetInternetProtocol dbip;
                            try {
                                dbip = db.getIP(args[2]);
                                if (dbip != null) {
                                    if (dbip.status == true) {
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.already.butwhitelist"), args[2])));
                                    } else {
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.already.blacklisted"), args[2])));
                                    }
                                } else {
                                    try {
                                        db.addIP(args[2], "Blacklisted", false);
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.blacklist.add"), args[2])));
                                    } catch (SQLException throwables) {
                                        sender.sendMessage(Util.chat("&cSomething went wrong while adding to blacklist!"));
                                        throwables.printStackTrace();
                                        }
                                    }
                            } catch (SQLException e) {
                                System.out.println(Util.chat("&cError while getting data from database!"));
                                e.printStackTrace();
                                return;
                            }
                        });
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.blacklist.invalid"))));
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseHandler.GetInternetProtocol dbip;
                            try {
                                dbip = db.getIP(args[2]);
                                if (dbip == null) {
                                    sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.blacklist.notfound"), args[2])));
                                } else {
                                    if (dbip.status == true) {
                                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.already.butwhitelist"), args[2])));
                                    } else {
                                        try {
                                            db.removeIP(args[2]);
                                            sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.blacklist.remove"), args[2])));
                                        } catch (SQLException throwables) {
                                            sender.sendMessage(Util.chat("&cSomething went wrong while removing from blacklist!"));
                                            throwables.printStackTrace();
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println(Util.chat("&cError while getting data from database!"));
                                e.printStackTrace();
                                return;
                            }
                        });
                    } else {
                        sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.blacklist.invalid"))));
                    }
                } else if (args[1].equalsIgnoreCase("clear")) {
                    if (args[2].equalsIgnoreCase("confirm")) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseHandler.GetInternetProtocol dbip;
                            try {
                                db.clearList(false);
                                sender.sendMessage(Util.chat(Placeholders.get(plugin.getConfig().getString("messages.success.blacklist.clear"))));
                            } catch (SQLException throwables) {
                                sender.sendMessage(Util.chat("&cSomething went wrong!"));
                                throwables.printStackTrace();
                            }
                        });
                    } else {
                        List<String> list = plugin.getConfig().getStringList("messages.confirm.clearblacklist");
                        String[] array = list.toArray(new String[0]);
                        for (String msg : array) {
                            sender.sendMessage(Util.chat(Placeholders.get(msg)));
                        }
                    }
                } else {
                    List<String> list = plugin.getConfig().getStringList("messages.blacklist");
                    String[] array = list.toArray(new String[0]);
                    for (String msg : array) {
                        sender.sendMessage(Util.chat(Placeholders.get(msg)));
                    }
                }
            }
        }
        return true;
    }
}
