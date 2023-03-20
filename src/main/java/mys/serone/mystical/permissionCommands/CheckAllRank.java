package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;

public class CheckAllRank implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        RanksManager ranksManager = new RanksManager(new File("C:/Users/ItsMystic01/Downloads/MyJava/Spigot/src/main/resources/ranks.yml"));

        List<Rank> allRanks = ranksManager.getRanks();

        for (Rank rank : allRanks) {
            sender.sendMessage(rank.getName());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rank.getPrefix()));
            sender.sendMessage(rank.getPermissions().toString());
            sender.sendMessage(String.valueOf(rank.getIsDefault()));
        }
        return true;
    }
}
