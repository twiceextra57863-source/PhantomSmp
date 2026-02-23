package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleManager {
    
    private final PhantomSMP plugin;
    
    public ParticleManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startCircleEffect(Player player, Runnable onComplete) {
        Location center = player.getLocation().clone().add(0, 1, 0);
        
        new BukkitRunnable() {
            int ticks = 0;
            double radius = 2.0;
            
            @Override
            public void run() {
                if (ticks >= 200) { // 10 seconds
                    onComplete.run();
                    cancel();
                    return;
                }
                
                // Spiral effect
                for (int i = 0; i < 3; i++) {
                    double offset = ticks * 0.1 + (i * Math.PI * 2 / 3);
                    
                    // Outer circle - electric
                    for (int j = 0; j < 360; j += 30) {
                        double angle = Math.toRadians(j + ticks * 5);
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);
                        
                        Location particleLoc = center.clone().add(x, Math.sin(offset) * 0.5, z);
                        
                        player.getWorld().spawnParticle(
                            Particle.ELECTRIC_SPARK,
                            particleLoc,
                            1, 0, 0, 0, 0.01
                        );
                    }
                    
                    // Inner circle - colored dust
                    radius = 1.5 + Math.sin(ticks * 0.1) * 0.5;
                    
                    for (int j = 0; j < 360; j += 45) {
                        double angle = Math.toRadians(j + ticks * 10);
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);
                        
                        Location particleLoc = center.clone().add(x, 1 + Math.sin(angle + ticks) * 0.3, z);
                        
                        player.getWorld().spawnParticle(
                            Particle.DUST,
                            particleLoc,
                            1, 0, 0, 0, 0,
                            new Particle.DustOptions(
                                org.bukkit.Color.fromRGB(
                                    255, 
                                    100 + (int)(Math.sin(ticks * 0.1) * 100), 
                                    255
                                ), 1
                            )
                        );
                    }
                }
                
                // Levitate player slowly
                if (ticks % 10 == 0 && ticks < 160) {
                    player.setVelocity(new Vector(0, 0.1, 0));
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 0.3f, 2.0f);
                }
                
                // Title messages
                if (ticks == 40) {
                    player.sendTitle("§d✨", "§fThe magic begins...", 0, 40, 0);
                } else if (ticks == 80) {
                    player.sendTitle("§5⚡", "§fPower courses through you", 0, 40, 0);
                } else if (ticks == 120) {
                    player.sendTitle("§6🌟", "§fEmbrace the Phantom", 0, 40, 0);
                } else if (ticks == 160) {
                    player.sendTitle("§e📖", "§fYour artifact awaits!", 0, 40, 0);
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
