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
        float originalYaw = player.getLocation().getYaw();
        float originalPitch = player.getLocation().getPitch();
        
        // Freeze player
        player.setWalkSpeed(0);
        player.setFlySpeed(0);
        player.setAllowFlight(true);
        player.setFlying(true);
        
        // Protect player
        plugin.getTimerManager().getProtectedPlayers().add(player.getUniqueId());
        plugin.getGraceManager().addPlayer(player);
        
        // Show book popup
        showBookPopup(player, book);
        
        // Floating animation
        new BukkitRunnable() {
            int ticks = 0;
            double startY = player.getLocation().getY();
            double maxHeight = startY + 5.0; // Go up 5 blocks
            
            @Override
            public void run() {
                if (ticks >= 100) { // 5 seconds
                    // Float down slowly
                    player.setVelocity(new Vector(0, -0.2, 0));
                    
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        // Unfreeze player
                        player.setWalkSpeed(0.2f);
                        player.setFlySpeed(0.1f);
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        
                        // Teleport to exact original location with same rotation
                        Location finalLoc = originalLoc.clone();
                        finalLoc.setYaw(originalYaw);
                        finalLoc.setPitch(originalPitch);
                        player.teleport(finalLoc);
                        
                        // Remove protection
                        plugin.getTimerManager().getProtectedPlayers().remove(player.getUniqueId());
                        
                        // Complete ceremony
                        onComplete.run();
                        cancel();
                    }, 20L); // 1 second to float down
                    
                    return;
                }
                
                // Floating up animation
                if (ticks < 80) {
                    // Go up slowly
                    double progress = ticks / 80.0;
                    double targetY = startY + (5.0 * progress);
                    
                    Location newLoc = player.getLocation().clone();
                    newLoc.setY(targetY);
                    newLoc.setYaw(originalYaw);
                    newLoc.setPitch(originalPitch);
                    player.teleport(newLoc);
                    
                    // Gentle sway
                    double sway = Math.sin(ticks * 0.2) * 0.3;
                    player.setVelocity(new Vector(sway, 0.1, 0));
                }
                
                // Particle effects
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 2.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        player.getLocation().clone().add(x, 1, z),
                        1, 0, 0, 0, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        player.getLocation().clone().add(x, 2, z),
                        1, 0, 0, 0, 0.01
                    );
                }
                
                // Title animation
                if (ticks == 20) {
                    player.sendTitle("§d✨", "§fThe ceremony begins...", 0, 40, 0);
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.5f, 1.5f);
                } else if (ticks == 40) {
                    player.sendTitle("§5🔮", "§fYou rise with the magic", 0, 40, 0);
                } else if (ticks == 60) {
                    player.sendTitle("§6⚡", "§fPower flows through you", 0, 40, 0);
                } else if (ticks == 80) {
                    player.sendTitle("§e🌟", "§fYour book awaits!", 0, 40, 0);
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
        }, 40L);
        
        // Final title
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendTitle("§6" + book.getDisplayName(), "§ehas been awakened!", 10, 70, 20);
        }, 100L);
    }
    
    public void startSimpleCeremony(Player player, MagicBook book, Runnable onComplete) {
        // Simpler ceremony for auto-join
        player.sendMessage("§d§l✨ You received: " + book.getDisplayName());
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        onComplete.run();
    }
}
