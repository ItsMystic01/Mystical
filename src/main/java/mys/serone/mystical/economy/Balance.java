package mys.serone.mystical.economy;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Balance implements CommandExecutor {
    private final Mystical PLUGIN;

    public Balance(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);

        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }

        if (!sender.hasPermission("mystical.balance")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        Double playerCoins = playerInfoManager.getPlayerCoins(uuid);
        chatFunctions.balanceEconomyChat(player, playerCoins);

        return true;
    }
}
