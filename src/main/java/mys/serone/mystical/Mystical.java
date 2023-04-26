package mys.serone.mystical;

import mys.serone.mystical.configurationSystem.Configuration;
import mys.serone.mystical.configurationSystem.ConfigurationManager;
import mys.serone.mystical.functions.CommandAndEventLoader;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Mystical extends JavaPlugin {

    @Override
    public void onEnable() {

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            boolean result = dataFolder.mkdirs();

            if (result) {
                Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Successfully");
            } else {
                Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Unsuccessfully.");
            }
        }

        File configurationFile = new File(getDataFolder().getAbsolutePath() + "/mystical_configuration.yml");
        File readMeFile = new File(getDataFolder().getAbsolutePath() + "/README.txt");
        File kitFile = new File(getDataFolder().getAbsolutePath() + "/personal_kit_configuration.yml");
        File ranksFile = new File(getDataFolder().getAbsolutePath() + "/ranks.yml");
        File playerInfoFile = new File(getDataFolder().getAbsolutePath() + "/player_info.yml");

        RanksManager ranksManager = new RanksManager(ranksFile);
        PersonalKitManager personalKitManager = new PersonalKitManager(kitFile);
        ConfigurationManager configurationManager = new ConfigurationManager(configurationFile);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(ranksManager, playerInfoFile);
        Configuration configuration = new Configuration();
        KitManager kitManager = new KitManager(this, ranksManager, playerInfoManager, personalKitManager);

        CommandAndEventLoader commandAndEventLoader = new CommandAndEventLoader();

        commandAndEventLoader.registerCommands(this, ranksManager, personalKitManager, playerInfoManager, kitManager);
        commandAndEventLoader.registerEvents(this, playerInfoManager, ranksManager, configurationManager, configuration, ranksFile);
        commandAndEventLoader.registerOrLoadData(readMeFile, ranksManager, playerInfoManager);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Mystical] Shutting down");
    }

}

