package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.commands.*;
import mys.serone.mystical.economy.Balance;
import mys.serone.mystical.economy.GiveCoins;
import mys.serone.mystical.economy.Pay;
import mys.serone.mystical.economy.RemoveCoins;
import mys.serone.mystical.handlers.Menu;
import mys.serone.mystical.handlers.NPCHandler;
import mys.serone.mystical.handlers.TorchHandler;
import mys.serone.mystical.permissionCommands.*;
import mys.serone.mystical.util.SpawnUtil;
import org.bukkit.command.CommandExecutor;
import java.util.Objects;

public class CommandEntry {
    private CommandEntry() {
    }

    public static void initCommands(Mystical plugin) {

        SpawnUtil spawnUtil = new SpawnUtil(plugin);
        new TorchHandler(plugin);
        new NPCHandler();

        CommandExecutor[] commandExecutors = {
                new Fly(),
                new Creative(),
                new Kit(),
                new Menu(plugin),
                new Spawn(spawnUtil),
                new SetSpawn(spawnUtil),
                new Rename(),
                new Help(plugin),
                new Balance(plugin),
                new GiveCoins(plugin),
                new RemoveCoins(plugin),
                new Pay(plugin),
                new PlayerPermission(),
                new GiveRank(plugin),
                new CheckAllRank(),
                new CreateRank(),
                new DeleteRank(),
                new CheckPlayerRank(plugin),
                new Message(plugin)
        };

        for (CommandExecutor executor : commandExecutors) {
            String commandName = executor.getClass().getSimpleName().toLowerCase();
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(executor);
        }
    }
}
