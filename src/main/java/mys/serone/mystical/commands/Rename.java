package mys.serone.mystical.commands;

import mys.serone.mystical.functions.MysticalPermission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Rename implements CommandExecutor {
    private final FileConfiguration LANG_FILE;
    public Rename(FileConfiguration langFile) {
        this.LANG_FILE = langFile;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.RENAME.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), "You need to hold an item in your hand.")));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), "/rename <name>")));
            return true;
        }

        String newName = String.join(" ", args);

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName));
        itemInHand.setItemMeta(itemMeta);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Item renamed to: " + newName)));

        return true;
    }
}
