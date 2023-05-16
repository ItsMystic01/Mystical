package mys.serone.mystical.kitSystem;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
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
import java.util.*;

/**
 * Class responsible in the creation of kits
 */
public class KitManager {

    private final Mystical PLUGIN;
    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param personalKitManager : Personal Kit Manager used in accessing its functions
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see RanksManager
     * @see PlayerInfoManager
     * @see PersonalKitManager
     * @see MysticalMessage
     */
    public KitManager(Mystical plugin, RanksManager ranksManager, PlayerInfoManager playerInfoManager,
                      PersonalKitManager personalKitManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.LANG_CONFIG = langConfig;
    }

    /**
     * @param player : CommandExecutor
     * @param args : List of strings to supply the missing parameters for the function
     */
    public void getRankKit(Player player, String[] args) {

        List<String> list = PLAYER_INFO_MANAGER.getPlayerRankList(player.getUniqueId().toString());

        if (args.length < 1) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Usage: /kit [rank]"), LANG_CONFIG));
            return;
        }

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);

        if (rank == null) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage( Collections.singletonMap("message", "Kit does not exist."), LANG_CONFIG));
            return;
        }
        if (list.stream().noneMatch(s -> s.equalsIgnoreCase(rankKitToGet))) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "You do not have access to that rank kit."), LANG_CONFIG));
            return;
        }

        List<Map<String, Object>> kit = rank.getKit();
        if (kit == null || kit.size() == 0) {
            System.out.println("[Mystical] " + rank.getName() + " does not have a kit in ranks.yml. (Read the README.txt file in the Mystical Folder in the Plugins Folder)");
            player.sendMessage(MysticalMessage.RANK_HAS_NO_EXISTING_KIT_CONFIGURATION_ERROR.formatMessage(Collections.singletonMap("rank", rank.getName()), LANG_CONFIG));
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

    /**
     * @param material : Material of the item indicated in the personal_kit_configuration for the kit
     * @param kitName : Name of the kit to gather materials from
     * @param itemName : Name to be set for the item
     * @return ItemStack
     */
    private ItemStack createMysticalItem(Material material, String kitName, String itemName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String itemNewName = itemName.split("_")[1].substring(0, 1).toUpperCase() + itemName.split("_")[1].substring(1);
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', kitName + " " + itemNewName ));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * @param player : CommandExecutor
     * @param item : Material to give to the Command Executor
     * @param kitName : Name of the kit to gather materials from
     * @param enchantment : String list of enchantments to be given on the material
     * @param level : Integer list of level per enchantment to give on the material
     * @param itemName : Name to be set for the item
     */
    private void giveMysticalKit(Player player, Material item, String kitName, List<String> enchantment, List<Integer> level, String itemName) {
        Inventory inventory = player.getInventory();

        ItemStack itemToAdd = createMysticalItem(item, kitName, itemName);
        setMaxEnchantments(itemToAdd, enchantment, level);

        inventory.addItem(itemToAdd);
    }

    /**
     * @param item : Item from the kit, in personal_kit_configuration.yml requested by the CommandExecutor, to be enchanted
     * @param toEnchant : String list of enchantments to enchant the items
     * @param level : Integer list of level to implement on certain enchantments
     */
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

    /**
     * @param player : CommandExecutor
     * @param kitName : Name of the kit to get
     */
    public void claimKit(Player player, String kitName) {
        File kitFile = new File(PLUGIN.getDataFolder(), "kits" + File.separator + kitName + ".yml");

        if (!kitFile.exists()) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Kit does not exist."), LANG_CONFIG));
            return;
        }

        if(!player.hasPermission("mystical."+ kitName)) { player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG)); return; }

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
        player.sendMessage(MysticalMessage.CLAIM_KIT_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

}
