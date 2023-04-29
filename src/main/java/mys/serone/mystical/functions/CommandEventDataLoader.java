package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.commands.*;
import mys.serone.mystical.configurationSystem.Configuration;
import mys.serone.mystical.configurationSystem.ConfigurationManager;
import mys.serone.mystical.handlers.RankConfigurationHandler;
import mys.serone.mystical.handlers.ReadMeConfiguration;
import mys.serone.mystical.kit.CreateKit;
import mys.serone.mystical.kit.DeleteKit;
import mys.serone.mystical.kit.EditKit;
import mys.serone.mystical.kit.SetKitPrefix;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.listeners.ChatListener;
import mys.serone.mystical.listeners.OnFirstJoinListener;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import mys.serone.mystical.roleCommands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Objects;

/**
 * Class for loading data, and registering commands, events, and data
 */
public class CommandEventDataLoader {

    /**
     * Used in registering commands for the plugin
     * @param plugin : Mystical Plugin
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param personalKitManager : Personal Kit Manager used in accessing its functions.
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param kitManager : Kit Manager used in gathering the kits from the Kits Folder
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see RanksManager
     * @see PlayerInfoManager
     * @see PersonalKitManager
     * @see MysticalMessage
     */
    public void registerCommands(Mystical plugin, RanksManager ranksManager,
                                 PersonalKitManager personalKitManager, PlayerInfoManager playerInfoManager, KitManager kitManager, FileConfiguration langConfig) {

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
                new CreateRank(ranksManager, langConfig),
                new DeleteAllRank(ranksManager, playerInfoManager, langConfig),
                new DeleteRank(ranksManager, playerInfoManager, langConfig),
                new GivePermission(plugin, playerInfoManager, langConfig),
                new GiveRank(plugin, playerInfoManager, ranksManager, langConfig),
                new RemovePermission(plugin, playerInfoManager, langConfig),
                new RemoveRank(plugin, playerInfoManager, ranksManager, langConfig),
        };

        for (CommandExecutor executor : commandExecutors) {
            String commandName = executor.getClass().getSimpleName().toLowerCase();
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(executor);
        }

    }

    /**
     * Used in registering events for the plugin
     * @param plugin : Mystical Plugin
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param configurationManager : Configuration used in accessing its function in managing mystical_configuration.yml
     * @param configuration : Configuration used in accessing data in mystical_configuration.yml
     * @param ranksFile : ranks.yml located by the onEnable Function in Mystical Main Class
     * @see PlayerInfoManager
     * @see RanksManager
     * @see ConfigurationManager
     * @see Configuration
     * @see Mystical
     */
    public void registerEvents(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager,
                               ConfigurationManager configurationManager, Configuration configuration, File ranksFile) {
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(playerInfoManager, ranksManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new OnFirstJoinListener(plugin, configurationManager, configuration, playerInfoManager, ranksManager, ranksFile), plugin);

    }

    /**
     * Used in registering, updating and loading for the plugin
     * @param readMeFile : README.txt located by the onEnable Function in Mystical Main Class
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @see Mystical
     * @see RanksManager
     * @see PlayerInfoManager
     */
    public void registerOrLoadData(File readMeFile, RanksManager ranksManager, PlayerInfoManager playerInfoManager) {
        new ReadMeConfiguration(readMeFile).writeToFile();
        new RankConfigurationHandler(ranksManager, playerInfoManager);
    }

}
