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

import java.io.File;
import java.util.Objects;

public class CommandAndEventLoader {

    public void registerCommands(Mystical plugin, RanksManager ranksManager,
                                 PersonalKitManager personalKitManager, PlayerInfoManager playerInfoManager, KitManager kitManager) {

        CommandExecutor[] commandExecutors = {
                new Fly(),
                new Kit(plugin, kitManager, ranksManager),
                new Message(plugin, ranksManager, playerInfoManager),
                new MysticalHelp(plugin),
                new Rename(),
                new CreateKit(plugin, personalKitManager),
                new DeleteKit(plugin, personalKitManager),
                new EditKit(plugin),
                new SetKitPrefix(plugin, personalKitManager, ranksManager),
                new CheckAllRank(ranksManager),
                new CheckPlayerRank(plugin, playerInfoManager, ranksManager),
                new CreateRank(ranksManager, playerInfoManager),
                new DeleteRank(ranksManager, playerInfoManager),
                new GivePermission(plugin, playerInfoManager),
                new GiveRank(plugin, playerInfoManager, ranksManager),
                new RemovePermission(plugin, playerInfoManager),
                new RemoveRank(plugin, playerInfoManager, ranksManager),
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
