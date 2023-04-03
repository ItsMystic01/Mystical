package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;

public class RemovePermission implements CommandExecutor {
    private final Mystical PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    public RemovePermission(Mystical plugin, ChatFunctions chatFunctions, PlayerInfoManager playerInfoManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("mystical.manageranks")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 2) { CHAT_FUNCTIONS.commandSyntaxError((Player) sender, "/removePermission [Player] [Permission]"); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { CHAT_FUNCTIONS.rankChat((Player) sender, "Invalid User"); return true; }

        String permission = args[1];
        HashMap<String, PlayerInfo> allPlayerInfo = PLAYER_INFO_MANAGER.getAllPlayerInfo();
        PlayerInfo playerInfo = allPlayerInfo.get(player.getUniqueId().toString());
        List<String> playerInfoPermissionList = playerInfo.getUserAdditionalPermission();

        if (!(playerInfoPermissionList.contains(permission))) {
            CHAT_FUNCTIONS.commandSyntaxError((Player) sender, player.getDisplayName() + " does not have that permission.");
            return true;
        }

        playerInfoPermissionList.remove(permission);
        playerInfo.setUserAdditionalPermission(playerInfoPermissionList);
        PLAYER_INFO_MANAGER.savePlayerInfoToFile();

        CHAT_FUNCTIONS.rankChat((Player) sender, permission + " has been removed from " + player.getDisplayName() + ".");
        CHAT_FUNCTIONS.rankChat((Player) sender, "It is recommended for " + player.getDisplayName() + " to re-log for the permission to take effect.");
        CHAT_FUNCTIONS.rankChat(player, "It is recommended to re-log for the changes to take effect.");

        return true;
    }
}
