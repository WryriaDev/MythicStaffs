package com.mythicstaff.task;

import com.mythicstaff.MythicStaffPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Fire Dash - creates a blazing trail behind the player as they dash forward.
 * Runs for about 10 ticks (0.5 seconds), spawning fire + smoke trails.
 */
public class FireDashTask extends BukkitRunnable {

    private final MythicStaffPlugin plugin;
    private final Player player;
    private int ticks = 0;

    public FireDashTask(MythicStaffPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        if (ticks > 10 || !player.isOnline()) {
            this.cancel();
            return;
        }

        Location loc = player.getLocation();

        // Main fire trail behind the player
        player.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0, 0.5, 0), 8, 0.3, 0.2, 0.3, 0.05);
        player.getWorld().spawnParticle(Particle.LAVA, loc.clone().add(0, 0.3, 0), 3, 0.2, 0.1, 0.2, 0);

        // Smoke wisps
        player.getWorld().spawnParticle(Particle.SMOKE, loc.clone().add(0, 0.8, 0), 4, 0.2, 0.3, 0.2, 0.02);

        // Ground fire marks
        player.getWorld().spawnParticle(Particle.SMALL_FLAME, loc.clone().add(0, 0.1, 0), 6, 0.4, 0.05, 0.4, 0.01);

        ticks++;
    }
}
