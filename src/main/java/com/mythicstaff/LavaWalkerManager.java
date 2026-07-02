package com.mythicstaff;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class LavaWalkerManager implements Listener {

    private final MythicStaffPlugin plugin;
    // Map of block location to the time it should revert back to lava
    private final Map<Location, Long> frostedLavaBlocks = new HashMap<>();

    public LavaWalkerManager(MythicStaffPlugin plugin) {
        this.plugin = plugin;
        startMeltTask();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (plugin.getStaffManager().isFireStaff(player.getInventory().getItemInMainHand())) {
                EntityDamageEvent.DamageCause cause = event.getCause();
                if (cause == EntityDamageEvent.DamageCause.LAVA ||
                    cause == EntityDamageEvent.DamageCause.FIRE ||
                    cause == EntityDamageEvent.DamageCause.FIRE_TICK ||
                    cause == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                    event.setCancelled(true);
                    player.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getStaffManager().isFireStaff(player.getInventory().getItemInMainHand())) {
            return;
        }

        Location to = event.getTo();
        if (to == null) return;
        
        Location from = event.getFrom();
        // Only trigger if they moved to a new block
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        // Check blocks in a 2-block radius below the player
        int radius = 2;
        Location center = player.getLocation().subtract(0, 1, 0);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block block = center.clone().add(x, 0, z).getBlock();
                
                // If it's a lava source block (level 0 is source in some versions, but checking Material.LAVA is enough)
                if (block.getType() == Material.LAVA) {
                    // Turn it to Magma Block temporarily
                    block.setType(Material.MAGMA_BLOCK);
                    frostedLavaBlocks.put(block.getLocation(), System.currentTimeMillis() + 4000L); // Melt after 4 seconds
                } else if (frostedLavaBlocks.containsKey(block.getLocation())) {
                    // Refresh the timer if they walk near it again
                    frostedLavaBlocks.put(block.getLocation(), System.currentTimeMillis() + 4000L);
                }
            }
        }
    }

    private void startMeltTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                frostedLavaBlocks.entrySet().removeIf(entry -> {
                    if (now > entry.getValue()) {
                        Block block = entry.getKey().getBlock();
                        if (block.getType() == Material.MAGMA_BLOCK) {
                            block.setType(Material.LAVA);
                        }
                        return true; // remove from map
                    }
                    return false;
                });
            }
        }.runTaskTimer(plugin, 20L, 10L); // Run every half second
    }
}
