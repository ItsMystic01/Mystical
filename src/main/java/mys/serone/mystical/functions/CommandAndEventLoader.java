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

public class CommandAndEventLoader {

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
                new CreateRank(ranksManager, playerInfoManager, langConfig),
                new DeleteAllRank(ranksManager, langConfig, playerInfoManager),
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

    public void registerEvents(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager,
                               ConfigurationManager configurationManager, Configuration configuration, File ranksFile) {
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(playerInfoManager, ranksManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new OnFirstJoinListener(plugin, configurationManager, configuration, playerInfoManager, ranksManager, ranksFile), plugin);

    }

    public void registerOrLoadData(File readMeFile, RanksManager ranksManager, PlayerInfoManager playerInfoManager) {
        new ReadMeConfiguration(readMeFile).writeToFile();
        new RankConfigurationHandler(ranksManager, playerInfoManager);
    }

}
