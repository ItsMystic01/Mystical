package mys.serone.mystical;

import mys.serone.mystical.configurationSystem.Configuration;
import mys.serone.mystical.configurationSystem.ConfigurationManager;
import mys.serone.mystical.functions.CommandAndEventLoader;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class Mystical extends JavaPlugin {

    @Override
    public void onEnable() {

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            boolean result = dataFolder.mkdirs();

            if (result) {
                Bukkit.getServer().getLogger().info("[Mystical] Data Folder Created Successfully");
            } else {
                Bukkit.getServer().getLogger().info("[Mystical] ERROR: Could Not Create Data Folder");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        InputStream langStream = getClass().getResourceAsStream( "/lang.yml");
        assert langStream != null;

        File langFile = new File(dataFolder, "lang.yml");
        if (!langFile.exists()) {
            try (InputStream in = getResource("lang.yml")) {
                assert in != null;
                Files.copy(in, langFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);

        File configurationFile = new File(getDataFolder().getAbsolutePath() + File.separator + "mystical_configuration.yml");
        File readMeFile = new File(getDataFolder().getAbsolutePath() + File.separator + "README.txt");
        File kitFile = new File(getDataFolder().getAbsolutePath() + File.separator + "personal_kit_configuration.yml");
        File ranksFile = new File(getDataFolder().getAbsolutePath() + File.separator + "ranks.yml");
        File playerInfoFile = new File(getDataFolder().getAbsolutePath() + File.separator + "player_info.yml");

        RanksManager ranksManager = new RanksManager(ranksFile, langConfig);
        PersonalKitManager personalKitManager = new PersonalKitManager(kitFile, langConfig);
        ConfigurationManager configurationManager = new ConfigurationManager(configurationFile);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(ranksManager, playerInfoFile, langConfig);
        Configuration configuration = new Configuration();
        KitManager kitManager = new KitManager(this, ranksManager, playerInfoManager, personalKitManager, langConfig);

        CommandAndEventLoader commandAndEventLoader = new CommandAndEventLoader();

        commandAndEventLoader.registerCommands(this, ranksManager, personalKitManager, playerInfoManager, kitManager, langConfig);
        commandAndEventLoader.registerEvents(this, playerInfoManager, ranksManager, configurationManager, configuration, ranksFile);
        commandAndEventLoader.registerOrLoadData(readMeFile, ranksManager, playerInfoManager);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Mystical] Shutting down");
    }

}

