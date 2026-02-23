package com.yourname.smpstarter.manager;

import com.yourname.smpstarter.SMPStarter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleManager {
    
    private final SMPStarter plugin;
    
    public ParticleManager(SMPStarter plugin) {
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
                
                // Circle particles
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location particleLoc = center.clone().add(x, 0, z);
                    
                    // Electric particles
                    player.getWorld().spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        particleLoc,
                        1, 0.1, 0.1, 0.1, 0
                    );
                    
                    // Dust particles for color
                    player.getWorld().spawnParticle(
                        Particle.DUST,
                        particleLoc,
                        1, 0.1, 0.5, 0.1, 0,
                        new Particle.DustOptions(
                            org.bukkit.Color.fromRGB(
                                255, 
                                100 + (int)(Math.sin(ticks * 0.1) * 100), 
                                255
                            ), 1
                        )
                    );
                }
                
                // Random sparks inside circle
                for (int i = 0; i < 10; i++) {
                    double randomAngle = Math.random() * Math.PI * 2;
                    double randomRadius = Math.random() * radius;
                    double x = randomRadius * Math.cos(randomAngle);
                    double z = randomRadius * Math.sin(randomAngle);
                    
                    Location sparkLoc = center.clone().add(x, Math.random() * 2, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        sparkLoc,
                        1, 0, 0, 0, 0.1
                    );
                }
                
                // Levitate player slowly
                if (ticks % 10 == 0) {
                    player.setVelocity(new Vector(0, 0.1, 0));
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 0.5f, 2);
                }
                
                // Title messages during ceremony
                if (ticks == 40) {
                    player.sendTitle("§d✨", "§fMagic is flowing...", 0, 40, 0);
                } else if (ticks == 80) {
                    player.sendTitle("§5✨", "§fPower is building...", 0, 40, 0);
                } else if (ticks == 120) {
                    player.sendTitle("§6✨", "§fAlmost there...", 0, 40, 0);
                } else if (ticks == 160) {
                    player.sendTitle("§e✨", "§fReceiving your book!", 0, 40, 0);
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
