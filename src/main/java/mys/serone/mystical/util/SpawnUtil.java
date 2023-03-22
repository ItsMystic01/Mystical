package mys.serone.mystical.util;

import mys.serone.mystical.Mystical;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.Objects;
import java.util.logging.Level;

public class SpawnUtil implements Listener {
    private final ConfigUtil CONFIG;
    private Location spawn;

    public SpawnUtil(Mystical plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        CONFIG = new ConfigUtil(plugin, "spawn.yml");

        String worldName = CONFIG.getConfig().getString("world");
        double x = CONFIG.getConfig().getDouble("x");
        double y = CONFIG.getConfig().getDouble("y");
        double z = CONFIG.getConfig().getDouble("z");
        float yaw = (float) CONFIG.getConfig().getDouble("yaw");
        float pitch = (float) CONFIG.getConfig().getDouble("pitch");

        if (worldName != null) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                Bukkit.getLogger().log(Level.SEVERE, "The world does not exist");
                return;
            }

            spawn = new Location(world, x, y, z, yaw, pitch);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        if (spawn != null) {
            event.setRespawnLocation(spawn);
        }
    }

    public void teleport(Player player) {
        if (spawn == null) {
            player.sendMessage("The spawn is not set");
            return;
        }

        player.teleport(spawn);
    }

    public void set(Location spawn) {
        this.spawn = spawn;

        String worldName = Objects.requireNonNull(spawn.getWorld()).getName();
        double x = spawn.getX();
        double y = spawn.getY();
        double z = spawn.getZ();
        double yaw = spawn.getYaw();
        double pitch = spawn.getPitch();

        CONFIG.getConfig().set("world", worldName);
        CONFIG.getConfig().set("x", x);
        CONFIG.getConfig().set("y", y);
        CONFIG.getConfig().set("z", z);
        CONFIG.getConfig().set("yaw", yaw);
        CONFIG.getConfig().set("pitch", pitch);
        CONFIG.save();

        Bukkit.getServer().getLogger().info(String.valueOf(CONFIG.save()));
    }
}
