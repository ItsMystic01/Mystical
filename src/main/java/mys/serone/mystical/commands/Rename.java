package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.functions.PermissionENUM;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Rename implements CommandExecutor {
    private final ChatFunctions CHAT_FUNCTIONS;
    public Rename(ChatFunctions chatFunctions) {
        this.CHAT_FUNCTIONS = chatFunctions;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission(PermissionENUM.permissionENUM.RENAME.getPermission())) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta == null) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "You need to hold an item in your hand.");
            return true;
        }
        if (args.length < 1) {
            CHAT_FUNCTIONS.commandSyntaxError(player, "/rename [name]");
            return true;
        }

        String newName = String.join(" ", args);

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName));
        itemInHand.setItemMeta(itemMeta);

        CHAT_FUNCTIONS.informationChat(player,"Item renamed to: " + newName);
        return true;
    }
}
