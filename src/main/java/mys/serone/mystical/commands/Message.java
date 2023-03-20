package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Message implements CommandExecutor {
    public ChatFunctions chatFunctions = new ChatFunctions();
    private final Mystical PLUGIN;

    public Message(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) { chatFunctions.commandSyntaxError(player, "/message [user] [message]"); return true; }

        Player targetPlayer = PLUGIN.getServer().getPlayer(args[0]);

        if (targetPlayer == player) { chatFunctions.commandSyntaxError(player, "You can not message yourself."); return true;}
        if (targetPlayer == null) { chatFunctions.commandSyntaxError(player, "/message [user] [message]"); return true; }

        String playerUUID = player.getUniqueId().toString();
        String targetUUID = targetPlayer.getUniqueId().toString();

        try (PreparedStatement statement = PLUGIN.getConnection().prepareStatement(
                "SELECT player_rank FROM player_info WHERE player_uuid IN (?, ?)")) {
            statement.setString(1, playerUUID);
            statement.setString(2, targetUUID);
            ResultSet result = statement.executeQuery();
            if (!result.next()) { chatFunctions.rankChat(player, "No account recorded in the database."); }
            String plRank = result.getString("player_rank");
            String targetUserRank = null;
            if(result.next()) { targetUserRank = result.getString("player_rank"); }

            List<String> currentRanks = Arrays.asList(plRank.split(","));
            String userRank = currentRanks.get(0);
            assert targetUserRank != null;
            List<String> targetCurrent = Arrays.asList(targetUserRank.split(","));
            String targetRank = targetCurrent.get(0);

            List<String> finalizedArguments = new ArrayList<>();
            Collections.addAll(finalizedArguments, Arrays.copyOfRange(args, 1, args.length));
            StringBuilder message = new StringBuilder();
            for ( String perMessage : finalizedArguments) {
                message.append(perMessage);
                message.append(" ");
            }

            RanksManager ranksManager = new RanksManager(new File("C:/Users/ItsMystic01/Downloads/MyJava/Spigot/src/main/resources/ranks.yml"));
            String senderPrefix = ranksManager.getRank(userRank).getPrefix();
            String targetPrefix = ranksManager.getRank(targetRank).getPrefix();
            chatFunctions.messageChat(player, senderPrefix, targetPlayer, targetPrefix, message.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
