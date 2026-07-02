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

public class MeteorTask extends BukkitRunnable {

    private final MythicStaffPlugin plugin;
    private final Player player;
    private final Location target;
    private Location current;
    private final Vector direction;
    private int ticks = 0;

    public MeteorTask(MythicStaffPlugin plugin, Player player, Location target) {
        this.plugin = plugin;
        this.player = player;
        this.target = target;

        // Start 25 blocks above the target
        this.current = target.clone().add(0, 25, 0);
        
        // Direction is straight down
        this.direction = new Vector(0, -1.5, 0); // Fast drop
    }

    @Override
    public void run() {
        if (!player.isOnline() || ticks > 100) {
            this.cancel();
            return;
        }

        World world = current.getWorld();
        
        // Move meteor
        current.add(direction);

        // Visuals (Huge fireball effect)
        world.spawnParticle(Particle.FLAME, current, 50, 1.5, 1.5, 1.5, 0.05);
        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, current, 20, 1.0, 1.0, 1.0, 0.05);
        world.spawnParticle(Particle.LAVA, current, 10, 1.0, 1.0, 1.0, 0);

        // Check for impact (hit the target Y level or hit a solid block)
        if (current.getY() <= target.getY() || current.getBlock().getType().isSolid()) {
            explode();
            this.cancel();
            return;
        }

        ticks++;
    }

    private void explode() {
        World world = current.getWorld();
        
        // Explosion effects
        world.playSound(current, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.8f);
        world.spawnParticle(Particle.EXPLOSION, current, 5, 2.0, 2.0, 2.0, 0);
        world.spawnParticle(Particle.FLAME, current, 200, 3.0, 3.0, 3.0, 0.2);
        world.spawnParticle(Particle.LAVA, current, 30, 2.0, 2.0, 2.0, 0);

        // Damage entities in a 5 block radius
        for (Entity entity : world.getNearbyEntities(current, 5, 5, 5)) {
            if (entity instanceof LivingEntity living && entity != player) {
                living.damage(12.0, player); // 6 hearts
                living.setFireTicks(100); // 5 seconds of fire
            }
        }
    }
}
