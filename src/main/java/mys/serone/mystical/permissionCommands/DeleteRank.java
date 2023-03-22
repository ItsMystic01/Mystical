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
        if (args.length < 1) { chatFunctions.commandSyntaxError( (Player) sender, "/deleteRank [Rank Name]"); return true; }

        RanksManager ranksManager = new RanksManager(PLUGIN);

        Rank rankChecker = ranksManager.getRank(args[0]);
        if (rankChecker == null) { chatFunctions.rankChat((Player) sender, "Rank does not exist"); return true; }
        ranksManager.removeRank(rankChecker);

        sender.sendMessage("Success");
        return true;
    }
}
