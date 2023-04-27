package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class CreateKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public CreateKit(Mystical plugin, PersonalKitManager personalKitManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.CREATE_KIT.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/createKit <name> <colored name>"), LANG_CONFIG));
            return true;
        }

        String kitName = args[0];
        String kitNameCode = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Inventory kitInventory = Bukkit.createInventory(null, 36, kitName);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits"  + File.separator + kitName + ".yml");

        if (kitFile.exists()) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Kit already exists"), LANG_CONFIG));
            return true;
        }

        KitCloseListener kitCloseListener = new KitCloseListener(player, kitInventory, kitFile, kitName, kitNameCode, PERSONAL_KIT_MANAGER, LANG_CONFIG);

        PLUGIN.getServer().getPluginManager().registerEvents(kitCloseListener, PLUGIN);
        player.openInventory(kitInventory);

        return true;
    }

    private static class KitCloseListener implements org.bukkit.event.Listener {
        private final Player PLAYER;
        private final Inventory KIT_INVENTORY;
        private final PersonalKitManager PERSONAL_KIT_MANAGER;
        public File kitFile;
        public String kitName;
        public String kitNameCode;
        private final FileConfiguration LANG_CONFIG;

        public KitCloseListener(Player player, Inventory kitInventory, File kitFile, String kitName, String kitNameCode, PersonalKitManager personalKitManager, FileConfiguration langConfig) {
            this.PLAYER = player;
            this.KIT_INVENTORY = kitInventory;
            this.kitFile = kitFile;
            this.kitName = kitName;
            this.kitNameCode = kitNameCode;
            this.PERSONAL_KIT_MANAGER = personalKitManager;
            this.LANG_CONFIG = langConfig;
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {

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

                try {
                    PERSONAL_KIT_MANAGER.createKit(PLAYER, kitName, kitNameCode);
                    kitConfig.save(kitFile);
                } catch (IOException e) {
                    PLAYER.sendMessage(MysticalMessage.CREATE_KIT_CONFIGURATION_ERROR.formatMessage(LANG_CONFIG));
                    e.printStackTrace();
                }
            }
        }
    }

}
