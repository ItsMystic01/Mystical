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
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    public RemoveRank(Mystical plugin, ChatFunctions chatFunctions, PlayerInfoManager playerInfoManager, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission("mystical.manageranks")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 2) { CHAT_FUNCTIONS.commandSyntaxError((Player) sender, "/removeRank [Player] [Rank Name]"); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { CHAT_FUNCTIONS.rankChat((Player) sender, "Invalid User"); return true; }

        String rankName = args[1];

        Rank rank = RANKS_MANAGER.getRank(rankName);
        if (rank == null) { CHAT_FUNCTIONS.rankChat((Player) sender, "Rank does not exist"); return true; }

        PLAYER_INFO_MANAGER.removeRankToPlayer(player, sender, rank.getName());

        return true;
    }
}
