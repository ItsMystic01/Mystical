package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CheckAllRank implements CommandExecutor {
    private final RanksManager RANKS_MANAGER;

    public CheckAllRank(RanksManager ranksManager) {
        this.RANKS_MANAGER = ranksManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        HashMap<String, Rank> allRanks = RANKS_MANAGER.getRanks();
        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.permissionENUM.CHECK_ALL_RANK.getPermission())) {
            player.sendMessage(MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage());
            return true;
        }

        for (String rank : allRanks.keySet()) {
            String rankPrefix = allRanks.get(rank).getPrefix();

            if (rankPrefix == null) {
                rankPrefix = "&c[&fInvalid Rank&c]";
                System.out.println("[Mystical] Incomplete/Invalid rank format in ranks.yml");
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', rankPrefix));
        }

        return true;
    }
}
