package com.mythicstaff.task;

import com.mythicstaff.MythicStaffPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Flame Burst - A forward-facing cone of flames.
 * Spawns fire particles in a cone shape ahead of the player and damages enemies.
 */
public class FlameBurstTask extends BukkitRunnable {

    private final MythicStaffPlugin plugin;
    private final Player player;
    private final Location origin;
    private final Vector direction;
    private int ticks = 0;

    public FlameBurstTask(MythicStaffPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.origin = player.getEyeLocation().clone();
        this.direction = player.getLocation().getDirection().normalize();
    }

    @Override
    public void run() {
        if (ticks > 6 || !player.isOnline()) {
            this.cancel();
            return;
        }

        // Expanding cone of fire each tick
        double distance = 1.0 + ticks * 1.2;
        double spread = 0.3 + ticks * 0.25;

        Location center = origin.clone().add(direction.clone().multiply(distance));

        // Calculate perpendicular vectors for spread
        Vector right = direction.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        Vector up = right.clone().crossProduct(direction).normalize();

        // Spawn particles in a cone pattern
        int particleCount = 8 + ticks * 4;
        for (int i = 0; i < particleCount; i++) {
            double offsetRight = (Math.random() - 0.5) * 2 * spread;
            double offsetUp = (Math.random() - 0.5) * 2 * spread;

            Location particleLoc = center.clone()
                    .add(right.clone().multiply(offsetRight))
                    .add(up.clone().multiply(offsetUp));

            player.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0.05, 0.05, 0.05, 0.02);
        }

        // Add some dramatic lava drips on later ticks
        if (ticks >= 3) {
            player.getWorld().spawnParticle(Particle.LAVA, center, 3, spread * 0.5, spread * 0.5, spread * 0.5, 0);
        }

        // Damage entities in the cone area
        Collection<Entity> entities = center.getWorld().getNearbyEntities(center, spread + 1.0, spread + 1.0, spread + 1.0);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && !entity.equals(player)) {
                livingEntity.damage(3.0, player);
                livingEntity.setFireTicks(60);
            }
        }

        // Sound on every other tick
        if (ticks % 2 == 0) {
            player.getWorld().playSound(center, Sound.BLOCK_FIRE_AMBIENT, 1.5f, 0.5f);
        }

        ticks++;
    }
}
