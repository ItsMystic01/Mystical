package mys.serone.mystical.playerInfoSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.handlers.ConfigManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlayerInfoManager {
    private final HashMap<String, PlayerInfo> PLAYER_INFO;
    private final File PLAYER_INFO_FILE;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;

    public PlayerInfoManager(RanksManager ranksManager, File playerInfoFile, FileConfiguration langFile) {
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_FILE = playerInfoFile;
        this.LANG_FILE = langFile;
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

    public void createPlayerInfo(String playerUUID, List<String> userRankList, List<String> userAdditionalPermission) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setUserRankList(userRankList);
        playerInfo.setUserAdditionalPermission(userAdditionalPermission);
        PLAYER_INFO.put(playerUUID, playerInfo);
        savePlayerInfoToFile();
    }

    public List<String> getPlayerRankList(String UUID) {
        PlayerInfo playerInfo = PLAYER_INFO.get(UUID);
        return playerInfo.getUserRankList();
    }

    public void addRankToPlayer(Player player, Player playerSender, String rankToAdd) {
        PlayerInfo playerInfo = PLAYER_INFO.get(player.getUniqueId().toString());
        List<String> playerInfoRankList = playerInfo.getUserRankList();
        String langMessage = LANG_FILE.getString("information");

        if (playerInfoRankList.stream().anyMatch(s -> s.equalsIgnoreCase(rankToAdd))) {
            playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), player.getDisplayName() + " already has that rank.")));
            return;
        }

        playerInfoRankList.add(rankToAdd);
        playerInfo.setUserRankList(playerInfoRankList);
        savePlayerInfoToFile();
        new ConfigManager(RANKS_MANAGER, this);

        playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), rankToAdd + " has been given to " + player.getDisplayName() + ".")));
        playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "It is recommended for " + player.getDisplayName() + " to re-log for the rank-update to take effect.")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "It is recommended to re-log for the rank-update to take effect.")));

    }

    public void removeRankToPlayer(Player player, Player playerSender, String rankToRemove) {
        PlayerInfo playerInfo = PLAYER_INFO.get(player.getUniqueId().toString());
        List<String> playerInfoRankList = playerInfo.getUserRankList();
        String langMessage = LANG_FILE.getString("information");

        if (playerInfoRankList.stream().noneMatch(s -> s.equalsIgnoreCase(rankToRemove))) {
            playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), player.getDisplayName() + " does not have that rank.")));
            return;
        }

        playerInfoRankList.remove(rankToRemove);
        playerInfo.setUserRankList(playerInfoRankList);
        savePlayerInfoToFile();
        new ConfigManager(RANKS_MANAGER, this);

        playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), rankToRemove + " has been removed from " + player.getDisplayName() + ".")));
        playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "It is recommended for " + player.getDisplayName() + " to re-log for the rank-update to take effect.")));
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

