package mys.serone.mystical.rankSystem;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RanksManager {
    private final List<Rank> RANKS;
    private final File RANKS_FILE;
    private final Mystical PLUGIN;

    public RanksManager(Mystical plugin) {
        this.PLUGIN = plugin;
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

    public void addRank(Mystical plugin, Player player, Rank rank, CommandSender sender) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        String playerUUID = player.getUniqueId().toString();
        try(Connection connection = plugin.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT player_rank FROM player_info WHERE player_uuid = ?");
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String rankString = resultSet.getString("player_rank");
                List<String> currentRanks = Arrays.asList(rankString.split(","));

                if (currentRanks.contains(rank.getName())) { chatFunctions.rankChat((Player) sender,  player.getDisplayName() + " already has that rank."); return; }

                statement = connection.prepareStatement("UPDATE player_info SET player_rank = CONCAT(player_rank, ',', ?) WHERE player_uuid = ?");
                statement.setString(1, rank.getName());
                statement.setString(2, playerUUID);
                statement.executeUpdate();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());}
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

