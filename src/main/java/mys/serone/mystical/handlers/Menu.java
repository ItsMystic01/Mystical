package mys.serone.mystical.handlers;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Listener, CommandExecutor {

    private final Mystical PLUGIN;

    public Menu(Mystical plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Menu")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 11) {
            player.performCommand("kit");
            player.closeInventory();
        }

        event.setCancelled(true);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command");
            return true;
        }

        if (!sender.hasPermission("mystic.menu")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;

        Inventory inv = Bukkit.createInventory(player, 27, "Menu");

        inv.setItem(11, getItem(new ItemStack(Material.DIAMOND_SWORD), "&4PVP", "&cClick to join the PvP Arena", "&cMore to come.."));
        inv.setItem(13, getItem(new ItemStack(Material.BEACON), "&bCreative Plots", "&cCreative Builds"));
        inv.setItem(15, getItem(new ItemStack(Material.GRASS_BLOCK), "&aSky-block", "&cBattle for the most resources with no void"));

        player.openInventory(inv);

        return true;
    }

    private ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> loreItem = new ArrayList<>();

        for (String s : lore) {
            loreItem.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(loreItem);

        item.setItemMeta(meta);
        return item;
    }
}
