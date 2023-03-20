package mys.serone.mystical.listeners;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

import java.sql.*;

public class OnFirstJoin implements Listener {
    private final Mystical PLUGIN;
    public ChatFunctions chatFunctions = new ChatFunctions();

    public OnFirstJoin(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement statement = PLUGIN.getConnection().prepareStatement(
                "SELECT * FROM player_info WHERE player_uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                try (PreparedStatement insertStatement = PLUGIN.getConnection().prepareStatement(
                        "INSERT INTO player_info (player_uuid, player_rank, player_coins) VALUES (?, ?, ?)")) {
                    insertStatement.setString(1, uuid);
                    insertStatement.setString(2, "Member");
                    insertStatement.setInt(3, 1000);
                    insertStatement.executeUpdate();
                    player = event.getPlayer();
                    chatFunctions.informationChat(player, "You have received &a$1000&f coins for your first join.");
                } catch (SQLException e) {
                    PLUGIN.getLogger().warning("Failed to insert new player " + uuid + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            PLUGIN.getLogger().warning("Failed to check if player " + uuid + " exists: " + e.getMessage());
        }
    }
}

