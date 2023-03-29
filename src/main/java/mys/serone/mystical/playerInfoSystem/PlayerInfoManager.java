package mys.serone.mystical.playerInfoSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
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

    public PlayerInfoManager(Mystical plugin) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_FILE = new File(plugin.getDataFolder().getAbsolutePath() + "/player_info.yml");
        if (!PLAYER_INFO_FILE.exists()) {
            try {
                boolean created = PLAYER_INFO_FILE.createNewFile();
                if (created) {
                    System.out.println("File created successfully");
                } else {
                    System.out.println("File already exists");
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
                System.out.println("Player Info file is empty.");
                return ranks;
            }
            ranks = mapper.readValue(PLAYER_INFO_FILE, new TypeReference<List<PlayerInfo>>() {});
        } catch (JsonParseException e) {
            System.out.println("Ranks file has invalid formatting.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error loading Player Info file.");
            e.printStackTrace();
        }
        return ranks;
    }


    public List<PlayerInfo> getAllPlayerInfo() {
        return PLAYER_INFO;
    }

    public void createPlayerInfo(PlayerInfo playerInfo) {
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

    public void updatePlayerRankList(Player player, CommandSender sender, String rankToAdd) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
         List<String> playerRankList = playerInfoManager.getPlayerRankList(player.getUniqueId().toString());
         if (playerRankList.stream().anyMatch(s -> s.equalsIgnoreCase(rankToAdd))) { chatFunctions.rankChat((Player) sender, player.getDisplayName() + " already has that rank."); }
         playerRankList.add(rankToAdd);
         savePlayerInfoToFile();
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

