package ml.thelt.antiproxy.lib;

import ml.thelt.antiproxy.Main;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class Placeholders {
    public static Plugin plugin = Main.getPlugin(Main.class);

    public static String get(String message, String... str) {
        message = message.replaceAll("%prefix%", Objects.requireNonNull(plugin.getConfig().getString("messages.prefix")));
        message = message.replaceAll("%version%", plugin.getDescription().getVersion());
        message = message.replaceAll("%plugin_status%", plugin.getConfig().getBoolean("enable") ? plugin.getConfig().getString("messages.enabled") : plugin.getConfig().getString("messages.disabled"));
        if (str.length > 0) {
            if (str.length == 1) {
                message = rep_1(message, str[0]);
            } else if (str.length == 2) {
                message = rep_1(message, str[0]);
                message = rep_2(message, str[1]);
            } else if (str.length == 3) {
                message = rep_1(message, str[0]);
                message = rep_2(message, str[1]);
                message = rep_3(message, str[2]);
            } else if (str.length == 4) {
                message = rep_1(message, str[0]);
                message = rep_2(message, str[1]);
                message = rep_3(message, str[2]);
                message = rep_4(message, str[3]);
            } else if (str.length == 5) {
                message = rep_1(message, str[0]);
                message = rep_2(message, str[1]);
                message = rep_3(message, str[2]);
                message = rep_4(message, str[3]);
                message = rep_5(message, str[4]);
            } else if (str.length == 6) {
                message = rep_1(message, str[0]);
                message = rep_2(message, str[1]);
                message = rep_3(message, str[2]);
                message = rep_4(message, str[3]);
                message = rep_5(message, str[4]);
                message = rep_6(message, str[5]);
            } else if (str.length == 7) {
                message = rep_1(message, str[0]);
                message = rep_2(message, str[1]);
                message = rep_3(message, str[2]);
                message = rep_4(message, str[3]);
                message = rep_5(message, str[4]);
                message = rep_6(message, str[5]);
                message = rep_7(message, str[6]);
            }
        }
        return message;
    }

    private static String rep_1(String message, String replace) {
        return message.replaceAll("%ip%", replace);
    }
    private static String rep_2(String message, String replace) {
        Boolean notify = plugin.getConfig().contains("staff." + replace + ".notify");
        if (notify) {
            message = message.replaceAll("%player_notify%", plugin.getConfig().getBoolean("staff." + replace + ".notify") ? plugin.getConfig().getString("messages.enabled") : plugin.getConfig().getString("messages.disabled"));
        } else {
            message = message.replaceAll("%player_notify%", plugin.getConfig().getString("messages.enabled"));
        }
        return message;
    }
    private static String rep_3(String message, String replace) {
        return message.replaceAll("%player%", replace);
    }
    private static String rep_4(String message, String replace) {
        return message.replaceAll("%location%", replace);
    }
    private static String rep_5(String message, String replace) {
        return message.replaceAll("%operation%", replace);
    }
    private static String rep_6(String message, String replace) {
        return message.replaceAll("%status%", replace);
    }
    private static String rep_7(String message, String replace) {
        return message.replaceAll("%proxy%", replace);
    }

}
