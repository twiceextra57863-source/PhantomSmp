package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CeremonyManager {
    
    private final PhantomSMP plugin;
    
    public CeremonyManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startCeremony(Player player, MagicBook book, Runnable onComplete) {
        // Store original location
        Location originalLoc = player.getLocation().clone();
        
        // Freeze player
        player.setWalkSpeed(0);
        player.setFlySpeed(0);
        player.setAllowFlight(true);
        player.setFlying(true);
        
        // Protect player
        plugin.getTimerManager().getProtectedPlayers().add(player.getUniqueId());
        
        // Show book popup
        showBookPopup(player, book);
        
        // Ceremony effect
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks >= 100) { // 5 seconds
                    // Unfreeze player
                    player.setWalkSpeed(0.2f);
                    player.setFlySpeed(0.1f);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    
                    // Remove protection
                    plugin.getTimerManager().getProtectedPlayers().remove(player.getUniqueId());
                    
                    // Complete ceremony
                    onComplete.run();
                    cancel();
                    return;
                }
                
                // Keep player in place
                player.setVelocity(new Vector(0, 0, 0));
                player.teleport(originalLoc);
                
                // Title animation
                if (ticks == 20) {
                    player.sendTitle("§d✨", "§fThe ceremony begins...", 0, 40, 0);
                } else if (ticks == 40) {
                    player.sendTitle("§5🔮", "§fMagic flows through you", 0, 40, 0);
                } else if (ticks == 60) {
                    player.sendTitle("§6⚡", "§fPower is building", 0, 40, 0);
                } else if (ticks == 80) {
                    player.sendTitle("§e🌟", "§fAlmost there...", 0, 40, 0);
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void showBookPopup(Player player, MagicBook book) {
        // Send multiple messages for popup effect
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§d§l════════════════════════════");
            player.sendMessage("§d§l✨ " + book.getDisplayName() + " ✨");
            player.sendMessage("§7" + book.getDescription());
            player.sendMessage("§e⏱️ Cooldown: §f" + book.getCooldown() + "s");
            player.sendMessage("§d§l════════════════════════════");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }, 40L); // Show after 2 seconds
        
        // Title animation
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendTitle("§6" + book.getDisplayName(), "§ehas been awakened!", 10, 70, 20);
        }, 100L); // Show at end
    }
}
