package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fly implements CommandExecutor {

    public ChatFunctions chatFunctions = new ChatFunctions();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystic.fly")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage("Off");
        } else {
            player.setAllowFlight(true);
            player.sendMessage("On");
        }

        return true;
    }
}
