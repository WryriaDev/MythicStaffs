package com.mythicstaff.task;

import com.mythicstaff.MythicStaffPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class VortexTask extends BukkitRunnable {

    private final MythicStaffPlugin plugin;
    private final Player player;
    private final Location origin;
    private double t = 0; // time variable for spiral
    private int ticks = 0;

    public VortexTask(MythicStaffPlugin plugin, Player player, Location target) {
        this.plugin = plugin;
        this.player = player;
        this.origin = target.clone();
        
        player.getWorld().playSound(origin, Sound.ENTITY_BLAZE_AMBIENT, 1.5f, 0.5f);
        player.getWorld().playSound(origin, Sound.ITEM_FIRECHARGE_USE, 1.5f, 0.8f);
    }

    @Override
    public void run() {
        if (!player.isOnline() || ticks > 60) { // 3 seconds duration
            this.cancel();
            return;
        }

        World world = origin.getWorld();
        
        // Create the spiral/vortex effect
        t += Math.PI / 8; // Speed of rotation
        
        for (double y = 0; y < 4; y += 0.5) { // Height of the vortex
            double radius = 1.0 + (y * 0.2); // Expands slightly as it goes up
            
            // Helix 1
            double x1 = radius * Math.cos(t + y);
            double z1 = radius * Math.sin(t + y);
            Location loc1 = origin.clone().add(x1, y, z1);
            world.spawnParticle(Particle.FLAME, loc1, 3, 0, 0, 0, 0.01);
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc1, 1, 0, 0, 0, 0.01);
            
            // Helix 2 (opposite side)
            double x2 = radius * Math.cos(t + y + Math.PI);
            double z2 = radius * Math.sin(t + y + Math.PI);
            Location loc2 = origin.clone().add(x2, y, z2);
            world.spawnParticle(Particle.FLAME, loc2, 3, 0, 0, 0, 0.01);
            world.spawnParticle(Particle.LAVA, loc2, 1, 0, 0, 0, 0);
        }

        // Damage and launch entities caught in the vortex
        if (ticks % 5 == 0) { // Apply effects every 5 ticks
            for (Entity entity : world.getNearbyEntities(origin, 3, 4, 3)) {
                if (entity instanceof LivingEntity living && entity != player) {
                    living.damage(4.0, player); // 2 hearts per tick
                    living.setFireTicks(60); // 3 seconds of fire
                    
                    // Throw them up and slightly pull them in
                    Vector pull = origin.toVector().subtract(living.getLocation().toVector()).normalize().multiply(0.2);
                    pull.setY(0.4); // Upward lift
                    living.setVelocity(living.getVelocity().add(pull));
                }
            }
        }

        ticks++;
    }
}
