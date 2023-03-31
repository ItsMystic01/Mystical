package mys.serone.mystical.playerInfoSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.handlers.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerInfoManager {
    private final List<PlayerInfo> PLAYER_INFO;
    private final File PLAYER_INFO_FILE;
    private final Mystical PLUGIN;
    private final ChatFunctions CHATFUNCTIONS;

    public PlayerInfoManager(Mystical plugin) {
        this.PLUGIN = plugin;
        this.CHATFUNCTIONS = new ChatFunctions(plugin);
        this.PLAYER_INFO_FILE = new File(plugin.getDataFolder().getAbsolutePath() + "/player_info.yml");
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

    private List<PlayerInfo> loadPlayerInfoFromFile() {
        List<PlayerInfo> ranks = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (PLAYER_INFO_FILE.length() == 0) {
                System.out.println("[Mystical] Player Info file is empty.");
                return ranks;
            }
            ranks = mapper.readValue(PLAYER_INFO_FILE, new TypeReference<List<PlayerInfo>>() {});
        } catch (JsonParseException e) {
            System.out.println("[Mystical] Ranks file has invalid formatting.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading Player Info file.");
            e.printStackTrace();
        }
        return ranks;
    }


    public List<PlayerInfo> getAllPlayerInfo() {
        return PLAYER_INFO;
    }

    public void createPlayerInfo(String playerUUID, Double userCoins, List<String> userRankList, List<String> userAdditionalPermission) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setPlayerUUID(playerUUID);
        playerInfo.setUserCoins(userCoins);
        playerInfo.setUserRankList(userRankList);
        playerInfo.setUserAdditionalPermission(userAdditionalPermission);
        PLAYER_INFO.add(playerInfo);
        savePlayerInfoToFile();
    }

    public List<String> getPlayerRankList(String UUID) {
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        List<PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        List<String> userRankList = null;

        for (PlayerInfo perInfo : allPlayerInfo) {
            if (Objects.equals(perInfo.getPlayerUUID(), UUID)) {
                userRankList = perInfo.getUserRankList();
            }
        }
        return userRankList;
    }

    public Double getPlayerCoins(String UUID) {
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        List<PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        Double userCoins = null;

        for (PlayerInfo perInfo : allPlayerInfo) {
            if (Objects.equals(perInfo.getPlayerUUID(), UUID)) {
                userCoins = perInfo.getUserCoins();
            }
        }
        return userCoins;
    }

    public void updatePlayerCoins(String UUID, Double newBalance) {
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        List<PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        for (PlayerInfo perInfo : allPlayerInfo) {
            if (Objects.equals(perInfo.getPlayerUUID(), UUID)) {
                perInfo.setUserCoins(newBalance);
                playerInfoManager.savePlayerInfoToFile();
            }
        }
    }

    public void addRankToPlayer(Player player, CommandSender sender, String rankToAdd) {
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        for (PlayerInfo perPlayer : playerInfoManager.getAllPlayerInfo()) {
            if (perPlayer.getPlayerUUID().equals(player.getUniqueId().toString())) {
                List<String> playerRankList = perPlayer.getUserRankList();
                if (playerRankList.stream().anyMatch(s -> s.equalsIgnoreCase(rankToAdd))) {
                    CHATFUNCTIONS.rankChat((Player) sender, player.getDisplayName() + " already has that rank.");
                    return;
                }
                playerRankList.add(rankToAdd);
                perPlayer.setUserRankList(playerRankList);
                playerInfoManager.savePlayerInfoToFile();
                CHATFUNCTIONS.rankChat((Player) sender, rankToAdd + " has been given to " + player.getDisplayName() + ".");
                CHATFUNCTIONS.rankChat((Player) sender, "It is recommended for " + player.getDisplayName() + " to re-log for the rank-update to take effect.");
                CHATFUNCTIONS.rankChat(player, "It is recommended to re-log for the rank-update to take effect.");
                new ConfigManager(PLUGIN);
            }
        }
    }

    public void removeRankToPlayer(Player player, CommandSender sender, String rankToRemove) {
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        for (PlayerInfo perPlayer : playerInfoManager.getAllPlayerInfo()) {
            if (perPlayer.getPlayerUUID().equals(player.getUniqueId().toString())) {
                List<String> playerRankList = perPlayer.getUserRankList();
                if (playerRankList.stream().noneMatch(s -> s.equalsIgnoreCase(rankToRemove))) {
                    CHATFUNCTIONS.rankChat((Player) sender, player.getDisplayName() + " does not have that rank.");
                    return;
                }
                playerRankList.remove(rankToRemove);
                perPlayer.setUserRankList(playerRankList);
                playerInfoManager.savePlayerInfoToFile();
                CHATFUNCTIONS.rankChat((Player) sender, rankToRemove + " has been removed from " + player.getDisplayName() + ".");
                CHATFUNCTIONS.rankChat((Player) sender, "It is recommended for " + player.getDisplayName() + " to re-log for the rank-update to take effect.");
                new ConfigManager(PLUGIN);
            }
        }
    }

    public List<String> getPlayerAdditionalPermission(String UUID) {
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        List<PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        List<String> additionalPermission = new ArrayList<>();

        for (PlayerInfo perInfo : allPlayerInfo) {
            if (Objects.equals(perInfo.getPlayerUUID(), UUID)) {
                additionalPermission = perInfo.getUserAdditionalPermission();
            }
        }
        return additionalPermission;
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

