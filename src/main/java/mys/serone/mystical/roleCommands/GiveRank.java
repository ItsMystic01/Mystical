package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
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

public class GiveRank implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;
    public GiveRank(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
        this.LANG_FILE = langFile;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!playerSender.hasPermission(MysticalPermission.permissionENUM.GIVE_RANK.getPermission())) { playerSender.sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 2) { playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "/giveRank <player> <rank name>"))); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) { playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Invalid User"))); return true; }

        String rankName = args[1];

        Rank rank = RANKS_MANAGER.getRank(rankName);

        if (rank == null) { playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Rank does not exist"))); return true; }

        PLAYER_INFO_MANAGER.addRankToPlayer(player, playerSender, rank.getName());

        return true;
    }
}
