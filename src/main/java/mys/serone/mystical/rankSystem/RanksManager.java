package mys.serone.mystical.rankSystem;

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

public class RanksManager {
    private final HashMap<UUID, Rank> RANKS_BY_ID;
    private final HashMap<String, UUID> RANKS_BY_NAME;
    private final File RANKS_FILE;
    private final FileConfiguration LANG_CONFIG;

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
                rank.setId(UUID.randomUUID());
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


    public HashMap<UUID, Rank> getRanks() {
        return RANKS_BY_ID;
    }

    public Rank getRank(UUID id) {
        return RANKS_BY_ID.get(id);
    }

    public Rank getRank(String name) {
        UUID id = RANKS_BY_NAME.get(name);
        if (id == null) {
            return null;
        }
        return RANKS_BY_ID.get(id);
    }

    public void setKitName(Player player, String rankName, String kitName) {
        Rank rankToConfigure = getRank(rankName);
        rankToConfigure.setKitName(kitName);
        saveRanksToFile();
        player.sendMessage(MysticalMessage.SET_KIT_PREFIX_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

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

    public void removeRank(String rank) {
        Rank toRemove = getRank(rank);
        RANKS_BY_ID.remove(toRemove.getId());
        RANKS_BY_NAME.remove(toRemove.getName());
        saveRanksToFile();
    }

    public void deleteAllRank() {
        RANKS_BY_NAME.clear();
        RANKS_BY_ID.clear();
        saveRanksToFile();
    }

    public void saveRanksToFile() {
        ObjectMapper mapperForID = new ObjectMapper(new YAMLFactory());
        try {
            mapperForID.writeValue(RANKS_FILE, RANKS_BY_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

