package mys.serone.mystical.handlers;

import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import java.util.*;
import java.util.stream.Collectors;

public class RankConfigurationHandler {

    public RankConfigurationHandler(RanksManager ranksManager, PlayerInfoManager playerInfoManager) {

        HashMap<String, Rank> allRanks = ranksManager.getRanks();
        List<Map<String, Integer>> playerRankPriority = new ArrayList<>();
        List<String> playerSortedRankList = new ArrayList<>();
        HashMap<String, PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        for (String rankName : allRanks.keySet()) {
            if (rankName == null) {
                System.out.println("[Mystical] Invalid Rank Format at ranks.yml");
            }
        }

        try {
            HashMap<String, Rank> rankPriority = ranksManager.getRanks();
            for (String rankToCheck : rankPriority.keySet()) {
                Map<String, Integer> newMap = new HashMap<>();
                newMap.put(rankPriority.get(rankToCheck).getName(), rankPriority.get(rankToCheck).getPriority());
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

        for (Map<String, Integer> stringIntegerMap : playerRankPriority) {
            String rankSort = stringIntegerMap.keySet().toString();
            playerSortedRankList.add(rankSort.replace("[", "").replace("]", ""));
        }

        for (Map.Entry<String, PlayerInfo> playerInfo : allPlayerInfo.entrySet()) {
            PlayerInfo playerInfoValues = playerInfo.getValue();
            List<String> playerInfoValuesUserRankList = playerInfoValues.getUserRankList();
            List<String> playerInfoValuesUserSortedRankList = playerInfoValuesUserRankList.stream()
                    .filter(playerSortedRankList::contains)
                    .sorted(Comparator.comparingInt(playerSortedRankList::indexOf))
                    .collect(Collectors.toList());
            playerInfoValues.setUserRankList(playerInfoValuesUserSortedRankList);
            playerInfoManager.savePlayerInfoToFile();
        }
    }
}