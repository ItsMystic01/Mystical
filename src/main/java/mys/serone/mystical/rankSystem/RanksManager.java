package mys.serone.mystical.rankSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.Mystical;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RanksManager {
    private final List<Rank> RANKS;
    private final File RANKS_FILE;

    public RanksManager(Mystical plugin) {
        this.RANKS_FILE = new File(plugin.getDataFolder().getAbsolutePath() + "/ranks.yml");
        if (!RANKS_FILE.exists()) {
            try {
                boolean created = RANKS_FILE.createNewFile();
                if (created) {
                    System.out.println("File created successfully");
                } else {
                    System.out.println("File already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.RANKS = loadRanksFromFile();
    }

    private List<Rank> loadRanksFromFile() {
        List<Rank> ranks = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (RANKS_FILE.length() == 0) {
                System.out.println("Ranks file is empty.");
                return ranks;
            }
            ranks = mapper.readValue(RANKS_FILE, new TypeReference<List<Rank>>() {});
        } catch (JsonParseException e) {
            System.out.println("Ranks file has invalid formatting.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error loading ranks file.");
            e.printStackTrace();
        }
        return ranks;
    }


    public List<Rank> getRanks() {
        return RANKS;
    }

    public Rank getRank(String name) {
        return RANKS.stream()
                .filter(rank -> rank.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void createRank(Rank rank) {
        RANKS.add(rank);
        saveRanksToFile();
    }

    public void removeRank(Rank rank) {
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

