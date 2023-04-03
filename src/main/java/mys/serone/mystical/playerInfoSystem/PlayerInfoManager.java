package mys.serone.mystical.playerInfoSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PlayerInfoManager {
    private final HashMap<String, PlayerInfo> PLAYER_INFO;
    private final File PLAYER_INFO_FILE;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final RanksManager RANKS_MANAGER;

    public PlayerInfoManager(ChatFunctions chatFunctions, RanksManager ranksManager, File playerInfoFile) {
        this.CHAT_FUNCTIONS = chatFunctions;
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_FILE = playerInfoFile;
        if (!PLAYER_INFO_FILE.exists()) {
            try {
                boolean created = PLAYER_INFO_FILE.createNewFile();
                if (created) {
                    System.out.println("[Mystical] Player Info File created successfully");
                } else {
                    System.out.println("[Mystical] Player Info File already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.PLAYER_INFO = loadPlayerInfoFromFile();
    }

    private HashMap<String, PlayerInfo> loadPlayerInfoFromFile() {
        HashMap<String, PlayerInfo> ranks = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (PLAYER_INFO_FILE.length() == 0) {
                System.out.println("[Mystical] Player Info file is empty.");
                return ranks;
            }
            ranks = mapper.readValue(PLAYER_INFO_FILE, new TypeReference<HashMap<String, PlayerInfo>>() {});
        } catch (JsonParseException e) {
            System.out.println("[Mystical] Ranks file has invalid formatting.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading Player Info file.");
            e.printStackTrace();
        }
        return ranks;
    }


    public HashMap<String, PlayerInfo> getAllPlayerInfo() {
        return PLAYER_INFO;
    }

    public void createPlayerInfo(String playerUUID, Double userCoins, List<String> userRankList, List<String> userAdditionalPermission) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setUserCoins(userCoins);
        playerInfo.setUserRankList(userRankList);
        playerInfo.setUserAdditionalPermission(userAdditionalPermission);
        PLAYER_INFO.put(playerUUID, playerInfo);
        savePlayerInfoToFile();
    }

    public List<String> getPlayerRankList(String UUID) {
        PlayerInfo playerInfo = PLAYER_INFO.get(UUID);
        return playerInfo.getUserRankList();
    }

    public Double getPlayerCoins(String UUID) {
        PlayerInfo playerInfo = PLAYER_INFO.get(UUID);
        return playerInfo.getUserCoins();
    }

    public void updatePlayerCoins(String UUID, Double newBalance) {
        PlayerInfo playerInfo = PLAYER_INFO.get(UUID);
        playerInfo.setUserCoins(newBalance);
        savePlayerInfoToFile();
    }

    public void addRankToPlayer(Player player, CommandSender sender, String rankToAdd) {
        PlayerInfo playerInfo = PLAYER_INFO.get(player.getUniqueId().toString());
        List<String> playerInfoRankList = playerInfo.getUserRankList();

        if (playerInfoRankList.stream().anyMatch(s -> s.equalsIgnoreCase(rankToAdd))) {
            CHAT_FUNCTIONS.rankChat((Player) sender, player.getDisplayName() + " already has that rank.");
            return;
        }

        playerInfoRankList.add(rankToAdd);
        playerInfo.setUserRankList(playerInfoRankList);
        savePlayerInfoToFile();
        new ConfigManager(RANKS_MANAGER, this);

        CHAT_FUNCTIONS.rankChat((Player) sender, rankToAdd + " has been given to " + player.getDisplayName() + ".");
        CHAT_FUNCTIONS.rankChat((Player) sender, "It is recommended for " + player.getDisplayName() + " to re-log for the rank-update to take effect.");
        CHAT_FUNCTIONS.rankChat(player, "It is recommended to re-log for the rank-update to take effect.");
    }

    public void removeRankToPlayer(Player player, CommandSender sender, String rankToRemove) {
        PlayerInfo playerInfo = PLAYER_INFO.get(player.getUniqueId().toString());
        List<String> playerInfoRankList = playerInfo.getUserRankList();

        if (playerInfoRankList.stream().noneMatch(s -> s.equalsIgnoreCase(rankToRemove))) {
            CHAT_FUNCTIONS.rankChat((Player) sender, player.getDisplayName() + " does not have that rank.");
            return;
        }

        playerInfoRankList.remove(rankToRemove);
        playerInfo.setUserRankList(playerInfoRankList);
        savePlayerInfoToFile();
        new ConfigManager(RANKS_MANAGER, this);

        CHAT_FUNCTIONS.rankChat((Player) sender, rankToRemove + " has been removed from " + player.getDisplayName() + ".");
        CHAT_FUNCTIONS.rankChat((Player) sender, "It is recommended for " + player.getDisplayName() + " to re-log for the rank-update to take effect.");
    }

    public void savePlayerInfoToFile() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(PLAYER_INFO_FILE, PLAYER_INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

