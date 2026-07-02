package com.mythicstaff.listener;

import com.mythicstaff.CooldownManager;
import com.mythicstaff.MythicStaffPlugin;
import com.mythicstaff.task.FireDashTask;
import com.mythicstaff.task.FireProjectileTask;
import com.mythicstaff.task.FlameBurstTask;
import com.mythicstaff.util.TextUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffComboListener implements Listener {

    private final MythicStaffPlugin plugin;
    private final Map<UUID, Boolean> spellReady = new HashMap<>();
    private final Map<UUID, Integer> readyParticleTaskId = new HashMap<>();

    private static final String CD_DASH = "fire_dash";
    private static final String CD_RING = "inferno_ring";
    private static final String CD_BURST = "flame_burst";
    private static final String CD_VORTEX = "fire_vortex";

    public StaffComboListener(MythicStaffPlugin plugin) {
        this.plugin = plugin;
        startActionBarTask();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!plugin.getStaffManager().isFireStaff(item)) return;

        Action action = event.getAction();
        CooldownManager cd = plugin.getCooldownManager();

        // ── ABILITY 1: LEFT CLICK (no sneak) = FIRE DASH ──
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && !player.isSneaking()) {

            // If spell is readied, shoot the ring instead
            if (spellReady.getOrDefault(player.getUniqueId(), false)) {
                shootInfernoRing(player);
                event.setCancelled(true);
                return;
            }

            if (cd.isOnCooldown(player.getUniqueId(), CD_DASH)) {
                int rem = cd.getRemainingSeconds(player.getUniqueId(), CD_DASH);
                sendActionBar(player, ChatColor.RED + "Fire Dash " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + rem + "s");
                return;
            }

            Vector direction = player.getLocation().getDirection().normalize();
            player.setVelocity(direction.multiply(2.0).setY(0.3));
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);

            new FireDashTask(plugin, player).runTaskTimer(plugin, 0L, 1L);

            cd.setCooldown(player.getUniqueId(), CD_DASH, 4);
            event.setCancelled(true);
            return;
        }

        // ── ABILITY 2: SNEAK + RIGHT CLICK = INFERNO RING ──
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {

            if (spellReady.getOrDefault(player.getUniqueId(), false)) {
                event.setCancelled(true);
                return;
            }

            if (cd.isOnCooldown(player.getUniqueId(), CD_RING)) {
                int rem = cd.getRemainingSeconds(player.getUniqueId(), CD_RING);
                sendActionBar(player, ChatColor.RED + "Inferno Ring " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + rem + "s");
                event.setCancelled(true);
                return;
            }

            spellReady.put(player.getUniqueId(), true);
            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 0.5f);

            BukkitRunnable ringVisual = new BukkitRunnable() {
                double angle = 0;
                int ticks = 0;

                @Override
                public void run() {
                    if (!spellReady.getOrDefault(player.getUniqueId(), false)
                            || !player.isOnline()
                            || !plugin.getStaffManager().isFireStaff(player.getInventory().getItemInMainHand())
                            || ticks > 100) {
                        spellReady.put(player.getUniqueId(), false);
                        this.cancel();
                        readyParticleTaskId.remove(player.getUniqueId());
                        return;
                    }

                    Location center = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2.5));
                    Vector forward = player.getLocation().getDirection().normalize();
                    Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize();
                    Vector up = right.clone().crossProduct(forward).normalize();

                    double radius = 1.2;
                    int points = 12;
                    for (int i = 0; i < points; i++) {
                        double theta = angle + (2 * Math.PI * i / points);
                        double x = Math.cos(theta) * radius;
                        double y = Math.sin(theta) * radius;

                        Location particleLoc = center.clone()
                                .add(right.clone().multiply(x))
                                .add(up.clone().multiply(y));
                        player.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0, 0, 0, 0);
                    }

                    for (int i = 0; i < 6; i++) {
                        double theta = -angle * 2 + (2 * Math.PI * i / 6);
                        double r2 = 0.6;
                        Location innerLoc = center.clone()
                                .add(right.clone().multiply(Math.cos(theta) * r2))
                                .add(up.clone().multiply(Math.sin(theta) * r2));
                        player.getWorld().spawnParticle(Particle.SMALL_FLAME, innerLoc, 1, 0, 0, 0, 0);
                    }

                    angle += 0.15;
                    ticks++;
                }
            };
            ringVisual.runTaskTimer(plugin, 0L, 1L);
            readyParticleTaskId.put(player.getUniqueId(), ringVisual.getTaskId());

            event.setCancelled(true);
            return;
        }

        // ── ABILITY 4: SNEAK + LEFT CLICK = FIRE VORTEX ──
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.isSneaking()) {
            if (cd.isOnCooldown(player.getUniqueId(), CD_VORTEX)) {
                int rem = cd.getRemainingSeconds(player.getUniqueId(), CD_VORTEX);
                sendActionBar(player, ChatColor.RED + "Fire Vortex " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + rem + "s");
                event.setCancelled(true);
                return;
            }

            // Get target block (max 20 blocks away)
            org.bukkit.block.Block targetBlock = player.getTargetBlockExact(20);
            Location targetLoc = targetBlock != null ? targetBlock.getLocation() : player.getEyeLocation().add(player.getLocation().getDirection().multiply(10));
            targetLoc.setY(targetLoc.getY() + 1); // Start slightly above the block

            new com.mythicstaff.task.VortexTask(plugin, player, targetLoc).runTaskTimer(plugin, 0L, 1L);

            cd.setCooldown(player.getUniqueId(), CD_VORTEX, 10); // 10s cooldown
            event.setCancelled(true);
            return;
        }

        // ── ABILITY 3: RIGHT CLICK (no sneak) = FLAME BURST ──
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && !player.isSneaking()) {

            if (cd.isOnCooldown(player.getUniqueId(), CD_BURST)) {
                int rem = cd.getRemainingSeconds(player.getUniqueId(), CD_BURST);
                sendActionBar(player, ChatColor.RED + "Flame Burst " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + rem + "s");
                event.setCancelled(true);
                return;
            }

            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
            new FlameBurstTask(plugin, player).runTaskTimer(plugin, 0L, 1L);

            cd.setCooldown(player.getUniqueId(), CD_BURST, 6);
            event.setCancelled(true);
        }
    }

    private void shootInfernoRing(Player player) {
        spellReady.put(player.getUniqueId(), false);

        if (readyParticleTaskId.containsKey(player.getUniqueId())) {
            plugin.getServer().getScheduler().cancelTask(readyParticleTaskId.get(player.getUniqueId()));
            readyParticleTaskId.remove(player.getUniqueId());
        }

        player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.2f);

        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2.5));
        Vector direction = player.getLocation().getDirection().normalize();

        new FireProjectileTask(plugin, player, spawnLoc, direction).runTaskTimer(plugin, 0L, 1L);

        plugin.getCooldownManager().setCooldown(player.getUniqueId(), CD_RING, 8);
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        spellReady.put(player.getUniqueId(), false);
    }

    // ── ActionBar HUD ──
    private void startActionBarTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (!plugin.getStaffManager().isFireStaff(player.getInventory().getItemInMainHand())) continue;

                    CooldownManager cd = plugin.getCooldownManager();
                    StringBuilder bar = new StringBuilder();

                    // ── Dash ──
                    if (cd.isOnCooldown(player.getUniqueId(), CD_DASH)) {
                        bar.append(TextUtil.color("<#555555>" + TextUtil.smallCaps("Dash ")));
                        bar.append(TextUtil.color("<#FF4400>")).append(cd.getRemainingSeconds(player.getUniqueId(), CD_DASH)).append("s");
                    } else {
                        bar.append(TextUtil.color("<#33FF33>" + TextUtil.smallCaps("Dash")));
                    }

                    bar.append(TextUtil.color("<#333333>  |  "));

                    // ── Ring ──
                    if (spellReady.getOrDefault(player.getUniqueId(), false)) {
                        bar.append(TextUtil.color("<#FF9900>&l" + TextUtil.smallCaps("RING READY")));
                    } else if (cd.isOnCooldown(player.getUniqueId(), CD_RING)) {
                        bar.append(TextUtil.color("<#555555>" + TextUtil.smallCaps("Ring ")));
                        bar.append(TextUtil.color("<#FF4400>")).append(cd.getRemainingSeconds(player.getUniqueId(), CD_RING)).append("s");
                    } else {
                        bar.append(TextUtil.color("<#33FF33>" + TextUtil.smallCaps("Ring")));
                    }

                    bar.append(TextUtil.color("<#333333>  |  "));

                    // ── Burst ──
                    if (cd.isOnCooldown(player.getUniqueId(), CD_BURST)) {
                        bar.append(TextUtil.color("<#555555>" + TextUtil.smallCaps("Burst ")));
                        bar.append(TextUtil.color("<#FF4400>")).append(cd.getRemainingSeconds(player.getUniqueId(), CD_BURST)).append("s");
                    } else {
                        bar.append(TextUtil.color("<#33FF33>" + TextUtil.smallCaps("Burst")));
                    }

                    bar.append(TextUtil.color("<#333333>  |  "));

                    // ── Vortex ──
                    if (cd.isOnCooldown(player.getUniqueId(), CD_VORTEX)) {
                        bar.append(TextUtil.color("<#555555>" + TextUtil.smallCaps("Vortex ")));
                        bar.append(TextUtil.color("<#FF4400>")).append(cd.getRemainingSeconds(player.getUniqueId(), CD_VORTEX)).append("s");
                    } else {
                        bar.append(TextUtil.color("<#33FF33>" + TextUtil.smallCaps("Vortex")));
                    }

                    sendActionBar(player, bar.toString());
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
    }
}
