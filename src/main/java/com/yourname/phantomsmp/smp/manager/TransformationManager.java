package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TransformationManager {
    
    private final PhantomSMP plugin;
    
    public TransformationManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void playTransformation(Player player, MagicBook book, int newLevel) {
        Location loc = player.getLocation();
        
        // Level up message
        String levelName = getLevelName(newLevel);
        player.sendMessage("§d§l⚡ PHANTOM TRANSFORMATION ⚡");
        player.sendMessage("§eYour " + book.getDisplayName() + " §ehas evolved to " + levelName + "!");
        
        // Play transformation sound
        player.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        player.playSound(loc, Sound.ITEM_TOTEM_USE, 1.0f, 1.5f);
        
        // Screen shake effect
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(loc) <= 20) {
                p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 0.5f);
            }
        }
        
        // Transformation animation
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks >= 60) { // 3 seconds
                    
                    // Final explosion
                    player.getWorld().spawnParticle(
                        Particle.FLASH,
                        player.getLocation().add(0, 1, 0),
                        1, 0, 0, 0, 0
                    );
                    
                    // Title message
                    player.sendTitle(
                        "§d§lLEVEL " + newLevel,
                        "§e" + levelName,
                        10, 40, 20
                    );
                    
                    cancel();
                    return;
                }
                
                // Dragon god transformation effect (Goku style)
                double radius = 2.0 + Math.sin(ticks * 0.2) * 1.0;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = Math.sin(angle + ticks) * 2;
                    
                    // Aura particles based on level
                    if (newLevel == 3) {
                        // Max level - golden aura
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            player.getLocation().clone().add(x, y + 1, z),
                            2, 0, 0, 0, 0
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            player.getLocation().clone().add(x, y + 2, z),
                            1, 0, 0, 0, 0.01
                        );
                    } else if (newLevel == 2) {
                        // Level 2 - blue aura
                        player.getWorld().spawnParticle(
                            Particle.SOUL_FIRE_FLAME,
                            player.getLocation().clone().add(x, y + 1, z),
                            1, 0, 0, 0, 0.01
                        );
                    } else {
                        // Level 1 - basic aura
                        player.getWorld().spawnParticle(
                            Particle.ELECTRIC_SPARK,
                            player.getLocation().clone().add(x, y + 1, z),
                            1, 0, 0, 0, 0.01
                        );
                    }
                }
                
                // Levitate during transformation
                if (ticks < 40) {
                    player.setVelocity(new Vector(0, 0.1, 0));
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private String getLevelName(int level) {
        switch(level) {
            case 1: return "§7§lInitiate";
            case 2: return "§b§lAscended";
            case 3: return "§6§l§lGodly";
            default: return "§fUnknown";
        }
    }
}
