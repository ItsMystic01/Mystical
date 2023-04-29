package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Class responsible for permission revoking to user
 */
public class RemovePermission implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see PlayerInfoManager
     * @see MysticalMessage
     */
    public RemovePermission(Mystical plugin, PlayerInfoManager playerInfoManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.LANG_CONFIG = langConfig;
    }

    /**
     * @param sender : CommandExecutor
     * @param command : Command Used
     * @param label : Aliases
     * @param args : String List Arguments
     * @return boolean true or false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.REMOVE_PERMISSION.getPermission())) {
            playerSender.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG)); return true; }
        if (args.length < 2) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/removePermission <player> <permission>"), LANG_CONFIG));
            return true;
        }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Invalid User"), LANG_CONFIG));
            return true;
        }

        String permission = args[1];
        HashMap<String, PlayerInfo> allPlayerInfo = PLAYER_INFO_MANAGER.getAllPlayerInfo();
        PlayerInfo playerInfo = allPlayerInfo.get(player.getUniqueId().toString());
        List<String> playerInfoPermissionList = playerInfo.getUserAdditionalPermission();

        if (!(playerInfoPermissionList.contains(permission))) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", player.getDisplayName() + " does not have that permission."), LANG_CONFIG));
            return true;
        }

        playerInfoPermissionList.remove(permission);
        playerInfo.setUserAdditionalPermission(playerInfoPermissionList);
        PLAYER_INFO_MANAGER.savePlayerInfoToFile();

        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", permission + " has been removed from " + player.getDisplayName() + "."), LANG_CONFIG));
        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended for " + player.getDisplayName() + " to re-log for the permission to take effect."), LANG_CONFIG));
        player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended to re-log for the changes to take effect."), LANG_CONFIG));

        return true;
    }
}
