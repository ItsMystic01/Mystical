package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class CheckAllRank implements CommandExecutor {
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public CheckAllRank(RanksManager ranksManager, FileConfiguration langConfig) {
        this.RANKS_MANAGER = ranksManager;
        this.LANG_CONFIG = langConfig;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        HashMap<UUID, Rank> allRanks = RANKS_MANAGER.getRanks();
        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.CHECK_ALL_RANK.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        for (UUID rank : allRanks.keySet()) {
            Rank rankAccount = allRanks.get(rank);
            String rankName = rankAccount.getName();
            String rankPrefix = rankAccount.getPrefix();

            if (rankPrefix == null) {
                rankPrefix = "&c[&fInvalid Rank&c]";
                System.out.println("[Mystical] Incomplete/Invalid rank format in ranks.yml");
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l- &7" + rankName + ": " + rankPrefix));
        }

        return true;
    }
}
