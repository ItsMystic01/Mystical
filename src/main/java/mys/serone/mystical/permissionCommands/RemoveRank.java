package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveRank implements CommandExecutor {
    private final Mystical PLUGIN;
    public RemoveRank(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        RanksManager ranksManager = new RanksManager(PLUGIN);

        if (!sender.hasPermission("mystical.manageranks")) { chatFunctions.commandPermissionError((Player) sender); return true; }
        if (args.length < 2) { chatFunctions.commandSyntaxError((Player) sender, "/removeRank [Player] [Rank Name]"); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { chatFunctions.rankChat((Player) sender, "Invalid User"); return true; }

        String rankName = args[1];

        Rank rank = ranksManager.getRank(rankName);
        if (rank == null) { chatFunctions.rankChat((Player) sender, "Rank does not exist"); return true; }

        playerInfoManager.removeRankToPlayer(player, sender, rank.getName());

        return true;
    }
}
