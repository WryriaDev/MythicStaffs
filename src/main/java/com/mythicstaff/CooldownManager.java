package com.mythicstaff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    // ability name -> (player uuid -> expire time)
    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public void setCooldown(UUID uuid, String ability, int seconds) {
        cooldowns.putIfAbsent(ability, new HashMap<>());
        cooldowns.get(ability).put(uuid, System.currentTimeMillis() + (seconds * 1000L));
    }

    public boolean isOnCooldown(UUID uuid, String ability) {
        if (!cooldowns.containsKey(ability)) return false;
        Map<UUID, Long> map = cooldowns.get(ability);
        if (!map.containsKey(uuid)) return false;
        if (map.get(uuid) <= System.currentTimeMillis()) {
            map.remove(uuid);
            return false;
        }
        return true;
    }

    public int getRemainingSeconds(UUID uuid, String ability) {
        if (!isOnCooldown(uuid, ability)) return 0;
        long remaining = cooldowns.get(ability).get(uuid) - System.currentTimeMillis();
        return (int) Math.ceil(remaining / 1000.0);
    }
}
