package mys.serone.mystical.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;

public class GiveItem implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        giveMysticalKit(player);
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

    private ItemStack createMysticalItem(Material material, String itemName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String name = "[Mystical " + itemName.replace("Netherite ", "") + "]";
        StringBuilder displayName = new StringBuilder();
        int nameLength = name.length();
        for (int i = 0; i < nameLength; i++) {
            String hexColor1 = "#fb4444"; // Start color
            String hexColor2 = "#0c5fff"; // End color
            String hexColor = gradientColor(hexColor1, hexColor2, nameLength - 1, i);
            char c = name.charAt(i);
            displayName.append(net.md_5.bungee.api.ChatColor.of(hexColor)).append(c);
        }
        assert meta != null;
        meta.setDisplayName(displayName.toString());
        item.setItemMeta(meta);
        return item;
    }

    private void giveMysticalKit(Player player) {
        Inventory inventory = player.getInventory();

        ItemStack helmet = createMysticalItem(Material.NETHERITE_HELMET, "Helmet");
        setMaxEnchantments(helmet);

        ItemStack chestplate = createMysticalItem(Material.NETHERITE_CHESTPLATE, "Chestplate");
        setMaxEnchantments(chestplate);

        ItemStack leggings = createMysticalItem(Material.NETHERITE_LEGGINGS, "Leggings");
        setMaxEnchantments(leggings);

        ItemStack boots = createMysticalItem(Material.NETHERITE_BOOTS, "Boots");
        setMaxEnchantments(boots);

        ItemStack sword = createMysticalItem(Material.NETHERITE_SWORD, "Sword");
        setMaxEnchantments(sword);

        ItemStack pickaxe = createMysticalItem(Material.NETHERITE_PICKAXE, "Pickaxe");
        setMaxEnchantments(pickaxe);

        ItemStack axe = createMysticalItem(Material.NETHERITE_AXE, "Axe");
        setMaxEnchantments(axe);

        ItemStack shovel = createMysticalItem(Material.NETHERITE_SHOVEL, "Shovel");
        setMaxEnchantments(shovel);

        inventory.addItem(helmet);
        inventory.addItem(chestplate);
        inventory.addItem(leggings);
        inventory.addItem(boots);
        inventory.addItem(sword);
        inventory.addItem(pickaxe);
        inventory.addItem(axe);
        inventory.addItem(shovel);
    }

    public static void setMaxEnchantments(ItemStack item) {
        Enchantment[] enchantments = Enchantment.values();

        String[] excludedEnchantments = { "minecraft:fire_protection",
                "minecraft:blast_protection",
                "minecraft:smite",
                "minecraft:bane_of_arthropods",
                "minecraft:silk_touch",
                "minecraft:projectile_protection" };

        for (Enchantment enchantment : enchantments) {
            if (enchantment.canEnchantItem(item) && !enchantment.isTreasure() && !Arrays.asList(excludedEnchantments).contains(enchantment.getKey().toString())) {
                boolean conflict = false;
                for (Enchantment existingEnchantment : item.getEnchantments().keySet()) {
                    if (enchantment.conflictsWith(existingEnchantment)) {
                        conflict = true;
                        break;
                    }
                }
                if (!conflict) {
                    int maxLevel = enchantment.getMaxLevel();
                    item.addEnchantment(enchantment, maxLevel);
                }
            }
        }
    }




}
