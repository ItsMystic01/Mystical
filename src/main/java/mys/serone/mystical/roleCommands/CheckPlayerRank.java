package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.RankConfigurationHandler;
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
    private final FileConfiguration LANG_CONFIG;

    public CheckPlayerRank(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
        this.LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.CHECK_PLAYER_RANK.getPermission())) {
            playerSender.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }
        if (args.length < 1) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/checkPlayerRank <username>"), LANG_CONFIG));
            return true;
        }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Player not found."), LANG_CONFIG));
            return true;
        }

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
                new RankConfigurationHandler(RANKS_MANAGER, PLAYER_INFO_MANAGER);

                System.out.println("[Mystical] " + player.getDisplayName() + " has invalid ranks on player_info.yml");
                player.sendMessage(MysticalMessage.USER_INVALID_RANK_CONFIGURATION_ERROR.formatMessage(Collections.singletonMap("player", player.getDisplayName()), LANG_CONFIG));
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
