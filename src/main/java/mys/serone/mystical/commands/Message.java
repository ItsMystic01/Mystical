package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Message implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;
    public Message(Mystical plugin, RanksManager ranksManager, PlayerInfoManager playerInfoManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
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

        if (!player.hasPermission(MysticalPermission.permissionENUM.MESSAGE.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 2) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "/message <user> <message>"))); return true; }

        Player targetPlayer = PLUGIN.getServer().getPlayer(args[0]);

        if (targetPlayer == player) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "You cannot message yourself."))); return true;}
        if (targetPlayer == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "/message <user> <message>"))); return true; }

        String playerUUID = player.getUniqueId().toString();
        String targetUUID = targetPlayer.getUniqueId().toString();

        List<String> senderRankList = PLAYER_INFO_MANAGER.getPlayerRankList(playerUUID);
        List<String> recipientRankList = PLAYER_INFO_MANAGER.getPlayerRankList(targetUUID);

        String senderPrefix = RANKS_MANAGER.getRank(senderRankList.get(0)).getPrefix();
        String recipientPrefix = RANKS_MANAGER.getRank(recipientRankList.get(0)).getPrefix();
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dTo &r" + recipientPrefix + " " + targetPlayer.getDisplayName() + "&7: ") + message);
        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dFrom &r" + senderPrefix + " " + player.getDisplayName() + "&7: ") + message);

        return true;
    }
}
