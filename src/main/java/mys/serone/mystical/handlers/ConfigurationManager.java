package mys.serone.mystical.handlers;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigurationManager {

    public ConfigurationManager(Mystical plugin) {

        RanksManager ranksManager = new RanksManager(plugin);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(plugin);

        List<Rank> allRanks = ranksManager.getRanks();

        for (Rank rankName : allRanks) {
            if (rankName.getName() == null) {
                 System.out.println("[Mystical] Invalid Rank Format at ranks.yml");
            }
        }

        List<Map<String, Integer>> playerRankPriority = new ArrayList<>();

            try {
                List<Rank> rankPriority = ranksManager.getRanks();
                for (Rank rankToCheck : rankPriority) {
                    Map<String, Integer> newMap = new HashMap<>();
                    newMap.put(rankToCheck.getName(), rankToCheck.getPriority());
                    playerRankPriority.add(newMap);
                }
            } catch (Exception e) {
                System.out.println("[Mystical] A problem has occurred in the ranks.yml");
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

        List<PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        for (PlayerInfo certainPlayer : allPlayerInfo) {
            List<String> certainPlayerRankList = certainPlayer.getUserRankList();
            List<String> sortedPlayerRankList = certainPlayerRankList.stream()
                    .filter(playerSortedRankList::contains)
                    .sorted(Comparator.comparingInt(playerSortedRankList::indexOf))
                    .collect(Collectors.toList());
            certainPlayer.setUserRankList(sortedPlayerRankList);
            playerInfoManager.savePlayerInfoToFile();
        }
    }
}
