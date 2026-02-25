package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, Map<String, Map<String, Long>>> cooldowns = new HashMap<>(); // Player -> BookKey -> AbilityType -> EndTime
    
    // Ability type constants - YAHI VARIABLES MISSING THE
    public static final String ABILITY_NORMAL = "normal";
    public static final String ABILITY_ADVANCED = "advanced";
    public static final String ABILITY_ULTIMATE = "ultimate";
    
    public CooldownManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Check if a specific ability type is on cooldown for a player
     */
    public boolean isOnCooldown(Player player, MagicBook book, String abilityType) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        
        Map<String, Map<String, Long>> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (!playerCooldowns.containsKey(book.getAbilityKey())) {
            return false;
        }
        
        Map<String, Long> abilityCooldowns = playerCooldowns.get(book.getAbilityKey());
        if (!abilityCooldowns.containsKey(abilityType)) {
            return false;
        }
        
        long timeLeft = abilityCooldowns.get(abilityType) - System.currentTimeMillis();
        return timeLeft > 0;
    }
    
    /**
     * Get remaining cooldown time in seconds for a specific ability type
     */
    public long getRemainingCooldown(Player player, MagicBook book, String abilityType) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return 0;
        }
        
        Map<String, Map<String, Long>> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (!playerCooldowns.containsKey(book.getAbilityKey())) {
            return 0;
        }
        
        Map<String, Long> abilityCooldowns = playerCooldowns.get(book.getAbilityKey());
        if (!abilityCooldowns.containsKey(abilityType)) {
            return 0;
        }
        
        long timeLeft = abilityCooldowns.get(abilityType) - System.currentTimeMillis();
        return Math.max(0, timeLeft / 1000);
    }
    
    /**
     * Set cooldown for a specific ability type
     */
    public void setCooldown(Player player, MagicBook book, String abilityType, int seconds) {
        UUID playerId = player.getUniqueId();
        long cooldownEnd = System.currentTimeMillis() + (seconds * 1000L);
        
        // Initialize maps if needed
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                 .computeIfAbsent(book.getAbilityKey(), k -> new HashMap<>())
                 .put(abilityType, cooldownEnd);
        
        // Show action bar timer
        showActionBarCooldown(player, book, abilityType, seconds);
    }
    
    /**
     * Display cooldown timer in action bar with progress bar
     */
    private void showActionBarCooldown(Player player, MagicBook book, String abilityType, int totalSeconds) {
        String abilityName = getAbilityName(abilityType);
        String abilityColor = getAbilityColor(abilityType);
        
        new BukkitRunnable() {
            int secondsLeft = totalSeconds;
            
            @Override
            public void run() {
                if (secondsLeft <= 0 || !isOnCooldown(player, book, abilityType)) {
                    // Clear action bar
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                        TextComponent.fromLegacyText(""));
                    cancel();
                    return;
                }
                
                // Create progress bar (20 characters)
                int totalBars = 20;
                int filledBars = (int)((double)secondsLeft / totalSeconds * totalBars);
                
                StringBuilder bar = new StringBuilder();
                for (int i = 0; i < totalBars; i++) {
                    if (i < filledBars) {
                        bar.append("§a■");
                    } else {
                        bar.append("§7■");
                    }
                }
                
                // Format: [Book Name] [Ability Type] [Time] [Progress Bar]
                String message = String.format("§d⚡ %s §7[%s%s§7] §f%d秒 %s", 
                    book.getDisplayName(), 
                    abilityColor, 
                    abilityName, 
                    secondsLeft, 
                    bar.toString());
                
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                    TextComponent.fromLegacyText(message));
                
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    /**
     * Get display name for ability type
     */
    private String getAbilityName(String abilityType) {
        switch(abilityType) {
            case ABILITY_NORMAL: return "Normal";
            case ABILITY_ADVANCED: return "Advanced";
            case ABILITY_ULTIMATE: return "Ultimate";
            default: return "Unknown";
        }
    }
    
    /**
     * Get color code for ability type
     */
    private String getAbilityColor(String abilityType) {
        switch(abilityType) {
            case ABILITY_NORMAL: return "§a";
            case ABILITY_ADVANCED: return "§b";
            case ABILITY_ULTIMATE: return "§6";
            default: return "§f";
        }
    }
    
    /**
     * Clear all cooldowns for a player (useful for admin commands)
     */
    public void clearCooldowns(Player player) {
        cooldowns.remove(player.getUniqueId());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
            TextComponent.fromLegacyText("§aAll cooldowns cleared!"));
    }
    
    /**
     * Get all active cooldowns for a player (for debugging)
     */
    public Map<String, Map<String, Long>> getPlayerCooldowns(Player player) {
        return cooldowns.getOrDefault(player.getUniqueId(), new HashMap<>());
    }
    
    // ========== LEGACY METHODS FOR BACKWARD COMPATIBILITY ==========
    
    /**
     * Check if NORMAL ability is on cooldown (legacy)
     */
    public boolean isOnCooldown(Player player, MagicBook book) {
        return isOnCooldown(player, book, ABILITY_NORMAL);
    }
    
    /**
     * Get remaining cooldown for NORMAL ability (legacy)
     */
    public long getRemainingCooldown(Player player, MagicBook book) {
        return getRemainingCooldown(player, book, ABILITY_NORMAL);
    }
    
    /**
     * Set cooldown for NORMAL ability (legacy)
     */
    public void setCooldown(Player player, MagicBook book) {
        setCooldown(player, book, ABILITY_NORMAL, book.getCooldown());
    }
    
    /**
     * Set custom cooldown for NORMAL ability (legacy)
     */
    public void setCustomCooldown(Player player, MagicBook book, int seconds) {
        setCooldown(player, book, ABILITY_NORMAL, seconds);
    }
}
