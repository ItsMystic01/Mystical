package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
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
import java.util.*;
import java.util.List;

public class GiveKit implements CommandExecutor {
    private final Mystical PLUGIN;

    public GiveKit(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        RanksManager ranksManager = new RanksManager(PLUGIN);

        Player player = (Player) sender;

        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);

        List<String> list = playerInfoManager.getPlayerRankList(player.getUniqueId().toString());

        if (args.length < 1) { player.sendMessage("Usage: /giveKit [rank]"); return true;}
        String rankKitToGet = args[0];
        Rank rank = ranksManager.getRank(rankKitToGet);
        if (rank == null) { player.sendMessage("Rank does not exist."); return true; }
        if (!list.contains(rankKitToGet)) { player.sendMessage("You do not have access to that rank kit."); return true; }


        List<Map<String, Object>> kit = rank.getKit();
        if (kit == null) { System.out.println(rank.getName() + " does not have a kit in ranks.yml"); return true; }
        String kitName = rank.getKitName();

        String itemName;
        Material materialName = null;
        String enchantmentNameLowerCase;
        int enchantmentLevel;
        for (Map<String, Object> firstMap : kit) {
            itemName = firstMap.keySet().toString().replace("[", "").replace("]", "");
            String[] itemAttributes = firstMap.get(itemName).toString().replace("[", "").replace("]", "").split(", ");
            List<String> listOfEnchantments = new ArrayList<>();
            List<Integer> listOfLevels = new ArrayList<>();
            for (String enchantAndLevel : itemAttributes) {

                try {
                    String[] separatedEnchantAndLevel = enchantAndLevel.split(":");

                    enchantmentNameLowerCase = "minecraft:" + separatedEnchantAndLevel[0].toLowerCase();
                    listOfEnchantments.add(enchantmentNameLowerCase);
                    enchantmentLevel = Integer.parseInt(separatedEnchantAndLevel[1]);
                    listOfLevels.add(enchantmentLevel);
                    materialName = Material.getMaterial(itemName.toUpperCase());
                } catch (Exception e) {
                    System.out.println(itemName + " in " + rank.getName() + " rank in ranks.yml has invalid Enchant and/or Enchant Level");
                }
            }
            giveMysticalKit(player, materialName, kitName, listOfEnchantments, listOfLevels, itemName);
        }
        
        return true;
    }

    private ItemStack createMysticalItem(Material material, String kitName, String itemName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String itemNewName = itemName.split("_")[1].substring(0, 1).toUpperCase() + itemName.split("_")[1].substring(1);
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', kitName + " " + itemNewName ));
        item.setItemMeta(meta);
        return item;
    }

    private void giveMysticalKit(Player player, Material item, String kitName, List<String> enchantment, List<Integer> level, String itemName) {
        Inventory inventory = player.getInventory();

        ItemStack itemToAdd = createMysticalItem(item, kitName, itemName);
        setMaxEnchantments(itemToAdd, enchantment, level);

        inventory.addItem(itemToAdd);
    }

    public static void setMaxEnchantments(ItemStack item, List<String> toEnchant, List<Integer> level) {
        Enchantment[] enchantments = Enchantment.values();

        for (int i = 0; i < toEnchant.size(); i++) {
            for (Enchantment enchantment : enchantments) {
                if (enchantment.canEnchantItem(item) && !enchantment.isTreasure() && toEnchant.get(i).contains(enchantment.getKey().toString())) {
                    item.addUnsafeEnchantment(enchantment, level.get(i));
                }
            }
        }
    }


}
