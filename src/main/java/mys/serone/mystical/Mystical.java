package mys.serone.mystical;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import mys.serone.mystical.functions.CommandEntry;
import mys.serone.mystical.listeners.ChatListener;
import mys.serone.mystical.listeners.OnFirstJoin;

public final class Mystical extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new OnFirstJoin(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        CommandEntry.initCommands(this);
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Shutting down");
    }

}

