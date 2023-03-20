package mys.serone.mystical.handlers;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCHandler extends Trait implements Listener {
    private double health = 20.0;
    public LivingEntity livingNpc = (LivingEntity) npc;
    public NPCHandler() {
        super("healthtrait");
    }

    @Override
    public void onSpawn() {
        livingNpc.setHealth(health);
        CitizensAPI.getPlugin().getServer().getPluginManager().registerEvents(this, CitizensAPI.getPlugin());
    }

    @Override
    public void onDespawn() {
        NPCDespawnEvent.getHandlerList().unregister(this);
    }

    @Override
    public void onRemove() {
        NPCDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onSpawn(NPCSpawnEvent event) {
        if (event.getNPC() == npc) {
            livingNpc.setHealth(health);
        }
    }

    @EventHandler
    public void onDamage(NPCDamageByEntityEvent event) {
        if (event.getNPC() == npc) {
            Entity attacker = event.getDamager();
            double damage = event.getDamage();
            health -= damage;
            if (health <= 0) {
                health = 0;
                livingNpc.remove();
            } else {
                livingNpc.setHealth(health);
            }
            if (attacker instanceof Player) {
                (attacker).sendMessage(ChatColor.RED + "You dealt " + damage + " damage to " + npc.getName() + "!");
            }
        }
    }
}
