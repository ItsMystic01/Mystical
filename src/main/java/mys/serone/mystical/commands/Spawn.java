package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.util.SpawnUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {
    private final SpawnUtil SPAWN_UTIL;

    public Spawn(SpawnUtil spawnUtil) {
        this.SPAWN_UTIL = spawnUtil;
    }

    public ChatFunctions chatFunctions = new ChatFunctions();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystic.spawn")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        SPAWN_UTIL.teleport(player);

        return true;
    }
}
