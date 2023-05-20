package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.Gradient;
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

/**
 * Class for checking all ranks present of a user
 */
public class PlayerRank implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param ranksManager : Player Info Manager used in accessing its functions.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see PlayerInfoManager
     * @see RanksManager
     * @see MysticalMessage
     */
    public PlayerRank(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
        this.LANG_CONFIG = langConfig;
    }

    /**
     * @param sender : CommandExecutor
     * @param command : Command Used
     * @param label : Aliases
     * @param args : String List Arguments
     * @return boolean true or false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.PLAYER_RANK.getPermission())) {
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

            String startingHexCode = RANKS_MANAGER.getRank(perRank).getStartingHexColor();
            String endingHexCode = RANKS_MANAGER.getRank(perRank).getEndingHexColor();
            if (startingHexCode != null && endingHexCode != null) {
                String gradientRank = String.valueOf(Gradient.displayName(perRank, startingHexCode, endingHexCode, false));
                rankPrefix = ChatColor.translateAlternateColorCodes('&', "&7&l[" + gradientRank + "&7&l]");
            } else if (RANKS_MANAGER.getRank(perRank) == null || RANKS_MANAGER.getRank(perRank).getPrefix() == null) {
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
