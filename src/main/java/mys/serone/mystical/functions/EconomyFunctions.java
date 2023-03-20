package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconomyFunctions {
    public ChatFunctions chatFunctions = new ChatFunctions();
    public void updateCoins(Mystical plugin, Player player, Player target, String targetUuid, double amount, Command command) {

        try (PreparedStatement statement = plugin.getConnection().prepareStatement(
                "SELECT player_coins FROM player_info WHERE player_uuid = ?")) {
            statement.setString(1, targetUuid);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                player.sendMessage("This player does not have an account yet.");
                return;
            }

            double currentBalance = result.getDouble("player_coins");
            double newBalance = 0;
            String status = "";
            if (command.getName().equalsIgnoreCase("removeCoins")) {
                if (currentBalance < amount) {
                    player.sendMessage("Invalid Amount.");
                    return;
                }
                newBalance = currentBalance - amount;
                status = "Deducted";
            } else if (command.getName().equalsIgnoreCase("giveCoins")) {
                newBalance = currentBalance + amount;
                status = "Added";
            }

            try (PreparedStatement updateStatement = plugin.getConnection().prepareStatement(
                    "UPDATE player_info SET player_coins = ? WHERE player_uuid = ?")) {
                updateStatement.setDouble(1, newBalance);
                updateStatement.setString(2, targetUuid);
                updateStatement.executeUpdate();
                chatFunctions.adminEconomyChat(player, target, status, newBalance, amount);
            } catch (SQLException e) {
                plugin.getLogger().warning("Failed to update balance for player " + targetUuid + ": " + e.getMessage());
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to check balance for player " + targetUuid + ": " + e.getMessage());
        }
    }

    public void payCoins(Mystical plugin, Player player, Player recipient, String senderUuid, String recipientUuid, double amount) {

        try (PreparedStatement senderStatement = plugin.getConnection().prepareStatement(
                "SELECT player_coins FROM player_info WHERE player_uuid = ?")) {
            senderStatement.setString(1, senderUuid);
            ResultSet result = senderStatement.executeQuery();

            if (result.next()) {
                double balance = result.getDouble("player_coins");
                if (balance < amount) {
                    player.sendMessage("You do not have enough coins.");
                    return;
                }
            } else {
                player.sendMessage("You do not have a balance.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement updateStatement = plugin.getConnection().prepareStatement(
                "UPDATE player_info SET player_coins = player_coins - ? WHERE player_uuid = ?")) {
            updateStatement.setDouble(1, amount);
            updateStatement.setString(2, senderUuid);
            double rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated == 0) {
                player.sendMessage("Failed to update your balance.");
                return;
            }

            try (PreparedStatement recipientStatement = plugin.getConnection().prepareStatement(
                    "UPDATE player_info SET player_coins = player_coins + ? WHERE player_uuid = ?")) {
                recipientStatement.setDouble(1, amount);
                recipientStatement.setString(2, recipientUuid);
                recipientStatement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().warning("Failed to update recipient balance: " + e.getMessage());
            }

        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to update sender balance: " + e.getMessage());
        }

        player.sendMessage(chatFunctions.payerEconomyChat(recipient, amount));
        recipient.sendMessage(chatFunctions.recipientEconomyChat(player, amount));
    }
}



