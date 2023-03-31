package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class CreateKit implements CommandExecutor {

    private final Mystical plugin;
    private static ChatFunctions chatFunctions = null;
    public CreateKit(Mystical plugin) {
        this.plugin = plugin;
        chatFunctions = new ChatFunctions(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }
        if (!sender.hasPermission("mystical.managekits")) { chatFunctions.commandPermissionError((Player) sender); return true; }
        if (args.length < 2) {
            chatFunctions.commandSyntaxError((Player) sender, "/createKit [name] [colored name]");
            return true;
        }

        String kitName = args[0];
        String kitNameCode = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Player player = (Player) sender;
        Inventory kitInventory = Bukkit.createInventory(null, 36, kitName);
        File kitFile = new File(plugin.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");
        if (kitFile.exists()) { chatFunctions.commandSyntaxError(player, "Kit already exists"); return true; }
        KitCloseListener kitCloseListener = new KitCloseListener(plugin, (Player) sender, kitInventory, kitFile, kitName, kitNameCode);
        plugin.getServer().getPluginManager().registerEvents(kitCloseListener, plugin);

        player.openInventory(kitInventory);

        return true;
    }

    private static class KitCloseListener implements org.bukkit.event.Listener {
        private final Mystical plugin;
        private final Player player;
        private final Inventory kitInventory;
        public File kitFile;
        public String kitName;
        public String kitNameCode;

        public KitCloseListener(Mystical plugin, Player player, Inventory kitInventory, File kitFile, String kitName, String kitNameCode) {
            this.plugin = plugin;
            this.player = player;
            this.kitInventory = kitInventory;
            this.kitFile = kitFile;
            this.kitName = kitName;
            this.kitNameCode = kitNameCode;
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getInventory().equals(kitInventory)) {
                YamlConfiguration kitConfig = new YamlConfiguration();

                ItemStack[] kitContents = kitInventory.getContents();

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

                try {
                    PersonalKitManager personalKitManager = new PersonalKitManager(plugin);
                    personalKitManager.createKit(player, kitName, kitNameCode);
                    kitConfig.save(kitFile);
                } catch (IOException e) {
                    chatFunctions.configurationError(player,"An error occurred while saving the kit.");
                    e.printStackTrace();
                }
            }
        }
    }


}
