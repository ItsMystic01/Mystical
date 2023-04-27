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

public class GivePermission implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public GivePermission(Mystical plugin, PlayerInfoManager playerInfoManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.GIVE_PERMISSION.getPermission())) {
            playerSender.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG)); return true; }
        if (args.length < 2) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/givePermission <Player> <Permission>"), LANG_CONFIG));
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
        List<String> playerInfoUserAdditionalPermission = playerInfo.getUserAdditionalPermission();

        playerInfoUserAdditionalPermission.add(permission);
        playerInfo.setUserAdditionalPermission(playerInfoUserAdditionalPermission);
        PLAYER_INFO_MANAGER.savePlayerInfoToFile();

        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", permission + " has been given to " + player.getDisplayName() + "."), LANG_CONFIG));
        playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended for " +
                player.getDisplayName() + " to re-log for the permission to take effect."), LANG_CONFIG));
        player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "It is recommended to re-log for the changes to take effect."), LANG_CONFIG));

        return true;
    }
}
