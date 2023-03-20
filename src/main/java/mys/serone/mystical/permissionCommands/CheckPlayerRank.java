package mys.serone.mystical.permissionCommands;

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
import java.util.Arrays;
import java.util.List;

public class CheckPlayerRank implements CommandExecutor {
    private final Mystical PLUGIN;
    public ChatFunctions chatFunctions = new ChatFunctions();
    public CheckPlayerRank(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length < 1) { chatFunctions.commandSyntaxError( (Player) sender, "/checkPlayerRank [username]"); return true; }
        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { chatFunctions.rankChat((Player) sender, "Player not found."); return true; }
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement statement = PLUGIN.getConnection().prepareStatement(
                "SELECT player_rank FROM player_info WHERE player_uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();
            if (!result.next()) { chatFunctions.rankChat((Player) sender, "User does not have an account yet."); }
            String playerRank = result.getString("player_rank");
            List<String> currentRanks = Arrays.asList(playerRank.split(","));
            chatFunctions.playerRankChat((Player) sender, currentRanks);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
