package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class GivePermission implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;

    public GivePermission(Mystical plugin, PlayerInfoManager playerInfoManager) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.permissionENUM.GIVE_PERMISSION.getPermission())) {
            playerSender.sendMessage(MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage()); return true; }
        if (args.length < 2) {
            playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("/givePermission <Player> <Permission>"));
            return true;
        }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) {
            playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("Invalid User"));
            return true;
        }

        String permission = args[1];
        HashMap<String, PlayerInfo> allPlayerInfo = PLAYER_INFO_MANAGER.getAllPlayerInfo();
        PlayerInfo playerInfo = allPlayerInfo.get(player.getUniqueId().toString());
        List<String> playerInfoUserAdditionalPermission = playerInfo.getUserAdditionalPermission();

        playerInfoUserAdditionalPermission.add(permission);
        playerInfo.setUserAdditionalPermission(playerInfoUserAdditionalPermission);
        PLAYER_INFO_MANAGER.savePlayerInfoToFile();

        playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage(permission + " has been given to " + player.getDisplayName() + "."));
        playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("It is recommended for " +
                player.getDisplayName() + " to re-log for the permission to take effect."));
        player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("It is recommended to re-log for the changes to take effect."));

        return true;
    }
}
