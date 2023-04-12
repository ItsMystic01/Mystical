package mys.serone.mystical.commands;

import mys.serone.mystical.functions.MysticalPermission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Fly implements CommandExecutor {
    private final FileConfiguration LANG_FILE;
    public Fly(FileConfiguration langFile) {
        this.LANG_FILE = langFile;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.FLY.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Objects.requireNonNull(langMessage), "Flight off")));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Objects.requireNonNull(langMessage), "Flight on")));
        }

        return true;
    }
}
