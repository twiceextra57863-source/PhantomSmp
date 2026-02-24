package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UltimateHoldParticleManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();
    private int globalTick = 0;
    
    public UltimateHoldParticleManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startHoldParticles(Player player, MagicBook book, int level) {
        stopHoldParticles(player);
        
        BukkitRunnable task = new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!isHoldingPhantomBook(player)) {
                    stopHoldParticles(player);
                    return;
                }
                
                // Only run particles every 3 ticks for better performance
                if (tick % 3 == 0) {
                    // Get unique particle shape based on book and level
                    switch(book.getAbilityKey().toLowerCase()) {
                        case "storm": stormParticles(player, tick, level); break;
                        case "shadow": shadowParticles(player, tick, level); break;
                        case "flame": flameParticles(player, tick, level); break;
                        case "frost": frostParticles(player, tick, level); break;
                        case "dragon": dragonParticles(player, tick, level); break;
                        case "void": voidParticles(player, tick, level); break;
                        case "life": lifeParticles(player, tick, level); break;
                        case "gravity": gravityParticles(player, tick, level); break;
                        case "phantom": phantomParticles(player, tick, level); break;
                        case "dawn": dawnParticles(player, tick, level); break;
                        case "terra": terraParticles(player, tick, level); break;
                        case "wind": windParticles(player, tick, level); break;
                        case "time": timeParticles(player, tick, level); break;
                        case "soul": soulParticles(player, tick, level); break;
                        case "crystal": crystalParticles(player, tick, level); break;
                        case "thunder": thunderParticles(player, tick, level); break;
                        case "ice": iceParticles(player, tick, level); break;
                        case "pyro": pyroParticles(player, tick, level); break;
                        case "spirit": spiritParticles(player, tick, level); break;
                        case "necro": necroParticles(player, tick, level); break;
                        case "seraph": seraphParticles(player, tick, level); break;
                        case "abyss": abyssParticles(player, tick, level); break;
                        case "chaos": chaosParticles(player, tick, level); break;
                        case "judge": judgeParticles(player, tick, level); break;
                        case "dream": dreamParticles(player, tick, level); break;
                        case "fear": fearParticles(player, tick, level); break;
                        case "aurora": auroraParticles(player, tick, level); break;
                        case "star": starParticles(player, tick, level); break;
                        case "inferno": infernoParticles(player, tick, level); break;
                        case "avalanche": avalancheParticles(player, tick, level); break;
                    }
                }
                
                tick++;
                globalTick++;
            }
        };
        
        task.runTaskTimer(plugin, 0L, 1L);
        activeTasks.put(player.getUniqueId(), task);
    }
    
    public void stopHoldParticles(Player player) {
        if (activeTasks.containsKey(player.getUniqueId())) {
            activeTasks.get(player.getUniqueId()).cancel();
            activeTasks.remove(player.getUniqueId());
        }
    }
    
    private boolean isHoldingPhantomBook(Player player) {
        if (player.getInventory().getItemInMainHand() != null &&
            player.getInventory().getItemInMainHand().hasItemMeta()) {
            return true;
        }
        return false;
    }
    
    // ========== 30 OPTIMIZED PARTICLE SHAPES ==========
    
    // 1. STORM - Lightning Ring (8 particles, every 3 ticks)
    private void stormParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle) * 0.3;
            
            player.getWorld().spawnParticle(
                Particle.ELECTRIC_SPARK,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    loc.clone().add(x * 1.3, y + 0.3, z * 1.3),
                    1, 0, 0, 0, 0.01
                );
            }
            
            if (level >= 3 && tick % 15 == 0) {
                player.getWorld().spawnParticle(
                    Particle.FLASH,
                    loc.clone().add(x * 1.8, y + 0.5, z * 1.8),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 2. SHADOW - Dark Ring
    private void shadowParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 6);
            double radius = 1.6;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.cos(angle) * 0.3;
            
            player.getWorld().spawnParticle(
                Particle.SMOKE,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.8, y + 0.2, z * 0.8),
                    1, 0, 0, 0, 0.01
                );
            }
            
            if (level >= 3 && tick % 12 == 0) {
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    loc.clone().add(x * 0.5, y + 0.4, z * 0.5),
                    1, 0, 0, 0, 0.05
                );
            }
        }
    }
    
    // 3. FLAME - Fire Ring
    private void flameParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 8);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle * 2) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.FLAME,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.LAVA,
                    loc.clone().add(x * 0.7, y - 0.1, z * 0.7),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 3 && tick % 10 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    loc.clone().add(x * 1.2, y + 0.2, z * 1.2),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 4. FROST - Snowflake Ring
    private void frostParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 4);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.cos(angle * 2) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.ITEM_SNOWBALL,
                    loc.clone().add(x * 0.8, y + 0.2, z * 0.8),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 5. DRAGON - Dragon Scale Ring
    private void dragonParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.8;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle * 3) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.DRAGON_BREATH,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 5 == 0) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    loc.clone().add(x * 1.2, y + 0.2, z * 1.2),
                    1, 0, 0, 0, 0.01
                );
            }
            
            if (level >= 3 && tick % 10 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    loc.clone().add(x * 1.5, y + 0.4, z * 1.5),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 6. VOID - Portal Ring
    private void voidParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 7);
            double radius = 1.5 + Math.sin(tick * 0.1) * 0.2;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.cos(angle * 2) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.1
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.6, y + 0.2, z * 0.6),
                    1, 0, 0, 0, 0.01
                );
            }
            
            if (level >= 3 && tick % 12 == 0) {
                player.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    loc.clone().add(x * 0.3, y + 0.3, z * 0.3),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 7. LIFE - Heart Ring
    private void lifeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 60) {
            double angle = Math.toRadians(i + tick * 3);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.HEART,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    loc.clone().add(x * 0.7, 0.8, z * 0.7),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 3 && tick % 10 == 0) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x * 1.2, 0.9, z * 1.2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 8. GRAVITY - Orbiting Ring
    private void gravityParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int orbit = 0; orbit < Math.min(level, 2); orbit++) {
            double orbitAngle = tick * 0.05 * (orbit + 1);
            
            for (int i = 0; i < 360; i += 60) {
                double angle = Math.toRadians(i + tick * 4);
                double radius = 1.2 + orbit * 0.4;
                
                double x = radius * Math.cos(angle + orbitAngle);
                double z = radius * Math.sin(angle + orbitAngle);
                double y = 1.0 + Math.sin(angle) * 0.2;
                
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 9. PHANTOM - Soul Ring
    private void phantomParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.6;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle * 2) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.7, y + 0.2, z * 0.7),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 10. DAWN - Light Ring
    private void dawnParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i);
            double radius = 1.5 + Math.sin(tick * 0.1) * 0.2;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    loc.clone().add(x * 1.2, 0.8, z * 1.2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 11. TERRA - Earth Ring
    private void terraParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 4);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.BLOCK,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0,
                org.bukkit.Material.DIRT.createBlockData()
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.FALLING_DUST,
                    loc.clone().add(x * 0.8, 0.8, z * 0.8),
                    1, 0, 0, 0, 0,
                    org.bukkit.Material.STONE.createBlockData()
                );
            }
        }
    }
    
    // 12. WIND - Cloud Ring
    private void windParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 60) {
            double angle = Math.toRadians(i + tick * 6);
            double radius = 1.8;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.CLOUD,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.GUST,
                    loc.clone().add(x * 1.2, y + 0.2, z * 1.2),
                    1, 0, 0, 0, 0.05
                );
            }
        }
    }
    
    // 13. TIME - Clock Ring
    private void timeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int hand = 0; hand < level; hand++) {
            double handAngle = Math.toRadians(tick * (hand + 1) * 3);
            double length = 1.0 + hand * 0.3;
            
            double x = length * Math.cos(handAngle);
            double z = length * Math.sin(handAngle);
            
            player.getWorld().spawnParticle(
                Particle.GLOW,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // 14. SOUL - Soul Ring
    private void soulParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle * 2) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    loc.clone().add(x * 0.6, y + 0.2, z * 0.6),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 15. CRYSTAL - Crystal Ring
    private void crystalParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle * 3) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    loc.clone().add(x * 0.8, y + 0.2, z * 0.8),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 16. THUNDER - Electric Ring
    private void thunderParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 8);
            double radius = 1.6;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.ELECTRIC_SPARK,
                loc.clone().add(x, 0.5 + Math.sin(angle) * 0.2, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.FLASH,
                    loc.clone().add(x * 1.3, 0.8, z * 1.3),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 17. ICE - Ice Ring
    private void iceParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 4);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                loc.clone().add(x, 0.5 + Math.cos(angle) * 0.2, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.ITEM_SNOWBALL,
                    loc.clone().add(x * 0.7, 0.7, z * 0.7),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 18. PYRO - Fire Ring
    private void pyroParticles(Player player, int tick, int level) {
        flameParticles(player, tick, level); // Same as flame
    }
    
    // 19. SPIRIT - Spirit Ring
    private void spiritParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 60) {
            double angle = Math.toRadians(i + tick * 4);
            double radius = 1.6;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1.0 + Math.sin(angle * 2) * 0.2;
            
            player.getWorld().spawnParticle(
                Particle.SCULK_SOUL,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2 && tick % 7 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    loc.clone().add(x * 0.7, y + 0.2, z * 0.7),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 20. NECRO - Death Ring
    private void necroParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, 0.5 + Math.sin(angle) * 0.2, z),
                1, 0, 0, 0, 0.02
            );
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.8, 0.7, z * 0.8),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 21. SERAPH - Angel Ring
    private void seraphParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int i = 0; i < 360; i += 60) {
            double angle = Math.toRadians(i + tick * 4);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    loc.clone().add(x * 1.2, 0.8, z * 1.2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 22. ABYSS - Dark Ring
    private void abyssParticles(Player player, int tick, int level) {
        shadowParticles(player, tick, level); // Similar to shadow
    }
    
    // 23. CHAOS - Random Ring
    private void chaosParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 4; i++) {
            double angle = Math.random() * Math.PI * 2;
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            Particle[] particles = {
                Particle.FLAME, Particle.ELECTRIC_SPARK, Particle.SOUL,
                Particle.END_ROD, Particle.GLOW, Particle.SNOWFLAKE
            };
            
            player.getWorld().spawnParticle(
                particles[(tick / 3) % particles.length],
                loc.clone().add(x, 0.7, z),
                1, 0, 0, 0, 0.01
            );
        }
    }
    
    // 24. JUDGE - Scale Ring
    private void judgeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int side = -1; side <= 1; side += 2) {
            double x = side * 1.2;
            
            for (double y = 0.5; y <= 1.5; y += 0.5) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, y, 0),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 2 && tick % 10 == 0) {
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    loc.clone().add(x * 1.3, 1.0, 0.3),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 25. DREAM - Dream Ring
    private void dreamParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        for (int i = 0; i < 360; i += 60) {
            double angle = Math.toRadians(i + tick * 3);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.WAX_OFF,
                loc.clone().add(x, 0.6 + Math.sin(angle) * 0.2, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2 && tick % 8 == 0) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x * 0.7, 0.8, z * 0.7),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 26. FEAR - Fear Ring
    private void fearParticles(Player player, int tick, int level) {
        shadowParticles(player, tick, level); // Similar to shadow
    }
    
    // 27. AURORA - Light Ring
    private void auroraParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 2, 0);
        
        for (int i = 0; i < 360; i += 60) {
            double angle = Math.toRadians(i + tick * 2);
            double radius = 2.0;
            double wave = Math.sin(angle + tick * 0.1) * 0.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = wave + 1.0;
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // 28. STAR - Star Ring
    private void starParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72 + tick * 2);
            double radius = 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.FIREWORK,
                loc.clone().add(x, 0.7, z),
                1, 0, 0, 0, 0.01
            );
        }
    }
    
    // 29. INFERNO - Hell Ring
    private void infernoParticles(Player player, int tick, int level) {
        flameParticles(player, tick, level); // Similar to flame but more intense
    }
    
    // 30. AVALANCHE - Snow Ring
    private void avalancheParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + tick * 3);
            double radius = 1.8;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (double y = 0; y < 2; y += 0.5) {
                player.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 2 && tick % 6 == 0) {
                player.getWorld().spawnParticle(
                    Particle.ITEM_SNOWBALL,
                    loc.clone().add(x * 0.8, 1.0, z * 0.8),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
}
