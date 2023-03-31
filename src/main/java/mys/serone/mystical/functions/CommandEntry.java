package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.commands.*;
import mys.serone.mystical.economy.Balance;
import mys.serone.mystical.economy.GiveCoins;
import mys.serone.mystical.economy.Pay;
import mys.serone.mystical.economy.RemoveCoins;
import mys.serone.mystical.kit.CreateKit;
import mys.serone.mystical.kit.DeleteKit;
import mys.serone.mystical.permissionCommands.*;
import org.bukkit.command.CommandExecutor;
import java.util.Objects;

public class CommandEntry {
    private CommandEntry() {
    }

    public static void initCommands(Mystical plugin) {

        CommandExecutor[] commandExecutors = {
                new Fly(plugin),
                new Rename(plugin),
                new MysticalHelp(plugin, plugin),
                new Balance(plugin),
                new GiveCoins(plugin),
                new RemoveCoins(plugin),
                new Pay(plugin),
                new CheckAllRank(plugin),
                new CreateRank(plugin),
                new DeleteRank(plugin),
                new CheckPlayerRank(plugin),
                new Message(plugin),
                new Kit(plugin),
                new GiveRank(plugin),
                new RemoveRank(plugin),
                new CreateKit(plugin),
                new DeleteKit(plugin),
                new GivePermission(plugin),
                new RemovePermission(plugin)
        };

        for (CommandExecutor executor : commandExecutors) {
            String commandName = executor.getClass().getSimpleName().toLowerCase();
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(executor);
        }
    }
}
