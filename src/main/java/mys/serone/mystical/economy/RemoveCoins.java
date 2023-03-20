package mys.serone.mystical.economy;

import mys.serone.mystical.Mystical;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mys.serone.mystical.functions.*;
import org.jetbrains.annotations.NotNull;

public class RemoveCoins implements CommandExecutor {
    private final Mystical PLUGIN;
    public ChatFunctions chatFunctions = new ChatFunctions();
    public RemoveCoins(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        Player player = (Player) sender;

        if(!(player.hasPermission("mystic.playerCoins.manageCoins"))) { chatFunctions.commandPermissionError(player); return false; }

        if (args.length < 2) {
            chatFunctions.commandSyntaxError(player, "/removeCoins [player] [amount]");
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

        if (target == null) {
            chatFunctions.commandSyntaxError(player, "Player not found.");
            return true;
        }

        String userUUID = player.getUniqueId().toString();

        EconomyFunctions economyFunctions = new EconomyFunctions();
        economyFunctions.updateCoins(PLUGIN, player, target, userUUID, amount, command);

        return true;
    }
}
