package mys.serone.mystical.economy;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.functions.PermissionENUM;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Balance implements CommandExecutor {
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;

    public Balance(ChatFunctions chatFunctions, PlayerInfoManager playerInfoManager) {
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }

        if (!sender.hasPermission(PermissionENUM.permissionENUM.BALANCE.getPermission())) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        Double playerCoins = PLAYER_INFO_MANAGER.getPlayerCoins(uuid);
        CHAT_FUNCTIONS.balanceEconomyChat(player, playerCoins);

        return true;
    }
}
