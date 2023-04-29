package mys.serone.mystical.playerInfoSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.functions.MysticalMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * * Class responsible for managing player information
 */
public class PlayerInfoManager {
    private final HashMap<String, PlayerInfo> PLAYER_INFO;
    private final File PLAYER_INFO_FILE;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param playerInfoFile : player_info.yml located by the onEnable Function in Mystical Main Class
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see mys.serone.mystical.Mystical
     */
    public PlayerInfoManager(File playerInfoFile, FileConfiguration langConfig) {
        this.PLAYER_INFO_FILE = playerInfoFile;
        this.LANG_CONFIG = langConfig;
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

    /**
     * @return HashMap of all loaded String and PlayerInfo
     */
    private HashMap<String, PlayerInfo> loadPlayerInfoFromFile() {
        HashMap<String, PlayerInfo> playerInfo = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (PLAYER_INFO_FILE.length() == 0) {
                System.out.println("[Mystical] Player Info file is empty.");
                return playerInfo;
            }
            playerInfo = mapper.readValue(PLAYER_INFO_FILE, new TypeReference<HashMap<String, PlayerInfo>>() {});
        } catch (JsonParseException e) {
            System.out.println("[Mystical] Ranks file has invalid formatting.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading Player Info file.");
            e.printStackTrace();
        }
        return playerInfo;
    }


    /**
     * @return HashMap of all loaded String and PlayerInfo
     */
    public HashMap<String, PlayerInfo> getAllPlayerInfo() {
        return PLAYER_INFO;
    }

    /**
     * @param playerUUID : UUID of the player
     * @param userRankList : String List of Ranks for the player
     * @param userAdditionalPermission : String List of Additional Permissions for the player
     */
    public void createPlayerInfo(String playerUUID, List<String> userRankList, List<String> userAdditionalPermission) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setUserRankList(userRankList);
        playerInfo.setUserAdditionalPermission(userAdditionalPermission);
        PLAYER_INFO.put(playerUUID, playerInfo);
        savePlayerInfoToFile();
    }

    /**
     * @param UUID : UUID of the player
     * @return String List of player rank list
     */
    public List<String> getPlayerRankList(String UUID) {
        PlayerInfo playerInfo = PLAYER_INFO.get(UUID);
        if (playerInfo == null) { return null; }
        return playerInfo.getUserRankList();
    }

    /**
     * @param player : Player to receive the rank
     * @param playerSender : CommandExecutor
     * @param rankToAdd : String Name of the rank to be given to the player
     */
    public void addRankToPlayer(Player player, Player playerSender, String rankToAdd) {
        PlayerInfo playerInfo = PLAYER_INFO.get(player.getUniqueId().toString());
        List<String> playerInfoRankList = playerInfo.getUserRankList();

        if (playerInfoRankList.stream().anyMatch(s -> s.equalsIgnoreCase(rankToAdd))) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", player.getDisplayName() + " already has that rank."), LANG_CONFIG));
            return;
        }

        playerInfoRankList.add(rankToAdd);
        playerInfo.setUserRankList(playerInfoRankList);
        savePlayerInfoToFile();

        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", rankToAdd + " has been given to " + player.getDisplayName() + "."), LANG_CONFIG));
        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended for " +
                player.getDisplayName() + " to re-log for the rank-update to take effect."), LANG_CONFIG));
        player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended to re-log for the rank-update to take effect."), LANG_CONFIG));

    }

    /**
     * @param player : Player that will get revoked of the rank
     * @param playerSender : CommandExecutor
     * @param rankToRemove : String Name of the rank to be revoked to the player
     */
    public void removeRankToPlayer(Player player, Player playerSender, String rankToRemove) {
        PlayerInfo playerInfo = PLAYER_INFO.get(player.getUniqueId().toString());
        List<String> playerInfoRankList = playerInfo.getUserRankList();

        if (playerInfoRankList.stream().noneMatch(s -> s.equalsIgnoreCase(rankToRemove))) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", player.getDisplayName() + " does not have that rank."), LANG_CONFIG));
            return;
        }

        playerInfoRankList.remove(rankToRemove);
        playerInfo.setUserRankList(playerInfoRankList);
        savePlayerInfoToFile();

        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", rankToRemove + " has been removed from " + player.getDisplayName() + "."), LANG_CONFIG));
        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended for " +
                player.getDisplayName() + " to re-log for the rank-update to take effect."), LANG_CONFIG));
    }

    /**
     * @param rankToRemove : String name of the rank to remove
     */
    public void updatePlayerRankList(String rankToRemove) {
        HashMap<String, PlayerInfo> allPlayerInfo = this.getAllPlayerInfo();
        for (Map.Entry<String, PlayerInfo> playerInfo : allPlayerInfo.entrySet()) {
            PlayerInfo playerInfoValues = playerInfo.getValue();
            List<String> playerRankList = playerInfoValues.getUserRankList();
            if(!playerRankList.contains(rankToRemove)) { continue; }
            playerRankList.remove(rankToRemove);
            playerInfoValues.setUserRankList(Collections.singletonList(rankToRemove));
            savePlayerInfoToFile();
        }
    }

    /**
     * @param listOfRankToRemove : String List of rank names to remove
     */
    public void updatePlayerRankList(List<String> listOfRankToRemove) {
        HashMap<String, PlayerInfo> allPlayerInfo = this.getAllPlayerInfo();
        for (Map.Entry<String, PlayerInfo> playerInfo : allPlayerInfo.entrySet()) {
            PlayerInfo playerInfoValues = playerInfo.getValue();
            List<String> playerRankList = playerInfoValues.getUserRankList();
            for (String rankToRemove : listOfRankToRemove) {
                if (!playerRankList.contains(rankToRemove)) {
                    continue;
                }
                playerRankList.remove(rankToRemove);
            }
            playerInfoValues.setUserRankList(playerRankList);
            savePlayerInfoToFile();
        }
    }

    /**
     * Saves all changes in player_info.yml
     */
    public void savePlayerInfoToFile() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(PLAYER_INFO_FILE, PLAYER_INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

