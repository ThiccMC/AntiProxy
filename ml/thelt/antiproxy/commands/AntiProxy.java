package ml.thelt.antiproxy.commands;

import ml.thelt.antiproxy.Main;
import ml.thelt.antiproxy.lib.BlacklistIP;
import ml.thelt.antiproxy.lib.PlayerInfo;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.lib.WhitelistIP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AntiProxy implements CommandExecutor {
    private Main plugin;

    public AntiProxy(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!sender.hasPermission("antiproxy.admin") || !sender.isOp()) {
            sender.sendMessage(Util.chat("&cYou don't have permission to execute this command!"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Util.chat("&bAntiProxy &eplugin. Version &6" + plugin.getDescription().getVersion()));
            sender.sendMessage(Util.chat("&eFor a list of &6AntiProxy &eplugin commands, type &6/ap help&e."));
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(Util.chat("&bAntiProxy &6commands:"));
                sender.sendMessage(Util.chat("&6/ap help&e: Print this help message."));
                sender.sendMessage(Util.chat("&6/ap enable [ true | false ]&e: Enable or disable plugin."));
                sender.sendMessage(Util.chat("&6/ap notification [ true | false ]&e: Turn notification on or off."));
                sender.sendMessage(Util.chat("&6/ap whitelist [ ... ]&e: Whitelist section (/ap whitelist help)."));
                sender.sendMessage(Util.chat("&6/ap blacklist [ ... ]&e: Blacklist section (/ap blacklist help)."));
                sender.sendMessage(Util.chat("&6/ap reload&e: Reload config."));
            } else if (args[0].equalsIgnoreCase("enable")) {
                sender.sendMessage(Util.chat("&6/ap enable [ true | false ]&e: Enable or disable &bAntiProxy &eplugin."));
            } else if (args[0].equalsIgnoreCase("whitelist")) {
                sender.sendMessage(Util.chat("&6/ap whitelist [ ... ]&e: &bAntiProxy &eplugin whitelist section (/ap whitelist help)."));
            } else if (args[0].equalsIgnoreCase("blacklist")) {
                sender.sendMessage(Util.chat("&6/ap blacklist [ ... ]&e: &bAntiProxy &eplugin blacklist section (/ap blacklist help)."));
            } else if (args[0].equalsIgnoreCase("notification")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Util.chat("&6[AntiProxy] &eThis command is only available for players."));
                } else {
                    sender.sendMessage(Util.chat("&6/ap notification [ true | false ]&e: Turn notification on or off."));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(Util.chat("&b[AntiProxy] &eSuccessfully reloaded config file!"));
            } else {
                sender.sendMessage(Util.chat("&b[AntiProxy] &eUnknown argument! Type &6/ap help &efor help."));
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable")) {
                Boolean enable = plugin.getConfig().getBoolean("enable");
                if (args[1].equalsIgnoreCase(String.valueOf(enable))) {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eThe plugin is already &6" + (enable ? "enabled" : "disabled") + "&e!"));
                } else {
                    if (args[1].equalsIgnoreCase("true")) {
                        plugin.getConfig().set("enable", true);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat("&b[AntiProxy] &ePlugin is now &6enabled&e!"));
                    } else if (args[1].equalsIgnoreCase("false")) {
                        plugin.getConfig().set("enable", false);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat("&b[AntiProxy] &ePlugin is now &6disabled&e!"));
                    } else {
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eInvalid boolean! Only &6true &eor &6false &eis allowed!"));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("whitelist")) {
                if (args[1].equalsIgnoreCase("help")) {
                    sender.sendMessage(Util.chat("&bAntiProxy &6commands - Whitelist"));
                    sender.sendMessage(Util.chat("&6/ap whitelist addip [ IPv4 ]&e: Add IPv4 Address to the whitelist file."));
                    sender.sendMessage(Util.chat("&6/ap whitelist clear&e: Clear whitelist file."));
                } else
                if (args[1].equalsIgnoreCase("addip")) {
                    sender.sendMessage(Util.chat("&6/ap whitelist addip [ IPv4 ]&e: Add IPv4 Address to the whitelist file."));
                } else if (args[1].equalsIgnoreCase("clear")) {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eAre you sure about clearing the whitelist file? Players will have to be IP checked again."));
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eType &6/ap whitelist clear confirm &eto confirm this decision."));
                } else {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eUnknown argument! Type &6/ap help &efor help."));
                }
            }
            if (args[0].equalsIgnoreCase("blacklist")) {
                if (args[1].equalsIgnoreCase("help")) {
                    sender.sendMessage(Util.chat("&bAntiProxy &6commands - Blacklist"));
                    sender.sendMessage(Util.chat("&6/ap blacklist addip [ IPv4 ]&e: Add IPv4 Address to the blacklist file."));
                    sender.sendMessage(Util.chat("&6/ap blacklist removeip [ IPv4 ]&e: Remove IPv4 Address from the blacklist file."));
                    sender.sendMessage(Util.chat("&6/ap blacklist clear&e: Clear blacklist file."));
                } else
                if (args[1].equalsIgnoreCase("addip")) {
                    sender.sendMessage(Util.chat("&6/ap blacklist addip [ IPv4 ]&e: Add IPv4 Address to the blacklist file."));
                }else if (args[1].equalsIgnoreCase("removeip")) {
                    sender.sendMessage(Util.chat("&6/ap blacklist removeip [ IPv4 ]&e: Remove IPv4 Address from the blacklist file."));
                } else if (args[1].equalsIgnoreCase("clear")) {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eAre you sure about clearing the blacklist file? Players will have to be IP checked again."));
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eType &6/ap blacklist clear confirm &eto confirm this decision."));
                } else {
                    sender.sendMessage(Util.chat("&6[AntiProxy] &eUnknown argument! Type &6/ap help &efor help."));
                }
            }

            if (args[0].equalsIgnoreCase("notification")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eThis command is only available for players."));
                    return true;
                }
                Player p = (Player) sender;
                Boolean notify = plugin.getConfig().getBoolean("staff." + p.getDisplayName() + ".notification");
                if (args[1].equalsIgnoreCase(String.valueOf(notify))) {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eYou are already &6" + (notify ? "turned on" : "turned off") + " &enotification!"));
                } else {
                    if (args[1].equalsIgnoreCase("true")) {
                        plugin.getConfig().set("staff." + p.getDisplayName() + ".notification", true);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eYou &6now &esee notification!"));
                    } else if (args[1].equalsIgnoreCase("false")) {
                        plugin.getConfig().set("staff." + p.getDisplayName() + ".notification", false);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eYou &6no longer &esee notification!"));
                    } else {
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eInvalid boolean! Only &6true &eor &6false &eis allowed!"));
                    }
                }
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("whitelist")) {
                if (args[1].equalsIgnoreCase("addip")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        WhitelistIP.saveWhitelist(args[2]);
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eAdded &6" + args[2] + " &eto the whitelist file!"));
                    }
                } else if (args[1].equalsIgnoreCase("clear")) {
                    if (args[2].equalsIgnoreCase("confirm")) {
                        Boolean result = WhitelistIP.clearWhitelist();
                        if (result) {
                            sender.sendMessage(Util.chat("&b[AntiProxy] &eCleared whitelist file!"));
                        } else {
                            sender.sendMessage(Util.chat("&b[AntiProxy] &eInvalid IP address. The IPv4 address should be this format: &6#.#.#.#"));
                        }
                    } else {
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eAre you sure about clearing the whitelist file? Players will have to be IP checked again."));
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eType &6/ap whitelist clear confirm &eto confirm this decision."));
                    }
                } else {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eUnknown argument! Type &6/ap help &efor help."));
                }
            }
            if (args[0].equalsIgnoreCase("blacklist")) {
                if (args[1].equalsIgnoreCase("addip")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        for (Player p: Bukkit.getOnlinePlayers()) {
                            String playerIP = PlayerInfo.getPlayerIP(p);
                            if (playerIP.equals(args[2])) {
                                if (p.hasPermission("antiproxy.admin")) {
                                    sender.sendMessage(Util.chat("&b[AntiProxy] &eCouldn't blacklist this IP since there is a staff member connecting with this IP."));
                                    return true;
                                } else {
                                    p.kickPlayer(Util.chat("&cYour IP has been blacklisted from the server!"));
                                }
                            }
                        }
                        BlacklistIP.saveBlacklist(args[2]);
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eAdded &6" + args[2] + " &eto the blacklist file!"));
                    } else {
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eInvalid IP address. The IPv4 address should be this format: &6#.#.#.#"));
                    }
                } else if (args[1].equalsIgnoreCase("removeip")) {
                    Boolean correct = Util.validIP(args[2]);
                    if (correct) {
                        Boolean rmv_bl = BlacklistIP.removeBlacklist(args[2]);
                        if (rmv_bl) {
                            WhitelistIP.saveWhitelist(args[2]);
                            sender.sendMessage(Util.chat("&b[AntiProxy] &eRemoved &6" + args[2] + " &efrom the blacklist file and added to whitelist file!"));
                        } else {
                            sender.sendMessage(Util.chat("&b[AntiProxy] &eIP &6" + args[2] + " &eis not blacklisted!"));
                        }
                    } else {
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eInvalid IP address. The IPv4 address should be this format: &6#.#.#.#"));
                    }
                } else if (args[1].equalsIgnoreCase("clear")) {
                    if (args[2].equalsIgnoreCase("confirm")) {
                        Boolean result = BlacklistIP.clearBlacklist();
                        if (result) {
                            sender.sendMessage(Util.chat("&b[AntiProxy] &eCleared blacklist file!"));
                        }
                    } else {
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eAre you sure about clearing the blacklist file? Players will have to be IP checked again."));
                        sender.sendMessage(Util.chat("&b[AntiProxy] &eType &6/ap whitelist clear confirm &eto confirm this decision."));
                    }
                } else {
                    sender.sendMessage(Util.chat("&b[AntiProxy] &eUnknown argument! Type &6/ap help &efor help."));
                }
            }
        }
        return true;
    }
}
