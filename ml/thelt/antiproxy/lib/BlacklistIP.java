package ml.thelt.antiproxy.lib;

import ml.thelt.antiproxy.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Scanner;

public class BlacklistIP {
    public static Plugin plugin = Main.getPlugin(Main.class);

    public static String getBlacklist(String ip) {
        try {
            File file = new File(plugin.getDataFolder() + File.separator + "blacklist.txt");
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

    public static void saveBlacklist(String ip) {
        try {
            FileWriter fstream = new FileWriter(plugin.getDataFolder() + File.separator + "blacklist.txt",true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("\n" +ip);
            out.close();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public static Boolean removeBlacklist(String ip) {
        try {
            File file = new File(plugin.getDataFolder() + File.separator + "blacklist.txt");
            File temp = File.createTempFile("temp_blacklist", ".txt", file.getParentFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp)));
            Boolean have_replaced = false;
            for (String line; (line = reader.readLine()) != null;) {
                if (!line.equals("")) {
                    line = line.replace(ip, "");
                    writer.println(line);
                    if (line.equals("")) {
                        have_replaced = true;
                    }
                }
            }
            reader.close();
            writer.close();
            file.delete();
            temp.renameTo(file);
            return have_replaced;
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public static Boolean clearBlacklist() {
        try {
            FileWriter fstream = new FileWriter(plugin.getDataFolder() + File.separator + "blacklist.txt");
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
