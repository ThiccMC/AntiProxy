package ml.thelt.antiproxy;

import ml.thelt.antiproxy.commands.AntiProxy;
import ml.thelt.antiproxy.commands.Player;
import ml.thelt.antiproxy.lib.Util;
import ml.thelt.antiproxy.sql.DatabaseHandler;
import ml.thelt.antiproxy.sql.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ml.thelt.antiproxy.listeners.PlayerJoin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin {
    private DatabaseHandler db = null;
    private File msgfile;
    private FileConfiguration msgconf;
    private File statsfile;
    private FileConfiguration statsconf;
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

        checkMessagesFile();

        try {
            this.db = new SQLite(this, new File(getDataFolder(), "database.db"));
        } catch (SQLException | IOException e) {
            getLogger().severe("Error setting up database");
            e.printStackTrace();
            this.setEnabled(false);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this, db), this);
        getCommand("ap").setExecutor(new AntiProxy(this, db));
        // getCommand("player").setExecutor(new Player(this));

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

    public void checkMessagesFile() {
        msgfile = new File(getDataFolder(), "messages.yml");
        if (!msgfile.exists()) {
            saveResource("messages.yml", false);
        }

        msgconf = YamlConfiguration.loadConfiguration(msgfile);
    }

    public FileConfiguration getMessages() {
        return msgconf;
    }

    public File getMessagesFile() {
        return msgfile;
    }

    public void saveMessagesFile() {
        checkMessagesFile();
    }

    public void checkStats() {
        statsfile = new File(getDataFolder(), "stats.yml");
        if (!statsfile.exists()) {
            saveResource("stats.yml", false);
        }

        statsconf = YamlConfiguration.loadConfiguration(statsfile);
    }

    public FileConfiguration getStats() {
        return statsconf;
    }

    public File getStatsFile() {
        return statsfile;
    }

    public void saveStatsFile() {
        checkStats();
    }
}
