package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class BookManager {
    
    private final PhantomSMP plugin;
    private final Random random = new Random();
    
    public BookManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    // ========== BOOK GIVING METHODS ==========
    
    public void giveBookToPlayer(Player player, String bookName) {
        for (MagicBook book : MagicBook.values()) {
            if (book.getDisplayName().contains(bookName) || 
                book.name().equalsIgnoreCase(bookName) ||
                book.getAbilityKey().equalsIgnoreCase(bookName)) {
                giveBookWithCeremony(player, book);
                return;
            }
        }
        
        giveRandomBook(player);
    }
    
    public void giveRandomBook(Player player) {
        MagicBook randomBook = MagicBook.getRandomBook();
        giveBookWithCeremony(player, randomBook);
    }
    
    public void giveBookWithCeremony(Player player, MagicBook book) {
        player.sendMessage("§d§l✨ PHANTOM CEREMONY ✨");
        player.sendMessage("§fThe ancient spirits have chosen you...");
        
        plugin.getCeremonyManager().startCeremony(player, book, () -> {
            player.getInventory().addItem(book.createBook());
            Bukkit.broadcastMessage("§d§l" + player.getName() + " §ehas awakened: §6" + book.getDisplayName());
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            
            for (int i = 0; i < 10; i++) {
                player.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    player.getLocation().add(0, 2, 0),
                    20, 1, 1, 1, 0.1
                );
            }
        });
    }
    
    // ========== MAIN ABILITY EXECUTION ==========
    
    public void useBookAbility(Player player, ItemStack book) {
        if (book == null || !book.hasItemMeta()) return;
        
        ItemMeta meta = book.getItemMeta();
        if (!meta.hasLore()) return;
        
        String abilityKey = null;
        for (String line : meta.getLore()) {
            if (line.contains("Ability:")) {
                abilityKey = ChatColor.stripColor(line).replace("Ability:", "").trim();
                break;
            }
        }
        
        if (abilityKey == null) return;
        
        MagicBook magicBook = MagicBook.getByAbilityKey(abilityKey);
        if (magicBook == null) return;
        
        int level = plugin.getLevelManager().getBookLevel(player, abilityKey);
        
        // Check cooldown for NORMAL ability
        if (plugin.getCooldownManager().isOnCooldown(player, magicBook, CooldownManager.ABILITY_NORMAL)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, magicBook, CooldownManager.ABILITY_NORMAL);
            player.sendMessage("§c❌ " + magicBook.getDisplayName() + " §7Normal ability on cooldown for §f" + remaining + "s");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.5f);
            return;
        }
        
        double multiplier = plugin.getLevelManager().getCooldownMultiplier(level);
        int baseCooldown = magicBook.getCooldown();
        int effectiveCooldown = (int)(baseCooldown * multiplier);
        
        plugin.getCooldownManager().setCooldown(player, magicBook, CooldownManager.ABILITY_NORMAL, effectiveCooldown);
        
        executeNormalAbility(player, magicBook, level);
    }
    
    public void executeAdvancedAbility(Player player, MagicBook book, int level) {
        if (plugin.getCooldownManager().isOnCooldown(player, book, CooldownManager.ABILITY_ADVANCED)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, book, CooldownManager.ABILITY_ADVANCED);
            player.sendMessage("§c❌ Advanced ability on cooldown for §f" + remaining + "s");
            return;
        }
        
        plugin.getCooldownManager().setCooldown(player, book, CooldownManager.ABILITY_ADVANCED, book.getCooldown() * 2);
        executeAdvancedAbilityByKey(player, book, level);
    }
    
    public void executeUltimateAbility(Player player, MagicBook book, int level) {
        if (plugin.getCooldownManager().isOnCooldown(player, book, CooldownManager.ABILITY_ULTIMATE)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, book, CooldownManager.ABILITY_ULTIMATE);
            player.sendMessage("§c❌ Ultimate ability on cooldown for §f" + remaining + "s");
            return;
        }
        
        plugin.getCooldownManager().setCooldown(player, book, CooldownManager.ABILITY_ULTIMATE, book.getCooldown() * 3);
        executeUltimateAbilityByKey(player, book, level);
    }
    
    // ========== NORMAL ABILITIES (PRIMARY - PROJECTILE) ==========
    
    private void executeNormalAbility(Player player, MagicBook book, int level) {
        switch(book.getAbilityKey().toLowerCase()) {
            case "sun": sunPrimary(player, level); break;
            case "water": waterPrimary(player, level); break;
            case "thunder": thunderPrimary(player, level); break;
            case "flame": flamePrimary(player, level); break;
            case "wind": windPrimary(player, level); break;
            case "stone": stonePrimary(player, level); break;
            case "mist": mistPrimary(player, level); break;
            case "beast": beastPrimary(player, level); break;
            case "sound": soundPrimary(player, level); break;
            case "serpent": serpentPrimary(player, level); break;
            case "love": lovePrimary(player, level); break;
            case "sovereign": sovereignPrimary(player, level); break;
            case "demonking": demonKingPrimary(player, level); break;
            case "beastlord": beastLordPrimary(player, level); break;
            case "snow": snowPrimary(player, level); break;
            case "limitless": limitlessPrimary(player, level); break;
            case "shadows": shadowsPrimary(player, level); break;
            case "disaster": disasterPrimary(player, level); break;
            case "blood": bloodPrimary(player, level); break;
            case "comedy": comedyPrimary(player, level); break;
            case "spirit": spiritPrimary(player, level); break;
            case "kame": kamePrimary(player, level); break;
            case "instant": instantPrimary(player, level); break;
            case "solar": solarPrimary(player, level); break;
            case "galaxy": galaxyPrimary(player, level); break;
            case "reaver": reaverPrimary(player, level); break;
            case "eater": eaterPrimary(player, level); break;
            case "starfall": starfallPrimary(player, level); break;
            case "time": timePrimary(player, level); break;
            case "writer": writerPrimary(player, level); break;
        }
    }
    
    private void executeAdvancedAbilityByKey(Player player, MagicBook book, int level) {
        switch(book.getAbilityKey().toLowerCase()) {
            case "sun": sunAdvanced(player, level); break;
            case "water": waterAdvanced(player, level); break;
            case "thunder": thunderAdvanced(player, level); break;
            case "flame": flameAdvanced(player, level); break;
            case "wind": windAdvanced(player, level); break;
            case "stone": stoneAdvanced(player, level); break;
            case "mist": mistAdvanced(player, level); break;
            case "beast": beastAdvanced(player, level); break;
            case "sound": soundAdvanced(player, level); break;
            case "serpent": serpentAdvanced(player, level); break;
            case "love": loveAdvanced(player, level); break;
            case "sovereign": sovereignAdvanced(player, level); break;
            case "demonking": demonKingAdvanced(player, level); break;
            case "beastlord": beastLordAdvanced(player, level); break;
            case "snow": snowAdvanced(player, level); break;
            case "limitless": limitlessAdvanced(player, level); break;
            case "shadows": shadowsAdvanced(player, level); break;
            case "disaster": disasterAdvanced(player, level); break;
            case "blood": bloodAdvanced(player, level); break;
            case "comedy": comedyAdvanced(player, level); break;
            case "spirit": spiritAdvanced(player, level); break;
            case "kame": kameAdvanced(player, level); break;
            case "instant": instantAdvanced(player, level); break;
            case "solar": solarAdvanced(player, level); break;
            case "galaxy": galaxyAdvanced(player, level); break;
            case "reaver": reaverAdvanced(player, level); break;
            case "eater": eaterAdvanced(player, level); break;
            case "starfall": starfallAdvanced(player, level); break;
            case "time": timeAdvanced(player, level); break;
            case "writer": writerAdvanced(player, level); break;
        }
    }
    
    private void executeUltimateAbilityByKey(Player player, MagicBook book, int level) {
        switch(book.getAbilityKey().toLowerCase()) {
            case "sun": sunUltimate(player, level); break;
            case "water": waterUltimate(player, level); break;
            case "thunder": thunderUltimate(player, level); break;
            case "flame": flameUltimate(player, level); break;
            case "wind": windUltimate(player, level); break;
            case "stone": stoneUltimate(player, level); break;
            case "mist": mistUltimate(player, level); break;
            case "beast": beastUltimate(player, level); break;
            case "sound": soundUltimate(player, level); break;
            case "serpent": serpentUltimate(player, level); break;
            case "love": loveUltimate(player, level); break;
            case "sovereign": sovereignUltimate(player, level); break;
            case "demonking": demonKingUltimate(player, level); break;
            case "beastlord": beastLordUltimate(player, level); break;
            case "snow": snowUltimate(player, level); break;
            case "limitless": limitlessUltimate(player, level); break;
            case "shadows": shadowsUltimate(player, level); break;
            case "disaster": disasterUltimate(player, level); break;
            case "blood": bloodUltimate(player, level); break;
            case "comedy": comedyUltimate(player, level); break;
            case "spirit": spiritUltimate(player, level); break;
            case "kame": kameUltimate(player, level); break;
            case "instant": instantUltimate(player, level); break;
            case "solar": solarUltimate(player, level); break;
            case "galaxy": galaxyUltimate(player, level); break;
            case "reaver": reaverUltimate(player, level); break;
            case "eater": eaterUltimate(player, level); break;
            case "starfall": starfallUltimate(player, level); break;
            case "time": timeUltimate(player, level); break;
            case "writer": writerUltimate(player, level); break;
        }
    }
    
    // ========== NORMAL ABILITIES (Brief - Already Working) ==========
    
    private void sunPrimary(Player player, int level) {
        player.sendMessage("§6§l☀️ SUN BREATHING: SOLAR PROJECTILE ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.2f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 6;
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 50;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i);
                    double radius = 0.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY(), z,
                        2, 0, 0, 0, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, current.getY(), z,
                        1, 0, 0, 0, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setFireTicks(60);
                        
                        player.getWorld().spawnParticle(
                            Particle.EXPLOSION,
                            current,
                            10, 0.5, 0.5, 0.5, 0.1
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // ========== 30 UNIQUE EPIC ABILITIES (ULTIMATE TIER) ==========
    
    // 1. SUN BREATHING ULTIMATE - SOLAR FLARE (Creates a miniature sun that pulls enemies)
    private void sunUltimate(Player player, int level) {
        player.sendMessage("§6§l☀️ SUN BREATHING ULTIMATE: SOLAR FLARE ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        Location center = player.getLocation().add(0, 3, 0);
        int damage = 15 + level * 5;
        
        // Create a miniature sun
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100; // 5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Sun implosion
                    for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage * 2, player);
                            e.setFireTicks(100);
                            
                            // Pull towards sun
                            Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                            e.setVelocity(pull.multiply(1.5));
                        }
                    }
                    
                    player.getWorld().createExplosion(center, 0, false, false);
                    player.getWorld().spawnParticle(Particle.FLASH, center, 1, 0, 0, 0, 0);
                    cancel();
                    return;
                }
                
                // Sun expands and contracts
                double radius = 2.0 + Math.sin(ticks * 0.2) * 1.0;
                
                // Sun sphere
                for (int i = 0; i < 360; i += 15) {
                    double phi = Math.toRadians(i);
                    for (int j = 0; j < 180; j += 30) {
                        double theta = Math.toRadians(j);
                        
                        double x = radius * Math.sin(theta) * Math.cos(phi);
                        double y = radius * Math.sin(theta) * Math.sin(phi) + 0.5;
                        double z = radius * Math.cos(theta);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            center.clone().add(x, y, z),
                            2, 0, 0, 0, 0.01
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            center.clone().add(x * 1.2, y + 0.2, z * 1.2),
                            1, 0, 0, 0, 0
                        );
                    }
                }
                
                // Pull enemies towards sun
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.1));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 2. WATER BREATHING ULTIMATE - OCEAN'S ABYSS (Creates a whirlpool that sucks enemies)
    private void waterUltimate(Player player, int level) {
        player.sendMessage("§b§l💧 WATER BREATHING ULTIMATE: OCEAN'S ABYSS 💧");
        player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 12 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 120; // 6 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Final explosion
                    for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage * 2, player);
                            e.setVelocity(new Vector(0, 2, 0));
                        }
                    }
                    
                    for (int i = 0; i < 360; i += 15) {
                        double angle = Math.toRadians(i);
                        double x = center.getX() + 5 * Math.cos(angle);
                        double z = center.getZ() + 5 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.SPLASH,
                            x, center.getY() + 1, z,
                            20, 0.5, 0.5, 0.5, 0.1
                        );
                    }
                    cancel();
                    return;
                }
                
                // Whirlpool effect
                double radius = 5.0 * (1 - (double)ticks / DURATION);
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = Math.sin(angle + ticks) * 1.0;
                    
                    player.getWorld().spawnParticle(
                        Particle.SPLASH,
                        center.clone().add(x, y, z),
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.WATER_BUBBLE,
                        center.clone().add(x * 0.5, y + 0.5, z * 0.5),
                        2, 0, 0, 0, 0.01
                    );
                }
                
                // Suck enemies in
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.2));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 3. THUNDER BREATHING ULTIMATE - STORM'S EYE (Summons a moving storm cloud that strikes enemies)
    private void thunderUltimate(Player player, int level) {
        player.sendMessage("§e§l⚡ THUNDER BREATHING ULTIMATE: STORM'S EYE ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 10 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 140; // 7 seconds
            Location stormLoc = center.clone().add(0, 10, 0);
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Storm cloud moves randomly
                if (ticks % 10 == 0) {
                    stormLoc.add(random.nextDouble() * 4 - 2, 0, random.nextDouble() * 4 - 2);
                }
                
                // Cloud particles
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 3.0;
                    
                    double x = stormLoc.getX() + radius * Math.cos(angle);
                    double z = stormLoc.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, stormLoc.getY(), z,
                        2, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                // Lightning strikes
                if (ticks % 10 == 0) {
                    for (Entity e : player.getWorld().getNearbyEntities(stormLoc, 5, 5, 5)) {
                        if (e instanceof LivingEntity && e != player) {
                            player.getWorld().strikeLightning(e.getLocation());
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 4. FLAME BREATHING ULTIMATE - INFERNO PHOENIX (Summons a phoenix that flies and attacks)
    private void flameUltimate(Player player, int level) {
        player.sendMessage("§c§l🔥 FLAME BREATHING ULTIMATE: INFERNO PHOENIX 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.6f);
        
        Location center = player.getLocation();
        int damage = 12 + level * 5;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 140; // 7 seconds
            Location phoenixLoc = center.clone().add(0, 5, 0);
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Phoenix explosion
                    for (Entity e : player.getWorld().getNearbyEntities(phoenixLoc, 5, 5, 5)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage * 2, player);
                            e.setFireTicks(200);
                        }
                    }
                    
                    for (int i = 0; i < 360; i += 20) {
                        double angle = Math.toRadians(i);
                        double x = phoenixLoc.getX() + 4 * Math.cos(angle);
                        double z = phoenixLoc.getZ() + 4 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            x, phoenixLoc.getY(), z,
                            20, 0.5, 0.5, 0.5, 0.02
                        );
                    }
                    cancel();
                    return;
                }
                
                // Phoenix movement - find nearest enemy
                LivingEntity target = null;
                double nearestDist = Double.MAX_VALUE;
                
                for (Entity e : player.getWorld().getNearbyEntities(phoenixLoc, 15, 15, 15)) {
                    if (e instanceof LivingEntity && e != player) {
                        double dist = e.getLocation().distance(phoenixLoc);
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            target = (LivingEntity) e;
                        }
                    }
                }
                
                if (target != null) {
                    // Move towards target
                    Vector toTarget = target.getLocation().toVector().subtract(phoenixLoc.toVector()).normalize();
                    phoenixLoc.add(toTarget.multiply(0.5));
                    
                    // Attack if close
                    if (nearestDist < 3) {
                        target.damage(damage / 2, player);
                        target.setFireTicks(60);
                    }
                }
                
                // Phoenix body
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 2.0;
                    
                    double x = phoenixLoc.getX() + radius * Math.cos(angle);
                    double z = phoenixLoc.getZ() + radius * Math.sin(angle);
                    double y = phoenixLoc.getY() + Math.sin(angle + ticks) * 0.5;
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, y, z,
                        3, 0.1, 0.1, 0.1, 0.02
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        x, y + 0.3, z,
                        1, 0, 0, 0, 0.01
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 5. WIND BREATHING ULTIMATE - TORNADO (Creates a moving tornado that throws enemies)
    private void windUltimate(Player player, int level) {
        player.sendMessage("§f§l🌪️ WIND BREATHING ULTIMATE: TORNADO 🌪️");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 0.6f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 3;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 160; // 8 seconds
            Location tornadoLoc = center.clone();
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Tornado moves towards nearest enemy
                LivingEntity target = null;
                double nearestDist = Double.MAX_VALUE;
                
                for (Entity e : player.getWorld().getNearbyEntities(tornadoLoc, 15, 15, 15)) {
                    if (e instanceof LivingEntity && e != player) {
                        double dist = e.getLocation().distance(tornadoLoc);
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            target = (LivingEntity) e;
                        }
                    }
                }
                
                if (target != null) {
                    Vector toTarget = target.getLocation().toVector().subtract(tornadoLoc.toVector()).normalize();
                    tornadoLoc.add(toTarget.multiply(0.3));
                }
                
                // Tornado effect
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double radius = 3.0 + Math.sin(ticks * 0.2) * 0.5;
                    
                    for (int h = 0; h < 5; h++) {
                        double yOffset = h * 1.5;
                        double x = tornadoLoc.getX() + radius * Math.cos(angle);
                        double z = tornadoLoc.getZ() + radius * Math.sin(angle);
                        double y = tornadoLoc.getY() + yOffset;
                        
                        player.getWorld().spawnParticle(
                            Particle.CLOUD,
                            x, y, z,
                            2, 0.1, 0.1, 0.1, 0.02
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.GUST,
                            x, y + 0.3, z,
                            1, 0, 0, 0, 0.05
                        );
                    }
                }
                
                // Damage and throw enemies
                for (Entity e : player.getWorld().getNearbyEntities(tornadoLoc, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage / 2, player);
                        e.setVelocity(new Vector(0, 2, 0));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 6. STONE BREATHING ULTIMATE - EARTH GOLEM (Summons a golem that fights for you)
    private void stoneUltimate(Player player, int level) {
        player.sendMessage("§7§l⛰️ STONE BREATHING ULTIMATE: EARTH GOLEM ⛰️");
        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_STEP, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 200; // 10 seconds
            Location golemLoc = center.clone();
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Golem crumbles
                    for (int i = 0; i < 50; i++) {
                        player.getWorld().spawnParticle(
                            Particle.BLOCK,
                            golemLoc.clone().add(random.nextDouble() * 2 - 1, random.nextDouble() * 3, random.nextDouble() * 2 - 1),
                            1, 0, 0, 0, 0,
                            Material.STONE.createBlockData()
                        );
                    }
                    cancel();
                    return;
                }
                
                // Find nearest enemy
                LivingEntity target = null;
                double nearestDist = Double.MAX_VALUE;
                
                for (Entity e : player.getWorld().getNearbyEntities(golemLoc, 15, 15, 15)) {
                    if (e instanceof LivingEntity && e != player) {
                        double dist = e.getLocation().distance(golemLoc);
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            target = (LivingEntity) e;
                        }
                    }
                }
        
        if (target != null) {
                    // Move towards target
                    Vector toTarget = target.getLocation().toVector().subtract(golemLoc.toVector()).normalize();
                    golemLoc.add(toTarget.multiply(0.2));
                    
                    // Attack if close
                    if (nearestDist < 3) {
                        target.damage(damage, player);
                        target.setVelocity(new Vector(0, 1, 0));
                        
                        // Attack effect
                        for (int i = 0; i < 20; i++) {
                            player.getWorld().spawnParticle(
                                Particle.BLOCK,
                                target.getLocation().add(0, 1, 0),
                                5, 0.3, 0.3, 0.3, 0,
                                Material.STONE.createBlockData()
                            );
                        }
                    }
                }
                
                // Golem body
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double radius = 1.5;
                    
                    double x = golemLoc.getX() + radius * Math.cos(angle);
                    double z = golemLoc.getZ() + radius * Math.sin(angle);
                    double y = golemLoc.getY() + 1.5 + Math.sin(angle) * 0.3;
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        x, y, z,
                        1, 0, 0, 0, 0,
                        Material.STONE.createBlockData()
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 7. MIST BREATHING ULTIMATE - FOG OF WAR (Creates a moving mist that confuses enemies)
    private void mistUltimate(Player player, int level) {
        player.sendMessage("§7§l🌫️ MIST BREATHING ULTIMATE: FOG OF WAR 🌫️");
        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 3;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 160; // 8 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Mist spreads
                double radius = 3.0 + Math.sin(ticks * 0.1) * 1.0;
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    for (int y = 0; y < 4; y++) {
                        player.getWorld().spawnParticle(
                            Particle.CLOUD,
                            x, center.getY() + y, z,
                            2, 0.2, 0.2, 0.2, 0.02
                        );
                    }
                }
                
                // Confuse enemies in mist
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 5, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 2));
                        ((LivingEntity) e).damage(1, player);
                        
                        // Random teleport within mist
                        if (random.nextInt(100) < 5) {
                            Location randomLoc = center.clone().add(
                                random.nextDouble() * 6 - 3,
                                0,
                                random.nextDouble() * 6 - 3
                            );
                            e.teleport(randomLoc);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 8. BEAST BREATHING ULTIMATE - WOLF PACK (Summons a pack of wolves that hunt)
    private void beastUltimate(Player player, int level) {
        player.sendMessage("§6§l🐗 BEAST BREATHING ULTIMATE: WOLF PACK 🐗");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 0.6f);
        
        int wolfCount = 4 + level;
        int damage = 5 + level * 2;
        
        // Summon wolves at different positions
        for (int i = 0; i < wolfCount; i++) {
            int wolfIndex = i;
            new BukkitRunnable() {
                int ticks = 0;
                final int DURATION = 200; // 10 seconds
                Location wolfLoc = player.getLocation().clone().add(
                    random.nextDouble() * 4 - 2,
                    0,
                    random.nextDouble() * 4 - 2
                );
                
                @Override
                public void run() {
                    if (ticks >= DURATION) {
                        cancel();
                        return;
                    }
                    
                    // Find nearest enemy
                    LivingEntity target = null;
                    double nearestDist = Double.MAX_VALUE;
                    
                    for (Entity e : player.getWorld().getNearbyEntities(wolfLoc, 15, 15, 15)) {
                        if (e instanceof LivingEntity && e != player && !(e instanceof Wolf)) {
                            double dist = e.getLocation().distance(wolfLoc);
                            if (dist < nearestDist) {
                                nearestDist = dist;
                                target = (LivingEntity) e;
                            }
                        }
                    }
                    
                    if (target != null) {
                        // Move towards target
                        Vector toTarget = target.getLocation().toVector().subtract(wolfLoc.toVector()).normalize();
                        wolfLoc.add(toTarget.multiply(0.3));
                        
                        // Attack if close
                        if (nearestDist < 2) {
                            target.damage(damage, player);
                            
                            // Attack effect
                            player.getWorld().spawnParticle(
                                Particle.SWEEP_ATTACK,
                                target.getLocation().add(0, 1, 0),
                                10, 0.3, 0.3, 0.3, 0
                            );
                        }
                    }
                    
                    // Wolf body
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        wolfLoc.clone().add(0, 1, 0),
                        2, 0.2, 0.2, 0.2, 0
                    );
                    
                    ticks++;
                }
            }.runTaskTimer(plugin, i * 5L, 1L);
        }
    }
    
    // 9. SOUND BREATHING ULTIMATE - SONIC BLAST (Cone-shaped sonic wave)
    private void soundUltimate(Player player, int level) {
        player.sendMessage("§e§l🔊 SOUND BREATHING ULTIMATE: SONIC BLAST 🔊");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.6f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 15 + level * 5;
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 30;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                // Sonic wave cone
                for (int i = -30; i <= 30; i += 10) {
                    double angle = Math.toRadians(i);
                    Vector rotated = direction.clone().rotateAroundY(angle);
                    
                    Location current = start.clone().add(rotated.clone().multiply(distance));
                    
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        current,
                        2, 0.2, 0.2, 0.2, 0
                    );
                    
                    // Damage enemies in cone
                    for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 10. SERPENT BREATHING ULTIMATE - WORLD SERPENT (Creates a giant serpent that coils around enemies)
    private void serpentUltimate(Player player, int level) {
        player.sendMessage("§a§l🐍 SERPENT BREATHING ULTIMATE: WORLD SERPENT 🐍");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 10 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 180; // 9 seconds
            List<Location> snakeSegments = new ArrayList<>();
            
            {
                // Initialize snake segments
                for (int i = 0; i < 20; i++) {
                    snakeSegments.add(center.clone());
                }
            }
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Find nearest enemy
                LivingEntity target = null;
                double nearestDist = Double.MAX_VALUE;
                
                for (Entity e : player.getWorld().getNearbyEntities(snakeSegments.get(0), 20, 20, 20)) {
                    if (e instanceof LivingEntity && e != player) {
                        double dist = e.getLocation().distance(snakeSegments.get(0));
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            target = (LivingEntity) e;
                        }
                    }
                }
                
                if (target != null) {
                    // Move head towards target
                    Vector toTarget = target.getLocation().toVector().subtract(snakeSegments.get(0).toVector()).normalize();
                    snakeSegments.set(0, snakeSegments.get(0).clone().add(toTarget.multiply(0.4)));
                    
                    // Attack if close
                    if (nearestDist < 2) {
                        target.damage(damage, player);
                    }
                }
                
                // Update body segments
                for (int i = snakeSegments.size() - 1; i > 0; i--) {
                    snakeSegments.set(i, snakeSegments.get(i - 1).clone());
                }
                
                // Draw snake
                for (int i = 0; i < snakeSegments.size(); i++) {
                    Location segLoc = snakeSegments.get(i);
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        segLoc.clone().add(0, 1, 0),
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    if (i == 0) {
                        player.getWorld().spawnParticle(
                            Particle.SOUL_FIRE_FLAME,
                            segLoc.clone().add(0, 1.5, 0),
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 11. LOVE BREATHING ULTIMATE - HEART BEAM (Creates a healing/damaging beam)
    private void loveUltimate(Player player, int level) {
        player.sendMessage("§d§l💖 LOVE BREATHING ULTIMATE: HEART BEAM 💖");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 0.6f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 8 + level * 3;
        int heal = level * 2;
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 40;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                player.getWorld().spawnParticle(
                    Particle.HEART,
                    current,
                    3, 0.2, 0.2, 0.2, 0
                );
                
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    current.clone().add(0, 0.3, 0),
                    1, 0, 0, 0, 0
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof Player && e != player) {
                        // Heal allies
                        Player target = (Player) e;
                        target.setHealth(Math.min(target.getHealth() + heal, target.getMaxHealth()));
                        
                        for (int i = 0; i < 5; i++) {
                            player.getWorld().spawnParticle(
                                Particle.HEART,
                                target.getLocation().add(0, 1, 0),
                                5, 0.3, 0.3, 0.3, 0
                            );
                        }
                    } else if (e instanceof Monster) {
                        // Damage enemies
                        ((LivingEntity) e).damage(damage, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 12. SHADOW SOVEREIGN ULTIMATE - SHADOW REALM (Teleports enemies to the void)
    private void sovereignUltimate(Player player, int level) {
        player.sendMessage("§5§l👑 SHADOW SOVEREIGN ULTIMATE: SHADOW REALM 👑");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 12 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 120; // 6 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Shadow realm effect
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 5.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0.3
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        x, center.getY() + 2, z,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                // Pull enemies and teleport them
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                        
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.2));
                        
                        // Teleport to random location if close enough
                        if (e.getLocation().distance(center) < 3 && random.nextInt(100) < 10) {
                            Location randomLoc = e.getWorld().getHighestBlockAt(
                                center.getBlockX() + random.nextInt(20) - 10,
                                center.getBlockZ() + random.nextInt(20) - 10
                            ).getLocation().add(0, 1, 0);
                            
                            e.teleport(randomLoc);
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 13. DEMON KING ULTIMATE - FROZEN THRONE (Creates an ice palace that traps enemies)
    private void demonKingUltimate(Player player, int level) {
        player.sendMessage("§b§l👹 DEMON KING ULTIMATE: FROZEN THRONE 👹");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 10 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 160; // 8 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Ice palace walls
                double radius = 4.0;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    for (int y = 0; y < 5; y++) {
                        player.getWorld().spawnParticle(
                            Particle.SNOWFLAKE,
                            x, center.getY() + y, z,
                            2, 0.1, 0.1, 0.1, 0
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.BLOCK,
                            x, center.getY() + y, z,
                            1, 0, 0, 0, 0,
                            Material.ICE.createBlockData()
                        );
                    }
                }
                
                // Freeze enemies inside
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 5, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(200);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 5));
                        ((LivingEntity) e).damage(1, player);
                        
                        // Push towards center
                        Vector push = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(push.multiply(0.1));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 14. BEAST LORD ULTIMATE - PRIMAL ROAR (Roar that damages and fears enemies)
    private void beastLordUltimate(Player player, int level) {
        player.sendMessage("§6§l🐺 BEAST LORD ULTIMATE: PRIMAL ROAR 🐺");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 12 + level * 4;
        
        // Shockwave ring
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 40; // 2 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = ticks * 0.5;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        x, center.getY() + 1, z,
                        3, 0.2, 0.2, 0.2, 0
                    );
                }
                
                // Damage and knockback
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 3, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage / 2, player);
                        
                        Vector away = e.getLocation().toVector().subtract(center.toVector()).normalize();
                        e.setVelocity(away.multiply(1.5));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 15. SNOW FIEND ULTIMATE - BLIZZARD (Creates a blizzard that damages and slows)
    private void snowUltimate(Player player, int level) {
        player.sendMessage("§b§l❄️ SNOW FIEND ULTIMATE: BLIZZARD ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 3;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 200; // 10 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 5.0;
                
                // Snowstorm effect
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        x, center.getY() + 1 + Math.sin(angle) * 2, z,
                        3, 0.2, 0.2, 0.2, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.ITEM_SNOWBALL,
                        x, center.getY() + 2, z,
                        1, 0, 0, 0, 0
                    );
                }
                
                // Damage and freeze enemies
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 5, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(100);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 3));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 16. LIMITLESS ULTIMATE - DOMAIN EXPANSION (Creates a sphere that damages everything inside)
    private void limitlessUltimate(Player player, int level) {
        player.sendMessage("§d§l∞ LIMITLESS ULTIMATE: DOMAIN EXPANSION ∞");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        int damage = 20 + level * 6;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 120; // 6 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Domain collapse
                    for (Entity e : player.getWorld().getNearbyEntities(center, 7, 7, 7)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                    
                    player.getWorld().spawnParticle(
                        Particle.FLASH,
                        center.clone().add(0, 2, 0),
                        1, 0, 0, 0, 0
                    );
                    cancel();
                    return;
                }
                
                double radius = 5.0;
                
                // Domain sphere
                for (int i = 0; i < 360; i += 15) {
                    double phi = Math.toRadians(i);
                    for (int j = 0; j < 180; j += 30) {
                        double theta = Math.toRadians(j);
                        
                        double x = radius * Math.sin(theta) * Math.cos(phi);
                        double y = radius * Math.sin(theta) * Math.sin(phi) + 1;
                        double z = radius * Math.cos(theta);
                        
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            center.clone().add(x, y, z),
                            2, 0.1, 0.1, 0.1, 0.3
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            center.clone().add(x * 0.5, y + 0.3, z * 0.5),
                            1, 0, 0, 0, 0
                        );
                    }
                }
                
                // Damage inside domain
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(2, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 17. TEN SHADOWS ULTIMATE - MAHORAGA (Summons a powerful shadow beast)
    private void shadowsUltimate(Player player, int level) {
        player.sendMessage("§8§l🕷️ TEN SHADOWS ULTIMATE: MAHORAGA 🕷️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        int damage = 15 + level * 5;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 160; // 8 seconds
            Location beastLoc = center.clone().add(0, 3, 0);
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Find nearest enemy
                LivingEntity target = null;
                double nearestDist = Double.MAX_VALUE;
                
                for (Entity e : player.getWorld().getNearbyEntities(beastLoc, 20, 20, 20)) {
                    if (e instanceof LivingEntity && e != player) {
                        double dist = e.getLocation().distance(beastLoc);
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            target = (LivingEntity) e;
                        }
                    }
                }
                
                if (target != null) {
                    // Move towards target
                    Vector toTarget = target.getLocation().toVector().subtract(beastLoc.toVector()).normalize();
                    beastLoc.add(toTarget.multiply(0.3));
                    
                    // Attack if close
                    if (nearestDist < 3) {
                        target.damage(damage, player);
                        
                        // Attack effect
                        for (int i = 0; i < 360; i += 30) {
                            double angle = Math.toRadians(i);
                            double x = target.getLocation().getX() + 2 * Math.cos(angle);
                            double z = target.getLocation().getZ() + 2 * Math.sin(angle);
                            
                            player.getWorld().spawnParticle(
                                Particle.SCULK_SOUL,
                                x, target.getLocation().getY() + 1, z,
                                5, 0.2, 0.2, 0.2, 0.01
                            );
                        }
                    }
                }
                
                // Beast body
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 2.0;
                    
                    double x = beastLoc.getX() + radius * Math.cos(angle);
                    double z = beastLoc.getZ() + radius * Math.sin(angle);
                    double y = beastLoc.getY() + Math.sin(angle + ticks) * 0.5;
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        x, y, z,
                        3, 0.1, 0.1, 0.1, 0.02
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        x, y + 0.3, z,
                        1, 0, 0, 0, 0.01
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 18. DISASTER FLAMES ULTIMATE - VOLCANIC ERUPTION (Creates eruptions under enemies)
    private void disasterUltimate(Player player, int level) {
        player.sendMessage("§c§l🔥 DISASTER FLAMES ULTIMATE: VOLCANIC ERUPTION 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 12 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 140; // 7 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Random eruptions
                if (ticks % 10 == 0) {
                    for (int i = 0; i < 3; i++) {
                        double angle = random.nextDouble() * Math.PI * 2;
                        double radius = random.nextDouble() * 8;
                        
                        double x = center.getX() + radius * Math.cos(angle);
                        double z = center.getZ() + radius * Math.sin(angle);
                        
                        Location eruptionLoc = new Location(player.getWorld(), x, center.getY(), z);
                        
                        // Eruption pillar
                        for (double y = 0; y < 5; y += 0.5) {
                            player.getWorld().spawnParticle(
                                Particle.FLAME,
                                eruptionLoc.clone().add(0, y, 0),
                                5, 0.3, 0.3, 0.3, 0.02
                            );
                            
                            player.getWorld().spawnParticle(
                                Particle.LAVA,
                                eruptionLoc.clone().add(0, y, 0),
                                2, 0.1, 0.1, 0.1, 0
                            );
                        }
                        
                        // Damage enemies near eruption
                        for (Entity e : player.getWorld().getNearbyEntities(eruptionLoc, 3, 3, 3)) {
                            if (e instanceof LivingEntity && e != player) {
                                ((LivingEntity) e).damage(damage, player);
                                e.setFireTicks(100);
                            }
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 19. BLOOD MANIPULATION ULTIMATE - BLOOD DIMENSION (Creates blood orbs that heal you and damage enemies)
    private void bloodUltimate(Player player, int level) {
        player.sendMessage("§4§l🩸 BLOOD MANIPULATION ULTIMATE: BLOOD DIMENSION 🩸");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 10 + level * 3;
        int heal = level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 140; // 7 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Blood orbs
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 3.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    Location orbLoc = new Location(player.getWorld(), x, center.getY() + 1 + Math.sin(angle) * 0.5, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.FALLING_LAVA,
                        orbLoc,
                        3, 0.1, 0.1, 0.1, 0
                    );
                    
                    // Blood orbs seek enemies
                    for (Entity e : player.getWorld().getNearbyEntities(orbLoc, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                            
                            // Heal player
                            player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
                            
                            for (int h = 0; h < 5; h++) {
                                player.getWorld().spawnParticle(
                                    Particle.HEART,
                                    player.getLocation().add(0, 1, 0),
                                    3, 0.2, 0.2, 0.2, 0
                                );
                            }
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 20. COMEDY ULTIMATE - FINAL JOKE (Creates random chaos effects)
    private void comedyUltimate(Player player, int level) {
        player.sendMessage("§a§l🎭 COMEDY ULTIMATE: FINAL JOKE 🎭");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 0.6f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 120; // 6 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Random chaos every second
                if (ticks % 20 == 0) {
                    int effect = random.nextInt(5);
                    
                    switch(effect) {
                        case 0: // Lightning strike
                            for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                                if (e instanceof LivingEntity && e != player) {
                                    player.getWorld().strikeLightning(e.getLocation());
                                }
                            }
                            break;
                            
                        case 1: // Explosion
                            player.getWorld().createExplosion(center, 2, false, false);
                            break;
                            
                        case 2: // Launch enemies
                            for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(new Vector(0, 2, 0));
                                }
                            }
                            break;
                            
                        case 3: // Random teleport
                            for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                                if (e instanceof LivingEntity && e != player) {
                                    Location randomLoc = e.getLocation().clone().add(
                                        random.nextDouble() * 10 - 5,
                                        0,
                                        random.nextDouble() * 10 - 5
                                    );
                                    e.teleport(randomLoc);
                                }
                            }
                            break;
                            
                        case 4: // Fireworks
                            for (int i = 0; i < 10; i++) {
                                player.getWorld().spawnParticle(
                                    Particle.FIREWORK,
                                    center.clone().add(random.nextDouble() * 10 - 5, random.nextDouble() * 3, random.nextDouble() * 10 - 5),
                                    20, 0.5, 0.5, 0.5, 0.1
                                );
                            }
                            break;
                    }
                }
                
                // Random notes
                for (int i = 0; i < 5; i++) {
                    player.getWorld().spawnParticle(
                        Particle.NOTE,
                        center.clone().add(random.nextDouble() * 8 - 4, random.nextDouble() * 3, random.nextDouble() * 8 - 4),
                        1, 0, 0, 0, 0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 21. SPIRIT BOMB ULTIMATE - UNIVERSE TREE (Giant energy sphere that grows and explodes)
    private void spiritUltimate(Player player, int level) {
        player.sendMessage("§b§l💫 SPIRIT BOMB ULTIMATE: UNIVERSE TREE 💫");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        Location center = player.getLocation().add(0, 5, 0);
        int damage = 25 + level * 7;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int CHARGE_TIME = 80; // 4 seconds charge
            double radius = 1.0;
            
            @Override
            public void run() {
                if (ticks < CHARGE_TIME) {
                    // Charging phase - sphere grows
                    radius = 1.0 + (double)ticks / CHARGE_TIME * 3.0;
                    
                    for (int i = 0; i < 360; i += 15) {
                        double angle = Math.toRadians(i + ticks * 5);
                        double x = center.getX() + radius * Math.cos(angle);
                        double z = center.getZ() + radius * Math.sin(angle);
                        
                        for (double y = 0; y < 3; y += 0.5) {
                            player.getWorld().spawnParticle(
                                Particle.END_ROD,
                                x, center.getY() + y, z,
                                2, 0, 0, 0, 0
                            );
                            
                            player.getWorld().spawnParticle(
                                Particle.FIREWORK,
                                x, center.getY() + y + 0.3, z,
                                1, 0, 0, 0, 0.01
                            );
                        }
                    }
                } else {
                    // Explosion phase
                    for (Entity e : player.getWorld().getNearbyEntities(center, radius + 2, radius + 2, radius + 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                    
                    // Massive explosion effect
                    player.getWorld().createExplosion(center, 5, false, true);
                    
                    for (int i = 0; i < 360; i += 10) {
                        double angle = Math.toRadians(i);
                        double x = center.getX() + 8 * Math.cos(angle);
                        double z = center.getZ() + 8 * Math.sin(angle);
                        
                        for (double y = 0; y < 5; y += 0.5) {
                            player.getWorld().spawnParticle(
                                Particle.FIREWORK,
                                x, center.getY() + y, z,
                                10, 0.5, 0.5, 0.5, 0.1
                            );
                        }
                    }
                    
                    cancel();
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 22. KAMEHAMEHA ULTIMATE - ULTRA INSTINCT (Giant beam that moves with cursor)
    private void kameUltimate(Player player, int level) {
        player.sendMessage("§b§l🌊 KAMEHAMEHA ULTIMATE: ULTRA INSTINCT 🌊");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 0.4f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 20 + level * 6;
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 60;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                // Update direction based on player's look
                Vector currentDir = player.getLocation().getDirection().normalize();
                Location currentStart = player.getEyeLocation();
                
                Location current = currentStart.clone().add(currentDir.clone().multiply(distance));
                
                // Giant beam
                for (int i = -3; i <= 3; i++) {
                    for (int j = -3; j <= 3; j++) {
                        if (i*i + j*j > 9) continue;
                        
                        double offsetX = i * 0.8;
                        double offsetY = j * 0.8;
                        
                        Vector rotated = currentDir.clone().rotateAroundX(Math.toRadians(offsetY)).rotateAroundY(Math.toRadians(offsetX));
                        Location beamLoc = currentStart.clone().add(rotated.multiply(distance));
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            beamLoc,
                            3, 0.1, 0.1, 0.1, 0
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            beamLoc.clone().add(0, 0.3, 0),
                            1, 0, 0, 0, 0.01
                        );
                        
                        // Damage enemies in beam
                        for (Entity e : player.getWorld().getNearbyEntities(beamLoc, 1.5, 1.5, 1.5)) {
                            if (e instanceof LivingEntity && e != player) {
                                ((LivingEntity) e).damage(damage, player);
                            }
                        }
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 23. INSTANT TRANSMISSION ULTIMATE - GOD OF SPEED (Teleports rapidly between enemies)
    private void instantUltimate(Player player, int level) {
        player.sendMessage("§e§l⚡ INSTANT TRANSMISSION ULTIMATE: GOD OF SPEED ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.4f);
        
        int damage = 12 + level * 4;
        int teleports = 8 + level * 2;
        
        new BukkitRunnable() {
            int count = 0;
            
            @Override
            public void run() {
                if (count >= teleports) {
                    cancel();
                    return;
                }
                
                // Find random enemy
                List<LivingEntity> enemies = plugin.getTargetSeekingCombat().getNearbyEnemies(player, 20);
                
                if (!enemies.isEmpty()) {
                    LivingEntity target = enemies.get(random.nextInt(enemies.size()));
                    
                    // Teleport to target
                    Location behind = target.getLocation().add(target.getLocation().getDirection().multiply(-2));
                    player.teleport(behind);
                    
                    // Attack
                    target.damage(damage, player);
                    
                    // Afterimage effect
                    for (int i = 0; i < 10; i++) {
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            behind.clone().add(random.nextDouble() * 2 - 1, random.nextDouble() * 2, random.nextDouble() * 2 - 1),
                            5, 0.2, 0.2, 0.2, 0.3
                        );
                    }
                }
                
                count++;
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    // 24. SOLAR FLARE ULTIMATE - SUPERNOVA (Creates a massive light explosion)
    private void solarUltimate(Player player, int level) {
        player.sendMessage("§6§l☀️ SOLAR FLARE ULTIMATE: SUPERNOVA ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        int damage = 18 + level * 5;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Final explosion
                    for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                    
                    for (int i = 0; i < 360; i += 10) {
                        double angle = Math.toRadians(i);
                        double x = center.getX() + 10 * Math.cos(angle);
                        double z = center.getZ() + 10 * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            x, center.getY() + 1, z,
                            30, 0.5, 0.5, 0.5, 0.1
                        );
                    }
                    cancel();
                    return;
                }
                
                double radius = ticks * 0.3;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 25. GALAXY BREAKER ULTIMATE - UNIVERSE DESTROYER (Creates a black hole that sucks everything)
    private void galaxyUltimate(Player player, int level) {
        player.sendMessage("§5§l🌌 GALAXY BREAKER ULTIMATE: UNIVERSE DESTROYER 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        Location center = player.getLocation().add(0, 3, 0);
        int damage = 30 + level * 8;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100; // 5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    // Black hole collapse
                    for (Entity e : player.getWorld().getNearbyEntities(center, 12, 12, 12)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                            
                            // Pull to center
                            Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                            e.setVelocity(pull.multiply(2));
                        }
                    }
                    
                    player.getWorld().createExplosion(center, 0, false, false);
                    cancel();
                    return;
                }
                
                double radius = 6.0 * (1 - (double)ticks / DURATION);
                
                // Black hole ring
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    for (double y = -2; y <= 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            x, center.getY() + y, z,
                            3, 0.1, 0.1, 0.1, 0.5
                        );
                    }
                }
                
                // Pull enemies
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 2, 5, radius + 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.3));
                        ((LivingEntity) e).damage(2, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 26. VOID REAVER ULTIMATE - DIMENSIONAL COLLAPSE (Creates rifts that teleport and damage)
    private void reaverUltimate(Player player, int level) {
        player.sendMessage("§8§l🌑 VOID REAVER ULTIMATE: DIMENSIONAL COLLAPSE 🌑");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        int damage = 15 + level * 4;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 140; // 7 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Create random rifts
                if (ticks % 10 == 0) {
                    for (int i = 0; i < 5; i++) {
                        double angle = random.nextDouble() * Math.PI * 2;
                        double radius = random.nextDouble() * 8;
                        
                        double x = center.getX() + radius * Math.cos(angle);
                        double z = center.getZ() + radius * Math.sin(angle);
                        
                        Location riftLoc = new Location(player.getWorld(), x, center.getY(), z);
                        
                        // Rift effect
                        for (int j = 0; j < 360; j += 45) {
                            double riftAngle = Math.toRadians(j);
                            double rx = riftLoc.getX() + 2 * Math.cos(riftAngle);
                            double rz = riftLoc.getZ() + 2 * Math.sin(riftAngle);
                            
                            player.getWorld().spawnParticle(
                                Particle.PORTAL,
                                rx, riftLoc.getY() + 1, rz,
                                5, 0.2, 0.2, 0.2, 0.4
                            );
                        }
                        
                        // Damage and teleport enemies near rift
                        for (Entity e : player.getWorld().getNearbyEntities(riftLoc, 3, 3, 3)) {
                            if (e instanceof LivingEntity && e != player) {
                                ((LivingEntity) e).damage(damage, player);
                                
                                // Random teleport
                                Location randomLoc = e.getLocation().clone().add(
                                    random.nextDouble() * 10 - 5,
                                    random.nextDouble() * 2,
                                    random.nextDouble() * 10 - 5
                                );
                                e.teleport(randomLoc);
                            }
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 27. SOUL EATER ULTIMATE - SOUL FEAST (Drains souls from all nearby enemies)
    private void eaterUltimate(Player player, int level) {
        player.sendMessage("§2§l💀 SOUL EATER ULTIMATE: SOUL FEAST 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        int damage = 10 + level * 3;
        int heal = level * 3;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 120; // 6 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Soul drain from all enemies
                for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                    if (e instanceof LivingEntity && e != player) {
                        LivingEntity target = (LivingEntity) e;
                        
                        target.damage(damage / 4, player);
                        player.setHealth(Math.min(player.getHealth() + heal / 4, player.getMaxHealth()));
                        
                        // Soul particles flowing to player
                        Location targetLoc = target.getLocation().add(0, 1, 0);
                        Location playerLoc = player.getLocation().add(0, 1, 0);
                        
                        Vector direction = playerLoc.toVector().subtract(targetLoc.toVector()).normalize();
                        
                        for (double d = 0; d < targetLoc.distance(playerLoc); d += 0.5) {
                            Location flowLoc = targetLoc.clone().add(direction.clone().multiply(d));
                            
                            player.getWorld().spawnParticle(
                                Particle.SOUL,
                                flowLoc,
                                2, 0.1, 0.1, 0.1, 0.01
                            );
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    // 28. STAR FALL ULTIMATE - GALACTIC COLLAPSE (Meteor shower that follows cursor)
    private void starfallUltimate(Player player, int level) {
        player.sendMessage("§e§l✨ STAR FALL ULTIMATE: GALACTIC COLLAPSE ✨");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        int damage = 15 + level * 5;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 120; // 6 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Launch meteors where player is looking
                if (ticks % 5 == 0) {
                    Location eyeLoc = player.getEyeLocation();
                    Vector lookDir = player.getLocation().getDirection().normalize();
                    
                    for (int m = 0; m < 3; m++) {
                        int meteorIndex = m;
                        new BukkitRunnable() {
                            int meteorDist = 0;
                            
                            @Override
                            public void run() {
                                if (meteorDist >= 40) {
                                    Location impactLoc = eyeLoc.clone().add(lookDir.clone().multiply(40));
                                    
                                    // Meteor impact
                                    player.getWorld().createExplosion(impactLoc, 3, false, true);
                                    
                                    for (Entity e : player.getWorld().getNearbyEntities(impactLoc, 5, 5, 5)) {
                                        if (e instanceof LivingEntity && e != player) {
                                            ((LivingEntity) e).damage(damage, player);
                                        }
                                    }
                                    cancel();
                                    return;
                                }
                                
                                Location meteorLoc = eyeLoc.clone().add(lookDir.clone().multiply(meteorDist));
                                
                                // Meteor trail
                                for (int i = 0; i < 360; i += 30) {
                                    double angle = Math.toRadians(i + meteorDist * 10);
                                    double radius = 1.0;
                                    
                                    double x = meteorLoc.getX() + radius * Math.cos(angle);
                                    double z = meteorLoc.getZ() + radius * Math.sin(angle);
                                    
                                    player.getWorld().spawnParticle(
                                        Particle.FIREWORK,
                                        x, meteorLoc.getY(), z,
                                        3, 0.1, 0.1, 0.1, 0.01
                                    );
                                    
                                    player.getWorld().spawnParticle(
                                        Particle.FLAME,
                                        x, meteorLoc.getY(), z,
                                        2, 0.1, 0.1, 0.1, 0.01
                                    );
                                }
                                
                                meteorDist += 2;
                            }
                        }.runTaskTimer(plugin, meteorIndex * 2L, 1L);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 29. TIME STOP ULTIMATE - ZA WARUDO (Stops time for enemies)
    private void timeUltimate(Player player, int level) {
        player.sendMessage("§b§l⏰ TIME STOP ULTIMATE: ZA WARUDO ⏰");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        int damage = 20 + level * 6;
        
        // Time stop effect
        player.sendTitle("§bTHE WORLD", "§fTime has stopped!", 10, 60, 20);
        
        // Store original velocities
        Map<Entity, Vector> velocities = new HashMap<>();
        
        for (Entity e : player.getWorld().getNearbyEntities(center, 15, 15, 15)) {
            if (e instanceof LivingEntity && e != player) {
                velocities.put(e, e.getVelocity());
                e.setVelocity(new Vector(0, 0, 0));
            }
        }
        
        // Time resumed after delay
        new BukkitRunnable() {
            @Override
            public void run() {
                // Damage all frozen enemies
                for (Entity e : player.getWorld().getNearbyEntities(center, 15, 15, 15)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        // Restore velocity
                        if (velocities.containsKey(e)) {
                            e.setVelocity(velocities.get(e));
                        }
                        
                        // Time resume effect
                        for (int i = 0; i < 10; i++) {
                            player.getWorld().spawnParticle(
                                Particle.GLOW,
                                e.getLocation().add(0, 1, 0),
                                10, 0.3, 0.3, 0.3, 0
                            );
                        }
                    }
                }
                
                player.sendTitle("§bTIME RESUMED", "§fThe world moves again", 10, 40, 20);
            }
        }.runTaskLater(plugin, 60L); // 3 seconds
    }
    
    // 30. REALITY WRITER ULTIMATE - NEW REALITY (Completely alters the battlefield)
    private void writerUltimate(Player player, int level) {
        player.sendMessage("§d§l📝 REALITY WRITER ULTIMATE: NEW REALITY 📝");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        int damage = 15 + level * 5;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 160; // 8 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                // Random reality alterations
                if (ticks % 20 == 0) {
                    int effect = random.nextInt(4);
                    
                    switch(effect) {
                        case 0: // Gravity flip
                            for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(new Vector(0, 2, 0));
                                }
                            }
                            break;
                            
                        case 1: // Swap positions
                            List<LivingEntity> enemies = plugin.getTargetSeekingCombat().getNearbyEnemies(player, 10);
                            if (enemies.size() >= 2) {
                                Location loc1 = enemies.get(0).getLocation();
                                Location loc2 = enemies.get(1).getLocation();
                                enemies.get(0).teleport(loc2);
                                enemies.get(1).teleport(loc1);
                            }
                            break;
                            
                        case 2: // Damage exchange
                            for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                                if (e instanceof LivingEntity && e != player) {
                                    ((LivingEntity) e).damage(damage / 2, player);
                                }
                            }
                            break;
                            
                        case 3: // Speed change
                            for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(e.getVelocity().multiply(2));
                                }
                            }
                            break;
                    }
                }
                
                // Reality particles
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 4.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    double y = center.getY() + 1 + Math.sin(angle + ticks) * 0.5;
                    
                    player.getWorld().spawnParticle(
                        Particle.ENCHANT,
                        x, y, z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // ========== ADVANCED ABILITIES (Brief - Will be called from menu) ==========
    // These are placeholders - they use the same ultimate system but with reduced power
    
    private void sunAdvanced(Player player, int level) {
        player.sendMessage("§6§l☀️ SUN BREATHING: SOLAR FLARE (Advanced) ☀️");
        // Similar to ultimate but shorter duration and less damage
        Location center = player.getLocation().add(0, 2, 0);
        int damage = 8 + level * 3;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 2.0;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, center.getY(), z,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void waterAdvanced(Player player, int level) {
        player.sendMessage("§b§l💧 WATER BREATHING: TSUNAMI (Advanced) 💧");
        // Similar pattern for all advanced abilities
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 3.0;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SPLASH,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void thunderAdvanced(Player player, int level) {
        player.sendMessage("§e§l⚡ THUNDER BREATHING: LIGHTNING STORM (Advanced) ⚡");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 10 == 0) {
                    for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                        if (e instanceof LivingEntity && e != player) {
                            player.getWorld().strikeLightningEffect(e.getLocation());
                            ((LivingEntity) e).damage(5 + level, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void flameAdvanced(Player player, int level) {
        player.sendMessage("§c§l🔥 FLAME BREATHING: PHOENIX (Advanced) 🔥");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 2.5;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setFireTicks(40);
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void windAdvanced(Player player, int level) {
        player.sendMessage("§f§l🌪️ WIND BREATHING: TORNADO (Advanced) 🌪️");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 3.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, center.getY() + 1 + Math.sin(angle) * 0.5, z,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setVelocity(new Vector(0, 1, 0));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void stoneAdvanced(Player player, int level) {
        player.sendMessage("§7§l⛰️ STONE BREATHING: EARTHQUAKE (Advanced) ⛰️");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 2.0 + ticks * 0.05;
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        x, center.getY(), z,
                        2, 0.1, 0.1, 0.1, 0,
                        Material.STONE.createBlockData()
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 3, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void mistAdvanced(Player player, int level) {
        player.sendMessage("§7§l🌫️ MIST BREATHING: FOG (Advanced) 🌫️");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double radius = 4.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, center.getY() + 1, z,
                        3, 0.2, 0.2, 0.2, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastAdvanced(Player player, int level) {
        player.sendMessage("§6§l🐗 BEAST BREATHING: WILD RUSH (Advanced) 🐗");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 90) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 3.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(3 + level, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void soundAdvanced(Player player, int level) {
        player.sendMessage("§e§l🔊 SOUND BREATHING: SONIC WAVE (Advanced) 🔊");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 20;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                player.getWorld().spawnParticle(
                    Particle.SONIC_BOOM,
                    current,
                    2, 0.2, 0.2, 0.2, 0
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(5 + level, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void serpentAdvanced(Player player, int level) {
        player.sendMessage("§a§l🐍 SERPENT BREATHING: COILING STRIKE (Advanced) 🐍");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double angle = Math.toRadians(ticks * 10);
                double radius = 2.0;
                
                double x = center.getX() + radius * Math.cos(angle);
                double z = center.getZ() + radius * Math.sin(angle);
                
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    x, center.getY() + 1, z,
                    3, 0.1, 0.1, 0.1, 0.01
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void loveAdvanced(Player player, int level) {
        player.sendMessage("§d§l💖 LOVE BREATHING: HEART BEAM (Advanced) 💖");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 20;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                player.getWorld().spawnParticle(
                    Particle.HEART,
                    current,
                    2, 0.1, 0.1, 0.1, 0
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof Player && e != player) {
                        ((Player) e).setHealth(Math.min(((Player) e).getHealth() + level, ((Player) e).getMaxHealth()));
                    } else if (e instanceof Monster) {
                        ((LivingEntity) e).damage(3 + level, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void sovereignAdvanced(Player player, int level) {
        player.sendMessage("§5§l👑 SHADOW SOVEREIGN: SHADOW ARMY (Advanced) 👑");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 60) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 3.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void demonKingAdvanced(Player player, int level) {
        player.sendMessage("§b§l👹 DEMON KING: FROST NOVA (Advanced) 👹");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 1.0 + ticks * 0.1;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 3, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(40);
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastLordAdvanced(Player player, int level) {
        player.sendMessage("§6§l🐺 BEAST LORD: WOLF PACK (Advanced) 🐺");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 120) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 2.5;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(2 + level, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void snowAdvanced(Player player, int level) {
        player.sendMessage("§b§l❄️ SNOW FIEND: BLIZZARD (Advanced) ❄️");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 4.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(20);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void limitlessAdvanced(Player player, int level) {
        player.sendMessage("§d§l∞ LIMITLESS: HOLLOW PURPLE (Advanced) ∞");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 30;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    current,
                    3, 0.2, 0.2, 0.2, 0.3
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(8 + level * 2, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void shadowsAdvanced(Player player, int level) {
        player.sendMessage("§8§l🕷️ TEN SHADOWS: DIVINE DOGS (Advanced) 🕷️");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 180) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 3.0;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(3 + level, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void disasterAdvanced(Player player, int level) {
        player.sendMessage("§c§l🔥 DISASTER FLAMES: VOLCANIC BURST (Advanced) 🔥");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 10 == 0) {
                    for (int i = 0; i < 3; i++) {
                        double angle = random.nextDouble() * Math.PI * 2;
                        double radius = random.nextDouble() * 4;
                        
                        double x = center.getX() + radius * Math.cos(angle);
                        double z = center.getZ() + radius * Math.sin(angle);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            x, center.getY() + 1, z,
                            5, 0.2, 0.2, 0.2, 0.02
                        );
                        
                        for (Entity e : player.getWorld().getNearbyEntities(new Location(player.getWorld(), x, center.getY(), z), 2, 2, 2)) {
                            if (e instanceof LivingEntity && e != player) {
                                ((LivingEntity) e).damage(4 + level, player);
                                e.setFireTicks(40);
                            }
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void bloodAdvanced(Player player, int level) {
        player.sendMessage("§4§l🩸 BLOOD MANIPULATION: BLOOD ORBS (Advanced) 🩸");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 90) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 2.5;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FALLING_LAVA,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(2, player);
                        player.setHealth(Math.min(player.getHealth() + 1, player.getMaxHealth()));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void comedyAdvanced(Player player, int level) {
        player.sendMessage("§a§l🎭 COMEDY: RANDOM CHAOS (Advanced) 🎭");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 10 == 0) {
                    int effect = random.nextInt(3);
                    
                    switch(effect) {
                        case 0:
                            for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(new Vector(0, 1, 0));
                                }
                            }
                            break;
                        case 1:
                            player.getWorld().spawnParticle(
                                Particle.NOTE,
                                center.clone().add(0, 2, 0),
                                20, 1, 1, 1, 0
                            );
                            break;
                        case 2:
                            player.getWorld().playSound(center, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 1.0f);
                            break;
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void spiritAdvanced(Player player, int level) {
        player.sendMessage("§b§l💫 SPIRIT BOMB: ENERGY BALL (Advanced) 💫");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 30;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i);
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY(), z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(6 + level * 2, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void kameAdvanced(Player player, int level) {
        player.sendMessage("§b§l🌊 KAMEHAMEHA: ENERGY WAVE (Advanced) 🌊");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 30;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        double x = current.getX() + i * 0.8;
                        double z = current.getZ() + j * 0.8;
                        
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            x, current.getY(), z,
                            2, 0, 0, 0, 0
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(8 + level * 2, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void instantAdvanced(Player player, int level) {
        player.sendMessage("§e§l⚡ INSTANT TRANSMISSION: BLINK STRIKE (Advanced) ⚡");
        
        new BukkitRunnable() {
            int count = 0;
            final int TELEPORTS = 3 + level;
            
            @Override
            public void run() {
                if (count >= TELEPORTS) {
                    cancel();
                    return;
                }
                
                List<LivingEntity> enemies = plugin.getTargetSeekingCombat().getNearbyEnemies(player, 10);
                
                if (!enemies.isEmpty()) {
                    LivingEntity target = enemies.get(random.nextInt(enemies.size()));
                    
                    Location behind = target.getLocation().add(target.getLocation().getDirection().multiply(-2));
                    player.teleport(behind);
                    
                    target.damage(4 + level * 2, player);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        behind,
                        10, 0.3, 0.3, 0.3, 0.3
                    );
                }
                
                count++;
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    private void solarAdvanced(Player player, int level) {
        player.sendMessage("§6§l☀️ SOLAR FLARE: FLASH BANG (Advanced) ☀️");
        Location center = player.getLocation();
        
        for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
            }
        }
        
        player.getWorld().spawnParticle(
            Particle.FLASH,
            center.clone().add(0, 2, 0),
            1, 0, 0, 0, 0
        );
        
        for (int i = 0; i < 360; i += 30) {
            double angle = Math.toRadians(i);
            double x = center.getX() + 3 * Math.cos(angle);
            double z = center.getZ() + 3 * Math.sin(angle);
            
            player.getWorld().spawnParticle(
                Particle.FIREWORK,
                x, center.getY() + 1, z,
                5, 0.2, 0.2, 0.2, 0.01
            );
        }
    }
    
    private void galaxyAdvanced(Player player, int level) {
        player.sendMessage("§5§l🌌 GALAXY BREAKER: COSMIC BLAST (Advanced) 🌌");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 30;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i);
                    double radius = 1.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0.3
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(10 + level * 2, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void reaverAdvanced(Player player, int level) {
        player.sendMessage("§8§l🌑 VOID REAVER: VOID SLASH (Advanced) 🌑");
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            final int MAX_DISTANCE = 25;
            
            @Override
            public void run() {
                if (distance >= MAX_DISTANCE) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    current,
                    5, 0.2, 0.2, 0.2, 0.4
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(6 + level * 2, player);
                        
                        Location randomLoc = e.getLocation().clone().add(
                            random.nextDouble() * 3 - 1.5,
                            0,
                            random.nextDouble() * 3 - 1.5
                        );
                        e.teleport(randomLoc);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void eaterAdvanced(Player player, int level) {
        player.sendMessage("§2§l💀 SOUL EATER: SOUL DRAIN (Advanced) 💀");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                        player.setHealth(Math.min(player.getHealth() + 1, player.getMaxHealth()));
                        
                        player.getWorld().spawnParticle(
                            Particle.SOUL,
                            e.getLocation().add(0, 1, 0),
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    private void starfallAdvanced(Player player, int level) {
        player.sendMessage("§e§l✨ STAR FALL: METEOR SHOWER (Advanced) ✨");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 5 == 0) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * 5;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    Location meteorLoc = new Location(player.getWorld(), x, center.getY() + 5, z);
                    
                    for (double y = 5; y > 0; y -= 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            meteorLoc.clone().subtract(0, 5 - y, 0),
                            2, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                    
                    for (Entity e : player.getWorld().getNearbyEntities(new Location(player.getWorld(), x, center.getY(), z), 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(4 + level, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void timeAdvanced(Player player, int level) {
        player.sendMessage("§b§l⏰ TIME STOP: TIME SLOW (Advanced) ⏰");
        Location center = player.getLocation();
        
        for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 4));
                ((LivingEntity) e).setVelocity(new Vector(0, 0, 0));
            }
        }
        
        player.getWorld().spawnParticle(
            Particle.GLOW,
            center.clone().add(0, 2, 0),
            50, 2, 2, 2, 0
        );
    }
    
    private void writerAdvanced(Player player, int level) {
        player.sendMessage("§d§l📝 REALITY WRITER: REWRITE (Advanced) 📝");
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.ENCHANT,
                    center.clone().add(random.nextDouble() * 4 - 2, random.nextDouble() * 2, random.nextDouble() * 4 - 2),
                    3, 0.1, 0.1, 0.1, 0
                );
                
                if (ticks % 20 == 0 && !player.getWorld().getNearbyEntities(center, 5, 5, 5).isEmpty()) {
                    for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(3, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
