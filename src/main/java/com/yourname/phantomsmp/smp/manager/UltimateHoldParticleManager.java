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
                
                // Get unique particle shape based on book and level
                switch(book.getAbilityKey().toLowerCase()) {
                    case "sun": sunParticles(player, tick, level); break;
                    case "water": waterParticles(player, tick, level); break;
                    case "thunder": thunderParticles(player, tick, level); break;
                    case "flame": flameParticles(player, tick, level); break;
                    case "wind": windParticles(player, tick, level); break;
                    case "stone": stoneParticles(player, tick, level); break;
                    case "mist": mistParticles(player, tick, level); break;
                    case "beast": beastParticles(player, tick, level); break;
                    case "sound": soundParticles(player, tick, level); break;
                    case "serpent": serpentParticles(player, tick, level); break;
                    case "love": loveParticles(player, tick, level); break;
                    case "sovereign": sovereignParticles(player, tick, level); break;
                    case "demonking": demonKingParticles(player, tick, level); break;
                    case "beastlord": beastLordParticles(player, tick, level); break;
                    case "snow": snowParticles(player, tick, level); break;
                    case "limitless": limitlessParticles(player, tick, level); break;
                    case "shadows": shadowsParticles(player, tick, level); break;
                    case "disaster": disasterParticles(player, tick, level); break;
                    case "blood": bloodParticles(player, tick, level); break;
                    case "comedy": comedyParticles(player, tick, level); break;
                    case "spirit": spiritParticles(player, tick, level); break;
                    case "kame": kameParticles(player, tick, level); break;
                    case "instant": instantParticles(player, tick, level); break;
                    case "solar": solarParticles(player, tick, level); break;
                    case "galaxy": galaxyParticles(player, tick, level); break;
                    case "reaver": reaverParticles(player, tick, level); break;
                    case "eater": eaterParticles(player, tick, level); break;
                    case "starfall": starfallParticles(player, tick, level); break;
                    case "time": timeParticles(player, tick, level); break;
                    case "writer": writerParticles(player, tick, level); break;
                }
                
                tick++;
                globalTick++;
            }
        };
        
        task.runTaskTimer(plugin, 0L, 2L); // Run every 2 ticks for better performance
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
    
    // ========== SUN - Solar Ring with Flames ==========
    private void sunParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.8;
        int points = level == 1 ? 6 : (level == 2 ? 8 : 12);
        
        for (int i = 0; i < points; i++) {
            double angle = Math.toRadians((360 / points) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = Math.sin(angle + tick) * 0.3;
            
            player.getWorld().spawnParticle(
                Particle.FLAME,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    loc.clone().add(x * 1.2, y + 0.2, z * 1.2),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // ========== WATER - Flowing Waves ==========
    private void waterParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.5;
        int points = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < points; i++) {
            double angle = Math.toRadians((360 / points) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double wave = Math.sin(angle * 2 + tick) * 0.5;
            
            player.getWorld().spawnParticle(
                Particle.SPLASH,
                loc.clone().add(x, wave, z),
                1, 0, 0, 0, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.DRIPPING_WATER,
                    loc.clone().add(x * 0.8, wave + 0.3, z * 0.8),
                    1, 0, 0, 0, 0
                );
            }
        }
    }
    
    // ========== THUNDER - Electric Sparks ==========
    private void thunderParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.6;
        int bolts = level == 1 ? 3 : (level == 2 ? 4 : 6);
        
        for (int i = 0; i < bolts; i++) {
            double angle = Math.toRadians((360 / bolts) * i + tick * 4);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.ELECTRIC_SPARK,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.02
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    loc.clone().add(x * 0.5, 1.2, z * 0.5),
                    1, 0, 0, 0, 0.02
                );
            }
        }
    }
    
    // ========== FLAME - Fire Circle ==========
    private void flameParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.7;
        int flames = level == 1 ? 5 : (level == 2 ? 7 : 10);
        
        for (int i = 0; i < flames; i++) {
            double angle = Math.toRadians((360 / flames) * i + tick * 5);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double height = Math.sin(angle * 2) * 0.3;
            
            player.getWorld().spawnParticle(
                Particle.FLAME,
                loc.clone().add(x, height, z),
                2, 0, 0, 0, 0.01
            );
        }
    }
    
    // ========== WIND - Swirling Clouds ==========
    private void windParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        double radius = 1.9;
        int streams = level == 1 ? 3 : (level == 2 ? 4 : 6);
        
        for (int i = 0; i < streams; i++) {
            double angle = Math.toRadians((360 / streams) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = Math.cos(angle + tick) * 0.5;
            
            player.getWorld().spawnParticle(
                Particle.CLOUD,
                loc.clone().add(x, y, z),
                2, 0.1, 0.1, 0.1, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.GUST,
                    loc.clone().add(x * 1.3, y + 0.2, z * 1.3),
                    1, 0, 0, 0, 0.05
                );
            }
        }
    }
    
    // ========== STONE - Rotating Rocks ==========
    private void stoneParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.6;
        int rocks = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < rocks; i++) {
            double angle = Math.toRadians((360 / rocks) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.BLOCK,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0,
                org.bukkit.Material.STONE.createBlockData()
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.FALLING_DUST,
                    loc.clone().add(x * 1.1, 0.8, z * 1.1),
                    1, 0, 0, 0, 0,
                    org.bukkit.Material.STONE.createBlockData()
                );
            }
        }
    }
    
    // ========== MIST - Fog Ring ==========
    private void mistParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 2.0;
        int clouds = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < clouds; i++) {
            double angle = Math.toRadians((360 / clouds) * i + tick * 1);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.CLOUD,
                loc.clone().add(x, 0.5, z),
                3, 0.2, 0.2, 0.2, 0.01
            );
        }
    }
    
    // ========== BEAST - Claw Marks ==========
    private void beastParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.8;
        int claws = level == 1 ? 3 : (level == 2 ? 4 : 6);
        
        for (int i = 0; i < claws; i++) {
            double angle = Math.toRadians((360 / claws) * i + tick * 4);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SWEEP_ATTACK,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== SOUND - Sonic Waves ==========
    private void soundParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.8;
        int waves = level == 1 ? 3 : (level == 2 ? 4 : 6);
        
        for (int i = 0; i < waves; i++) {
            double angle = Math.toRadians((360 / waves) * i + tick * 5);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SONIC_BOOM,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== SERPENT - Coiling Snake ==========
    private void serpentParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.7;
        int coils = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < coils; i++) {
            double angle = Math.toRadians((360 / coils) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = Math.sin(angle * 2 + tick) * 0.4;
            
            player.getWorld().spawnParticle(
                Particle.SCULK_SOUL,
                loc.clone().add(x, y, z),
                1, 0, 0, 0, 0.01
            );
        }
    }
    
    // ========== LOVE - Floating Hearts ==========
    private void loveParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        double radius = 1.6;
        int hearts = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < hearts; i++) {
            double angle = Math.toRadians((360 / hearts) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.HEART,
                loc.clone().add(x, 0.3, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== SHADOW SOVEREIGN - Shadow Spirals ==========
    private void sovereignParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.9;
        int shadows = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < shadows; i++) {
            double angle = Math.toRadians((360 / shadows) * i + tick * 4);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.01
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    loc.clone().add(x * 0.7, 0.8, z * 0.7),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // ========== DEMON KING - Ice Crystals ==========
    private void demonKingParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.8;
        int crystals = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < crystals; i++) {
            double angle = Math.toRadians((360 / crystals) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0
            );
        }
    }
    
    // ========== BEAST LORD - Wolf Spirits ==========
    private void beastLordParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.9;
        int wolves = level == 1 ? 2 : (level == 2 ? 3 : 4);
        
        for (int i = 0; i < wolves; i++) {
            double angle = Math.toRadians((360 / wolves) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SWEEP_ATTACK,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0
            );
        }
    }
    
    // ========== SNOW FIEND - Snow Storm ==========
    private void snowParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        double radius = 2.0;
        int flakes = level == 1 ? 6 : (level == 2 ? 8 : 12);
        
        for (int i = 0; i < flakes; i++) {
            double angle = Math.toRadians((360 / flakes) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                loc.clone().add(x, 0.3, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== LIMITLESS - Purple Portals ==========
    private void limitlessParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.8;
        int portals = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < portals; i++) {
            double angle = Math.toRadians((360 / portals) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.2
            );
        }
    }
    
    // ========== TEN SHADOWS - Shadow Dogs ==========
    private void shadowsParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.7;
        int dogs = level == 1 ? 2 : (level == 2 ? 3 : 4);
        
        for (int i = 0; i < dogs; i++) {
            double angle = Math.toRadians((360 / dogs) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SOUL_FIRE_FLAME,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.01
            );
        }
    }
    
    // ========== DISASTER FLAMES - Magma Bubbles ==========
    private void disasterParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.8;
        int bubbles = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < bubbles; i++) {
            double angle = Math.toRadians((360 / bubbles) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.LAVA,
                loc.clone().add(x, 0.3, z),
                1, 0, 0, 0, 0
            );
            
            if (level >= 2) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    loc.clone().add(x * 0.8, 0.6, z * 0.8),
                    1, 0, 0, 0, 0.01
                );
            }
        }
    }
    
    // ========== BLOOD - Red Drops ==========
    private void bloodParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.6;
        int drops = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < drops; i++) {
            double angle = Math.toRadians((360 / drops) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.FALLING_LAVA,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== COMEDY - Musical Notes ==========
    private void comedyParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        double radius = 1.8;
        int notes = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < notes; i++) {
            double angle = Math.toRadians((360 / notes) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.NOTE,
                loc.clone().add(x, 0.3, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== SPIRIT BOMB - Energy Sphere ==========
    private void spiritParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.7;
        int energies = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < energies; i++) {
            double angle = Math.toRadians((360 / energies) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0
            );
        }
    }
    
    // ========== KAMEHAMEHA - Energy Waves ==========
    private void kameParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.9;
        int waves = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < waves; i++) {
            double angle = Math.toRadians((360 / waves) * i + tick * 4);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, 0.5, z),
                3, 0.1, 0.1, 0.1, 0
            );
        }
    }
    
    // ========== INSTANT TRANSMISSION - Portal Rings ==========
    private void instantParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.6;
        int portals = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < portals; i++) {
            double angle = Math.toRadians((360 / portals) * i + tick * 5);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.2
            );
        }
    }
    
    // ========== SOLAR FLARE - Bright Sparks ==========
    private void solarParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.8;
        int sparks = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < sparks; i++) {
            double angle = Math.toRadians((360 / sparks) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.FIREWORK,
                loc.clone().add(x, 0.5, z),
                1, 0, 0, 0, 0.01
            );
        }
    }
    
    // ========== GALAXY - Cosmic Portals ==========
    private void galaxyParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.9;
        int portals = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < portals; i++) {
            double angle = Math.toRadians((360 / portals) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, 0.5, z),
                3, 0.1, 0.1, 0.1, 0.3
            );
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x * 1.2, 0.7, z * 1.2),
                1, 0, 0, 0, 0
            );
        }
    }
    
    // ========== VOID REAVER - Void Portals ==========
    private void reaverParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.0, 0);
        double radius = 1.7;
        int voids = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < voids; i++) {
            double angle = Math.toRadians((360 / voids) * i + tick * 4);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                loc.clone().add(x, 0.5, z),
                3, 0.1, 0.1, 0.1, 0.4
            );
        }
    }
    
    // ========== SOUL EATER - Soul Orbs ==========
    private void eaterParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.6;
        int souls = level == 1 ? 3 : (level == 2 ? 5 : 7);
        
        for (int i = 0; i < souls; i++) {
            double angle = Math.toRadians((360 / souls) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.SOUL,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.01
            );
        }
    }
    
    // ========== STAR FALL - Star Sparks ==========
    private void starfallParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        double radius = 1.8;
        int stars = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < stars; i++) {
            double angle = Math.toRadians((360 / stars) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.FIREWORK,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0.01
            );
        }
    }
    
    // ========== TIME STOP - Clock Glow ==========
    private void timeParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.7;
        int clocks = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < clocks; i++) {
            double angle = Math.toRadians((360 / clocks) * i + tick * 2);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.GLOW,
                loc.clone().add(x, 0.5, z),
                2, 0.1, 0.1, 0.1, 0
            );
        }
    }
    
    // ========== REALITY WRITER - Enchantment Glow ==========
    private void writerParticles(Player player, int tick, int level) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        double radius = 1.8;
        int writes = level == 1 ? 4 : (level == 2 ? 6 : 8);
        
        for (int i = 0; i < writes; i++) {
            double angle = Math.toRadians((360 / writes) * i + tick * 3);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.ENCHANT,
                loc.clone().add(x, 0.5, z),
                3, 0.1, 0.1, 0.1, 0
            );
        }
    }
}
