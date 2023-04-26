package mys.serone.mystical.commands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.permissionENUM.FLY.getPermission())) { player.sendMessage(
                MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage()); return true; }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage( "Flight off!"));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage( "Flight on!"));
        }

        return true;
    }
}
