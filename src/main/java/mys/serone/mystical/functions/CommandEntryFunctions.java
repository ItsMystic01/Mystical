package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.commands.*;
import mys.serone.mystical.configurationSystem.*;
import mys.serone.mystical.handlers.*;
import mys.serone.mystical.kit.*;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.listeners.*;
import mys.serone.mystical.roleCommands.*;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class CommandEntryFunctions {

    public void initCommands(Mystical plugin) {

        InputStream langStream = getClass().getResourceAsStream("/lang.yml");
        assert langStream != null;
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(langStream));

        File configurationFile = new File(plugin.getDataFolder().getAbsolutePath() + "/mystical_configuration.yml");
        File readMeFile = new File(plugin.getDataFolder().getAbsolutePath() + "/README.txt");
        File kitFile = new File(plugin.getDataFolder().getAbsolutePath() + "/personal_kit_configuration.yml");
        File ranksFile = new File(plugin.getDataFolder().getAbsolutePath() + "/ranks.yml");
        File playerInfoFile = new File(plugin.getDataFolder().getAbsolutePath() + "/player_info.yml");

        RanksManager ranksManager = new RanksManager(ranksFile, langConfig);
        PersonalKitManager personalKitManager = new PersonalKitManager(kitFile, langConfig);
        ConfigurationManager configurationManager = new ConfigurationManager(configurationFile);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(ranksManager, playerInfoFile, langConfig);
        Configuration configuration = new Configuration();
        KitManager kitManager = new KitManager(plugin, ranksManager, playerInfoManager, personalKitManager, langConfig);

        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(playerInfoManager, ranksManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new OnFirstJoinListener(plugin, configurationManager, configuration, playerInfoManager, ranksManager, ranksFile), plugin);

        new ReadMeConfiguration(readMeFile).writeToFile();
        new ConfigManager(ranksManager, playerInfoManager);

        CommandExecutor[] commandExecutors = {
                new Fly(langConfig),
                new Kit(plugin, kitManager, ranksManager, langConfig),
                new Message(plugin, ranksManager, playerInfoManager, langConfig),
                new MysticalHelp(plugin, langConfig),
                new Rename(langConfig),
                new CreateKit(plugin, personalKitManager, langConfig),
                new DeleteKit(plugin, personalKitManager, langConfig),
                new EditKit(plugin, langConfig),
                new SetKitPrefix(plugin, personalKitManager, ranksManager, langConfig),
                new CheckAllRank(ranksManager, langConfig),
                new CheckPlayerRank(plugin, playerInfoManager, ranksManager, langConfig),
                new CreateRank(ranksManager, playerInfoManager, langConfig),
                new DeleteRank(ranksManager, playerInfoManager, langConfig),
                new GivePermission(plugin, playerInfoManager, langConfig),
                new GiveRank(plugin, playerInfoManager, ranksManager, langConfig),
                new RemovePermission(plugin, playerInfoManager, langConfig),
                new RemoveRank(plugin, playerInfoManager, ranksManager, langConfig)
        };

        for (CommandExecutor executor : commandExecutors) {
            String commandName = executor.getClass().getSimpleName().toLowerCase();
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(executor);
        }
    }
}
