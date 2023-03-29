package mys.serone.mystical;

import mys.serone.mystical.handlers.ConfigurationManager;
import mys.serone.mystical.handlers.ReadMeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import mys.serone.mystical.functions.CommandEntry;
import mys.serone.mystical.listeners.ChatListener;
import mys.serone.mystical.listeners.OnFirstJoin;

public final class Mystical extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new OnFirstJoin(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        new ConfigurationManager(this);
        CommandEntry.initCommands(this);
        ReadMeConfiguration readMeConfiguration = new ReadMeConfiguration(this);
        readMeConfiguration.writeToFile();
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Mystical] Shutting down");
    }

}

