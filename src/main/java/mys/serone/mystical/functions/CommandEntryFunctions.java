package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.beta.CustomParticle;
import mys.serone.mystical.commands.*;
import mys.serone.mystical.configurationSystem.*;
import mys.serone.mystical.economy.*;
import mys.serone.mystical.handlers.*;
import mys.serone.mystical.kit.*;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.listeners.*;
import mys.serone.mystical.roleCommands.*;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.CommandExecutor;
import java.io.File;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class CommandEntryFunctions {
    public CommandEntryFunctions() {
    }

    public void initCommands(Mystical plugin) {

        File configurationFile = new File(plugin.getDataFolder().getAbsolutePath() + "/mystical_configuration.yml");
        File readMeFile = new File(plugin.getDataFolder().getAbsolutePath() + "/README.txt");
        File kitFile = new File(plugin.getDataFolder().getAbsolutePath() + "/personal_kit_configuration.yml");
        File ranksFile = new File(plugin.getDataFolder().getAbsolutePath() + "/ranks.yml");
        File playerInfoFile = new File(plugin.getDataFolder().getAbsolutePath() + "/player_info.yml");

        RanksManager ranksManager = new RanksManager(ranksFile);
        ChatFunctions chatFunctions = new ChatFunctions(ranksManager);
        PersonalKitManager personalKitManager = new PersonalKitManager(chatFunctions, kitFile);
        ConfigurationManager configurationManager = new ConfigurationManager(configurationFile);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(chatFunctions, ranksManager, playerInfoFile);
        Configuration configuration = new Configuration();
        KitManager kitManager = new KitManager(plugin, chatFunctions, ranksManager, playerInfoManager, personalKitManager);

        getServer().getPluginManager().registerEvents(new ChatListener(playerInfoManager, ranksManager), plugin);
        getServer().getPluginManager().registerEvents(new OnFirstJoinListener(plugin, configurationManager, configuration, playerInfoManager, chatFunctions, ranksManager, ranksFile), plugin);

        new ReadMeConfiguration(readMeFile).writeToFile();
        new ConfigManager(ranksManager, playerInfoManager);

        CommandExecutor[] commandExecutors = {
                new Fly(chatFunctions),
                new Rename(chatFunctions),
                new MysticalHelp(plugin, chatFunctions),
                new Balance(chatFunctions, playerInfoManager),
                new Pay(plugin, chatFunctions, playerInfoManager),
                new CheckAllRank(chatFunctions, ranksManager),
                new CreateRank(chatFunctions, ranksManager),
                new DeleteRank(chatFunctions, ranksManager, playerInfoManager),
                new CheckPlayerRank(plugin, chatFunctions, playerInfoManager, ranksManager),
                new Message(plugin, ranksManager, chatFunctions, playerInfoManager),
                new Kit(plugin, chatFunctions, kitManager, ranksManager),
                new GiveRank(plugin, chatFunctions, playerInfoManager, ranksManager),
                new RemoveRank(plugin, chatFunctions, playerInfoManager, ranksManager),
                new CreateKit(plugin, ranksManager, personalKitManager),
                new DeleteKit(plugin, chatFunctions, personalKitManager),
                new GivePermission(plugin, chatFunctions, playerInfoManager),
                new RemovePermission(plugin, chatFunctions, playerInfoManager),
                new SetKitPrefix(plugin, chatFunctions, personalKitManager, ranksManager),
                new EditKit(plugin, ranksManager),
                new CustomParticle()
        };

        for (CommandExecutor executor : commandExecutors) {
            String commandName = executor.getClass().getSimpleName().toLowerCase();
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(executor);
        }
    }
}
