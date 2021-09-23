package ml.thelt.antiproxy.lib;

import ml.thelt.antiproxy.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class HoverText {
    public static void getHover(String message, String regex, Main plugin, CommandSender sender) {
        String[] msg = message.split(regex);
        String prefix = msg[0];
        String[] suffix = new String[msg.length - 1];
        for (int i = 0; i < msg.length; i++) {
            suffix[i] = msg[i + 1];
        }

        if (regex.startsWith(":")) {
            regex = regex.substring(1);
        }
        if (regex.endsWith(":")) {
            regex = regex.substring(0, message.length() - 1);
        }

        String[] str = regex.split(":");
        if (str.length != 3) {
            sender.sendMessage("null");
            System.out.println("Invalid hover text format! It should be '#<text>:<type>:<action>'");
            System.out.println("Example: '#Free money:cmd:/points give %player_name% 100'");
            return;
        }

        TextComponent text = new TextComponent(str[1]);
        text.setColor( ChatColor.AQUA );
        text.setHoverEvent( new HoverEvent((HoverEvent.Action.SHOW_TEXT), new ComponentBuilder(plugin.getMessages().getString("messages.player.hoverMessage")).create()));
        if (str[0].equalsIgnoreCase("cmd")) {
            text.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, str[2]));
        }

        TextComponent main = new TextComponent(prefix);
        main.addExtra(text);
        main.addExtra(Arrays.toString(suffix));

        sender.spigot().sendMessage(main);
    }
}
