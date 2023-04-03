package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
import java.util.Map;
import java.util.Objects;

public class EditKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private static ChatFunctions chatFunctions = null;

    public EditKit(Mystical plugin, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        chatFunctions = new ChatFunctions(ranksManager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("mystical.managekits")) {
            chatFunctions.commandPermissionError((Player) sender);
            return true;
        }
        if (args.length < 1) {
            chatFunctions.commandSyntaxError((Player) sender, "/editKit [name]");
            return true;
        }

        String kitName = args[0];
        Player player = (Player) sender;
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");
        if (!kitFile.exists()) {
            chatFunctions.commandSyntaxError(player, "Kit doesn't exist");
            return true;
        }
        YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);

        assert kitName != null;
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

        KitCloseListener kitCloseListener = new KitCloseListener(player, kitInventory, kitFile, kitName, kitName);
        PLUGIN.getServer().getPluginManager().registerEvents(kitCloseListener, PLUGIN);

        player.openInventory(kitInventory);

        return true;
    }

    private static class KitCloseListener implements Listener {
        private final Player PLAYER;
        private final Inventory KIT_INVENTORY;
        public File kitFile;
        public String kitName;
        public String kitNameCode;

        public KitCloseListener(Player player, Inventory kitInventory, File kitFile, String kitName, String kitNameCode) {
            this.PLAYER = player;
            this.KIT_INVENTORY = kitInventory;
            this.kitFile = kitFile;
            this.kitName = kitName;
            this.kitNameCode = kitNameCode;
        }

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
                chatFunctions.configurationError(PLAYER, "&aKit saved successfully.");
            }
        }
    }
}

