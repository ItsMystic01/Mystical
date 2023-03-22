package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Help implements CommandExecutor {
    private final JavaPlugin PLUGIN;
    private final Mystical Plugin;
    public Help(JavaPlugin javaPlugin, Mystical plugin) {
        this.PLUGIN = javaPlugin;
        this.Plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(Plugin);
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length > 0) {
            String givenCommand = args[0];
            StringBuilder textToSend = new StringBuilder();
            PluginDescriptionFile desc = PLUGIN.getDescription();
            Map<String, Map<String, Object>> cmd = desc.getCommands();

            for (Map.Entry<String, Map<String, Object>> entry : cmd.entrySet()) {
                String cmdName = entry.getKey();
                Map<String, Object> cmdInfo = entry.getValue();
                String description = (String) cmdInfo.get("description");

                if (!cmdName.equalsIgnoreCase(givenCommand)) { continue; }

                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6=============== &f&lCommand Info &6===============\n"));
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&fCommand Name: &6" + cmdName.substring(0, 1).toUpperCase() +
                        cmdName.substring(1) + "\n&fDescription: &6" + description + "\n"));
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6============================================="));
                sender.sendMessage(textToSend.toString());
                return true;
            }

            chatFunctions.commandSyntaxError((Player) sender,"&fCommand not found.");
        } else {
            StringBuilder textToSend = new StringBuilder();
            PluginDescriptionFile desc = PLUGIN.getDescription();
            Map<String, Map<String, Object>> cmd = desc.getCommands();
            textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6============ &f&lCommand List &6==================\n"));
            for (Map.Entry<String, Map<String, Object>> entry : cmd.entrySet()) {
                String cmdName = entry.getKey();
                Map<String, Object> cmdInfo = entry.getValue();
                String description = (String) cmdInfo.get("description");
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&2" + cmdName.substring(0, 1).toUpperCase() + cmdName.substring(1) + "&f: &6" + description + "\n"));
            }
            textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6============================================"));
            sender.sendMessage(textToSend.toString());
        }
        return true;
    }
}
