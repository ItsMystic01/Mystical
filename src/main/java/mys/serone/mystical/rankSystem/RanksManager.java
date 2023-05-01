package mys.serone.mystical.rankSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class responsible for managing ranks
 */
public class RanksManager {
    private final HashMap<UUID, Rank> RANKS_BY_ID;
    private final HashMap<String, UUID> RANKS_BY_NAME;
    private final File RANKS_FILE;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param ranksFile : ranks.yml file that contains all rank information for the server.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see MysticalMessage
     */
    public RanksManager(File ranksFile, FileConfiguration langConfig) {
        this.RANKS_FILE = ranksFile;
        this.LANG_CONFIG = langConfig;
        if (!RANKS_FILE.exists()) {
            try {
                boolean created = RANKS_FILE.createNewFile();
                if (created) {
                    System.out.println("[Mystical] ranks file created successfully");
                } else {
                    System.out.println("[Mystical] ranks file already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.RANKS_BY_ID = loadRanksFromFile();
        this.RANKS_BY_NAME = new HashMap<>();
        for (Rank rank : RANKS_BY_ID.values()) {
            RANKS_BY_NAME.put(rank.getName(), rank.getId());
        }
    }

    /**
     * @return HashMap<UUID, Rank>
     */
    private HashMap<UUID, Rank> loadRanksFromFile() {
        HashMap<UUID, Rank> ranks = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (RANKS_FILE.length() == 0) {
                System.out.println("[Mystical] ranks file is empty.");
                return ranks;
            }
            ranks = mapper.readValue(RANKS_FILE, new TypeReference<HashMap<UUID, Rank>>() {});
            for (Rank rank : ranks.values()) {
                rank.setId(rank.getId());
            }
        } catch (JsonParseException e) {
            System.out.println("[Mystical] ranks file has invalid formatting.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading ranks file.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return ranks;
    }


    /**
     * @return HashMap of UUID as keys, and Rank as values
     */
    public HashMap<UUID, Rank> getRanks() {
        return RANKS_BY_ID;
    }

    /**
     * @param id : UUID of the rank
     * @return Rank
     */
    public Rank getRank(UUID id) {
        return RANKS_BY_ID.get(id);
    }

    /**
     * @param name : name of the rank
     * @return Rank
     */
    public Rank getRank(String name) {
        UUID id = RANKS_BY_NAME.get(name);
        if (id == null) {
            return null;
        }
        return RANKS_BY_ID.get(id);
    }

    /**
     * @param player : Command Executor
     * @param rankName : Rank Name to set a kit name into
     * @param kitName : Kit Name to be set for the rank
     */
    public void setKitName(Player player, String rankName, String kitName) {
        Rank rankToConfigure = getRank(rankName);
        rankToConfigure.setKitName(kitName);
        saveRanksToFile();
        player.sendMessage(MysticalMessage.SET_KIT_PREFIX_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

    /**
     * @param id : Random UUID Generated at CreateRank
     * @param name : Name of the rank provided at CreateRank
     * @param prefix : Prefix of the rank provided at CreateRank
     * @param priority : Priority of the rank provided at CreateRank
     * @param newRankPermission : Rank Permissions for the rank provided at CreateRank
     * @param kit : Kit contents for the rank provided at CreateRank | Default: null
     * @param kitName : Kit name for the kit contents for the rank provided at CreateRank | Default: null
     */
    public void createRank(UUID id, String name, String prefix, int priority, List<String> newRankPermission, List<Map<String, Object>> kit, String kitName) {
        Rank rank = new Rank();
        rank.setId(id);
        rank.setName(name);
        rank.setPrefix(prefix);
        rank.setPriority(priority);
        rank.setPermissions(newRankPermission);
        rank.setKit(kit);
        rank.setKitName(kitName);
        RANKS_BY_ID.put(rank.getId(), rank);
        RANKS_BY_NAME.put(rank.getName(), rank.getId());
        saveRanksToFile();
    }

    /**
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param rank : rank name provided to be removed from the rank list in ranks.yml
     */
    public void removeRank(PlayerInfoManager playerInfoManager, String rank) {
        rankRemovalPerPlayer(playerInfoManager, rank);
        Rank toRemove = getRank(rank);
        RANKS_BY_ID.remove(toRemove.getId());
        RANKS_BY_NAME.remove(toRemove.getName());
        saveRanksToFile();
    }

    /**
     * Deletes all listed ranks in ranks.yml
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     */
    public void deleteAllRank(PlayerInfoManager playerInfoManager) {
        RANKS_BY_NAME.clear();
        RANKS_BY_ID.clear();
        rankRemovalPerPlayer(playerInfoManager);
        saveRanksToFile();
    }


    /**
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param name : deletes all listed rank in string list from ranks.yml
     */
    public void deleteAllRank(PlayerInfoManager playerInfoManager, List<String> name) {
        for (String rankNameToRemove : name) {
            UUID id = RANKS_BY_NAME.get(rankNameToRemove);
            RANKS_BY_ID.remove(id);
            RANKS_BY_NAME.remove(rankNameToRemove);
            rankRemovalPerPlayer(playerInfoManager, rankNameToRemove);
        }
        saveRanksToFile();
    }

    /**
     * Saves all changes in ranks.yml
     */
    public void saveRanksToFile() {
        ObjectMapper mapperForID = new ObjectMapper(new YAMLFactory());
        try {
            mapperForID.writeValue(RANKS_FILE, RANKS_BY_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param rank : String name of the rank to remove
     */
    public void rankRemovalPerPlayer(PlayerInfoManager playerInfoManager, String rank) {
        HashMap<String, PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();
        for (Map.Entry<String, PlayerInfo> playerInfo : allPlayerInfo.entrySet()) {
            PlayerInfo playerInfoValues = playerInfo.getValue();
            List<String> playerRankList = playerInfoValues.getUserRankList();

            if (!playerRankList.contains(rank)) { continue; }
            playerRankList.remove(rank);
            playerInfoValues.setUserRankList(playerRankList);

            UUID uuidOfPlayer = UUID.fromString(playerInfo.getKey());
            Player rankPlayer = Bukkit.getServer().getPlayer(uuidOfPlayer);
            if (rankPlayer == null) { return; }
            if (rankPlayer.isOnline()) {
                rankPlayer.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", rank + " rank has been removed from you due to its deletion"), LANG_CONFIG));
            }

        }
    }

    /**
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     */
    public void rankRemovalPerPlayer(PlayerInfoManager playerInfoManager) {
        HashMap<String, PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();
        for (Map.Entry<String, PlayerInfo> playerInfo : allPlayerInfo.entrySet()) {
            PlayerInfo playerInfoValues = playerInfo.getValue();
            List<String> playerRankList = playerInfoValues.getUserRankList();
            playerRankList.clear();
            playerInfoValues.setUserRankList(playerRankList);

            UUID uuidOfPlayer = UUID.fromString(playerInfo.getKey());
            Player rankPlayer = Bukkit.getServer().getPlayer(uuidOfPlayer);
            if (rankPlayer == null) { return; }
            if (rankPlayer.isOnline()) {
                rankPlayer.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message",  "All of your ranks has been removed due to its deletion"), LANG_CONFIG));
            }
        }
    }

    /**
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @see RanksManager
     * @see PlayerInfoManager
     */
    public void rankConfigurationHandler(RanksManager ranksManager, PlayerInfoManager playerInfoManager) {

        HashMap<UUID, Rank> allRanks = ranksManager.getRanks();
        List<Map<String, Integer>> playerRankPriority = new ArrayList<>();
        List<String> playerSortedRankList = new ArrayList<>();
        HashMap<String, PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        for (UUID rankId : allRanks.keySet()) {
            if (rankId == null) {
                System.out.println("[Mystical] Invalid Rank Format at ranks.yml");
            }
        }

        try {
            HashMap<UUID, Rank> rankPriority = ranksManager.getRanks();
            for (UUID rankToCheck : rankPriority.keySet()) {
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

