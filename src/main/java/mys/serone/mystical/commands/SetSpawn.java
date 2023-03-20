package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.util.SpawnUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawn implements CommandExecutor {
    private final SpawnUtil SPAWN_UTIL;

    public SetSpawn(SpawnUtil spawnUtil) {
        this.SPAWN_UTIL = spawnUtil;
    }
    public ChatFunctions chatFunctions = new ChatFunctions();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystic.setSpawn")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        SPAWN_UTIL.set(player.getLocation());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSuccessfully set the world spawn."));
        return true;
    }
}
