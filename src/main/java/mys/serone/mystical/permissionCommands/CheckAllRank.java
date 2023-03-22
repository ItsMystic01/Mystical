package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CheckAllRank implements CommandExecutor {
    private final Mystical PLUGIN;
    public CheckAllRank(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        RanksManager ranksManager = new RanksManager(PLUGIN);

        List<Rank> allRanks = ranksManager.getRanks();

        for (Rank rank : allRanks) {
            sender.sendMessage(rank.getName());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rank.getPrefix()));
            sender.sendMessage(rank.getPermissions().toString());
            sender.sendMessage(String.valueOf(rank.getIsDefault()));
        }
        return true;
    }
}
