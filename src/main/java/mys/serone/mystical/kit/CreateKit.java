package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.Map;
import java.util.Objects;

public class CreateKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private static FileConfiguration LANG_FILE;

    public CreateKit(Mystical plugin, PersonalKitManager personalKitManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        LANG_FILE = langFile;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.CREATE_KIT.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), "/createKit <name> <colored name>")));
            return true;
        }

        String kitName = args[0];
        String kitNameCode = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Inventory kitInventory = Bukkit.createInventory(null, 36, kitName);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");

        if (kitFile.exists()) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Kit already exists"))); return true; }

        KitCloseListener kitCloseListener = new KitCloseListener(player, kitInventory, kitFile, kitName, kitNameCode, PERSONAL_KIT_MANAGER);

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

        public KitCloseListener(Player player, Inventory kitInventory, File kitFile, String kitName, String kitNameCode, PersonalKitManager personalKitManager) {
            this.PLAYER = player;
            this.KIT_INVENTORY = kitInventory;
            this.kitFile = kitFile;
            this.kitName = kitName;
            this.kitNameCode = kitNameCode;
            this.PERSONAL_KIT_MANAGER = personalKitManager;
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {

            String langCreateKitConfigurationErrorMessage = LANG_FILE.getString("create_kit_configuration_error");

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
                    PLAYER.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(langCreateKitConfigurationErrorMessage)));
                    e.printStackTrace();
                }
            }
        }
    }

}
