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
 * Inferno Ring Projectile - a spinning ring of fire that flies forward.
 * On impact: massive fire explosion with AoE damage.
 */
public class FireProjectileTask extends BukkitRunnable {

    private final MythicStaffPlugin plugin;
    private final Player shooter;
    private final Location currentLocation;
    private final Vector direction;
    private final Vector right;
    private final Vector up;
    private int ticksLived = 0;
    private double angle = 0;
    private static final int MAX_TICKS = 60;
    private static final double SPEED = 0.8;
    private static final double RING_RADIUS = 1.2;

    public FireProjectileTask(MythicStaffPlugin plugin, Player shooter, Location startLocation, Vector direction) {
        this.plugin = plugin;
        this.shooter = shooter;
        this.currentLocation = startLocation.clone();
        this.direction = direction.normalize().multiply(SPEED);

        // Calculate perpendicular vectors for the ring orientation
        Vector forward = direction.clone().normalize();
        this.right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        this.up = right.clone().crossProduct(forward).normalize();
    }

    @Override
    public void run() {
        if (ticksLived > MAX_TICKS || !shooter.isOnline()) {
            this.cancel();
            return;
        }

        // Move forward
        currentLocation.add(direction);
        ticksLived++;
        angle += 0.3;

        // Draw spinning ring of fire
        int points = 14;
        for (int i = 0; i < points; i++) {
            double theta = angle + (2 * Math.PI * i / points);
            double x = Math.cos(theta) * RING_RADIUS;
            double y = Math.sin(theta) * RING_RADIUS;

            Location particleLoc = currentLocation.clone()
                    .add(right.clone().multiply(x))
                    .add(up.clone().multiply(y));

            currentLocation.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0, 0, 0, 0);
        }

        // Inner core particles
        currentLocation.getWorld().spawnParticle(Particle.SMALL_FLAME, currentLocation, 3, 0.15, 0.15, 0.15, 0.02);

        // Trailing smoke
        Location trailLoc = currentLocation.clone().subtract(direction.clone().normalize().multiply(0.5));
        currentLocation.getWorld().spawnParticle(Particle.SMOKE, trailLoc, 2, 0.1, 0.1, 0.1, 0.01);

        // Check block collision
        if (!currentLocation.getBlock().isPassable()) {
            explode();
            return;
        }

        // Check entity collision
        Collection<Entity> nearby = currentLocation.getWorld().getNearbyEntities(currentLocation, 1.2, 1.2, 1.2);
        for (Entity entity : nearby) {
            if (entity instanceof LivingEntity && !entity.equals(shooter)) {
                explode();
                return;
            }
        }
    }

    private void explode() {
        this.cancel();

        // Massive fire explosion
        currentLocation.getWorld().playSound(currentLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.2f);

        // Expanding ring on impact
        for (int ring = 0; ring < 3; ring++) {
            double r = 1.0 + ring * 0.8;
            int pts = 16 + ring * 8;
            for (int i = 0; i < pts; i++) {
                double theta = 2 * Math.PI * i / pts;
                double x = Math.cos(theta) * r;
                double z = Math.sin(theta) * r;
                Location loc = currentLocation.clone().add(x, 0.5, z);
                currentLocation.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0.05);
            }
        }

        // Central burst
        currentLocation.getWorld().spawnParticle(Particle.FLAME, currentLocation, 60, 1.5, 1.0, 1.5, 0.15);
        currentLocation.getWorld().spawnParticle(Particle.LAVA, currentLocation, 25, 1.5, 1.0, 1.5, 0.1);
        currentLocation.getWorld().spawnParticle(Particle.SMOKE, currentLocation, 20, 1.0, 0.5, 1.0, 0.05);

        // AoE damage and ignite
        Collection<Entity> targets = currentLocation.getWorld().getNearbyEntities(currentLocation, 3.5, 3.5, 3.5);
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity && !entity.equals(shooter)) {
                livingEntity.damage(8.0, shooter);
                livingEntity.setFireTicks(100);
            }
        }
    }
}
