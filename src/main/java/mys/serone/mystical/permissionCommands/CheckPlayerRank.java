package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
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
    public CheckPlayerRank(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);

        if (args.length < 1) { chatFunctions.commandSyntaxError( (Player) sender, "/checkPlayerRank [username]"); return true; }
        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { chatFunctions.rankChat((Player) sender, "Player not found."); return true; }
        String uuid = player.getUniqueId().toString();

        RanksManager ranksManager = new RanksManager(PLUGIN);

        List<String> playerRankList = playerInfoManager.getPlayerRankList(uuid);
        List<Map<String, Integer>> playerRankPriority = new ArrayList<>();

        for (String playerRank : playerRankList) {
            try {
                int rankPriority = ranksManager.getRank(playerRank).getPriority();
                Map<String, Integer> newMap = new HashMap<>();
                newMap.put(playerRank, rankPriority);
                playerRankPriority.add(newMap);
            } catch (Exception e) {
                System.out.println(player.getDisplayName() + " has invalid ranks on player_info.yml");
                sender.sendMessage(player.getDisplayName() + " has invalid ranks on player_info.yml");
                return true;
            }
        }

        playerRankPriority.sort((o1, o2) -> {
            Integer value1 = Collections.min(o1.values());
            Integer value2 = Collections.max(o2.values());
            return value1.compareTo(value2);
        });

        List<String> playerSortedRankList = new ArrayList<>();

        for (Map<String, Integer> stringIntegerMap : playerRankPriority) {
            String rankSort = stringIntegerMap.keySet().toString();
            playerSortedRankList.add(rankSort.replace("[", "").replace("]", ""));
        }

        chatFunctions.playerRankChat((Player) sender, playerSortedRankList);

        return true;
    }
}
