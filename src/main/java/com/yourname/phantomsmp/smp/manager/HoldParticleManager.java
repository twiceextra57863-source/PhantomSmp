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

public class HoldParticleManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();
    
    public HoldParticleManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startHoldParticles(Player player, MagicBook book) {
        // Stop any existing task for this player
        stopHoldParticles(player);
        
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                // Check if player still holds the book
                if (!isHoldingPhantomBook(player)) {
                    stopHoldParticles(player);
                    return;
                }
                
                // Get unique particles based on book
                switch (book.getAbilityKey().toLowerCase()) {
                    case "storm":
                        stormParticles(player);
                        break;
                    case "shadow":
                        shadowParticles(player);
                        break;
                    case "flame":
                        flameParticles(player);
                        break;
                    case "frost":
                        frostParticles(player);
                        break;
                    case "dragon":
                        dragonParticles(player);
                        break;
                    case "void":
                        voidParticles(player);
                        break;
                    case "life":
                        lifeParticles(player);
                        break;
                    case "gravity":
                        gravityParticles(player);
                        break;
                    case "phantom":
                        phantomParticles(player);
                        break;
                    case "dawn":
                        dawnParticles(player);
                        break;
                    case "terra":
                        terraParticles(player);
                        break;
                    case "wind":
                        windParticles(player);
                        break;
                    case "time":
                        timeParticles(player);
                        break;
                    case "soul":
                        soulParticles(player);
                        break;
                    case "crystal":
                        crystalParticles(player);
                        break;
                    case "thunder":
                        thunderParticles(player);
                        break;
                    case "ice":
                        iceParticles(player);
                        break;
                    case "pyro":
                        pyroParticles(player);
                        break;
                    case "spirit":
                        spiritParticles(player);
                        break;
                    case "necro":
                        necroParticles(player);
                        break;
                    case "seraph":
                        seraphParticles(player);
                        break;
                    case "abyss":
                        abyssParticles(player);
                        break;
                    case "chaos":
                        chaosParticles(player);
                        break;
                    case "judge":
                        judgeParticles(player);
                        break;
                    case "dream":
                        dreamParticles(player);
                        break;
                    case "fear":
                        fearParticles(player);
                        break;
                    case "aurora":
                        auroraParticles(player);
                        break;
                    case "star":
                        starParticles(player);
                        break;
                    case "inferno":
                        infernoParticles(player);
                        break;
                    case "avalanche":
                        avalancheParticles(player);
                        break;
                }
            }
        };
        
        task.runTaskTimer(plugin, 0L, 2L); // Run every 2 ticks
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
            player.getInventory().getItemInMainHand().hasItemMeta() &&
            player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            
            for (String line : player.getInventory().getItemInMainHand().getItemMeta().getLore()) {
                if (line.contains("Phantom SMP")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // 30 Unique Particle Effects for each book
    
    private void stormParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        for (int i = 0; i < 3; i++) {
            player.getWorld().spawnParticle(
                Particle.ELECTRIC_SPARK,
                loc.clone().add(randomOffset(), randomOffset(), randomOffset()),
                1, 0, 0, 0, 0
            );
        }
    }
    
    private void shadowParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SMOKE,
            loc,
            5, 0.3, 0.3, 0.3, 0.01
        );
    }
    
    private void flameParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.FLAME,
            loc,
            3, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void frostParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SNOWFLAKE,
            loc,
            5, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void dragonParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.DRAGON_BREATH,
            loc,
            2, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void voidParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.PORTAL,
            loc,
            10, 0.3, 0.3, 0.3, 0.5
        );
    }
    
    private void lifeParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.HAPPY_VILLAGER,
            loc,
            5, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void gravityParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.END_ROD,
            loc,
            3, 0.3, 0.1, 0.3, 0
        );
    }
    
    private void phantomParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SOUL,
            loc,
            4, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void dawnParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.GLOW,
            loc,
            5, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void terraParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.BLOCK,
            loc,
            3, 0.2, 0.2, 0.2, 0,
            org.bukkit.Material.DIRT.createBlockData()
        );
    }
    
    private void windParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.CLOUD,
            loc,
            5, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void timeParticles(Player player) {
        Location loc = player.getLocation().add(0, 2, 0);
        double angle = System.currentTimeMillis() / 100.0;
        for (int i = 0; i < 3; i++) {
            double x = Math.cos(angle + i) * 0.5;
            double z = Math.sin(angle + i) * 0.5;
            player.getWorld().spawnParticle(
                Particle.GLOW,
                loc.clone().add(x, 0, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    private void soulParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SOUL_FIRE_FLAME,
            loc,
            3, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void crystalParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.END_ROD,
            loc,
            4, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void thunderParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.ELECTRIC_SPARK,
            loc,
            6, 0.3, 0.2, 0.3, 0.1
        );
    }
    
    private void iceParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.ITEM_SNOWBALL,
            loc,
            4, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void pyroParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.LAVA,
            loc,
            2, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void spiritParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SCULK_SOUL,
            loc,
            3, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void necroParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SOUL,
            loc,
            5, 0.3, 0.2, 0.3, 0.02
        );
    }
    
    private void seraphParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.END_ROD,
            loc,
            8, 0.3, 0.3, 0.3, 0
        );
    }
    
    private void abyssParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SCULK_SOUL,
            loc,
            6, 0.2, 0.2, 0.2, 0.03
        );
    }
    
    private void chaosParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        // Random particle each time
        Particle[] particles = {
            Particle.FLAME, Particle.ELECTRIC_SPARK, Particle.SOUL,
            Particle.END_ROD, Particle.GLOW, Particle.SNOWFLAKE
        };
        player.getWorld().spawnParticle(
            particles[(int)(Math.random() * particles.length)],
            loc,
            3, 0.2, 0.2, 0.2, 0.01
        );
    }
    
    private void judgeParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.END_ROD,
            loc,
            4, 0.3, 0.5, 0.3, 0
        );
    }
    
    private void dreamParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.WAX_OFF,
            loc,
            5, 0.2, 0.2, 0.2, 0
        );
    }
    
    private void fearParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SCULK_SOUL,
            loc,
            5, 0.3, 0.3, 0.3, 0.02
        );
    }
    
    private void auroraParticles(Player player) {
        Location loc = player.getLocation().add(0, 2, 0);
        for (int i = 0; i < 360; i += 45) {
            double angle = Math.toRadians(i + System.currentTimeMillis() / 20.0);
            double x = Math.cos(angle) * 1.5;
            double z = Math.sin(angle) * 1.5;
            
            player.getWorld().spawnParticle(
                Particle.END_ROD,
                loc.clone().add(x, 0, z),
                1, 0, 0, 0, 0
            );
        }
    }
    
    private void starParticles(Player player) {
        Location loc = player.getLocation().add(0, 2, 0);
        player.getWorld().spawnParticle(
            Particle.FIREWORK,
            loc,
            5, 0.3, 0.2, 0.3, 0.01
        );
    }
    
    private void infernoParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.FLAME,
            loc,
            8, 0.4, 0.2, 0.4, 0.02
        );
    }
    
    private void avalancheParticles(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(
            Particle.SNOWFLAKE,
            loc,
            10, 0.3, 0.2, 0.3, 0
        );
    }
    
    private double randomOffset() {
        return (Math.random() - 0.5) * 0.5;
    }
          }
