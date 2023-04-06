package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.functions.PermissionENUM;
import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class CheckPlayerRank implements CommandExecutor {
    private final Mystical PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    public CheckPlayerRank(Mystical plugin, ChatFunctions chatFunctions, PlayerInfoManager playerInfoManager, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { return true; }
        if (!sender.hasPermission(PermissionENUM.permissionENUM.CHECK_PLAYER_RANK.getPermission())) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 1) { CHAT_FUNCTIONS.commandSyntaxError( (Player) sender, "/checkPlayerRank [username]"); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { CHAT_FUNCTIONS.rankChat((Player) sender, "Player not found."); return true; }
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
                CHAT_FUNCTIONS.configurationError(player, player.getDisplayName() + " has invalid ranks on player_info.yml");
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

        CHAT_FUNCTIONS.playerRankChat((Player) sender, playerSortedRankList);

        return true;
    }
}
