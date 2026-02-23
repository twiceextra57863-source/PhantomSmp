package com.phantom.smp.manager;
package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    private final Map<UUID, Map<String, BossBar>> activeBars = new HashMap<>();
    
    public CooldownManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public boolean isOnCooldown(Player player, MagicBook book) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (!playerCooldowns.containsKey(book.getAbilityKey())) {
            return false;
        }
        
        long timeLeft = playerCooldowns.get(book.getAbilityKey()) - System.currentTimeMillis();
        return timeLeft > 0;
    }
    
    public long getRemainingCooldown(Player player, MagicBook book) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return 0;
        }
        
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (!playerCooldowns.containsKey(book.getAbilityKey())) {
            return 0;
        }
        
        long timeLeft = playerCooldowns.get(book.getAbilityKey()) - System.currentTimeMillis();
        return Math.max(0, timeLeft / 1000);
    }
    
    public void setCooldown(Player player, MagicBook book) {
        UUID playerId = player.getUniqueId();
        long cooldownEnd = System.currentTimeMillis() + (book.getCooldown() * 1000L);
        
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                 .put(book.getAbilityKey(), cooldownEnd);
        
        showCooldownBar(player, book);
    }
    
    private void showCooldownBar(Player player, MagicBook book) {
        BossBar bar = Bukkit.createBossBar(
            "§c⏳ " + book.getDisplayName() + " §7- Cooldown",
            BarColor.RED,
            BarStyle.SOLID
        );
        
        bar.addPlayer(player);
        
        activeBars.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                  .put(book.getAbilityKey(), bar);
        
        new BukkitRunnable() {
            int secondsLeft = book.getCooldown();
            
            @Override
            public void run() {
                if (secondsLeft <= 0 || !isOnCooldown(player, book)) {
                    bar.removeAll();
                    Map<String, BossBar> playerBars = activeBars.get(player.getUniqueId());
                    if (playerBars != null) {
                        playerBars.remove(book.getAbilityKey());
                    }
                    cancel();
                    return;
                }
                
                double progress = (double) secondsLeft / book.getCooldown();
                bar.setProgress(progress);
                bar.setTitle("§c⏳ " + book.getDisplayName() + " §7- §f" + secondsLeft + "s");
                
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    public void removeCooldownBar(Player player, MagicBook book) {
        if (activeBars.containsKey(player.getUniqueId())) {
            BossBar bar = activeBars.get(player.getUniqueId()).remove(book.getAbilityKey());
            if (bar != null) {
                bar.removeAll();
            }
        }
    }
}
