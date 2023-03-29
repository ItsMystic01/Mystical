package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
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
    private final Mystical PLUGIN;
    public CheckAllRank(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        RanksManager ranksManager = new RanksManager(PLUGIN);
        List<Rank> allRanks = ranksManager.getRanks();

        if (!sender.hasPermission("mystical.manageranks")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        for (Rank rank : allRanks) {
            String rankPrefix = rank.getPrefix();
            if (rankPrefix == null) { rankPrefix = "&c[&fInvalid Rank&c]"; System.out.println("[Mystical] Incomplete/Invalid rank format in ranks.yml"); }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rankPrefix));
        }

        return true;
    }
}
