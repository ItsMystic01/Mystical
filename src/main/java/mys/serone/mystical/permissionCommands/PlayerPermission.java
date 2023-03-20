package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerPermission implements CommandExecutor {

    public ChatFunctions chatFunctions = new ChatFunctions();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        Player player = Bukkit.getPlayer(sender.getName());

        if (player == null) {
            chatFunctions.commandSyntaxError((Player) sender, "Player not found.");
            return true;
        }

        Set<PermissionAttachmentInfo> perms = player.getEffectivePermissions();

        List<PermissionAttachmentInfo> permList = new ArrayList<>(perms);

        for (PermissionAttachmentInfo perm : permList) {
            String permName = perm.getPermission();
            boolean hasPerm = perm.getValue();
            player.sendMessage(player.getName() + " has permission " + permName + ": " + hasPerm);
        }

        return true;
    }
}
