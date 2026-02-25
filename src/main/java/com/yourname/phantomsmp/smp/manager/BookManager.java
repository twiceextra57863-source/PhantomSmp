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
    
    // ========== PRIMARY ABILITIES (LEVEL 1) ==========
    
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
    
    private void waterPrimary(Player player, int level) {
        player.sendMessage("§b§l💧 WATER BREATHING: WATER WAVE 💧");
        player.playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 5;
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.DRIPPING_WATER,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SPLASH,
                        x, current.getY() + 0.3, z,
                        2, 0, 0, 0, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setVelocity(new Vector(0, 1, 0));
                        
                        player.getWorld().spawnParticle(
                            Particle.SPLASH,
                            current,
                            20, 0.5, 0.5, 0.5, 0.1
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void thunderPrimary(Player player, int level) {
        player.sendMessage("§e§l⚡ THUNDER BREATHING: LIGHTNING BOLT ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                    double radius = 0.8;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        player.getWorld().strikeLightningEffect(e.getLocation());
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 3;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void flamePrimary(Player player, int level) {
        player.sendMessage("§c§l🔥 FLAME BREATHING: FIREBALL 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setFireTicks(100);
                        
                        player.getWorld().createExplosion(current, 2, false, true);
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void windPrimary(Player player, int level) {
        player.sendMessage("§f§l🌪️ WIND BREATHING: AIR SLASH 🌪️");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 1.2f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 5;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.2;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setVelocity(direction.clone().multiply(2).setY(1));
                        
                        player.getWorld().spawnParticle(
                            Particle.GUST,
                            current,
                            10, 0.3, 0.3, 0.3, 0.1
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 3;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void stonePrimary(Player player, int level) {
        player.sendMessage("§7§l⛰️ STONE BREATHING: ROCK PROJECTILE ⛰️");
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0,
                        Material.STONE.createBlockData()
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setVelocity(new Vector(0, 1, 0));
                        
                        player.getWorld().spawnParticle(
                            Particle.BLOCK,
                            current,
                            20, 0.5, 0.5, 0.5, 0,
                            Material.STONE.createBlockData()
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void mistPrimary(Player player, int level) {
        player.sendMessage("§7§l🌫️ MIST BREATHING: OBSCURING CLOUD 🌫️");
        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 5;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.2;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, current.getY(), z,
                        5, 0.2, 0.2, 0.2, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        for (int j = 0; j < 10; j++) {
                            player.getWorld().spawnParticle(
                                Particle.SMOKE,
                                e.getLocation().add(0, 1, 0),
                                10, 0.3, 0.3, 0.3, 0.01
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastPrimary(Player player, int level) {
        player.sendMessage("§6§l🐗 BEAST BREATHING: FANG STRIKE 🐗");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 1.0f, 1.0f);
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        x, current.getY(), z,
                        2, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().spawnParticle(
                            Particle.SWEEP_ATTACK,
                            current,
                            10, 0.5, 0.5, 0.5, 0
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void soundPrimary(Player player, int level) {
        player.sendMessage("§e§l🔊 SOUND BREATHING: SONIC WAVE 🔊");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.2f);
        
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
                    double radius = 1.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        x, current.getY(), z,
                        1, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().spawnParticle(
                            Particle.SONIC_BOOM,
                            current,
                            5, 0.3, 0.3, 0.3, 0
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 3;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void serpentPrimary(Player player, int level) {
        player.sendMessage("§a§l🐍 SERPENT BREATHING: COILING STRIKE 🐍");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_AMBIENT, 1.0f, 1.0f);
        
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
                
                double offset = Math.sin(distance * 0.3) * 0.5;
                double x = current.getX() + offset * Math.cos(distance);
                double z = current.getZ() + offset * Math.sin(distance);
                
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    x, current.getY(), z,
                    3, 0.1, 0.1, 0.1, 0.01
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        for (int j = 0; j < 8; j++) {
                            double angle = Math.toRadians(j * 45);
                            double bx = e.getLocation().getX() + 1.5 * Math.cos(angle);
                            double bz = e.getLocation().getZ() + 1.5 * Math.sin(angle);
                            
                            player.getWorld().spawnParticle(
                                Particle.SCULK_SOUL,
                                bx, e.getLocation().getY() + 1, bz,
                                2, 0, 0, 0, 0.01
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void lovePrimary(Player player, int level) {
        player.sendMessage("§d§l💖 LOVE BREATHING: HEART STRIKE 💖");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 1.2f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 5;
        int heal = level * 2;
        
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
                    double radius = 0.8;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.HEART,
                        x, current.getY(), z,
                        1, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
                        
                        for (int j = 0; j < 10; j++) {
                            player.getWorld().spawnParticle(
                                Particle.HEART,
                                e.getLocation().add(0, 1, 0),
                                5, 0.3, 0.3, 0.3, 0
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void sovereignPrimary(Player player, int level) {
        player.sendMessage("§5§l👑 SHADOW SOVEREIGN: SHADOW DAGGER 👑");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 8;
        
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
                    double radius = 0.8;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        x, current.getY() + 0.2, z,
                        1, 0, 0, 0, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            e.getLocation().add(0, 1, 0),
                            20, 0.3, 0.3, 0.3, 0.3
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 3;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void demonKingPrimary(Player player, int level) {
        player.sendMessage("§b§l👹 DEMON KING: FROST SPEAR 👹");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setFreezeTicks(100);
                        
                        player.getWorld().spawnParticle(
                            Particle.BLOCK,
                            current,
                            15, 0.5, 0.5, 0.5, 0,
                            Material.ICE.createBlockData()
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastLordPrimary(Player player, int level) {
        player.sendMessage("§6§l🐺 BEAST LORD: CLAW STRIKE 🐺");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                
                for (int i = 0; i < 3; i++) {
                    double offset = (i - 1) * 0.5;
                    double x = current.getX() + offset * Math.cos(distance);
                    double z = current.getZ() + offset * Math.sin(distance);
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        x, current.getY(), z,
                        2, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().spawnParticle(
                            Particle.SWEEP_ATTACK,
                            current,
                            10, 0.5, 0.5, 0.5, 0
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void snowPrimary(Player player, int level) {
        player.sendMessage("§b§l❄️ SNOW FIEND: ICE SHARD ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
        
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
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i);
                    double radius = 0.8;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setFreezeTicks(100);
                        
                        player.getWorld().spawnParticle(
                            Particle.ITEM_SNOWBALL,
                            current,
                            15, 0.5, 0.5, 0.5, 0
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void limitlessPrimary(Player player, int level) {
        player.sendMessage("§d§l∞ LIMITLESS: HOLLOW PURPLE ∞");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 10;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.2;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0.3
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY() + 0.3, z,
                        2, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().createExplosion(current, 0, false, false);
                        player.getWorld().spawnParticle(
                            Particle.FLASH,
                            current,
                            1, 0, 0, 0, 0
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void shadowsPrimary(Player player, int level) {
        player.sendMessage("§8§l🕷️ TEN SHADOWS: DIVINE DOGS 🕷️");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                
                for (int i = 0; i < 2; i++) {
                    double offset = (i * 2 - 1) * 0.8;
                    double x = current.getX() + offset * Math.cos(distance * 0.5);
                    double z = current.getZ() + offset * Math.sin(distance * 0.5);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        for (int j = 0; j < 8; j++) {
                            player.getWorld().spawnParticle(
                                Particle.SCULK_SOUL,
                                e.getLocation().add(0, 1, 0),
                                5, 0.3, 0.3, 0.3, 0.01
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void disasterPrimary(Player player, int level) {
        player.sendMessage("§c§l🔥 DISASTER FLAMES: VOLCANIC ERUPTION 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0.02
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.LAVA,
                        x, current.getY() - 0.2, z,
                        1, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        e.setFireTicks(150);
                        
                        player.getWorld().createExplosion(current, 2, false, true);
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void bloodPrimary(Player player, int level) {
        player.sendMessage("§4§l🩸 BLOOD MANIPULATION: BLOOD BLADE 🩸");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 8;
        
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
                
                for (int i = 0; i < 3; i++) {
                    double offset = (i - 1) * 0.6;
                    double x = current.getX() + offset * Math.cos(distance);
                    double z = current.getZ() + offset * Math.sin(distance);
                    
                    player.getWorld().spawnParticle(
                        Particle.FALLING_LAVA,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        for (int j = 0; j < 10; j++) {
                            player.getWorld().spawnParticle(
                                Particle.FALLING_LAVA,
                                e.getLocation().add(0, 1, 0),
                                5, 0.3, 0.3, 0.3, 0
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void comedyPrimary(Player player, int level) {
        player.sendMessage("§a§l🎭 COMEDY: CURSED SPEECH 🎭");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 1.2f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 5;
        
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
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i);
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.NOTE,
                        x, current.getY(), z,
                        1, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        for (int j = 0; j < 8; j++) {
                            player.getWorld().spawnParticle(
                                Particle.NOTE,
                                e.getLocation().add(0, 1, 0),
                                3, 0.2, 0.2, 0.2, 0
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void spiritPrimary(Player player, int level) {
        player.sendMessage("§b§l💫 SPIRIT BOMB: ENERGY SPHERE 💫");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 9;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY(), z,
                        5, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        x, current.getY() + 0.3, z,
                        3, 0, 0, 0, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2.5, 2.5, 2.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().createExplosion(current, 3, false, true);
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void kamePrimary(Player player, int level) {
        player.sendMessage("§b§l🌊 KAMEHAMEHA: ENERGY WAVE 🌊");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 9;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY(), z,
                        5, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2.5, 2.5, 2.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().createExplosion(current, 3, false, true);
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void instantPrimary(Player player, int level) {
        player.sendMessage("§e§l⚡ INSTANT TRANSMISSION: AFTERIMAGE STRIKE ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                    double radius = 0.8;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        x, current.getY(), z,
                        2, 0, 0, 0, 0.2
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        Location behind = e.getLocation().add(e.getLocation().getDirection().multiply(-2));
                        player.teleport(behind);
                        
                        for (int j = 0; j < 10; j++) {
                            player.getWorld().spawnParticle(
                                Particle.PORTAL,
                                e.getLocation().add(0, 1, 0),
                                10, 0.3, 0.3, 0.3, 0.3
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 3;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void solarPrimary(Player player, int level) {
        player.sendMessage("§6§l☀️ SOLAR FLARE: BRIGHT EXPLOSION ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.2f);
        
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
                    double radius = 1.2;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        x, current.getY(), z,
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLASH,
                            current,
                            1, 0, 0, 0, 0
                        );
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void galaxyPrimary(Player player, int level) {
        player.sendMessage("§5§l🌌 GALAXY BREAKER: COSMIC BLAST 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 10;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.5;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        x, current.getY(), z,
                        5, 0.1, 0.1, 0.1, 0.3
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        x, current.getY() + 0.3, z,
                        3, 0, 0, 0, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2.5, 2.5, 2.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().createExplosion(current, 4, false, true);
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void reaverPrimary(Player player, int level) {
        player.sendMessage("§8§l🌑 VOID REAVER: VOID SLASH 🌑");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 8;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        x, current.getY(), z,
                        5, 0.1, 0.1, 0.1, 0.4
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        Location randomLoc = e.getLocation().clone().add(
                            random.nextDouble() * 5 - 2.5,
                            random.nextDouble() * 2,
                            random.nextDouble() * 5 - 2.5
                        );
                        e.teleport(randomLoc);
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void eaterPrimary(Player player, int level) {
        player.sendMessage("§2§l💀 SOUL EATER: SOUL DRAIN 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.7f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        int heal = level * 2;
        
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
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i);
                    double radius = 0.8;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 1.5, 1.5, 1.5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
                        
                        for (int j = 0; j < 10; j++) {
                            player.getWorld().spawnParticle(
                                Particle.HEART,
                                player.getLocation().add(0, 1, 0),
                                3, 0.2, 0.2, 0.2, 0
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void starfallPrimary(Player player, int level) {
        player.sendMessage("§e§l✨ STAR FALL: METEOR SHOWER ✨");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 8;
        
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
                    double radius = 1.2;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        player.getWorld().createExplosion(current, 3, false, true);
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void timePrimary(Player player, int level) {
        player.sendMessage("§b§l⏰ TIME STOP: TIME FREEZE ⏰");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 8;
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        x, current.getY(), z,
                        4, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        e.setVelocity(new Vector(0, 0, 0));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 10));
                        
                        for (int j = 0; j < 20; j++) {
                            player.getWorld().spawnParticle(
                                Particle.GLOW,
                                e.getLocation().add(0, 1, 0),
                                3, 0.2, 0.2, 0.2, 0
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void writerPrimary(Player player, int level) {
        player.sendMessage("§d§l📝 REALITY WRITER: REWRITE 📝");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = level * 7;
        
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
                    double radius = 1.0;
                    
                    double x = current.getX() + radius * Math.cos(angle);
                    double z = current.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.ENCHANT,
                        x, current.getY(), z,
                        5, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        if (random.nextBoolean()) {
                            e.setFireTicks(60);
                        } else {
                            e.setVelocity(new Vector(0, 1, 0));
                        }
                        
                        for (int j = 0; j < 15; j++) {
                            player.getWorld().spawnParticle(
                                Particle.ENCHANT,
                                e.getLocation().add(0, 1, 0),
                                10, 0.3, 0.3, 0.3, 0
                            );
                        }
                        
                        cancel();
                        return;
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // ========== EPIC ABILITIES (ULTIMATE - LEVEL 3) ==========
    
    private void sunUltimate(Player player, int level) {
        player.sendMessage("§6§l☀️ SUN BREATHING ULTIMATE: SOLAR FLARE ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        Location center = player.getLocation().add(0, 3, 0);
        int damage = 15 + level * 5;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100; // 5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage * 2, player);
                            e.setFireTicks(100);
                            
                            Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                            e.setVelocity(pull.multiply(1.5));
                        }
                    }
                    
                    player.getWorld().createExplosion(center, 0, false, false);
                    player.getWorld().spawnParticle(Particle.FLASH, center, 1, 0, 0, 0, 0);
                    cancel();
                    return;
                }
                
                double radius = 2.0 + Math.sin(ticks * 0.2) * 1.0;
                
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
                        Particle.BUBBLE_POP,
                        center.clone().add(x * 0.5, y + 0.5, z * 0.5),
                        2, 0, 0, 0, 0.01
                    );
                }
                
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
                
                if (ticks % 10 == 0) {
                    stormLoc.add(random.nextDouble() * 4 - 2, 0, random.nextDouble() * 4 - 2);
                }
                
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
                    Vector toTarget = target.getLocation().toVector().subtract(phoenixLoc.toVector()).normalize();
                    phoenixLoc.add(toTarget.multiply(0.5));
                    
                    if (nearestDist < 3) {
                        target.damage(damage / 2, player);
                        target.setFireTicks(60);
                    }
                }
                
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
                    Vector toTarget = target.getLocation().toVector().subtract(golemLoc.toVector()).normalize();
                    golemLoc.add(toTarget.multiply(0.2));
                    
                    if (nearestDist < 3) {
                        target.damage(damage, player);
                        target.setVelocity(new Vector(0, 1, 0));
                        
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
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 5, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 2));
                        ((LivingEntity) e).damage(1, player);
                        
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
    
    private void beastUltimate(Player player, int level) {
        player.sendMessage("§6§l🐗 BEAST BREATHING ULTIMATE: WOLF PACK 🐗");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 0.6f);
        
        int wolfCount = 4 + level;
        int damage = 5 + level * 2;
        
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
                        Vector toTarget = target.getLocation().toVector().subtract(wolfLoc.toVector()).normalize();
                        wolfLoc.add(toTarget.multiply(0.3));
                        
                        if (nearestDist < 2) {
                            target.damage(damage, player);
                            
                            player.getWorld().spawnParticle(
                                Particle.SWEEP_ATTACK,
                                target.getLocation().add(0, 1, 0),
                                10, 0.3, 0.3, 0.3, 0
                            );
                        }
                    }
                    
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
                
                for (int i = -30; i <= 30; i += 10) {
                    double angle = Math.toRadians(i);
                    Vector rotated = direction.clone().rotateAroundY(angle);
                    
                    Location current = start.clone().add(rotated.clone().multiply(distance));
                    
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        current,
                        2, 0.2, 0.2, 0.2, 0
                    );
                    
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
                    Vector toTarget = target.getLocation().toVector().subtract(snakeSegments.get(0).toVector()).normalize();
                    snakeSegments.set(0, snakeSegments.get(0).clone().add(toTarget.multiply(0.4)));
                    
                    if (nearestDist < 2) {
                        target.damage(damage, player);
                    }
                }
                
                for (int i = snakeSegments.size() - 1; i > 0; i--) {
                    snakeSegments.set(i, snakeSegments.get(i - 1).clone());
                }
                
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
                        ((LivingEntity) e).damage(damage, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
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
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                        
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.2));
                        
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
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 5, radius + 1)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(200);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 5));
                        ((LivingEntity) e).damage(1, player);
                        
                        Vector push = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(push.multiply(0.1));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastLordUltimate(Player player, int level) {
        player.sendMessage("§6§l🐺 BEAST LORD ULTIMATE: PRIMAL ROAR 🐺");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        int damage = 12 + level * 4;
        
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
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(2, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
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
                    Vector toTarget = target.getLocation().toVector().subtract(beastLoc.toVector()).normalize();
                    beastLoc.add(toTarget.multiply(0.3));
                    
                    if (nearestDist < 3) {
                        target.damage(damage, player);
                        
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
                
                if (ticks % 10 == 0) {
                    for (int i = 0; i < 3; i++) {
                        double angle = random.nextDouble() * Math.PI * 2;
                        double radius = random.nextDouble() * 8;
                        
                        double x = center.getX() + radius * Math.cos(angle);
                        double z = center.getZ() + radius * Math.sin(angle);
                        
                        Location eruptionLoc = new Location(player.getWorld(), x, center.getY(), z);
                        
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
                    
                    for (Entity e : player.getWorld().getNearbyEntities(orbLoc, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                            
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
                
                if (ticks % 20 == 0) {
                    int effect = random.nextInt(5);
                    
                    switch(effect) {
                        case 0:
                            for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                                if (e instanceof LivingEntity && e != player) {
                                    player.getWorld().strikeLightning(e.getLocation());
                                }
                            }
                            break;
                            
                        case 1:
                            player.getWorld().createExplosion(center, 2, false, false);
                            break;
                            
                        case 2:
                            for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(new Vector(0, 2, 0));
                                }
                            }
                            break;
                            
                        case 3:
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
                            
                        case 4:
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
                    for (Entity e : player.getWorld().getNearbyEntities(center, radius + 2, radius + 2, radius + 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                    
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
                
                Vector currentDir = player.getLocation().getDirection().normalize();
                Location currentStart = player.getEyeLocation();
                
                Location current = currentStart.clone().add(currentDir.clone().multiply(distance));
                
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
                
                List<LivingEntity> enemies = plugin.getTargetSeekingCombat().getNearbyEnemies(player, 20);
                
                if (!enemies.isEmpty()) {
                    LivingEntity target = enemies.get(random.nextInt(enemies.size()));
                    
                    Location behind = target.getLocation().add(target.getLocation().getDirection().multiply(-2));
                    player.teleport(behind);
                    
                    target.damage(damage, player);
                    
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
                    for (Entity e : player.getWorld().getNearbyEntities(center, 12, 12, 12)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                            
                            Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                            e.setVelocity(pull.multiply(2));
                        }
                    }
                    
                    player.getWorld().createExplosion(center, 0, false, false);
                    cancel();
                    return;
                }
                
                double radius = 6.0 * (1 - (double)ticks / DURATION);
                
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
                
                if (ticks % 10 == 0) {
                    for (int i = 0; i < 5; i++) {
                        double angle = random.nextDouble() * Math.PI * 2;
                        double radius = random.nextDouble() * 8;
                        
                        double x = center.getX() + radius * Math.cos(angle);
                        double z = center.getZ() + radius * Math.sin(angle);
                        
                        Location riftLoc = new Location(player.getWorld(), x, center.getY(), z);
                        
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
                        
                        for (Entity e : player.getWorld().getNearbyEntities(riftLoc, 3, 3, 3)) {
                            if (e instanceof LivingEntity && e != player) {
                                ((LivingEntity) e).damage(damage, player);
                                
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
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                    if (e instanceof LivingEntity && e != player) {
                        LivingEntity target = (LivingEntity) e;
                        
                        target.damage(damage / 4, player);
                        player.setHealth(Math.min(player.getHealth() + heal / 4, player.getMaxHealth()));
                        
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
    
    private void timeUltimate(Player player, int level) {
        player.sendMessage("§b§l⏰ TIME STOP ULTIMATE: ZA WARUDO ⏰");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        int damage = 20 + level * 6;
        
        player.sendTitle("§bTHE WORLD", "§fTime has stopped!", 10, 60, 20);
        
        Map<Entity, Vector> velocities = new HashMap<>();
        
        for (Entity e : player.getWorld().getNearbyEntities(center, 15, 15, 15)) {
            if (e instanceof LivingEntity && e != player) {
                velocities.put(e, e.getVelocity());
                e.setVelocity(new Vector(0, 0, 0));
            }
        }
        
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity e : player.getWorld().getNearbyEntities(center, 15, 15, 15)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        if (velocities.containsKey(e)) {
                            e.setVelocity(velocities.get(e));
                        }
                        
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
        }.runTaskLater(plugin, 60L);
    }
    
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
                
                if (ticks % 20 == 0) {
                    int effect = random.nextInt(4);
                    
                    switch(effect) {
                        case 0:
                            for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(new Vector(0, 2, 0));
                                }
                            }
                            break;
                            
                        case 1:
                            List<LivingEntity> enemies = plugin.getTargetSeekingCombat().getNearbyEnemies(player, 10);
                            if (enemies.size() >= 2) {
                                Location loc1 = enemies.get(0).getLocation();
                                Location loc2 = enemies.get(1).getLocation();
                                enemies.get(0).teleport(loc2);
                                enemies.get(1).teleport(loc1);
                            }
                            break;
                            
                        case 2:
                            for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                                if (e instanceof LivingEntity && e != player) {
                                    ((LivingEntity) e).damage(damage / 2, player);
                                }
                            }
                            break;
                            
                        case 3:
                            for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                                if (e instanceof LivingEntity && e != player) {
                                    e.setVelocity(e.getVelocity().multiply(2));
                                }
                            }
                            break;
                    }
                }
                
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
    
    // ========== ADVANCED ABILITIES (LEVEL 2) - All 30 ==========
    
    private void sunAdvanced(Player player, int level) {
        player.sendMessage("§6§l☀️ SUN BREATHING: SOLAR FLARE (Advanced) ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        
        Location center = player.getLocation().add(0, 2, 0);
        int damage = 8 + level * 3;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
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
        player.playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80; // 4 seconds
            
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
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 5 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
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
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void flameAdvanced(Player player, int level) {
        player.sendMessage("§c§l🔥 FLAME BREATHING: FIRE WALL (Advanced) 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 70; // 3.5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 3.0;
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 4);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0.02
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
        player.sendMessage("§f§l🌪️ WIND BREATHING: WIND BLAST (Advanced) 🌪️");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 50; // 2.5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 4.0;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 6);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0.02
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.GUST,
                        x, center.getY() + 1.5, z,
                        1, 0, 0, 0, 0.05
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 4, radius)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector away = e.getLocation().toVector().subtract(center.toVector()).normalize();
                        e.setVelocity(away.multiply(0.5));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void stoneAdvanced(Player player, int level) {
        player.sendMessage("§7§l⛰️ STONE BREATHING: ROCK SHIELD (Advanced) ⛰️");
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80; // 4 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 2.5;
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0,
                        Material.STONE.createBlockData()
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                        e.setVelocity(new Vector(0, 0.5, 0));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void mistAdvanced(Player player, int level) {
        player.sendMessage("§7§l🌫️ MIST BREATHING: DENSE FOG (Advanced) 🌫️");
        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 5 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 100; // 5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 4.0;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        x, center.getY() + 1, z,
                        4, 0.2, 0.2, 0.2, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 4, radius)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastAdvanced(Player player, int level) {
        player.sendMessage("§6§l🐗 BEAST BREATHING: WILD CHARGE (Advanced) 🐗");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double angle = Math.toRadians(ticks * 15);
                double x = center.getX() + 3 * Math.cos(angle);
                double z = center.getZ() + 3 * Math.sin(angle);
                
                player.getWorld().spawnParticle(
                    Particle.SWEEP_ATTACK,
                    x, center.getY() + 1, z,
                    3, 0.1, 0.1, 0.1, 0
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
    
    private void soundAdvanced(Player player, int level) {
        player.sendMessage("§e§l🔊 SOUND BREATHING: SONIC PULSE (Advanced) 🔊");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 50; // 2.5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = ticks * 0.2;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SONIC_BOOM,
                        x, center.getY() + 1, z,
                        1, 0, 0, 0, 0
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
    
    private void serpentAdvanced(Player player, int level) {
        player.sendMessage("§a§l🐍 SERPENT BREATHING: COILING STRIKE (Advanced) 🐍");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_AMBIENT, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 70; // 3.5 seconds
            double lastAngle = 0;
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                lastAngle += 0.3;
                double radius = 2.5;
                double x = center.getX() + radius * Math.cos(lastAngle);
                double z = center.getZ() + radius * Math.sin(lastAngle);
                
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
        player.sendMessage("§d§l💖 LOVE BREATHING: HEALING AURA (Advanced) 💖");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int heal = level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80; // 4 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 3.0;
                
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.HEART,
                        x, center.getY() + 1, z,
                        1, 0, 0, 0, 0
                    );
                }
                
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(center) <= 4 && p != player) {
                        if (p.getHealth() < p.getMaxHealth()) {
                            p.setHealth(Math.min(p.getHealth() + 1, p.getMaxHealth()));
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void sovereignAdvanced(Player player, int level) {
        player.sendMessage("§5§l👑 SHADOW SOVEREIGN: SHADOW ARMY (Advanced) 👑");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        for (int i = 0; i < 3; i++) {
            int index = i;
            new BukkitRunnable() {
                int ticks = 0;
                final int DURATION = 60; // 3 seconds
                Location shadowLoc = center.clone().add(
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
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        shadowLoc.clone().add(0, 1, 0),
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(shadowLoc, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(1, player);
                        }
                    }
                    
                    ticks++;
                }
            }.runTaskTimer(plugin, i * 10L, 1L);
        }
    }
    
    private void demonKingAdvanced(Player player, int level) {
        player.sendMessage("§b§l👹 DEMON KING: ICE PRISON (Advanced) 👹");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 80; // 4 seconds
            
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
                        Particle.SNOWFLAKE,
                        x, center.getY() + 1, z,
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setFreezeTicks(40);
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void beastLordAdvanced(Player player, int level) {
        player.sendMessage("§6§l🐺 BEAST LORD: WOLF PACK (Advanced) 🐺");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        for (int i = 0; i < 3; i++) {
            int index = i;
            new BukkitRunnable() {
                int ticks = 0;
                final int DURATION = 70; // 3.5 seconds
                Location wolfLoc = center.clone().add(
                    random.nextDouble() * 5 - 2.5,
                    0,
                    random.nextDouble() * 5 - 2.5
                );
                
                @Override
                public void run() {
                    if (ticks >= DURATION) {
                        cancel();
                        return;
                    }
                    
                    player.getWorld().spawnParticle(
                        Particle.SWEEP_ATTACK,
                        wolfLoc.clone().add(0, 1, 0),
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(wolfLoc, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(1, player);
                        }
                    }
                    
                    ticks++;
                }
            }.runTaskTimer(plugin, i * 8L, 1L);
        }
    }
    
    private void snowAdvanced(Player player, int level) {
        player.sendMessage("§b§l❄️ SNOW FIEND: BLIZZARD (Advanced) ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 90; // 4.5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                double radius = 4.0;
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 4);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        x, center.getY() + 1, z,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setFreezeTicks(30);
                        ((LivingEntity) e).damage(1, player);
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void limitlessAdvanced(Player player, int level) {
        player.sendMessage("§d§l∞ LIMITLESS: HOLLOW PURPLE (Advanced) ∞");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.9f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 9 + level * 3;
        
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
                    4, 0.2, 0.2, 0.2, 0.3
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void shadowsAdvanced(Player player, int level) {
        player.sendMessage("§8§l🕷️ TEN SHADOWS: DIVINE DOGS (Advanced) 🕷️");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        for (int i = 0; i < 2; i++) {
            int index = i;
            new BukkitRunnable() {
                int ticks = 0;
                final int DURATION = 60; // 3 seconds
                Location dogLoc = center.clone().add(
                    random.nextDouble() * 6 - 3,
                    0,
                    random.nextDouble() * 6 - 3
                );
                
                @Override
                public void run() {
                    if (ticks >= DURATION) {
                        cancel();
                        return;
                    }
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        dogLoc.clone().add(0, 1, 0),
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(dogLoc, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(1, player);
                        }
                    }
                    
                    ticks++;
                }
            }.runTaskTimer(plugin, i * 10L, 1L);
        }
    }
    
    private void disasterAdvanced(Player player, int level) {
        player.sendMessage("§c§l🔥 DISASTER FLAMES: MAGMA BURST (Advanced) 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 10 == 0) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * 4;
                    
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.LAVA,
                        x, center.getY() + 1, z,
                        5, 0.2, 0.2, 0.2, 0
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(new Location(player.getWorld(), x, center.getY(), z), 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(1, player);
                            e.setFireTicks(30);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void bloodAdvanced(Player player, int level) {
        player.sendMessage("§4§l🩸 BLOOD MANIPULATION: BLOOD ORBS (Advanced) 🩸");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        int heal = level;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 70; // 3.5 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 90) {
                    double angle = Math.toRadians(i + ticks * 4);
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
                        ((LivingEntity) e).damage(1, player);
                        player.setHealth(Math.min(player.getHealth() + 1, player.getMaxHealth()));
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void comedyAdvanced(Player player, int level) {
        player.sendMessage("§a§l🎭 COMEDY: PRANKSTER (Advanced) 🎭");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 10 == 0) {
                    for (int i = 0; i < 5; i++) {
                        player.getWorld().spawnParticle(
                            Particle.NOTE,
                            center.clone().add(random.nextDouble() * 5 - 2.5, random.nextDouble() * 2, random.nextDouble() * 5 - 2.5),
                            1, 0, 0, 0, 0
                        );
                    }
                    
                    for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                        if (e instanceof LivingEntity && e != player) {
                            e.setVelocity(new Vector(random.nextDouble() - 0.5, random.nextDouble() * 0.5, random.nextDouble() - 0.5));
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void spiritAdvanced(Player player, int level) {
        player.sendMessage("§b§l💫 SPIRIT BOMB: ENERGY SPHERE (Advanced) 💫");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.9f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 8 + level * 3;
        
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
                    Particle.END_ROD,
                    current,
                    5, 0.2, 0.2, 0.2, 0
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void kameAdvanced(Player player, int level) {
        player.sendMessage("§b§l🌊 KAMEHAMEHA: ENERGY WAVE (Advanced) 🌊");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 0.9f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 9 + level * 3;
        
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
                        ((LivingEntity) e).damage(damage, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void instantAdvanced(Player player, int level) {
        player.sendMessage("§e§l⚡ INSTANT TRANSMISSION: BLINK (Advanced) ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.9f);
        
        int damage = 5 + level * 2;
        int teleports = 3;
        
        new BukkitRunnable() {
            int count = 0;
            
            @Override
            public void run() {
                if (count >= teleports) {
                    cancel();
                    return;
                }
                
                List<LivingEntity> enemies = plugin.getTargetSeekingCombat().getNearbyEnemies(player, 10);
                
                if (!enemies.isEmpty()) {
                    LivingEntity target = enemies.get(random.nextInt(enemies.size()));
                    
                    Location behind = target.getLocation().add(target.getLocation().getDirection().multiply(-2));
                    player.teleport(behind);
                    
                    target.damage(damage, player);
                    
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
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                ((LivingEntity) e).damage(damage, player);
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
                3, 0.1, 0.1, 0.1, 0.01
            );
        }
    }
    
    private void galaxyAdvanced(Player player, int level) {
        player.sendMessage("§5§l🌌 GALAXY BREAKER: COSMIC BLAST (Advanced) 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 10 + level * 3;
        
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
                    5, 0.2, 0.2, 0.2, 0.3
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void reaverAdvanced(Player player, int level) {
        player.sendMessage("§8§l🌑 VOID REAVER: VOID SLASH (Advanced) 🌑");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.8f);
        
        Location start = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();
        int damage = 8 + level * 2;
        
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
                    Particle.PORTAL,
                    current,
                    4, 0.1, 0.1, 0.1, 0.4
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(damage, player);
                        
                        if (random.nextInt(100) < 20) {
                            Location randomLoc = e.getLocation().clone().add(
                                random.nextDouble() * 3 - 1.5,
                                0,
                                random.nextDouble() * 3 - 1.5
                            );
                            e.teleport(randomLoc);
                        }
                    }
                }
                
                distance += 2;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void eaterAdvanced(Player player, int level) {
        player.sendMessage("§2§l💀 SOUL EATER: SOUL DRAIN (Advanced) 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        int heal = level;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(1, player);
                        player.setHealth(Math.min(player.getHealth() + 1, player.getMaxHealth()));
                        
                        player.getWorld().spawnParticle(
                            Particle.SOUL,
                            e.getLocation().add(0, 1, 0),
                            2, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    private void starfallAdvanced(Player player, int level) {
        player.sendMessage("§e§l✨ STAR FALL: METEOR SHOWER (Advanced) ✨");
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
            @Override
            public void run() {
                if (ticks >= DURATION) {
                    cancel();
                    return;
                }
                
                if (ticks % 5 == 0) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * 4;
                    
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
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void timeAdvanced(Player player, int level) {
        player.sendMessage("§b§l⏰ TIME STOP: TIME SLOW (Advanced) ⏰");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 7 + level * 2;
        
        for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 3));
                ((LivingEntity) e).damage(damage, player);
                e.setVelocity(new Vector(0, 0, 0));
            }
        }
        
        player.getWorld().spawnParticle(
            Particle.GLOW,
            center.clone().add(0, 2, 0),
            30, 2, 2, 2, 0
        );
    }
    
    private void writerAdvanced(Player player, int level) {
        player.sendMessage("§d§l📝 REALITY WRITER: REWRITE (Advanced) 📝");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.9f);
        
        Location center = player.getLocation();
        int damage = 6 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            final int DURATION = 60; // 3 seconds
            
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
                
                if (ticks % 15 == 0) {
                    for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                            
                            if (random.nextBoolean()) {
                                e.setFireTicks(30);
                            } else {
                                e.setVelocity(new Vector(0, 1, 0));
                            }
                        }
                    }
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
