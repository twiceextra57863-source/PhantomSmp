package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
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

public class TimerManager {
    
    private final PhantomSMP plugin;
    private BukkitTask timerTask;
    private BossBar bossBar;
    private int remainingSeconds;
    private final Set<UUID> protectedPlayers = new HashSet<>();
    
    public TimerManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startTimer(int seconds) {
        if (timerTask != null) {
            timerTask.cancel();
        }
        
        this.remainingSeconds = seconds;
        
        bossBar = Bukkit.createBossBar(
            "§6§lSMP Starting in: §e" + formatTime(seconds),
            BarColor.YELLOW,
            BarStyle.SOLID
        );
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
            protectedPlayers.add(player.getUniqueId());
        }
        
        Bukkit.broadcastMessage("§6§l═══ SMP START TIMER ═══");
        Bukkit.broadcastMessage("§eThe SMP will start in §6" + seconds + "§e seconds!");
        Bukkit.broadcastMessage("§c§lNO PVP §7during this time!");
        Bukkit.broadcastMessage("§6§l══════════════════════");
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
        
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    finishTimer();
                    cancel();
                    return;
                }
                
                double progress = (double) remainingSeconds / seconds;
                bossBar.setProgress(progress);
                bossBar.setTitle("§6§lSMP Starting in: §e" + formatTime(remainingSeconds));
                
                if (remainingSeconds <= 10) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                }
                
                if (remainingSeconds <= 10 && remainingSeconds > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(
                            "§e" + remainingSeconds,
                            "§7SMP starting soon!",
                            0, 20, 10
                        );
                    }
                }
                
                remainingSeconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void finishTimer() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
        
        protectedPlayers.clear();
        
        Bukkit.broadcastMessage("§6§l═══ SMP HAS STARTED! ═══");
        Bukkit.broadcastMessage("§aThe SMP is now active!");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§6§lRULES:");
        Bukkit.broadcastMessage("§e• §fNo griefing without permission");
        Bukkit.broadcastMessage("§e• §fRespect other players");
        Bukkit.broadcastMessage("§e• §fHave fun and build awesome things!");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§d§l✨ MAY THE MAGIC BE WITH YOU! ✨");
        Bukkit.broadcastMessage("§6§l══════════════════════════");
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            player.sendTitle(
                "§6§lSMP STARTED!",
                "§eMay the magic be with you!",
                10, 70, 20
            );
        }
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                startBookCeremony(player);
            }
        }, 1200L);
    }
    
    private void startBookCeremony(Player player) {
        player.sendMessage("§d§l✨ The magic ceremony begins in 10 seconds! ✨");
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            protectedPlayers.add(player.getUniqueId());
            
            ParticleManager particleManager = new ParticleManager(plugin);
            particleManager.startCircleEffect(player, () -> {
                MagicBook randomBook = MagicBook.getRandomBook();
                player.getInventory().addItem(randomBook.createBook());
                protectedPlayers.remove(player.getUniqueId());
                player.sendMessage("§d§l✨ You received: " + randomBook.getDisplayName());
            });
        }, 200L);
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    public boolean isProtected(Player player) {
        return protectedPlayers.contains(player.getUniqueId());
    }
    
    public Set<UUID> getProtectedPlayers() {
        return protectedPlayers;
    }
}
