package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteRank implements CommandExecutor {

    private final Mystical PLUGIN;
    public DeleteRank(Mystical plugin) { this.PLUGIN = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        RanksManager ranksManager = new RanksManager(PLUGIN);

        if (!sender.hasPermission("mystical.manageranks")) { chatFunctions.commandPermissionError((Player) sender); return true; }
        if (args.length < 1) { chatFunctions.commandSyntaxError( (Player) sender, "/deleteRank [Rank Name]"); return true; }

        Rank rankChecker = ranksManager.getRank(args[0]);
        if (rankChecker == null) { chatFunctions.rankChat((Player) sender, "Rank does not exist"); return true; }
        ranksManager.removeRank(rankChecker);

        chatFunctions.rankChat((Player) sender, args[0] + " Rank has been deleted successfully.");
        return true;
    }
}
