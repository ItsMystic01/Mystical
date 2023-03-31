package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

public class RemovePermission implements CommandExecutor {
    private final Mystical PLUGIN;
    public RemovePermission(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);

        if (!sender.hasPermission("mystical.manageranks")) { chatFunctions.commandPermissionError((Player) sender); return true; }
        if (args.length < 2) { chatFunctions.commandSyntaxError((Player) sender, "/removePermission [Player] [Permission]"); return true; }

        Player player = PLUGIN.getServer().getPlayer(args[0]);
        if (player == null) { chatFunctions.rankChat((Player) sender, "Invalid User"); return true; }

        String permission = args[1];

        List<String> permissionToAdd = playerInfoManager.getPlayerAdditionalPermission(player.getUniqueId().toString());
        if (!(permissionToAdd.contains(permission))) { chatFunctions.commandSyntaxError((Player) sender, player.getDisplayName() + " does not have that permission."); return true; }
        permissionToAdd.remove(permission);

        List<PlayerInfo> allPlayerInfo = playerInfoManager.getAllPlayerInfo();

        for (PlayerInfo perInfo : allPlayerInfo) {
            if (Objects.equals(perInfo.getPlayerUUID(), player.getUniqueId().toString())) {
                perInfo.setUserAdditionalPermission(permissionToAdd);
                playerInfoManager.savePlayerInfoToFile();
            }
        }

        chatFunctions.rankChat((Player) sender, permission + " has been removed from " + player.getDisplayName() + ".");
        chatFunctions.rankChat((Player) sender, "It is recommended for " + player.getDisplayName() + " to re-log for the permission to take effect.");
        chatFunctions.rankChat(player, "It is recommended to re-log for the changes to take effect.");

        return true;
    }
}
