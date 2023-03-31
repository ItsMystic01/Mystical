package mys.serone.mystical;

import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.handlers.ReadMeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import mys.serone.mystical.functions.CommandEntry;
import mys.serone.mystical.listeners.ChatListener;
import mys.serone.mystical.listeners.OnFirstJoin;
import java.io.File;

public final class Mystical extends JavaPlugin {

    @Override
    public void onEnable() {

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            boolean result = dataFolder.mkdirs();
            if (result) { Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Successfully"); }
            else { Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Unsuccessfully."); }
        }

        getServer().getPluginManager().registerEvents(new OnFirstJoin(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        new ConfigManager(this);
        CommandEntry.initCommands(this);
        ReadMeConfiguration readMeConfiguration = new ReadMeConfiguration(this);
        readMeConfiguration.writeToFile();
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Mystical] Shutting down");
    }

}

