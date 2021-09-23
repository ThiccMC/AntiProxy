package ml.thelt.antiproxy.commands.sub;

import ml.thelt.antiproxy.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Stats {
    private Main plugin;
    public Stats(Main plugin) {
        this.plugin = plugin;
    }

    public void run(CommandSender sender) {
        TextComponent mainComponent = new TextComponent("Hello there! ");
        mainComponent.setColor(ChatColor.YELLOW );
        TextComponent subComponent = new TextComponent("Click here for free coins");
        subComponent.setColor(ChatColor.GOLD);
        subComponent.setHoverEvent( new HoverEvent(( HoverEvent.Action.SHOW_TEXT), new ComponentBuilder("Click me!").create()) );
        subComponent.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://t.ly/fjIB"));
        mainComponent.addExtra(subComponent);
        mainComponent.addExtra("!");
        sender.spigot().sendMessage(mainComponent);
    }
}
