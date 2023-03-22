package mys.serone.mystical.handlers;

import mys.serone.mystical.Mystical;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class TorchHandler implements Listener  {

    public TorchHandler(Mystical plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onTorchPlace_LOW(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.TORCH) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You have placed a torch!");
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onTorchBreak_LOW(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.TORCH) {
            event.getPlayer().sendMessage("You have broken a torch!");
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType() == Material.TORCH){
            Player player = event.getPlayer();
            player.sendMessage("You dropped a Torch!");
        }
    }

}
