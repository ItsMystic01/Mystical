package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CheckPlayerRank implements CommandExecutor {
    private final Mystical PLUGIN;
    public CheckPlayerRank(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);

        if (args.length < 1) { chatFunctions.commandSyntaxError( (Player) sender, "/checkPlayerRank [username]"); return true; }
        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { chatFunctions.rankChat((Player) sender, "Player not found."); return true; }
        String uuid = player.getUniqueId().toString();

        List<String> playerRankList = playerInfoManager.getPlayerRankList(uuid);

        chatFunctions.playerRankChat((Player) sender, playerRankList);

        return true;
    }
}
