package mys.serone.mystical.economy;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveCoins implements CommandExecutor {
    private final Mystical PLUGIN;
    public GiveCoins(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);

        Player player = (Player) sender;

        if(!(player.hasPermission("mystical.managecoins"))) { chatFunctions.commandPermissionError(player); return true; }
        if (args.length < 2) {
            chatFunctions.commandSyntaxError(player, "/giveCoins [player] [amount]");
            return true;
        }

        String targetName = args[0];
        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            chatFunctions.commandSyntaxError(player, "Invalid amount.");
            return true;
        }

        Player target = PLUGIN.getServer().getPlayer(targetName);

        if (target == null) { chatFunctions.commandSyntaxError(player, "Player not found."); return true; }

        String userUUID = target.getUniqueId().toString();

        double userCoins = playerInfoManager.getPlayerCoins(userUUID);
        double newBalance = userCoins + amount;
        playerInfoManager.updatePlayerCoins(userUUID, newBalance);

        chatFunctions.adminEconomyChat(player, target, "Added", newBalance, amount);
        return true;
    }
}