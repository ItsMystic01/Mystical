package mys.serone.mystical.rankSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.functions.MysticalMessage;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RanksManager {
    private final HashMap<String, Rank> RANKS;
    private final File RANKS_FILE;

    public RanksManager(File ranksFile) {
        this.RANKS_FILE = ranksFile;
        if (!RANKS_FILE.exists()) {
            try {
                boolean created = RANKS_FILE.createNewFile();
                if (created) {
                    System.out.println("[Mystical] File created successfully");
                } else {
                    System.out.println("[Mystical] File already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.RANKS = loadRanksFromFile();
    }

    private HashMap<String, Rank> loadRanksFromFile() {
        HashMap<String, Rank> ranks = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (RANKS_FILE.length() == 0) {
                System.out.println("[Mystical] Ranks file is empty.");
                return ranks;
            }
            ranks = mapper.readValue(RANKS_FILE, new TypeReference<HashMap<String, Rank>>() {});
        } catch (JsonParseException e) {
            System.out.println("[Mystical] Ranks file has invalid formatting.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading ranks file.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return ranks;
    }


    public HashMap<String, Rank> getRanks() {
        return RANKS;
    }

    public Rank getRank(String name) {
        return RANKS.get(name);
    }

    public void setKitName(Player player, String rankName, String kitName) {
        Rank rankToConfigure = getRank(rankName);
        rankToConfigure.setKitName(kitName);
        saveRanksToFile();
        player.sendMessage(MysticalMessage.messageENUM.SET_KIT_PREFIX_SUCCESSFUL.formatMessage());
    }

    public void createRank(String name, String prefix, int priority, List<String> newRankPermission, List<Map<String, Object>> kit, String kitName) {
        Rank rank = new Rank();
        rank.setName(name);
        rank.setPrefix(prefix);
        rank.setPriority(priority);
        rank.setPermissions(newRankPermission);
        rank.setKit(kit);
        rank.setKitName(kitName);
        RANKS.put(name, rank);
        saveRanksToFile();
    }

    public void removeRank(String rank) {
        RANKS.remove(rank);
        saveRanksToFile();
    }

    public void saveRanksToFile() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(RANKS_FILE, RANKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

