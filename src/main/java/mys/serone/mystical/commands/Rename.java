package mys.serone.mystical.commands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Rename implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.permissionENUM.RENAME.getPermission())) { player.sendMessage(
                MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage()); return true; }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("You need to hold an item in your hand."));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("/rename <name>"));
            return true;
        }

        String newName = String.join(" ", args);

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName));
        itemInHand.setItemMeta(itemMeta);
        player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("Item renamed to: " + newName));

        return true;
    }
}
