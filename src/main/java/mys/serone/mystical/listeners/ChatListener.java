package mys.serone.mystical.listeners;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ChatListener implements Listener {
    private final Mystical PLUGIN;

    public ChatListener(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        Player player = event.getPlayer();
        String message = event.getMessage();
        String playerUUID = player.getUniqueId().toString();

        try (PreparedStatement statement = PLUGIN.getConnection().prepareStatement(
                "SELECT player_rank FROM player_info WHERE player_uuid = ?")) {
            statement.setString(1, playerUUID);
            ResultSet result = statement.executeQuery();
            if (!result.next()) { chatFunctions.rankChat(player, "No account recorded in the database."); }
            String playerRank = result.getString("player_rank");
            List<String> currentRanks = Arrays.asList(playerRank.split(","));
            String userRank = currentRanks.get(0);
            RanksManager ranksManager = new RanksManager(PLUGIN);
            String prefix = ranksManager.getRank(userRank).getPrefix();
            String displayName = prefix + " " + player.getDisplayName();
            String formattedMessage = ChatColor.translateAlternateColorCodes('&',  displayName + " : " + message);
            event.setFormat(formattedMessage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

