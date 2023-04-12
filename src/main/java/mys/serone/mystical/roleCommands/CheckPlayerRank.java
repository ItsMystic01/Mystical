package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class CheckPlayerRank implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;
    public CheckPlayerRank(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager, FileConfiguration langFile) {
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
        String langUserInvalidRankConfigurationErrorMessage = LANG_FILE.getString("user_invalid_rank_configuration_error");

        if (!playerSender.hasPermission(MysticalPermission.permissionENUM.CHECK_PLAYER_RANK.getPermission())) { playerSender.sendMessage(
                ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 1) { playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "/checkPlayerRank <username>"))); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) { playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Player not found."))); return true; }

        String uuid = player.getUniqueId().toString();
        List<String> playerRankList = PLAYER_INFO_MANAGER.getPlayerRankList(uuid);
        List<Map<String, Integer>> playerRankPriority = new ArrayList<>();
        List<String> playerSortedRankList = new ArrayList<>();

        for (String playerRank : playerRankList) {
            try {
                int rankPriority = RANKS_MANAGER.getRank(playerRank).getPriority();
                Map<String, Integer> newMap = new HashMap<>();

                newMap.put(playerRank, rankPriority);
                playerRankPriority.add(newMap);
            } catch (Exception e) {
                new ConfigManager(RANKS_MANAGER, PLAYER_INFO_MANAGER);

                System.out.println("[Mystical] " + player.getDisplayName() + " has invalid ranks on player_info.yml");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        String.format(Objects.requireNonNull(langUserInvalidRankConfigurationErrorMessage), player)));
                return true;
            }
        }

        playerRankPriority.sort((o1, o2) -> {
            Integer value1 = Collections.min(o1.values());
            Integer value2 = Collections.max(o2.values());

            return value1.compareTo(value2);
        });


        for (Map<String, Integer> stringIntegerMap : playerRankPriority) {
            String rankSort = stringIntegerMap.keySet().toString();
            playerSortedRankList.add(rankSort.replace("[", "").replace("]", ""));
        }

        StringBuilder userRank = new StringBuilder();
        for ( String perRank : playerSortedRankList ) {
            String rankPrefix;
            if (RANKS_MANAGER.getRank(perRank) == null || RANKS_MANAGER.getRank(perRank).getPrefix() == null) {
                rankPrefix = "&c[&fInvalid Rank&c]";
            } else {
                rankPrefix = RANKS_MANAGER.getRank(perRank).getPrefix();
            }
            userRank.append(rankPrefix);
            userRank.append(" ");
        }

        playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(userRank)));

        return true;
    }
}
