package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fly implements CommandExecutor {
    private final ChatFunctions CHAT_FUNCTIONS;
    public Fly(ChatFunctions chatFunctions) {
        this.CHAT_FUNCTIONS = chatFunctions;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }
        if (!sender.hasPermission("mystical.fly")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        Player player = (Player) sender;

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            CHAT_FUNCTIONS.informationChat(player, "Flight off");
        } else {
            player.setAllowFlight(true);
            CHAT_FUNCTIONS.informationChat(player, "Flight on");
        }
        return true;
    }
}
