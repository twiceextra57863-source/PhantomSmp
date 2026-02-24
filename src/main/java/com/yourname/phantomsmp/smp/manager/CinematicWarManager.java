package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class CinematicWarManager {
    
    private final PhantomSMP plugin;
    private final Random random = new Random();
    private final Map<UUID, Boolean> inCinematic = new HashMap<>();
    
    public CinematicWarManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startCinematicFight(Player player, List<LivingEntity> targets, String bookType, int level, Runnable onComplete) {
        inCinematic.put(player.getUniqueId(), true);
        
        // Freeze player during cinematic
        player.setWalkSpeed(0);
        player.setFlySpeed(0);
        
        // Play epic intro sound
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        new BukkitRunnable() {
            int phase = 0;
            int tick = 0;
            
            @Override
            public void run() {
                if (phase >= 5) {
                    // End cinematic
                    player.setWalkSpeed(0.2f);
                    player.setFlySpeed(0.1f);
                    inCinematic.remove(player.getUniqueId());
                    onComplete.run();
                    cancel();
                    return;
                }
                
                switch(phase) {
                    case 0: // Preparation phase
                        if (tick == 0) {
                            player.sendTitle("§d⚡", "§fCinematic Battle Initiated!", 0, 20, 0);
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                        }
                        
                        // Energy build-up around player
                        for (int i = 0; i < 360; i += 30) {
                            double angle = Math.toRadians(i + tick * 10);
                            double radius = 2.0 + Math.sin(tick * 0.1) * 0.5;
                            
                            double x = radius * Math.cos(angle);
                            double z = radius * Math.sin(angle);
                            
                            player.getWorld().spawnParticle(
                                Particle.END_ROD,
                                player.getLocation().clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                                2, 0, 0, 0, 0
                            );
                        }
                        
                        if (tick >= 20) {
                            phase = 1;
                            tick = 0;
                        }
                        break;
                        
                    case 1: // Target locking
                        if (tick == 0) {
                            player.sendTitle("§5🎯", "§fTargets Locked!", 0, 10, 0);
                        }
                        
                        // Draw lines to targets
                        for (LivingEntity target : targets) {
                            if (target.isDead()) continue;
                            
                            Location playerLoc = player.getLocation().add(0, 1, 0);
                            Location targetLoc = target.getLocation().add(0, 1, 0);
                            
                            Vector direction = targetLoc.toVector().subtract(playerLoc.toVector());
                            double length = direction.length();
                            direction.normalize();
                            
                            for (double d = 0; d < length; d += 0.5) {
                                Location beamLoc = playerLoc.clone().add(direction.clone().multiply(d));
                                
                                player.getWorld().spawnParticle(
                                    Particle.ELECTRIC_SPARK,
                                    beamLoc,
                                    1, 0.1, 0.1, 0.1, 0.01
                                );
                            }
                        }
                        
                        if (tick >= 30) {
                            phase = 2;
                            tick = 0;
                        }
                        break;
                        
                    case 2: // Attack phase - depends on book type
                        if (tick == 0) {
                            player.sendTitle("§6⚔️", "§fExecute!", 0, 10, 0);
                        }
                        
                        // Execute book-specific attack pattern
                        executeAttackPattern(player, targets, bookType, level, tick);
                        
                        if (tick >= 40) {
                            phase = 3;
                            tick = 0;
                        }
                        break;
                        
                    case 3: // Impact phase
                        if (tick == 0) {
                            player.sendTitle("§e💥", "§fImpact!", 0, 10, 0);
                            
                            // Final damage and effects
                            for (LivingEntity target : targets) {
                                if (target.isDead()) continue;
                                
                                // Calculate damage based on level
                                int damage = level * 4;
                                target.damage(damage, player);
                                
                                // Impact effect
                                target.getWorld().spawnParticle(
                                    Particle.EXPLOSION,
                                    target.getLocation().add(0, 1, 0),
                                    10, 0.5, 0.5, 0.5, 0.1
                                );
                                
                                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                            }
                        }
                        
                        if (tick >= 20) {
                            phase = 4;
                            tick = 0;
                        }
                        break;
                        
                    case 4: // Victory pose
                        if (tick == 0) {
                            player.sendTitle("§a🏆", "§fVictory!", 0, 30, 10);
                            
                            // Victory effects
                            for (int i = 0; i < 10; i++) {
                                player.getWorld().spawnParticle(
                                    Particle.FIREWORK,
                                    player.getLocation().add(0, 2, 0),
                                    20, 1, 1, 1, 0.1
                                );
                            }
                        }
                        
                        if (tick >= 30) {
                            phase = 5;
                        }
                        break;
                }
                
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void executeAttackPattern(Player player, List<LivingEntity> targets, String bookType, int level, int tick) {
        switch(bookType) {
            case "storm":
                // Lightning barrage
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    if (tick % 5 == 0) {
                        target.getWorld().strikeLightningEffect(target.getLocation());
                    }
                }
                break;
                
            case "shadow":
                // Shadow clones attack
                for (int i = 0; i < 3; i++) {
                    double angle = Math.toRadians(tick * 10 + i * 120);
                    double radius = 3.0;
                    
                    double x = player.getLocation().getX() + radius * Math.cos(angle);
                    double z = player.getLocation().getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SMOKE,
                        x, player.getLocation().getY() + 1, z,
                        10, 0.2, 0.2, 0.2, 0.01
                    );
                }
                break;
                
            case "flame":
                // Fire tornado
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 360; i += 30) {
                        double angle = Math.toRadians(i + tick * 10);
                        double x = target.getLocation().getX() + 2 * Math.cos(angle);
                        double z = target.getLocation().getZ() + 2 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            x, target.getLocation().getY() + 1 + Math.sin(angle) * 0.5, z,
                            2, 0, 0, 0, 0.01
                        );
                    }
                }
                break;
                
            case "frost":
            case "ice":
                // Ice spikes
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 5; i++) {
                        Location spikeLoc = target.getLocation().clone().add(
                            random.nextDouble() * 4 - 2,
                            0,
                            random.nextDouble() * 4 - 2
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.SNOWFLAKE,
                            spikeLoc.add(0, 1, 0),
                            15, 0.3, 0.5, 0.3, 0
                        );
                    }
                }
                break;
                
            case "dragon":
                // Dragon breath barrage
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    
                    for (double d = 0; d < 10; d += 0.5) {
                        Location breathLoc = player.getLocation().clone().add(direction.clone().multiply(d));
                        
                        player.getWorld().spawnParticle(
                            Particle.DRAGON_BREATH,
                            breathLoc,
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                break;
                
            case "void":
                // Void rifts
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 5; i++) {
                        Location riftLoc = target.getLocation().clone().add(
                            random.nextDouble() * 4 - 2,
                            random.nextDouble() * 2,
                            random.nextDouble() * 4 - 2
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            riftLoc,
                            20, 0.3, 0.3, 0.3, 0.5
                        );
                    }
                }
                break;
                
            case "gravity":
                // Gravity crush
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    Vector pull = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
                    target.setVelocity(pull.multiply(0.5));
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        target.getLocation().add(0, 1, 0),
                        10, 0.3, 0.3, 0.3, 0
                    );
                }
                break;
                
            case "phantom":
                // Phantom strikes
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        target.getLocation().add(0, 1, 0),
                        20, 0.3, 0.3, 0.3, 0.01
                    );
                }
                break;
                
            case "dawn":
                // Holy light
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        target.getLocation().add(0, 2, 0),
                        30, 0.5, 0.5, 0.5, 0
                    );
                }
                break;
                
            case "terra":
                // Earth shaker
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    target.setVelocity(new Vector(0, 1, 0));
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        target.getLocation(),
                        30, 0.5, 0.5, 0.5, 0,
                        org.bukkit.Material.STONE.createBlockData()
                    );
                }
                break;
                
            case "wind":
                // Wind blast
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    Vector away = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    target.setVelocity(away.multiply(1.5));
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        target.getLocation().add(0, 1, 0),
                        20, 0.3, 0.3, 0.3, 0.02
                    );
                }
                break;
                
            case "time":
                // Time slow
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    target.setVelocity(new Vector(0, 0, 0));
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        target.getLocation().add(0, 1, 0),
                        20, 0.3, 0.3, 0.3, 0
                    );
                }
                break;
                
            case "soul":
                // Soul drain
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    Location targetLoc = target.getLocation().add(0, 1, 0);
                    Location playerLoc = player.getLocation().add(0, 1, 0);
                    
                    Vector direction = playerLoc.toVector().subtract(targetLoc.toVector());
                    double length = direction.length();
                    direction.normalize();
                    
                    for (double d = 0; d < length; d += 0.3) {
                        Location beamLoc = targetLoc.clone().add(direction.clone().multiply(d));
                        
                        player.getWorld().spawnParticle(
                            Particle.SOUL,
                            beamLoc,
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                break;
                
            case "crystal":
                // Crystal shards
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 8; i++) {
                        double angle = Math.toRadians(i * 45 + tick * 5);
                        double x = target.getLocation().getX() + 2 * Math.cos(angle);
                        double z = target.getLocation().getZ() + 2 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            x, target.getLocation().getY() + 1, z,
                            5, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                break;
                
            case "thunder":
                // Thunder storm
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 3; i++) {
                        target.getWorld().strikeLightningEffect(target.getLocation());
                    }
                }
                break;
                
            case "pyro":
                // Fire storm
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 360; i += 30) {
                        double angle = Math.toRadians(i + tick * 8);
                        double x = target.getLocation().getX() + 3 * Math.cos(angle);
                        double z = target.getLocation().getZ() + 3 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            x, target.getLocation().getY() + 1, z,
                            5, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                break;
                
            case "spirit":
                // Spirit wolves
                for (int i = 0; i < 3; i++) {
                    Location spawnLoc = player.getLocation().add(
                        random.nextDouble() * 4 - 2,
                        0,
                        random.nextDouble() * 4 - 2
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        spawnLoc.add(0, 1, 0),
                        15, 0.3, 0.3, 0.3, 0.01
                    );
                }
                break;
                
            case "necro":
                // Undead rising
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        target.getLocation().add(0, 1, 0),
                        25, 0.4, 0.4, 0.4, 0.02
                    );
                }
                break;
                
            case "seraph":
                // Angelic wings
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int w = 0; w < 5; w++) {
                        double progress = w / 4.0;
                        double angle = Math.toRadians(45 * progress);
                        
                        double x = target.getLocation().getX() + 2 * Math.cos(angle);
                        double z = target.getLocation().getZ() + 2 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            x, target.getLocation().getY() + 1 + progress, z,
                            3, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                break;
                
            case "abyss":
                // Dark void
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        target.getLocation().add(0, 1, 0),
                        20, 0.4, 0.4, 0.4, 0.02
                    );
                }
                break;
                
            case "chaos":
                // Random chaos
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    Particle[] particles = {
                        Particle.FLAME, Particle.ELECTRIC_SPARK, Particle.SOUL,
                        Particle.END_ROD, Particle.GLOW, Particle.SNOWFLAKE,
                        Particle.DRAGON_BREATH, Particle.PORTAL, Particle.CLOUD
                    };
                    
                    player.getWorld().spawnParticle(
                        particles[random.nextInt(particles.length)],
                        target.getLocation().add(0, 1, 0),
                        15, 0.3, 0.3, 0.3, 0.02
                    );
                }
                break;
                
            case "judge":
                // Divine judgment
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    target.getWorld().strikeLightningEffect(target.getLocation());
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        target.getLocation().add(0, 2, 0),
                        30, 0.5, 0.5, 0.5, 0
                    );
                }
                break;
                
            case "dream":
                // Dream catcher
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    player.getWorld().spawnParticle(
                        Particle.WAX_OFF,
                        target.getLocation().add(0, 1, 0),
                        15, 0.3, 0.3, 0.3, 0
                    );
                }
                break;
                
            case "fear":
                // Terror
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    Vector away = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    target.setVelocity(away.multiply(1.2));
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        target.getLocation().add(0, 1, 0),
                        20, 0.3, 0.3, 0.3, 0.02
                    );
                }
                break;
                
            case "aurora":
                // Northern lights
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 360; i += 30) {
                        double angle = Math.toRadians(i + tick * 3);
                        double x = target.getLocation().getX() + 4 * Math.cos(angle);
                        double z = target.getLocation().getZ() + 4 * Math.sin(angle);
                        double y = target.getLocation().getY() + 2 + Math.sin(angle) * 1.5;
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            x, y, z,
                            2, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                break;
                
            case "star":
                // Meteor shower
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 5; i++) {
                        Location meteorLoc = target.getLocation().clone().add(
                            random.nextDouble() * 6 - 3,
                            10,
                            random.nextDouble() * 6 - 3
                        );
                        
                        for (double y = 10; y > 0; y -= 0.5) {
                            player.getWorld().spawnParticle(
                                Particle.FIREWORK,
                                meteorLoc.clone().subtract(0, 10 - y, 0),
                                1, 0, 0, 0, 0
                            );
                        }
                    }
                }
                break;
                
            case "inferno":
                // Hellfire
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 360; i += 20) {
                        double angle = Math.toRadians(i + tick * 8);
                        double x = target.getLocation().getX() + 3 * Math.cos(angle);
                        double z = target.getLocation().getZ() + 3 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            x, target.getLocation().getY() + 1, z,
                            5, 0.2, 0.2, 0.2, 0.02
                        );
                    }
                }
                break;
                
            case "avalanche":
                // Snow storm
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    for (int i = 0; i < 10; i++) {
                        Location snowLoc = target.getLocation().clone().add(
                            random.nextDouble() * 6 - 3,
                            3,
                            random.nextDouble() * 6 - 3
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.SNOWFLAKE,
                            snowLoc,
                            10, 0.3, 0.3, 0.3, 0
                        );
                    }
                }
                break;
        }
    }
    
    public boolean isInCinematic(Player player) {
        return inCinematic.getOrDefault(player.getUniqueId(), false);
    }
}
