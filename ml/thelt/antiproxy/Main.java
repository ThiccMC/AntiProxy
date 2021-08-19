package ml.thelt.antiproxy;

import ml.thelt.antiproxy.commands.AntiProxy;
import ml.thelt.antiproxy.lib.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ml.thelt.antiproxy.listeners.PlayerJoin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()){
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            checkConfig();
            saveConfig();
            reloadConfig();
        }

        checkWhitelist();
        checkBlacklist();
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        getCommand("ap").setExecutor(new AntiProxy(this));

        System.out.println(Util.chat("&aPlugin has been enabled!"));
    }

    @Override
    public void onDisable() {
        System.out.println(Util.chat("&cPlugin has been disabled!"));
    }

    public void checkConfig() {
        if (getConfig().get("enable") == null) {
            getConfig().set("enable", true);
            saveConfig();
            reloadConfig();
        }
    }

    public void checkWhitelist() {
        File file = new File(getDataFolder() + File.separator + "whitelist.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fstream = new FileWriter(getDataFolder() + File.separator + "whitelist.txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write("127.0.0.1");
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void checkBlacklist() {
        File file = new File(getDataFolder() + File.separator + "blacklist.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fstream = new FileWriter(getDataFolder() + File.separator + "blacklist.txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write("1.1.1.1");
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
