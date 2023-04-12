package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteRank implements CommandExecutor {

    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final FileConfiguration LANG_FILE;
    public DeleteRank(RanksManager ranksManager, PlayerInfoManager playerInfoManager, FileConfiguration langFile) {
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.LANG_FILE = langFile;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.DELETE_RANK.getPermission())) { player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 1) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "/deleteRank <rank name>"))); return true; }

        Rank rankChecker = RANKS_MANAGER.getRank(args[0]);

        if (rankChecker == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Rank does not exist"))); return true; }

        RANKS_MANAGER.removeRank(rankChecker);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), args[0] + " Rank has been deleted successfully.")));

        new ConfigManager(RANKS_MANAGER, PLAYER_INFO_MANAGER);

        return true;
    }
}
