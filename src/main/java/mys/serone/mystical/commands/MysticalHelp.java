package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.Gradient;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * Class for showcasing the features, commands, and format of the plugin
 */
public class MysticalHelp implements CommandExecutor {
    private final JavaPlugin PLUGIN;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see MysticalMessage
     */
    public MysticalHelp(Mystical plugin, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.LANG_CONFIG = langConfig;
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

        if (!player.hasPermission(MysticalPermission.MYSTICAL_HELP.getPermission())) { player.sendMessage(
                MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG)); return true; }

        StringBuilder textToSend = new StringBuilder();
        PluginDescriptionFile desc = PLUGIN.getDescription();
        Map<String, Map<String, Object>> cmd = desc.getCommands();

        if (args.length > 0) {
            String givenCommand = args[0];

            for (Map.Entry<String, Map<String, Object>> entry : cmd.entrySet()) {
                String cmdName = entry.getKey();
                Map<String, Object> cmdInfo = entry.getValue();
                String description = (String) cmdInfo.get("description");
                StringBuilder cmdInfoTextGradient = Gradient.displayName("Command Info", "#fb4444", "#0c5fff", false);

                if (!cmdName.equalsIgnoreCase(givenCommand)) { continue; }

                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6================= " + cmdInfoTextGradient + " &6==================\n"));
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&fCommand Name: &6" + cmdName.substring(0, 1).toUpperCase() +
                        cmdName.substring(1) + "\n&fDescription: &6" + description + "\n"));
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6================================================"));
                sender.sendMessage(textToSend.toString());

                return true;
            }

            player.sendMessage(
                    MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Command not found."), LANG_CONFIG));

        } else {
            StringBuilder cmdListTextGradient = Gradient.displayName("Command List", "#fb4444", "#0c5fff", false);

            textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6================== " + cmdListTextGradient + " &6==================\n"));

            for (Map.Entry<String, Map<String, Object>> entry : cmd.entrySet()) {
                String cmdName = entry.getKey();
                Map<String, Object> cmdInfo = entry.getValue();
                String description = (String) cmdInfo.get("description");
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&2" + cmdName.substring(0, 1).toUpperCase() + cmdName.substring(1) + "&f: &6" + description + "\n"));
            }

            textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6================================================"));
            sender.sendMessage(textToSend.toString());
        }

        return true;
    }
}
