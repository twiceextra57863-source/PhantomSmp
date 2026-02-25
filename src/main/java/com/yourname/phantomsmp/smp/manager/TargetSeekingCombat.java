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

public class TargetSeekingCombat {
    
    private final PhantomSMP plugin;
    private final Random random = new Random();
    
    public TargetSeekingCombat(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public List<LivingEntity> getNearbyEnemies(Player player, double radius) {
        List<LivingEntity> enemies = new ArrayList<>();
        for (org.bukkit.entity.Entity e : player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof org.bukkit.entity.ArmorStand)) {
                enemies.add((LivingEntity) e);
            }
        }
        return enemies;
    }
    
    // ========== DEMON SLAYER STYLE COMBAT ==========
    
    public void executeSunBreathing(Player player, int level, boolean isEpic) {
        Location center = player.getLocation();
        List<LivingEntity> enemies = getNearbyEnemies(player, 10);
        
        int damage = isEpic ? 20 : (level == 2 ? 12 : 8);
        int hits = isEpic ? 12 : (level == 2 ? 8 : 5);
        
        player.getWorld().playSound(center, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.5f);
        
        new BukkitRunnable() {
            int hitCount = 0;
            
            @Override
            public void run() {
                if (hitCount >= hits || enemies.isEmpty()) {
                    // Final flash
                    player.getWorld().spawnParticle(
                        Particle.FLASH,
                        center.clone().add(0, 2, 0),
                        1, 0, 0, 0, 0
                    );
                    cancel();
                    return;
                }
                
                LivingEntity target = enemies.get(random.nextInt(enemies.size()));
                if (target.isDead()) {
                    enemies.remove(target);
                    return;
                }
                
                // Teleport to target
                Location behind = target.getLocation().add(
                    target.getLocation().getDirection().multiply(-2)
                );
                player.teleport(behind);
                
                // Flame slash effect
                Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                for (double d = 0; d < 3; d += 0.3) {
                    Location slashLoc = player.getLocation().clone().add(direction.clone().multiply(d));
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        slashLoc,
                        5, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                // Damage
                target.damage(damage, player);
                target.setFireTicks(40);
                
                hitCount++;
            }
        }.runTaskTimer(plugin, 0L, isEpic ? 3L : 5L);
    }
    
    public void executeWaterBreathing(Player player, int level, boolean isEpic) {
        Location center = player.getLocation();
        List<LivingEntity> enemies = getNearbyEnemies(player, 12);
        
        int damage = isEpic ? 18 : (level == 2 ? 10 : 6);
        int waves = isEpic ? 5 : (level == 2 ? 3 : 2);
        
        player.getWorld().playSound(center, Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0f, 0.5f);
        
        new BukkitRunnable() {
            int waveCount = 0;
            
            @Override
            public void run() {
                if (waveCount >= waves || enemies.isEmpty()) {
                    cancel();
                    return;
                }
                
                // Create water dragon
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + waveCount * 20);
                    double radius = 3.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    for (double y = 0; y < 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.DRIPPING_WATER,
                            x, center.getY() + y, z,
                            2, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                
                // Damage all enemies
                for (LivingEntity target : new ArrayList<>(enemies)) {
                    if (target.isDead()) {
                        enemies.remove(target);
                        continue;
                    }
                    
                    target.damage(damage, player);
                    target.setVelocity(new Vector(0, 1, 0));
                }
                
                waveCount++;
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    public void executeThunderBreathing(Player player, int level, boolean isEpic) {
        Location center = player.getLocation();
        List<LivingEntity> enemies = getNearbyEnemies(player, 15);
        
        int damage = isEpic ? 25 : (level == 2 ? 15 : 10);
        int dashes = isEpic ? 7 : (level == 2 ? 5 : 3);
        
        player.getWorld().playSound(center, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.5f);
        
        new BukkitRunnable() {
            int dashCount = 0;
            
            @Override
            public void run() {
                if (dashCount >= dashes || enemies.isEmpty()) {
                    cancel();
                    return;
                }
                
                LivingEntity target = enemies.get(random.nextInt(enemies.size()));
                if (target.isDead()) {
                    enemies.remove(target);
                    return;
                }
                
                // Lightning dash
                Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                player.setVelocity(direction.multiply(2));
                
                // Lightning effect
                for (double d = 0; d < 10; d += 0.5) {
                    Location boltLoc = player.getLocation().clone().add(direction.clone().multiply(d));
                    player.getWorld().spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        boltLoc,
                        5, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                // Strike
                player.getWorld().strikeLightningEffect(target.getLocation());
                target.damage(damage, player);
                
                dashCount++;
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    // ========== SOLO LEVELING STYLE ==========
    
    public void executeShadowSovereign(Player player, int level, boolean isEpic) {
        Location center = player.getLocation();
        List<LivingEntity> enemies = getNearbyEnemies(player, 12);
        
        int shadows = isEpic ? 8 : (level == 2 ? 5 : 3);
        int damage = isEpic ? 15 : (level == 2 ? 10 : 5);
        
        player.getWorld().playSound(center, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        // Summon shadow soldiers
        for (int i = 0; i < shadows; i++) {
            int index = i;
            new BukkitRunnable() {
                int attacks = 0;
                Location shadowLoc = center.clone().add(
                    random.nextDouble() * 5 - 2.5,
                    0,
                    random.nextDouble() * 5 - 2.5
                );
                
                @Override
                public void run() {
                    if (attacks >= 3 || enemies.isEmpty()) {
                        cancel();
                        return;
                    }
                    
                    // Shadow appear
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        shadowLoc.clone().add(0, 1, 0),
                        10, 0.3, 0.5, 0.3, 0.01
                    );
                    
                    // Attack nearest enemy
                    LivingEntity nearest = null;
                    double nearestDist = Double.MAX_VALUE;
                    for (LivingEntity e : enemies) {
                        if (e.isDead()) continue;
                        double dist = e.getLocation().distance(shadowLoc);
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            nearest = e;
                        }
                    }
                    
                    if (nearest != null) {
                        nearest.damage(damage, player);
                        
                        // Attack effect
                        Vector toTarget = nearest.getLocation().toVector().subtract(shadowLoc.toVector()).normalize();
                        for (double d = 0; d < nearestDist; d += 0.5) {
                            Location attackLoc = shadowLoc.clone().add(toTarget.clone().multiply(d));
                            player.getWorld().spawnParticle(
                                Particle.SOUL_FIRE_FLAME,
                                attackLoc,
                                2, 0.1, 0.1, 0.1, 0.01
                            );
                        }
                    }
                    
                    attacks++;
                }
            }.runTaskTimer(plugin, i * 10L, 20L);
        }
    }
    
    // ========== JUJUTSU KAISEN STYLE ==========
    
    public void executeLimitless(Player player, int level, boolean isEpic) {
        Location center = player.getLocation();
        List<LivingEntity> enemies = getNearbyEnemies(player, 20);
        
        int damage = isEpic ? 50 : (level == 2 ? 30 : 20);
        
        player.getWorld().playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
        
        // Hollow Purple effect
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks >= 40) {
                    // Final explosion
                    for (LivingEntity target : enemies) {
                        if (target.isDead()) continue;
                        target.damage(damage, player);
                        
                        target.getWorld().spawnParticle(
                            Particle.FLASH,
                            target.getLocation().add(0, 1, 0),
                            1, 0, 0, 0, 0
                        );
                    }
                    cancel();
                    return;
                }
                
                // Purple sphere expanding
                double radius = ticks * 0.5;
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        center.clone().add(x, 1, z),
                        3, 0.1, 0.1, 0.1, 0.3
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // ========== DRAGON BALL Z STYLE ==========
    
    public void executeKamehameha(Player player, int level, boolean isEpic) {
        Location start = player.getLocation().add(0, 1.5, 0);
        Vector direction = player.getLocation().getDirection().normalize();
        
        int damage = isEpic ? 40 : (level == 2 ? 25 : 15);
        int range = isEpic ? 30 : 20;
        
        player.getWorld().playSound(start, Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 0.5f);
        
        new BukkitRunnable() {
            int distance = 0;
            
            @Override
            public void run() {
                if (distance >= range) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                // Energy wave
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i);
                    double radius = 1.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                // Damage enemies in wave
                for (LivingEntity target : getNearbyEnemies(player, range)) {
                    if (target.getLocation().distance(current) < 3) {
                        target.damage(damage, player);
                    }
                }
                
                distance++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    public void executeSpiritBomb(Player player, int level, boolean isEpic) {
        Location center = player.getLocation().add(0, 5, 0);
        List<LivingEntity> enemies = getNearbyEnemies(player, 25);
        
        int damage = isEpic ? 60 : (level == 2 ? 35 : 20);
        
        player.getWorld().playSound(center, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks >= 60) {
                    // Spirit bomb drops
                    for (LivingEntity target : enemies) {
                        if (target.isDead()) continue;
                        target.damage(damage, player);
                        
                        target.getWorld().createExplosion(target.getLocation(), 2, false, false);
                    }
                    
                    center.getWorld().spawnParticle(
                        Particle.EXPLOSION,
                        center,
                        10, 2, 2, 2, 0.1
                    );
                    
                    cancel();
                    return;
                }
                
                // Charging spirit bomb
                double radius = 1.0 + Math.sin(ticks * 0.1) * 0.5;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        center.clone().add(x, 0, z),
                        2, 0, 0, 0, 0.01
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
                        }
