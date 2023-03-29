package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Rename implements CommandExecutor {
    private final Mystical PLUGIN;

    public Rename(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystical.rename")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta == null) {
            chatFunctions.commandSyntaxError(player, "You need to hold an item in your hand.");
            return true;
        }
        if (args.length < 1) {
            chatFunctions.commandSyntaxError(player, "/rename [name]");
            return true;
        }

        String newName = String.join(" ", args);

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName));
        itemInHand.setItemMeta(itemMeta);

        chatFunctions.informationChat(player,"Item renamed to: " + newName);
        return true;
    }
}
