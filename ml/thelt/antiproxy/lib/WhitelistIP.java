package ml.thelt.antiproxy.lib;

import ml.thelt.antiproxy.Main;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Scanner;

public class WhitelistIP {
    public static Plugin plugin = Main.getPlugin(Main.class);

    public static String getWhitelist(String ip) {
        try {
            File file = new File(plugin.getDataFolder() + File.separator + "whitelist.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.equals(ip)) {
                    return "true";
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
        return "false";
    }

    public static void saveWhitelist(String ip) {
        try {
            FileWriter fstream = new FileWriter(plugin.getDataFolder() + File.separator + "whitelist.txt",true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("\n" +ip);
            out.close();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public static Boolean clearWhitelist() {
        try {
            FileWriter fstream = new FileWriter(plugin.getDataFolder() + File.separator + "whitelist.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("");
            out.close();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
