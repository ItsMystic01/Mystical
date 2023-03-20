package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Creative implements CommandExecutor {
    public ChatFunctions chatFunctions = new ChatFunctions();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystic.creative")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;

        if (player.getGameMode().name().equals("SURVIVAL")) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage("On");
        } else if (player.getGameMode().name().equals("CREATIVE")) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("Off");
        }

        return true;
    }
}
