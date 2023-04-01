package mys.serone.mystical.economy;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Pay implements CommandExecutor {
    private final Mystical PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;

    public Pay(Mystical plugin, ChatFunctions chatFunctions, PlayerInfoManager playerInfoManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystical.pay")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        if (args.length != 2) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "/pay [player] [amount]");
            return true;
        }
        String recipientName = args[0];
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "Invalid amount.");
            return true;
        }
        if (amount <= 0) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "Amount must be greater than 0.");
            return true;
        }
        Player recipient = PLUGIN.getServer().getPlayer(recipientName);
        if (recipient == null) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "Player not found.");
            return true;
        }
        String senderUuid = player.getUniqueId().toString();
        String recipientUuid = recipient.getUniqueId().toString();
        if (senderUuid.equals(recipientUuid)) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "You cannot send coins to yourself.");
            return true;
        }

        Double senderCoins = PLAYER_INFO_MANAGER.getPlayerCoins(senderUuid);
        Double recipientCoins = PLAYER_INFO_MANAGER.getPlayerCoins(recipientUuid);

        if (senderCoins < amount) { CHAT_FUNCTIONS.commandSyntaxError(player, "Insufficient Balance."); return true;}

        Double senderNewBalance = senderCoins - amount;
        PLAYER_INFO_MANAGER.updatePlayerCoins(senderUuid, senderNewBalance);

        Double recipientNewBalance = recipientCoins + amount;
        PLAYER_INFO_MANAGER.updatePlayerCoins(recipientUuid, recipientNewBalance);
        player.sendMessage(CHAT_FUNCTIONS.payerEconomyChat(recipient, amount));
        recipient.sendMessage(CHAT_FUNCTIONS.recipientEconomyChat(player, amount));

        return true;
    }
}
