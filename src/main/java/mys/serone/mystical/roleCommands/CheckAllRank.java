package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CheckAllRank implements CommandExecutor {
    private final ChatFunctions CHAT_FUNCTIONS;
    private final RanksManager RANKS_MANAGER;
    public CheckAllRank(ChatFunctions chatFunctions, RanksManager ranksManager) {
        this.CHAT_FUNCTIONS = chatFunctions;
        this.RANKS_MANAGER = ranksManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<Rank> allRanks = RANKS_MANAGER.getRanks();

        if (!sender.hasPermission("mystical.manageranks")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        for (Rank rank : allRanks) {
            String rankPrefix = rank.getPrefix();

            if (rankPrefix == null) {
                rankPrefix = "&c[&fInvalid Rank&c]";
                System.out.println("[Mystical] Incomplete/Invalid rank format in ranks.yml");
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rankPrefix));
        }

        return true;
    }
}
