package mys.serone.mystical.economy;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Balance implements CommandExecutor {
    private final Mystical PLUGIN;

    public Balance(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement statement = PLUGIN.getConnection().prepareStatement(
                "SELECT player_coins FROM player_info WHERE player_uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();
            if (!result.next()) { chatFunctions.balanceEconomyChat(player, 0); }
            double balance = result.getDouble("player_coins");
            chatFunctions.balanceEconomyChat(player, balance);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
