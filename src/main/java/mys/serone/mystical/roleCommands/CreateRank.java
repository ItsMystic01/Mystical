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

import java.util.*;

public class CreateRank implements CommandExecutor {

    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final FileConfiguration LANG_FILE;

    public CreateRank(RanksManager ranksManager, PlayerInfoManager playerInfoManager, FileConfiguration langFile) {
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

        if (!player.hasPermission(MysticalPermission.permissionENUM.CREATE_RANK.getPermission())) { player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 4) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "/createRank <rank name> <prefix> <priority> <permission...>"))); return true; }

        List<String> finalizedArguments = new ArrayList<>();

        Collections.addAll(finalizedArguments, Arrays.copyOfRange(args, 3, args.length));

        Rank rankChecker = RANKS_MANAGER.getRank(args[0]);

        if (rankChecker != null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Rank already exists"))); return true; }

        RANKS_MANAGER.createRank(args[0], args[1], Integer.parseInt(args[2]), finalizedArguments, null, null);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), args[0] + " Rank has been created successfully.")));

        new ConfigManager(RANKS_MANAGER, PLAYER_INFO_MANAGER);
        return true;
    }
}
