package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.Map;

public class MysticalHelp implements CommandExecutor {
    private final JavaPlugin PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    public MysticalHelp(JavaPlugin javaPlugin, ChatFunctions chatFunctions) {
        this.PLUGIN = javaPlugin;
        this.CHAT_FUNCTIONS = chatFunctions;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { return true; }

        if (!sender.hasPermission("mystical.help")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        StringBuilder textToSend = new StringBuilder();
        PluginDescriptionFile desc = PLUGIN.getDescription();
        Map<String, Map<String, Object>> cmd = desc.getCommands();
        if (args.length > 0) {
            String givenCommand = args[0];

            for (Map.Entry<String, Map<String, Object>> entry : cmd.entrySet()) {
                String cmdName = entry.getKey();
                Map<String, Object> cmdInfo = entry.getValue();
                String description = (String) cmdInfo.get("description");
                StringBuilder cmdInfoTextGradient = displayName("Command Info");
                if (!cmdName.equalsIgnoreCase(givenCommand)) { continue; }
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6================= " + cmdInfoTextGradient + " &6==================\n"));
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&fCommand Name: &6" + cmdName.substring(0, 1).toUpperCase() +
                        cmdName.substring(1) + "\n&fDescription: &6" + description + "\n"));
                textToSend.append(ChatColor.translateAlternateColorCodes('&', "&6================================================"));
                sender.sendMessage(textToSend.toString());
                return true;
            }

            CHAT_FUNCTIONS.commandSyntaxError((Player) sender,"&fCommand not found.");
        } else {
            StringBuilder cmdListTextGradient = displayName("Command List");
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

    private String gradientColor(String hexColor1, String hexColor2, int steps, int currentStep) {
        Color color1 = Color.decode(hexColor1);
        Color color2 = Color.decode(hexColor2);
        float[] rgb1 = color1.getRGBComponents(null);
        float[] rgb2 = color2.getRGBComponents(null);
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = rgb1[i] + (rgb2[i] - rgb1[i]) * currentStep / (float) steps;
        }
        Color color = new Color(result[0], result[1], result[2]);
        return String.format("#%06x", color.getRGB() & 0xFFFFFF);
    }

    private StringBuilder displayName(String name) {

     StringBuilder displayName = new StringBuilder();
        int nameLength = name.length();
        for (int i = 0; i < nameLength; i++) {
            String hexColor1 = "#fb4444";
            String hexColor2 = "#0c5fff";
            String hexColor = gradientColor(hexColor1, hexColor2, nameLength - 1, i);
            char c = name.charAt(i);
            displayName.append(net.md_5.bungee.api.ChatColor.of(hexColor)).append(c);
        }
        return displayName;
    }
}
