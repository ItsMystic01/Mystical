package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.commands.*;
import mys.serone.mystical.economy.Balance;
import mys.serone.mystical.economy.GiveCoins;
import mys.serone.mystical.economy.Pay;
import mys.serone.mystical.economy.RemoveCoins;
import mys.serone.mystical.handlers.Menu;
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

        CommandExecutor[] commandExecutors = {
                new Fly(plugin),
                new Creative(plugin),
                new Kit(),
                new Menu(plugin),
                new Spawn(plugin, spawnUtil),
                new SetSpawn(plugin, spawnUtil),
                new Rename(plugin),
                new Help(plugin, plugin),
                new Balance(plugin),
                new GiveCoins(plugin),
                new RemoveCoins(plugin),
                new Pay(plugin),
                new PlayerPermission(plugin),
                new GiveRank(plugin),
                new CheckAllRank(plugin),
                new CreateRank(plugin),
                new DeleteRank(plugin),
                new CheckPlayerRank(plugin),
                new Message(plugin),
                new GiveItem(),
                new GiveKit(plugin)
        };

        for (CommandExecutor executor : commandExecutors) {
            String commandName = executor.getClass().getSimpleName().toLowerCase();
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(executor);
        }
    }
}
