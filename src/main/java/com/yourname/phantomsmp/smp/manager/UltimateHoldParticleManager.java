package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
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
                
                // Get unique particle shape based on book and level
                switch(book.getAbilityKey().toLowerCase()) {
                    case "storm":
                        stormParticles(player, tick, level);
                        break;
                    case "shadow":
                        shadowParticles(player, tick, level);
                        break;
                    case "flame":
                        flameParticles(player, tick, level);
                        break;
                    case "frost":
                        frostParticles(player, tick, level);
                        break;
                    case "dragon":
                        dragonParticles(player, tick, level);
                        break;
                    case "void":
                        voidParticles(player, tick, level);
                        break;
                    case "life":
                        lifeParticles(player, tick, level);
                        break;
                    case "gravity":
                        gravityParticles(player, tick, level);
                        break;
                    case "phantom":
                        phantomParticles(player, tick, level);
                        break;
                    case "dawn":
                        dawnParticles(player, tick, level);
                        break;
                    case "terra":
                        terraParticles(player, tick, level);
                        break;
                    case "wind":
                        windParticles(player, tick, level);
                        break;
                    case "time":
                        timeParticles(player, tick, level);
                        break;
                    case "soul":
                        soulParticles(player, tick, level);
                        break;
                    case "crystal":
                        crystalParticles(player, tick, level);
                        break;
                    case "thunder":
                        thunderParticles(player, tick, level);
                        break;
                    case "ice":
                        iceParticles(player, tick, level);
                        break;
                    case "pyro":
                        pyroParticles(player, tick, level);
                        break;
                    case "spirit":
                        spiritParticles(player, tick, level);
                        break;
                    case "necro":
                        necroParticles(player, tick, level);
                        break;
                    case "seraph":
                        seraphParticles(player, tick, level);
                        break;
                    case "abyss":
                        abyssParticles(player, tick, level);
                        break;
                    case "chaos":
                        chaosParticles(player, tick, level);
                        break;
                    case "judge":
                        judgeParticles(player, tick, level);
                        break;
                    case "dream":
                        dreamParticles(player, tick, level);
                        break;
                    case "fear":
                        fearParticles(player, tick, level);
                        break;
                    case "aurora":
                        auroraParticles(player, tick, level);
                        break;
                    case "star":
                        starParticles(player, tick, level);
                        break;
                    case "inferno":
                        infernoParticles(player, tick, level);
                        break;
                    case "avalanche":
                        avalancheParticles(player, tick, level);
                        break;
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
    
    // ========== 30 UNIQUE ANIMATED SHAPES ==========
    
    // 1. STORM - Tornado Shape
    private void stormParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += (15 / Math.max(1, level))) {
            double angle = Math.toRadians(i + tick * 5);
            double radius = 1.5 + Math.sin(tick * 0.1) * 0.5;
            double yOffset = Math.sin(angle + tick) * 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (int h = 0; h < 3; h++) {
                double y = yOffset + h * 0.8;
                
                player.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0.02
                );
                
                if (level >= 2) {
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        loc.clone().add(x * 1.5, y, z * 1.5),
                        1, 0, 0, 0, 0.01
                    );
                }
                
                if (level >= 3) {
                    player.getWorld().spawnParticle(
                        Particle.FLASH,
                        loc.clone().add(x * 2, y + 1, z * 2),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
    
    // 2. SHADOW - Spiral of Darkness
    private void shadowParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i + tick * 8);
            double radius = 2.0;
            double spiral = tick * 0.1;
            
            double x = radius * Math.cos(angle + spiral);
            double z = radius * Math.sin(angle + spiral);
            double y = 1 + Math.sin(angle + tick) * 1;
            
            player.getWorld().spawnParticle(
                Particle.SMOKE,
                loc.clone().add(x, y, z),
                level, 0.1, 0.1, 0.1, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.5, y + 1, z * 0.5),
                    1, 0, 0, 0, 0.02
                );
            }
            
            if (level >= 3) {
                double x2 = radius * 0.5 * Math.cos(angle - spiral);
                double z2 = radius * 0.5 * Math.sin(angle - spiral);
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    loc.clone().add(x2, y + 0.5, z2),
                    2, 0, 0, 0, 0.1
                );
            }
        }
    }
    
    // 3. FLAME - Burning Circle with Fire Spirals
    private void flameParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += (10 / Math.max(1, level))) {
            double angle = Math.toRadians(i + tick * 10);
            double radius = 1.8 + Math.sin(tick * 0.2) * 0.3;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.sin(angle * 2 + tick) * 0.8;
            
            player.getWorld().spawnParticle(
                Particle.FLAME,
                loc.clone().add(x, y, z),
                level, 0.05, 0.05, 0.05, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.LAVA,
                    loc.clone().add(x * 0.7, y - 0.3, z * 0.7),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 3) {
                double x2 = (radius - 0.5) * Math.cos(angle + Math.PI);
                double z2 = (radius - 0.5) * Math.sin(angle + Math.PI);
                player.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    loc.clone().add(x2, y + 0.3, z2),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 4. FROST - Snowflake Crystal
    private void frostParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60 + tick * 2);
            double radius = 2.0;
            
            for (int r = 0; r < 3; r++) {
                double offset = r * 0.7;
                double x = (radius - offset) * Math.cos(angle);
                double z = (radius - offset) * Math.sin(angle);
                double y = 1 + Math.sin(angle + tick) * 0.5;
                
                player.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0
                );
                
                if (level >= 2) {
                    for (int b = -1; b <= 1; b += 2) {
                        double bx = x * 0.7 + b * 0.5 * Math.cos(angle + Math.PI/2);
                        double bz = z * 0.7 + b * 0.5 * Math.sin(angle + Math.PI/2);
                        player.getWorld().spawnParticle(
                            Particle.ITEM_SNOWBALL,
                            loc.clone().add(bx, y, bz),
                            1, 0, 0, 0, 0
                        );
                    }
                }
            }
        }
    }
    
    // 5. DRAGON - Dragon Head Shape
    private void dragonParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 2, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i + tick * 3);
            double radius = 2.0;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.sin(angle * 2) * 0.5;
            
            player.getWorld().spawnParticle(
                Particle.DRAGON_BREATH,
                loc.clone().add(x, y, z),
                level, 0.1, 0.1, 0.1, 0.01
            );
            
            if (level >= 2) {
                double wingX = x * 1.5;
                double wingZ = z * 0.5;
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    loc.clone().add(wingX, y + 0.3, wingZ),
                    1, 0, 0, 0, 0.01
                );
            }
            
            if (level >= 3) {
                double breathX = x * 2.5;
                double breathZ = z * 2.5;
                player.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    loc.clone().add(breathX, y + 1, breathZ),
                    2, 0, 0, 0, 0.02
                );
            }
        }
    }
    
    // 6. VOID - Black Hole Spiral
    private void voidParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i + tick * 10);
            double radius = 0.5 + Math.abs(Math.sin(tick * 0.1)) * 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.sin(angle + tick) * 1;
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, y, z),
                level * 2, 0, 0, 0, 0.2
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.3, y + 0.5, z * 0.3),
                    1, 0, 0, 0, 0.01
                );
            }
            
            if (level >= 3) {
                double x2 = (radius + 0.8) * Math.cos(angle - Math.PI/2);
                double z2 = (radius + 0.8) * Math.sin(angle - Math.PI/2);
                player.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    loc.clone().add(x2, y - 0.3, z2),
                    1, 0, 0, 0, 0.02
                );
            }
        }
    }
    
    // 7. LIFE - Healing Heart Beat
    private void lifeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        double pulse = 1 + Math.sin(tick * 0.2) * 0.3;
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i);
            double radius = 1.5 * pulse;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.HEART,
                loc.clone().add(x, 0.5, z),
                level, 0.1, 0.1, 0.1, 0
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    loc.clone().add(x * 0.7, 1, z * 0.7),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 3) {
                double y2 = 1.5 + Math.sin(angle + tick) * 0.5;
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x * 1.2, y2, z * 1.2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 8. GRAVITY - Orbiting Spheres
    private void gravityParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int orbit = 0; orbit < level + 1; orbit++) {
            double orbitAngle = tick * 0.1 * (orbit + 1);
            
            for (int i = 0; i < 360; i += 45) {
                double angle = Math.toRadians(i + tick * 5);
                double radius = 1.0 + orbit * 0.5;
                
                double x = radius * Math.cos(angle + orbitAngle);
                double z = radius * Math.sin(angle + orbitAngle);
                double y = 1 + Math.sin(angle * 2 + tick) * 0.5;
                
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 9. PHANTOM - Ghostly Wraith
    private void phantomParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i + tick * 4);
            double radius = 1.8;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.cos(angle * 2) * 0.8;
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, y, z),
                level, 0.1, 0.1, 0.1, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.6, y + 0.5, z * 0.6),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 10. DAWN - Rising Sun
    private void dawnParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 2, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i);
            double radius = 1.5 + Math.sin(tick * 0.1) * 0.3;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (double y = 0; y < 2; y += 0.5) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 2) {
                double x2 = (radius + 0.5) * Math.cos(angle + 0.5);
                double z2 = (radius + 0.5) * Math.sin(angle + 0.5);
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    loc.clone().add(x2, 1, z2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 11. TERRA - Rotating Earth
    private void terraParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int lat = -90; lat <= 90; lat += 30) {
            double latRad = Math.toRadians(lat);
            double y = Math.sin(latRad) * 1.5 + 1;
            double r = Math.cos(latRad) * 1.5;
            
            for (int lon = 0; lon < 360; lon += 30) {
                double lonRad = Math.toRadians(lon + tick * 2);
                double x = r * Math.cos(lonRad);
                double z = r * Math.sin(lonRad);
                
                player.getWorld().spawnParticle(
                    Particle.BLOCK,
                    loc.clone().add(x, y, z),
                    1, 0, 0, 0, 0,
                    org.bukkit.Material.GRASS_BLOCK.createBlockData()
                );
                
                if (level >= 2) {
                    player.getWorld().spawnParticle(
                        Particle.FALLING_DUST,
                        loc.clone().add(x * 1.2, y + 0.3, z * 1.2),
                        1, 0, 0, 0, 0,
                        org.bukkit.Material.STONE.createBlockData()
                    );
                }
            }
        }
    }
    
    // 12. WIND - Flowing Stream
    private void windParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i + tick * 8);
            double radius = 2.0;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.sin(angle * 2 + tick) * 0.5;
            
            Vector direction = new Vector(x, 0, z).normalize();
            
            player.getWorld().spawnParticle(
                Particle.CLOUD,
                loc.clone().add(x, y, z),
                level, direction.getX() * 0.1, 0.1, direction.getZ() * 0.1, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.GUST,
                    loc.clone().add(x * 1.5, y + 0.5, z * 1.5),
                    1, 0.1, 0.1, 0.1, 0.1
                );
            }
        }
    }
    
    // 13. TIME - Clock Hands
    private void timeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int hand = 0; hand < 3; hand++) {
            double handAngle = Math.toRadians(tick * (hand + 1) * 2);
            double length = 1.0 + hand * 0.5;
            
            for (double d = 0; d < length; d += 0.3) {
                double x = d * Math.cos(handAngle);
                double z = d * Math.sin(handAngle);
                
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    loc.clone().add(x, 1, z),
                    1, 0, 0, 0, 0
                );
            }
            
            if (level >= 2) {
                double x2 = length * 1.2 * Math.cos(handAngle + 0.5);
                double z2 = length * 1.2 * Math.sin(handAngle + 0.5);
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x2, 1.3, z2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // 14. SOUL - Soul Vortex
    private void soulParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 15) {
            double angle = Math.toRadians(i + tick * 6);
            double radius = 1.2 + Math.sin(tick * 0.2) * 0.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.sin(angle * 3 + tick) * 1;
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, y, z),
                level, 0, 0, 0, 0.02
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    loc.clone().add(x * 0.5, y - 0.3, z * 0.5),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 15. CRYSTAL - Geometric Crystal
    private void crystalParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45 + tick * 3);
            double radius = 2.0;
            
            for (int layer = 0; layer < 3; layer++) {
                double yOffset = layer * 0.8;
                double layerRadius = radius - layer * 0.3;
                
                double x = layerRadius * Math.cos(angle);
                double z = layerRadius * Math.sin(angle);
                
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, yOffset + 0.5, z),
                    level, 0, 0, 0, 0
                );
                
                if (level >= 2) {
                    for (int c = 0; c < 8; c++) {
                        double cAngle = Math.toRadians(c * 45);
                        double cx = layerRadius * 0.7 * Math.cos(cAngle + tick * 0.1);
                        double cz = layerRadius * 0.7 * Math.sin(cAngle + tick * 0.1);
                        player.getWorld().spawnParticle(
                            Particle.GLOW,
                            loc.clone().add(cx, yOffset + 0.8, cz),
                            1, 0, 0, 0, 0
                        );
                    }
                }
            }
        }
    }
    
    // 16. THUNDER - Electric Field
    private void thunderParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i);
            double radius = 2.0;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (int j = 0; j < level * 2; j++) {
                double bx = x + (Math.random() - 0.5) * 0.5;
                double bz = z + (Math.random() - 0.5) * 0.5;
                
                player.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    loc.clone().add(bx, 1 + Math.random() * 2, bz),
                    1, 0, 0, 0, 0.05
                );
            }
        }
    }
    
    // 17. ICE - Ice Spikes
    private void iceParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            double radius = 2.0;
            
            for (int spike = 0; spike < level; spike++) {
                double height = 1 + spike * 0.5;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                
                for (double y = 0; y < height; y += 0.3) {
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        loc.clone().add(x * (1 - y/height), 1 + y, z * (1 - y/height)),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
    
    // 18. PYRO - Fire Storm
    private void pyroParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i + tick * 10);
            double radius = 1.5 + Math.sin(tick * 0.3) * 0.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (double y = 0; y < 2; y += 0.5) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    loc.clone().add(x, 1 + y + Math.sin(angle) * 0.3, z),
                    level, 0.1, 0.1, 0.1, 0.01
                );
            }
        }
    }
    
    // 19. SPIRIT - Spirit Orbs
    private void spiritParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i + tick * 3);
            double radius = 2.0;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.sin(angle * 2) * 0.5;
            
            player.getWorld().spawnParticle(
                Particle.SCULK_SOUL,
                loc.clone().add(x, y, z),
                level, 0.1, 0.1, 0.1, 0.01
            );
            
            if (level >= 2) {
                double x2 = radius * 0.6 * Math.cos(angle + Math.PI/2);
                double z2 = radius * 0.6 * Math.sin(angle + Math.PI/2);
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    loc.clone().add(x2, y + 0.3, z2),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 20. NECRO - Death's Scythe
    private void necroParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 2; i++) {
            double baseAngle = Math.toRadians(i * 180 + tick * 5);
            
            for (double r = 0; r < 2; r += 0.3) {
                double x = r * Math.cos(baseAngle);
                double z = r * Math.sin(baseAngle);
                
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    loc.clone().add(x, 1 + Math.sin(r * 2) * 0.5, z),
                    level, 0, 0, 0, 0.02
                );
                
                if (level >= 2) {
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        loc.clone().add(x * 1.3, 1.5 + Math.cos(r) * 0.3, z * 1.3),
                        1, 0, 0, 0, 0.02
                    );
                }
            }
        }
    }
    
    // 21. SERAPH - Angel Wings
    private void seraphParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int wing = -1; wing <= 1; wing += 2) {
            for (int i = 0; i < 5; i++) {
                double progress = i / 4.0;
                double angle = Math.toRadians(45 * progress);
                
                double x = wing * (1.5 + progress * 1.5);
                double z = Math.sin(angle) * 1.5;
                double y = Math.cos(angle) * 1.5 + 0.5;
                
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, y, z),
                    level, 0.1, 0.1, 0.1, 0
                );
                
                if (level >= 2) {
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        loc.clone().add(x * 0.8, y + 0.3, z * 0.8),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
    
    // 22. ABYSS - Void Eye
    private void abyssParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i);
            double radius = 2.0;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SCULK_SOUL,
                loc.clone().add(x, 1 + Math.sin(angle + tick) * 0.5, z),
                level, 0.1, 0.1, 0.1, 0.02
            );
            
            if (level >= 2) {
                double pupilX = radius * 0.3 * Math.cos(angle + tick * 0.1);
                double pupilZ = radius * 0.3 * Math.sin(angle + tick * 0.1);
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    loc.clone().add(pupilX, 1.2, pupilZ),
                    2, 0, 0, 0, 0.1
                );
            }
        }
    }
    
    // 23. CHAOS - Random Shapes
    private void chaosParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < level * 5; i++) {
            double angle = Math.random() * Math.PI * 2;
            double radius = Math.random() * 2.5;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = 1 + Math.random() * 2;
            
            Particle[] particles = {
                Particle.FLAME, Particle.ELECTRIC_SPARK, Particle.SOUL,
                Particle.END_ROD, Particle.GLOW, Particle.SNOWFLAKE,
                Particle.DRAGON_BREATH, Particle.PORTAL, Particle.CLOUD
            };
            
            player.getWorld().spawnParticle(
                particles[(int)(Math.random() * particles.length)],
                loc.clone().add(x, y, z),
                1, 0.1, 0.1, 0.1, 0.02
            );
        }
    }
    
    // 24. JUDGE - Scales of Justice
    private void judgeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        double leftScale = 1.0 + Math.sin(tick * 0.1) * 0.3;
        double rightScale = 1.0 - Math.sin(tick * 0.1) * 0.3;
        
        for (int i = -1; i <= 1; i += 2) {
            double scale = (i == -1) ? leftScale : rightScale;
            
            for (int j = 0; j < 360; j += 45) {
                double angle = Math.toRadians(j);
                double x = i * 1.5;
                double y = 1 + Math.sin(angle) * 0.5 * scale;
                double z = Math.cos(angle) * 0.5;
                
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x, y, z),
                    level, 0.05, 0.05, 0.05, 0
                );
            }
        }
    }
    
    // 25. DREAM - Dream Catcher
    private void dreamParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i + tick * 2);
            double radius = 2.0;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.WAX_OFF,
                loc.clone().add(x, 1 + Math.sin(angle) * 0.3, z),
                level, 0.1, 0.1, 0.1, 0
            );
            
            if (level >= 2) {
                for (int w = 0; w < 8; w++) {
                    double webAngle = Math.toRadians(w * 45);
                    double wx = (radius - 0.5) * Math.cos(webAngle);
                    double wz = (radius - 0.5) * Math.sin(webAngle);
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        loc.clone().add(wx, 1.2, wz),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
    
    // 26. FEAR - Terrifying Face
    private void fearParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        for (int eye = -1; eye <= 1; eye += 2) {
            double eyeX = eye * 0.8;
            double eyeY = 1.8;
            
            for (int i = 0; i < 360; i += 45) {
                double angle = Math.toRadians(i);
                double x = eyeX + Math.cos(angle) * 0.3;
                double z = Math.sin(angle) * 0.3;
                
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x, eyeY, z),
                    level, 0, 0, 0, 0.02
                );
            }
        }
        
        for (int i = -1; i <= 1; i += 2) {
            for (double m = -1; m <= 1; m += 0.5) {
                double x = m * 0.5;
                double y = 1.2;
                double z = i * 0.3;
                
                player.getWorld().spawnParticle(
                    Particle.SMOKE,
                    loc.clone().add(x, y, z),
                    level, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 27. AURORA - Northern Lights
    private void auroraParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 3, 0);
        
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i + tick * 2);
            double radius = 3.0;
            double wave = Math.sin(angle + tick * 0.2) * 1.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = wave + 1.5;
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, y, z),
                level, 0.1, 0.1, 0.1, 0
            );
        }
    }
    
    // 28. STAR - Constellation
    private void starParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 2, 0);
        
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72 + tick * 2);
            double nextAngle = Math.toRadians((i + 1) * 72 + tick * 2);
            
            for (double t = 0; t <= 1; t += 0.1) {
                double x = (2.0 * (1 - t) * Math.cos(angle) + 2.0 * t * Math.cos(nextAngle));
                double z = (2.0 * (1 - t) * Math.sin(angle) + 2.0 * t * Math.sin(nextAngle));
                
                player.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    loc.clone().add(x, 1, z),
                    level, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // 29. INFERNO - Hell Gate
    private void infernoParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1, 0);
        
        for (int i = 0; i < 360; i += 15) {
            double angle = Math.toRadians(i + tick * 8);
            double radius = 2.0 + Math.sin(tick * 0.2) * 0.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (double y = 0; y < 3; y += 0.5) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    loc.clone().add(x, y + 0.5, z),
                    level * 2, 0.1, 0.1, 0.1, 0.02
                );
                
                if (level >= 2) {
                    player.getWorld().spawnParticle(
                        Particle.LAVA,
                        loc.clone().add(x * 0.5, y + 0.8, z * 0.5),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
    
    // 30. AVALANCHE - Snow Storm
    private void avalancheParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 2, 0);
        
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i + tick * 3);
            double radius = 2.5;
            
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            for (double y = 0; y < 3; y += 0.3) {
                player.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
                    loc.clone().add(x, y, z),
                    level, 0.2, 0.2, 0.2, 0
                );
                
                if (level >= 2) {
                    player.getWorld().spawnParticle(
                        Particle.ITEM_SNOWBALL,
                        loc.clone().add(x * 0.7, y + 0.3, z * 0.7),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }
    }
}
