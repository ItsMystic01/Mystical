package mys.serone.mystical.roleCommands;

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
import java.util.List;
import java.util.Objects;

public class CheckAllRank implements CommandExecutor {
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;
    public CheckAllRank(RanksManager ranksManager, FileConfiguration langFile) {
        this.RANKS_MANAGER = ranksManager;
        this.LANG_FILE = langFile;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        List<Rank> allRanks = RANKS_MANAGER.getRanks();
        Player player = (Player) sender;
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.CHECK_ALL_RANK.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }

        for (Rank rank : allRanks) {
            String rankPrefix = rank.getPrefix();

            if (rankPrefix == null) {
                rankPrefix = "&c[&fInvalid Rank&c]";
                System.out.println("[Mystical] Incomplete/Invalid rank format in ranks.yml");
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', rankPrefix));
        }

        return true;
    }
}
