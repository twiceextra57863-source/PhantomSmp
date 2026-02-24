package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GraceManager {
    
    private final PhantomSMP plugin;
    private BukkitTask graceTask;
    private BossBar bossBar;
    private int remainingSeconds;
    private boolean graceActive = false;
    private final Set<UUID> protectedPlayers = new HashSet<>();
    
    public GraceManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startGracePeriod(int seconds) {
        if (graceTask != null) {
            graceTask.cancel();
        }
        
        this.remainingSeconds = seconds;
        this.graceActive = true;
        
        // Protect all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            protectedPlayers.add(player.getUniqueId());
        }
        
        // Create boss bar
        bossBar = Bukkit.createBossBar(
            "§a§l✨ GRACE PERIOD ✨ §7- §f" + formatTime(seconds) + " §7remaining",
            BarColor.GREEN,
            BarStyle.SOLID
        );
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
        
        // Broadcast message
        Bukkit.broadcastMessage("§a§l═══ GRACE PERIOD STARTED ═══");
        Bukkit.broadcastMessage("§eNo PVP for §f" + seconds + " §eseconds!");
        Bukkit.broadcastMessage("§7Use this time to prepare!");
        Bukkit.broadcastMessage("§a§l══════════════════════════");
        
        // Play sound
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
        
        graceTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    endGracePeriod();
                    cancel();
                    return;
                }
                
                // Update boss bar
                double progress = (double) remainingSeconds / seconds;
                bossBar.setProgress(progress);
                bossBar.setTitle("§a§l✨ GRACE PERIOD ✨ §7- §f" + formatTime(remainingSeconds) + " §7remaining");
                
                // Warning at 10 seconds
                if (remainingSeconds == 10) {
                    Bukkit.broadcastMessage("§e⚠️ Grace period ends in §f10 §eseconds!");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                        player.sendTitle("§e10", "§fseconds remaining", 0, 20, 10);
                    }
                }
                
                // Countdown last 5 seconds
                if (remainingSeconds <= 5 && remainingSeconds > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("§c" + remainingSeconds, "§fGrace period ending", 0, 20, 10);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                }
                
                remainingSeconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void endGracePeriod() {
        graceActive = false;
        protectedPlayers.clear();
        
        if (bossBar != null) {
            bossBar.removeAll();
        }
        
        Bukkit.broadcastMessage("§c§l═══ GRACE PERIOD ENDED ═══");
        Bukkit.broadcastMessage("§cPVP is now enabled!");
        Bukkit.broadcastMessage("§7Fight with honor!");
        Bukkit.broadcastMessage("§c§l══════════════════════════");
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
            player.sendTitle("§cGRACE ENDED", "§fPVP is now enabled", 10, 70, 20);
        }
    }
    
    public void addPlayer(Player player) {
        if (graceActive) {
            protectedPlayers.add(player.getUniqueId());
            if (bossBar != null) {
                bossBar.addPlayer(player);
            }
            player.sendMessage("§aYou are now protected by the grace period!");
        }
    }
    
    public boolean isProtected(Player player) {
        return protectedPlayers.contains(player.getUniqueId());
    }
    
    public boolean isGraceActive() {
        return graceActive;
    }
    
    public int getRemainingTime() {
        return remainingSeconds;
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}
