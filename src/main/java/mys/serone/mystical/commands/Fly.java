package mys.serone.mystical.commands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Class for enabling or disabling flight
 */
public class Fly implements CommandExecutor {

    private final FileConfiguration LANG_CONFIG;

    /**
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages.
     * @see MysticalMessage
     */
    public Fly(FileConfiguration langConfig) {
        LANG_CONFIG = langConfig;
    }

    /**
     * @param sender : CommandExecutor
     * @param command : Command Used
     * @param label : Aliases
     * @param args : String List Arguments
     * @return boolean true or false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.FLY.getPermission())) { player.sendMessage(
                MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG)); return true; }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Flight disabled!"), LANG_CONFIG));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Flight enabled!"), LANG_CONFIG));
        }

        return true;
    }
}
