package mys.serone.mystical.commands;

import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class Kit implements CommandExecutor {

    public ChatFunctions chatFunctions = new ChatFunctions();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        Map<Enchantment, Integer> weaponEnchantmentList = new HashMap<>();

        weaponEnchantmentList.put(Enchantment.DAMAGE_ALL, 5);
        weaponEnchantmentList.put(Enchantment.DURABILITY, 5);
        weaponEnchantmentList.put(Enchantment.LOOT_BONUS_MOBS, 4);
        weaponEnchantmentList.put(Enchantment.FIRE_ASPECT, 3);
        weaponEnchantmentList.put(Enchantment.MENDING, 1);

        ItemStack weaponPiece = new ItemStack(Material.NETHERITE_SWORD, 1);
        weaponPiece.addUnsafeEnchantments(weaponEnchantmentList);
        ItemMeta weaponPieceMeta = weaponPiece.getItemMeta();

        assert weaponPieceMeta != null;

        StringBuilder weaponNewName = new StringBuilder();

        String[] weaponSplitName = Material.NETHERITE_SWORD.name().toLowerCase().split("_");
        for (String itemName : weaponSplitName) {
            weaponNewName.append(itemName.substring(0, 1).toUpperCase()).append(itemName.substring(1)).append(" ");
        }

        weaponPieceMeta.setDisplayName(ChatColor.BLUE + weaponNewName.toString());
        weaponPiece.setItemMeta(weaponPieceMeta);
        player.getInventory().addItem(weaponPiece);

        Object[] toolList = {Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_AXE};
        Object[] armorList = {Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS};

        for (Object toolItem : toolList) {
            Map<Enchantment, Integer> toolEnchantmentList = new HashMap<>();

            toolEnchantmentList.put(Enchantment.DIG_SPEED, 5);
            toolEnchantmentList.put(Enchantment.DURABILITY, 5);
            toolEnchantmentList.put(Enchantment.LOOT_BONUS_BLOCKS, 4);
            toolEnchantmentList.put(Enchantment.MENDING, 1);

            ItemStack toolPiece = new ItemStack((Material) toolItem, 1);
            toolPiece.addUnsafeEnchantments(toolEnchantmentList);
            ItemMeta toolPieceMeta = toolPiece.getItemMeta();

            assert toolPieceMeta != null;

            StringBuilder itemNewName = new StringBuilder();

            String[] splitItemName = ((Material) toolItem).name().toLowerCase().split("_");
            for (String itemName : splitItemName) {
                itemNewName.append(itemName.substring(0, 1).toUpperCase()).append(itemName.substring(1)).append(" ");
            }

            toolPieceMeta.setDisplayName(ChatColor.BLUE + itemNewName.toString());
            toolPiece.setItemMeta(toolPieceMeta);
            player.getInventory().addItem(toolPiece);
        }

        for (Object armorItem : armorList) {
            Map<Enchantment, Integer> armorEnchantmentList = new HashMap<>();

            armorEnchantmentList.put(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            armorEnchantmentList.put(Enchantment.DURABILITY, 5);
            armorEnchantmentList.put(Enchantment.THORNS, 3);
            armorEnchantmentList.put(Enchantment.MENDING, 1);

            ItemStack armorPiece = new ItemStack((Material) armorItem, 1);
            armorPiece.addUnsafeEnchantments(armorEnchantmentList);
            ItemMeta armorPieceMeta = armorPiece.getItemMeta();

            assert armorPieceMeta != null;

            StringBuilder itemNewName = new StringBuilder();

            String[] splitItemName = ((Material) armorItem).name().toLowerCase().split("_");
            for (String itemName : splitItemName) {
                itemNewName.append(itemName.substring(0, 1).toUpperCase()).append(itemName.substring(1)).append(" ");
            }

            armorPieceMeta.setDisplayName(ChatColor.BLUE + itemNewName.toString());
            armorPiece.setItemMeta(armorPieceMeta);
            player.getInventory().addItem(armorPiece);

        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[&aOP &fKit&c]&f claimed."));
        return true;
    }
}
