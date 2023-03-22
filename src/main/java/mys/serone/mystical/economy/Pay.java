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
    public Pay(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 2) {
            chatFunctions.commandSyntaxError(player, "/pay [player] [amount]");
            return true;
        }
        String recipientName = args[0];
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            chatFunctions.commandSyntaxError(player, "Invalid amount.");
            return true;
        }
        if (amount <= 0) {
            chatFunctions.commandSyntaxError(player, "Amount must be greater than 0.");
            return true;
        }
        Player recipient = PLUGIN.getServer().getPlayer(recipientName);
        if (recipient == null) {
            chatFunctions.commandSyntaxError(player, "Player not found.");
            return true;
        }
        String senderUuid = player.getUniqueId().toString();
        String recipientUuid = recipient.getUniqueId().toString();
        if (senderUuid.equals(recipientUuid)) {
            chatFunctions.commandSyntaxError(player, "You cannot send coins to yourself.");
            return true;
        }

        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        Double senderCoins = playerInfoManager.getPlayerCoins(senderUuid);
        Double recipientCoins = playerInfoManager.getPlayerCoins(recipientUuid);

        if (senderCoins < amount) { chatFunctions.commandSyntaxError(player, "Insufficient Balance."); return true;}


        Double senderNewBalance = senderCoins - amount;
        playerInfoManager.updatePlayerCoins(senderUuid, senderNewBalance);

        Double recipientNewBalance = recipientCoins + amount;
        playerInfoManager.updatePlayerCoins(recipientUuid, recipientNewBalance);
        player.sendMessage(chatFunctions.payerEconomyChat(recipient, amount));
        recipient.sendMessage(chatFunctions.recipientEconomyChat(player, amount));

        return true;
    }
}
