package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteRank implements CommandExecutor {

    private final ChatFunctions CHAT_FUNCTIONS;
    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    public DeleteRank(ChatFunctions chatFunctions, RanksManager ranksManager, PlayerInfoManager playerInfoManager) {
        this.CHAT_FUNCTIONS = chatFunctions;
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("mystical.manageranks")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 1) { CHAT_FUNCTIONS.commandSyntaxError( (Player) sender, "/deleteRank [Rank Name]"); return true; }

        Rank rankChecker = RANKS_MANAGER.getRank(args[0]);
        if (rankChecker == null) { CHAT_FUNCTIONS.rankChat((Player) sender, "Rank does not exist"); return true; }
        RANKS_MANAGER.removeRank(rankChecker);

        CHAT_FUNCTIONS.rankChat((Player) sender, args[0] + " Rank has been deleted successfully.");
        new ConfigManager(RANKS_MANAGER, PLAYER_INFO_MANAGER);
        return true;
    }
}
