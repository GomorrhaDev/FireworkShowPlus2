package dev.gomorrha.fireworkshowplus2;

import dev.gomorrha.fireworkshowplus2.commands.CommandHandler;
import dev.gomorrha.fireworkshowplus2.listeners.FireworkClickEvent;
import dev.gomorrha.fireworkshowplus2.objects.Frame;
import dev.gomorrha.fireworkshowplus2.objects.Show;
import dev.gomorrha.fireworkshowplus2.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class FireworkShowPlus2 extends JavaPlugin {
    private static final HashMap<String, Show> shows = new HashMap<>();
    private static FileConfiguration showsfile = new YamlConfiguration();
    public static FireworkShowPlus2 fws;
    public static File dataFolder;
    public static Boolean placeMode = false;
    private static Server server;

    @Override
    public void onEnable()
    {

        getServer().getPluginManager().registerEvents(new FireworkClickEvent(), this);
        CommandHandler commands = new CommandHandler();
        fws = this;
        server = getServer();

        dataFolder = getDataFolder();

        shows.clear();

        File f = new File(dataFolder, "shows.yml");
        if ( !f.exists() )
        {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigurationSerialization.registerClass(Show.class);
        ConfigurationSerialization.registerClass(Frame.class);
        ConfigurationSerialization.registerClass(NormalFireworks.class);
        try
        {
            showsfile.load(f);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        for ( String key : showsfile.getKeys(false) ) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Indexing show '" + key + "'...");
            Show show = new Show((Show) showsfile.get(key)); //Create new show object to put in our hashmap
            shows.put(key, show);
        }

        saveShows();

        getCommand("fireworkshow").setExecutor(commands);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Fireworks show is Enabled!");
    }

    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FireworkShow]: Fireworks show is Disabled!");
    }

    //Getters
    public static HashMap<String, Show> getShows()
    {
        return shows;
    }

    public static FileConfiguration getShowFiles()
    {
        return showsfile;
    }

    public static void saveShows()
    {
        try {
            server.getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Attempting to save fireworkshow to 'shows.yml'!");
            showsfile.save(new File(dataFolder, "shows.yml"));
        } catch (IOException e) {
            server.getConsoleSender().sendMessage(ChatColor.RED + "[FireworkShow]: Failed to save fireworkshow to 'shows.yml'!");
            e.printStackTrace();
        }
    }

    public static void createShow(String name)
    {
        shows.put(name, new Show());
        showsfile.set(name, shows.get(name));
    }
    public static Boolean getPlaceMode(){
        return placeMode;
    }
    public static void togglePlaceMode(Player player) {
        placeMode = !placeMode;
        player.sendMessage(ChatColor.GREEN + "Place mode is now " + (placeMode ? "enabled" : "disabled"));
    }
}
