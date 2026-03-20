package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    private final PhantomSMP plugin;
    private final Map<String, Long> cooldowns = new HashMap<>();
    
    public CooldownManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void setCooldown(String key, int seconds) {
        cooldowns.put(key, System.currentTimeMillis() + (seconds * 1000L));
    }
    
    public boolean isOnCooldown(String key) {
        if (!cooldowns.containsKey(key)) return false;
        
        if (System.currentTimeMillis() >= cooldowns.get(key)) {
            cooldowns.remove(key);
            return false;
        }
        return true;
    }
    
    public long getRemainingCooldown(String key) {
        if (!cooldowns.containsKey(key)) return 0;
        return (cooldowns.get(key) - System.currentTimeMillis()) / 1000;
    }
    
    public void removeCooldown(String key) {
        cooldowns.remove(key);
    }
    
    public void resetPlayerCooldowns(Player player) {
        cooldowns.entrySet().removeIf(entry -> entry.getKey().startsWith(player.getUniqueId().toString()));
    }
}
