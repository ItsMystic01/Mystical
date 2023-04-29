package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Class for Editing Kits that are available in personal_kit_configuration.yml
 */
public class EditKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see MysticalMessage
     */
    public EditKit(Mystical plugin, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.LANG_CONFIG = langConfig;
    }

    /**
     * @param sender : CommandExecutor
     * @param command : Command Used
     * @param label : Aliases
     * @param args : String List Arguments
     * @return boolean true or false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.EDIT_KIT.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/editKit <name>"), LANG_CONFIG));
            return true;
        }

        String kitName = args[0];
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits" + File.separator + kitName + ".yml");

        if (!kitFile.exists()) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Kit doesn't exist"), LANG_CONFIG));
            return true;
        }

        YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);

        Inventory kitInventory = Bukkit.createInventory(null, 36, kitName);
        ItemStack[] kitContents = new ItemStack[36];

        for (int i = 0; i < 36; i++) {
            String path = "items." + i + ".";
            ItemStack item = kitConfig.getItemStack(path + "item");

            if (item != null) {
                kitContents[i] = item;
                ConfigurationSection enchantments = kitConfig.getConfigurationSection(path + "enchantments");
                if (enchantments != null) {

                    for (String enchantmentKey : enchantments.getKeys(false)) {
                        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey));

                        if (enchantment != null) {
                            int level = enchantments.getInt(enchantmentKey);
                            kitContents[i].addEnchantment(enchantment, level);
                        }

                    }

                }

                String name = kitConfig.getString(path + "name");
                if (name != null && !name.isEmpty()) {
                    ItemMeta itemMeta = kitContents[i].getItemMeta();
                    assert itemMeta != null;

                    itemMeta.setDisplayName(name);
                    kitContents[i].setItemMeta(itemMeta);
                }
            }
        }

        kitInventory.setContents(kitContents);

        KitCloseListener kitCloseListener = new KitCloseListener(player, kitInventory, kitFile, kitName, kitName, LANG_CONFIG);
        PLUGIN.getServer().getPluginManager().registerEvents(kitCloseListener, PLUGIN);
        player.openInventory(kitInventory);

        return true;
    }

    /**
     * Class for checks in inventory closing of the user indicating its end on edit of a kit
     */
    private static class KitCloseListener implements Listener {
        private final Player PLAYER;
        private final Inventory KIT_INVENTORY;
        public File kitFile;
        public String kitName;
        public String kitNameCode;
        private final FileConfiguration LANG_CONFIG;

        /**
         * @param player : Player provided by the onCommand Event
         * @param kitInventory : Inventory provided by the onCommand Event
         * @param kitFile : Kit File provided by the onCommand Event
         * @param kitName : Kit Name provided by the onCommand Event
         * @param kitNameCode : Kit Name Code provided by the onCommand Event
         * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
         * @see MysticalMessage
         */
        public KitCloseListener(Player player, Inventory kitInventory, File kitFile, String kitName, String kitNameCode, FileConfiguration langConfig) {
            this.PLAYER = player;
            this.KIT_INVENTORY = kitInventory;
            this.kitFile = kitFile;
            this.kitName = kitName;
            this.kitNameCode = kitNameCode;
            this.LANG_CONFIG = langConfig;
        }

        /**
         * @param event : Event responsible for inventory closing calls
         * @throws IOException : Error handler for Inventory Close Event calls
         */
        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) throws IOException {

            if (event.getInventory().equals(KIT_INVENTORY)) {
                YamlConfiguration kitConfig = new YamlConfiguration();
                ItemStack[] kitContents = KIT_INVENTORY.getContents();

                for (int i = 0; i < kitContents.length; i++) {
                    if (kitContents[i] != null) {
                        String path = "items." + i + ".";

                        kitConfig.set(path + "item", kitContents[i]);

                        Map<Enchantment, Integer> enchantments = kitContents[i].getEnchantments();

                        if (!enchantments.isEmpty()) {
                            for (Enchantment enchantment : enchantments.keySet()) {
                                kitConfig.set(path + "enchantments." + enchantment.getKey(), enchantments.get(enchantment));
                            }
                        }
                        if (kitContents[i].hasItemMeta() && Objects.requireNonNull(kitContents[i].getItemMeta()).hasDisplayName()) {
                            kitConfig.set(path + "name", Objects.requireNonNull(kitContents[i].getItemMeta()).getDisplayName());
                        }
                    }
                }

                kitConfig.save(kitFile);
                PLAYER.sendMessage(MysticalMessage.EDIT_KIT_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
            }
        }
    }

}
