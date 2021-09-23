package ml.thelt.antiproxy.lib;

import ml.thelt.antiproxy.Main;

import java.util.Objects;

public class Placeholders {

    // message, plugin, type, args...
    public static String get(String message, Main plugin, String... str) {
        message = message.replace("%prefix%", Objects.requireNonNull(plugin.getMessages().getString("messages.prefix")));
        message = message.replace("%version%", plugin.getDescription().getVersion());
        message = message.replace("%plugin_status%", Objects.requireNonNull(plugin.getConfig().getBoolean("enable") ? plugin.getMessages().getString("messages.enabled") : plugin.getMessages().getString("messages.disabled")));

        if (str.length != 0) {
            if (str[0].equals("notify")) {
                boolean notify = Boolean.parseBoolean(str[1]);
                message = message.replace("%player_notify%", (Objects.requireNonNull(notify ? plugin.getMessages().getString("messages.enabled") : plugin.getMessages().getString("messages.disabled"))));
            }

            if (str[0].equals("player_connect")) {
                // player_connect, player_name, player_action, ip, proxy_type, location, score
                message = message.replace("%player%", str[1]);
                message = message.replace("%playerAction%", str[2]);
                message = message.replace("%ip%", str[3]);
                message = message.replace("%proxy_type%", str[4]);
                message = message.replace("%location%", str[5]);
                message = message.replace("%score%", str[6]);
                String scoreColor = "";
                if ((int) Float.parseFloat(str[6]) >= plugin.getConfig().getInt("score.kick.at")) {
                    message = message.replace("%scoreColor%", Objects.requireNonNull(plugin.getConfig().getString("score.kick.color")));
                } else if ((int) Float.parseFloat(str[6]) >= plugin.getConfig().getInt("score.alert.at")) {
                    message = message.replace("%scoreColor%", Objects.requireNonNull(plugin.getConfig().getString("score.alert.color")));
                } else {
                    message = message.replace("%scoreColor%", Objects.requireNonNull(plugin.getConfig().getString("score.safe.color")));
                }
            }

            if (str[0].equals("IP")) {
                // IP, client_ip
                message = message.replace("%ip%", str[1]);
            }

            if (str[0].equals("clear_data")) {
                // clear data option
                String option = str[1];
                if (option.equalsIgnoreCase("player_ip")) {
                    message = message.replace("%clear_option%", "Player IP");
                } else if (option.equalsIgnoreCase("good_ip")) {
                    message = message.replace("%clear_option%", "Good IP");
                } else if (option.equalsIgnoreCase("proxy_ip")) {
                    message = message.replace("%clear_option%", "VPN/Proxy IP");
                } else if (option.equalsIgnoreCase("bad_ip")) {
                    message = message.replace("%clear_option%", "Bad IP");
                } else if (option.equalsIgnoreCase("ip_database")) {
                    message = message.replace("%clear_option%", "IP");
                } else if (option.equalsIgnoreCase("staff")) {
                    message = message.replace("%clear_option%", "Staff");
                } else if (option.equalsIgnoreCase("all")) {
                    message = message.replace("%clear_option%", "All");
                }
            }

            if (str[0].equals("API_Status")) {
                // API_Status, api_status, api_provider
                message = message.replace("%api_status%", Objects.requireNonNull(Boolean.parseBoolean(str[1]) ? plugin.getMessages().getString("messages.enabled") : plugin.getMessages().getString("messages.disabled")));
                message = message.replace("%api%", str[2]);
            }

            if (str[0].equals("API_Key")) {
                // API_Key, api_provider
                message = message.replace("%api%", str[1]);
            }

            if (str[0].equals("player_info")) {
                // Player, player, player_name, ip
                message = message.replace("%player%", str[1]);
                message = message.replace("%player_name%", str[2]);
                message = message.replace("%group%", "null");
                message = message.replace("%ip%", str[3]);
                message = message.replace("%discord_tag%", "null");
            }

            if (str[0].equals("player_option")) {
                // Player, player, ip
                message = message.replace("%player_name%", str[1]);
                message = message.replace("%ip%", str[2]);
            }
        }

        return message;
    }
}
