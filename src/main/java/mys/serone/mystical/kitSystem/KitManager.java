package mys.serone.mystical.kitSystem;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class KitManager {

    private final Mystical PLUGIN;
    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final FileConfiguration LANG_FILE;

    public KitManager(Mystical plugin, RanksManager ranksManager,
                      PlayerInfoManager playerInfoManager, PersonalKitManager personalKitManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.LANG_FILE = langFile;
    }

    public void kitOne(Player player, String[] args) {

        List<String> list = PLAYER_INFO_MANAGER.getPlayerRankList(player.getUniqueId().toString());
        String langMessage = LANG_FILE.getString("information");
        String langNoExistingRankKitConfigurationErrorMessage = LANG_FILE.getString("rank_has_no_existing_kit_configuration_error");

        if (args.length < 1) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Usage: /kit [rank]"))); return;}
        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        if (rank == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Kit does not exist."))); return; }
        if (list.stream().noneMatch(s -> s.equalsIgnoreCase(rankKitToGet))) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "You do not have access to that rank kit."))); return; }

        List<Map<String, Object>> kit = rank.getKit();
        if (kit == null || kit.size() == 0) {
            System.out.println("[Mystical] " + rank.getName() + " does not have a kit in ranks.yml. (Read the README.txt file in the Mystical Folder in the Plugins Folder)");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langNoExistingRankKitConfigurationErrorMessage))));
            return;
        }
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
                    System.out.println("[Mystical] " + itemName + " in " + rank.getName() + " rank in ranks.yml has invalid Enchant and/or Enchant Level");
                }
            }
            giveMysticalKit(player, materialName, kitName, listOfEnchantments, listOfLevels, itemName);
        }
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

    public void setMaxEnchantments(ItemStack item, List<String> toEnchant, List<Integer> level) {
        Enchantment[] enchantments = Enchantment.values();

        for (int i = 0; i < toEnchant.size(); i++) {
            for (Enchantment enchantment : enchantments) {
                if (enchantment.canEnchantItem(item) && !enchantment.isTreasure() && toEnchant.get(i).contains(enchantment.getKey().toString())) {
                    item.addUnsafeEnchantment(enchantment, level.get(i));
                }
            }
        }
    }

    public void claimKit(Player player, String kitName) {
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");
        String langKitClaimSuccessfulMessage = LANG_FILE.getString("claim_kit_successful");

        if (!kitFile.exists()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), "Kit does not exist.")));
            return;
        }

        if(!player.hasPermission("mystical."+ kitName)) { player.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', Objects.requireNonNull(langPermissionMessage))); return; }

        YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);
        ItemStack[] contents = new ItemStack[36];

        for (String path : Objects.requireNonNull(kitConfig.getConfigurationSection("items")).getKeys(false)) {
            int slot = Integer.parseInt(path);
            ItemStack item = kitConfig.getItemStack("items." + path + ".item");
            if (item != null) {
                if (kitConfig.contains("items." + path + ".enchantments")) {
                    for (String enchantmentPath : Objects.requireNonNull(kitConfig.getConfigurationSection("items." + path + ".enchantments")).getKeys(false)) {
                        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentPath));
                        int level = kitConfig.getInt("items." + path + ".enchantments." + enchantmentPath);
                        if (enchantment != null) {
                            item.addEnchantment(enchantment, level);
                        }
                    }
                }

                if (kitConfig.contains("items." + path + ".name")) {
                    String name = kitConfig.getString("items." + path + ".name");
                    if (name != null && !name.isEmpty()) {
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                        item.setItemMeta(meta);
                    }
                }
                contents[slot] = item;
            }
        }

        List<PersonalKit> allKit = PERSONAL_KIT_MANAGER.getKITS();
        String prefix = null;

        for (PersonalKit perPersonalKit : allKit) {
            if (perPersonalKit.getKitName().equalsIgnoreCase(kitName)) {
                prefix = perPersonalKit.getKitCodeName();
            }
        }

        for (ItemStack item : contents) {
            if (item != null) {
                ItemMeta dot = item.getItemMeta();
                assert dot != null;
                String itemName = String.valueOf(item.getType()).toLowerCase();
                if (itemName.contains("_")) {
                    String[] splitItemName = String.valueOf(item.getType()).split("_");
                    StringBuilder configuredItemName = new StringBuilder();
                    configuredItemName.append(prefix);
                    configuredItemName.append(" ");
                    for (String stringItemName : splitItemName) {
                        configuredItemName.append(stringItemName.substring(0, 1).toUpperCase()).append(stringItemName.substring(1).toLowerCase());
                        configuredItemName.append(" ");
                    }
                    configuredItemName.deleteCharAt(configuredItemName.length() - 1);
                    dot.setDisplayName(ChatColor.translateAlternateColorCodes('&', configuredItemName.toString()));
                } else {
                    String configuredItemName = prefix + " " + itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
                    dot.setDisplayName(ChatColor.translateAlternateColorCodes('&', configuredItemName));
                }
                item.setItemMeta(dot);
                player.getInventory().addItem(item);
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Objects.requireNonNull(langKitClaimSuccessfulMessage))));
    }

}
