package mys.serone.mystical;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import mys.serone.mystical.functions.CommandEntryFunctions;
import java.io.File;

public final class Mystical extends JavaPlugin {

    @Override
    public void onEnable() {

        CommandEntryFunctions commandEntry = new CommandEntryFunctions();

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            boolean result = dataFolder.mkdirs();
            if (result) { Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Successfully"); }
            else { Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Unsuccessfully."); }
        }

        commandEntry.initCommands(this);
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Mystical] Shutting down");
    }

}

