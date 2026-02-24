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
    
    // ========== MAIN ABILITY EXECUTION METHODS ==========
    
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
        
        // Check cooldown
        if (plugin.getCooldownManager().isOnCooldown(player, magicBook)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, magicBook);
            player.sendMessage("§c❌ " + magicBook.getDisplayName() + " §7is on cooldown for §f" + remaining + "s");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.5f);
            return;
        }
        
        // Set cooldown with level multiplier
        double multiplier = plugin.getLevelManager().getCooldownMultiplier(level);
        int baseCooldown = magicBook.getCooldown();
        int effectiveCooldown = (int)(baseCooldown * multiplier);
        
        plugin.getCooldownManager().setCustomCooldown(player, magicBook, effectiveCooldown);
        
        // Execute normal ability (Level 1)
        executeNormalAbility(player, magicBook, level);
    }
    
    public void executeAdvancedAbility(Player player, MagicBook book, int level) {
        // Check cooldown for advanced ability
        if (plugin.getCooldownManager().isOnCooldown(player, book)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, book);
            player.sendMessage("§c❌ Advanced ability on cooldown for §f" + remaining + "s");
            return;
        }
        
        // Set longer cooldown for advanced abilities
        plugin.getCooldownManager().setCustomCooldown(player, book, book.getCooldown() * 2);
        
        switch(book.getAbilityKey().toLowerCase()) {
            case "storm": advancedStormAbility(player, level); break;
            case "shadow": advancedShadowAbility(player, level); break;
            case "flame": advancedFlameAbility(player, level); break;
            case "frost": advancedFrostAbility(player, level); break;
            case "dragon": advancedDragonAbility(player, level); break;
            case "void": advancedVoidAbility(player, level); break;
            case "life": advancedLifeAbility(player, level); break;
            case "gravity": advancedGravityAbility(player, level); break;
            case "phantom": advancedPhantomAbility(player, level); break;
            case "dawn": advancedDawnAbility(player, level); break;
            case "terra": advancedTerraAbility(player, level); break;
            case "wind": advancedWindAbility(player, level); break;
            case "time": advancedTimeAbility(player, level); break;
            case "soul": advancedSoulAbility(player, level); break;
            case "crystal": advancedCrystalAbility(player, level); break;
            case "thunder": advancedThunderAbility(player, level); break;
            case "ice": advancedIceAbility(player, level); break;
            case "pyro": advancedPyroAbility(player, level); break;
            case "spirit": advancedSpiritAbility(player, level); break;
            case "necro": advancedNecroAbility(player, level); break;
            case "seraph": advancedSeraphAbility(player, level); break;
            case "abyss": advancedAbyssAbility(player, level); break;
            case "chaos": advancedChaosAbility(player, level); break;
            case "judge": advancedJudgeAbility(player, level); break;
            case "dream": advancedDreamAbility(player, level); break;
            case "fear": advancedFearAbility(player, level); break;
            case "aurora": advancedAuroraAbility(player, level); break;
            case "star": advancedStarAbility(player, level); break;
            case "inferno": advancedInfernoAbility(player, level); break;
            case "avalanche": advancedAvalancheAbility(player, level); break;
        }
    }
    
    public void executeUltimateAbility(Player player, MagicBook book, int level) {
        // Check cooldown for ultimate ability
        if (plugin.getCooldownManager().isOnCooldown(player, book)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, book);
            player.sendMessage("§c❌ Ultimate ability on cooldown for §f" + remaining + "s");
            return;
        }
        
        // Set longest cooldown for ultimate abilities
        plugin.getCooldownManager().setCustomCooldown(player, book, book.getCooldown() * 3);
        
        switch(book.getAbilityKey().toLowerCase()) {
            case "storm": ultimateStormAbility(player, level); break;
            case "shadow": ultimateShadowAbility(player, level); break;
            case "flame": ultimateFlameAbility(player, level); break;
            case "frost": ultimateFrostAbility(player, level); break;
            case "dragon": ultimateDragonAbility(player, level); break;
            case "void": ultimateVoidAbility(player, level); break;
            case "life": ultimateLifeAbility(player, level); break;
            case "gravity": ultimateGravityAbility(player, level); break;
            case "phantom": ultimatePhantomAbility(player, level); break;
            case "dawn": ultimateDawnAbility(player, level); break;
            case "terra": ultimateTerraAbility(player, level); break;
            case "wind": ultimateWindAbility(player, level); break;
            case "time": ultimateTimeAbility(player, level); break;
            case "soul": ultimateSoulAbility(player, level); break;
            case "crystal": ultimateCrystalAbility(player, level); break;
            case "thunder": ultimateThunderAbility(player, level); break;
            case "ice": ultimateIceAbility(player, level); break;
            case "pyro": ultimatePyroAbility(player, level); break;
            case "spirit": ultimateSpiritAbility(player, level); break;
            case "necro": ultimateNecroAbility(player, level); break;
            case "seraph": ultimateSeraphAbility(player, level); break;
            case "abyss": ultimateAbyssAbility(player, level); break;
            case "chaos": ultimateChaosAbility(player, level); break;
            case "judge": ultimateJudgeAbility(player, level); break;
            case "dream": ultimateDreamAbility(player, level); break;
            case "fear": ultimateFearAbility(player, level); break;
            case "aurora": ultimateAuroraAbility(player, level); break;
            case "star": ultimateStarAbility(player, level); break;
            case "inferno": ultimateInfernoAbility(player, level); break;
            case "avalanche": ultimateAvalancheAbility(player, level); break;
        }
    }
    
    private void executeNormalAbility(Player player, MagicBook book, int level) {
        switch(book.getAbilityKey().toLowerCase()) {
            case "storm": stormAbility(player); break;
            case "shadow": shadowAbility(player); break;
            case "flame": flameAbility(player); break;
            case "frost": frostAbility(player); break;
            case "dragon": dragonAbility(player); break;
            case "void": voidAbility(player); break;
            case "life": lifeAbility(player); break;
            case "gravity": gravityAbility(player); break;
            case "phantom": phantomAbility(player); break;
            case "dawn": dawnAbility(player); break;
            case "terra": terraAbility(player); break;
            case "wind": windAbility(player); break;
            case "time": timeAbility(player); break;
            case "soul": soulAbility(player); break;
            case "crystal": crystalAbility(player); break;
            case "thunder": thunderAbility(player); break;
            case "ice": iceAbility(player); break;
            case "pyro": pyroAbility(player); break;
            case "spirit": spiritAbility(player); break;
            case "necro": necroAbility(player); break;
            case "seraph": seraphAbility(player); break;
            case "abyss": abyssAbility(player); break;
            case "chaos": chaosAbility(player); break;
            case "judge": judgeAbility(player); break;
            case "dream": dreamAbility(player); break;
            case "fear": fearAbility(player); break;
            case "aurora": auroraAbility(player); break;
            case "star": starAbility(player); break;
            case "inferno": infernoAbility(player); break;
            case "avalanche": avalancheAbility(player); break;
        }
    }
    
    // ========== STORM ABILITIES ==========
    
    private void stormAbility(Player player) {
        player.sendMessage("§b§l🌩️ STORM CALLER 🌩️");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 3; i++) {
                    Location strikeLoc = center.clone().add(
                        random.nextInt(10) - 5,
                        0,
                        random.nextInt(10) - 5
                    );
                    
                    player.getWorld().strikeLightningEffect(strikeLoc);
                    
                    for (Entity e : player.getWorld().getNearbyEntities(strikeLoc, 3, 3, 3)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(8, player);
                            e.setFireTicks(40);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    private void advancedStormAbility(Player player, int level) {
        player.sendMessage("§b§l⚡ ADVANCED STORM: LIGHTNING BARRAGE ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 8 + (level * 2);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(center, 15, 15, 15)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        new BukkitRunnable() {
            int wave = 0;
            
            @Override
            public void run() {
                if (wave >= 5) {
                    cancel();
                    return;
                }
                
                for (LivingEntity target : targets) {
                    if (target.isDead()) continue;
                    
                    player.getWorld().strikeLightningEffect(target.getLocation());
                    
                    for (Entity nearby : target.getWorld().getNearbyEntities(target.getLocation(), 5, 5, 5)) {
                        if (nearby instanceof LivingEntity && nearby != player && nearby != target) {
                            player.getWorld().strikeLightningEffect(nearby.getLocation());
                            ((LivingEntity) nearby).damage(damage / 2, player);
                        }
                    }
                    
                    target.damage(damage, player);
                }
                
                wave++;
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    private void ultimateStormAbility(Player player, int level) {
        player.sendMessage("§6§l⚡ ULTIMATE STORM: GOD OF THUNDER ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "storm", level, () -> {
            player.getWorld().createExplosion(player.getLocation(), 0, false, false);
            
            for (int i = 0; i < 20; i++) {
                player.getWorld().spawnParticle(
                    Particle.FLASH,
                    player.getLocation().clone().add(random.nextDouble() * 10 - 5, random.nextDouble() * 5, random.nextDouble() * 10 - 5),
                    1, 0, 0, 0, 0
                );
            }
            
            player.sendTitle("§6⚡ THUNDER GOD §e⚡", "§fAll enemies defeated!", 10, 40, 20);
        });
    }
    
    // ========== SHADOW ABILITIES ==========
    
    private void shadowAbility(Player player) {
        player.sendMessage("§7§l👻 SHADOW DANCE 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.5f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.SMOKE,
                    player.getLocation().add(0, 1, 0),
                    10, 0.3, 0.5, 0.3, 0.01
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        Location behind = e.getLocation().add(
                            e.getLocation().getDirection().multiply(-2)
                        );
                        player.teleport(behind);
                        ((LivingEntity) e).damage(10, player);
                        break;
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    private void advancedShadowAbility(Player player, int level) {
        player.sendMessage("§7§l👻 ADVANCED SHADOW: SHADOW CLONES 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
        
        int cloneCount = level;
        
        for (int i = 0; i < cloneCount; i++) {
            int cloneIndex = i;
            new BukkitRunnable() {
                int steps = 0;
                Location startLoc = player.getLocation();
                
                @Override
                public void run() {
                    if (steps >= 20) {
                        cancel();
                        return;
                    }
                    
                    double angle = Math.toRadians(steps * 18 + cloneIndex * 120);
                    double radius = 3.0;
                    
                    double x = startLoc.getX() + radius * Math.cos(angle);
                    double z = startLoc.getZ() + radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        x, startLoc.getY() + 1, z,
                        5, 0.2, 0.2, 0.2, 0.01
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(new Location(player.getWorld(), x, startLoc.getY(), z), 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(5, player);
                        }
                    }
                    
                    steps++;
                }
            }.runTaskTimer(plugin, i * 5L, 1L);
        }
    }
    
    private void ultimateShadowAbility(Player player, int level) {
        player.sendMessage("§6§l👻 ULTIMATE SHADOW: REALM OF DARKNESS 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.5f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "shadow", level, () -> {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
            
            for (int i = 0; i < 50; i++) {
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    player.getLocation().add(random.nextDouble() * 10 - 5, random.nextDouble() * 5, random.nextDouble() * 10 - 5),
                    10, 0.5, 0.5, 0.5, 0.5
                );
            }
            
            player.sendTitle("§6👻 SHADOW LORD §e👻", "§fVictory from darkness!", 10, 40, 20);
        });
    }
    
    // ========== FLAME ABILITIES ==========
    
    private void flameAbility(Player player) {
        player.sendMessage("§c§l🔥 FLAME WALKER 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                double radius = 3.0;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location flameLoc = center.clone().add(x, 0.5, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        flameLoc,
                        5, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(flameLoc, 1, 1, 1)) {
                        if (e instanceof LivingEntity && e != player) {
                            e.setFireTicks(100);
                            ((LivingEntity) e).damage(2, player);
                        }
                    }
                }
                
                player.setFireTicks(20);
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedFlameAbility(Player player, int level) {
        player.sendMessage("§c§l🔥 ADVANCED FLAME: INFERNO RING 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 4 + level;
        
        new BukkitRunnable() {
            int ticks = 0;
            double radius = 2.0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                radius = 2.0 + Math.sin(ticks * 0.2) * 1.0;
                
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            center.clone().add(x, y, z),
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                        
                        if (level >= 3 && ticks % 10 == 0) {
                            player.getWorld().spawnParticle(
                                Particle.LAVA,
                                center.clone().add(x, y, z),
                                1, 0, 0, 0, 0
                            );
                        }
                    }
                    
                    for (Entity e : player.getWorld().getNearbyEntities(center.clone().add(x, 1, z), 1, 1, 1)) {
                        if (e instanceof LivingEntity && e != player) {
                            e.setFireTicks(80);
                            ((LivingEntity) e).damage(damage, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateFlameAbility(Player player, int level) {
        player.sendMessage("§6§l🔥 ULTIMATE FLAME: PHOENIX RISING 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.6f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "flame", level, () -> {
            for (int i = 0; i < 30; i++) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    player.getLocation().add(random.nextDouble() * 15 - 7.5, random.nextDouble() * 5, random.nextDouble() * 15 - 7.5),
                    10, 0.3, 0.3, 0.3, 0.02
                );
            }
            
            player.sendTitle("§6🔥 PHOENIX LORD §e🔥", "§fConsumed by flames!", 10, 40, 20);
        });
    }
    
    // ========== FROST ABILITIES ==========
    
    private void frostAbility(Player player) {
        player.sendMessage("§3§l❄️ FROST BITE ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 1.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 2));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                Location center = player.getLocation();
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 3.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        center.clone().add(x, 0.5, z),
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(100);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 3));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void advancedFrostAbility(Player player, int level) {
        player.sendMessage("§3§l❄️ ADVANCED FROST: ICE PRISON ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.SNOWFLAKE,
                            center.clone().add(x, y, z),
                            2, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(200);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 5));
                        
                        if (ticks % 10 == 0) {
                            ((LivingEntity) e).damage(2, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateFrostAbility(Player player, int level) {
        player.sendMessage("§6§l❄️ ULTIMATE FROST: PERMAFROST ❄️");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "frost", level, () -> {
            for (int i = 0; i < 50; i++) {
                player.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
                    player.getLocation().add(random.nextDouble() * 20 - 10, random.nextDouble() * 5, random.nextDouble() * 20 - 10),
                    20, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6❄️ FROST KING §e❄️", "§fFrozen wasteland!", 10, 40, 20);
        });
    }
    
    // ========== DRAGON ABILITIES ==========
    
    private void dragonAbility(Player player) {
        player.sendMessage("§5§l🐉 DRAGON'S FURY 🐉");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.8f);
        
        Location start = player.getLocation().add(0, 1.5, 0);
        Vector direction = player.getLocation().getDirection().normalize();
        
        new BukkitRunnable() {
            int distance = 0;
            
            @Override
            public void run() {
                if (distance++ >= 30) {
                    cancel();
                    return;
                }
                
                Location current = start.clone().add(direction.clone().multiply(distance));
                
                player.getWorld().spawnParticle(
                    Particle.DRAGON_BREATH,
                    current,
                    15, 0.3, 0.3, 0.3, 0.01
                );
                
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(6, player);
                        e.setFireTicks(80);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void advancedDragonAbility(Player player, int level) {
        player.sendMessage("§5§l🐉 ADVANCED DRAGON: DRAGON'S WRATH 🐉");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.6f);
        
        int heads = level + 1;
        
        for (int h = 0; h < heads; h++) {
            double headAngle = Math.toRadians(h * (360 / heads));
            
            new BukkitRunnable() {
                int distance = 0;
                
                @Override
                public void run() {
                    if (distance++ >= 20) {
                        cancel();
                        return;
                    }
                    
                    Vector direction = player.getLocation().getDirection().rotateAroundY(headAngle).normalize();
                    Location current = player.getLocation().add(0, 1.5, 0).clone().add(direction.clone().multiply(distance));
                    
                    player.getWorld().spawnParticle(
                        Particle.DRAGON_BREATH,
                        current,
                        10, 0.3, 0.3, 0.3, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        current,
                        5, 0.2, 0.2, 0.2, 0.01
                    );
                    
                    for (Entity e : player.getWorld().getNearbyEntities(current, 3, 3, 3)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(8, player);
                            e.setFireTicks(100);
                        }
                    }
                }
            }.runTaskTimer(plugin, h * 5L, 1L);
        }
    }
    
    private void ultimateDragonAbility(Player player, int level) {
        player.sendMessage("§6§l🐉 ULTIMATE DRAGON: APOCALYPSE 🐉");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 0.5f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "dragon", level, () -> {
            for (int i = 0; i < 30; i++) {
                player.getWorld().spawnParticle(
                    Particle.DRAGON_BREATH,
                    player.getLocation().add(random.nextDouble() * 20 - 10, random.nextDouble() * 10, random.nextDouble() * 20 - 10),
                    30, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.sendTitle("§6🐉 DRAGON EMPEROR §e🐉", "§fThe world burns!", 10, 40, 20);
        });
    }
    
    // ========== VOID ABILITIES ==========
    
    private void voidAbility(Player player) {
        player.sendMessage("§8§l🌌 VOID WALK 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        Location targetLoc = player.getTargetBlock(null, 30).getLocation().add(0, 1, 0);
        Location startLoc = player.getLocation();
        
        for (int i = 0; i < 50; i++) {
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                startLoc,
                1, 1, 1, 1, 0.5
            );
        }
        
        player.teleport(targetLoc);
        
        for (int i = 0; i < 50; i++) {
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                targetLoc,
                1, 1, 1, 1, 0.5
            );
        }
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
    }
    
    private void advancedVoidAbility(Player player, int level) {
        player.sendMessage("§8§l🌌 ADVANCED VOID: DIMENSIONAL RIFT 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.7f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double radius = 3.0 + Math.sin(ticks * 0.2) * 1.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        center.clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        3, 0.2, 0.2, 0.2, 0.3
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        if (random.nextInt(100) < 20) {
                            Location randomLoc = e.getLocation().clone().add(
                                random.nextDouble() * 10 - 5,
                                random.nextDouble() * 3,
                                random.nextDouble() * 10 - 5
                            );
                            e.teleport(randomLoc);
                            ((LivingEntity) e).damage(5, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateVoidAbility(Player player, int level) {
        player.sendMessage("§6§l🌌 ULTIMATE VOID: NULL ZONE 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "void", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    player.getLocation().add(random.nextDouble() * 30 - 15, random.nextDouble() * 10, random.nextDouble() * 30 - 15),
                    20, 0.5, 0.5, 0.5, 0.5
                );
            }
            
            player.sendTitle("§6🌌 VOID LORD §e🌌", "§fReality bends!", 10, 40, 20);
        });
    }
    
    // ========== LIFE ABILITIES ==========
    
    private void lifeAbility(Player player) {
        player.sendMessage("§2§l🌿 LIFE BINDER 🌿");
        player.playSound(player.getLocation(), Sound.BLOCK_GROWING_PLANT_CROP, 1.0f, 1.0f);
        
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(10);
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(player.getLocation()) <= 10) {
                p.setHealth(Math.min(p.getHealth() + 10, p.getMaxHealth()));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                p.sendMessage("§aYou feel renewed by life energy!");
            }
        }
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    player.getLocation().add(0, 2, 0),
                    20, 2, 1, 2, 0
                );
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    private void advancedLifeAbility(Player player, int level) {
        player.sendMessage("§2§l🌿 ADVANCED LIFE: NATURE'S BLESSING 🌿");
        player.playSound(player.getLocation(), Sound.BLOCK_GROWING_PLANT_CROP, 1.0f, 1.2f);
        
        Location center = player.getLocation();
        int healAmount = 5 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 4);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.HAPPY_VILLAGER,
                        center.clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.HEART,
                        center.clone().add(x, 2, z),
                        1, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(center) <= 8) {
                        if (p.getHealth() < p.getMaxHealth()) {
                            p.setHealth(Math.min(p.getHealth() + healAmount / 10.0, p.getMaxHealth()));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void ultimateLifeAbility(Player player, int level) {
        player.sendMessage("§6§l🌿 ULTIMATE LIFE: WORLD TREE 🌿");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.8f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof Monster) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "life", level, () -> {
            for (int i = 0; i < 50; i++) {
                player.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    player.getLocation().add(random.nextDouble() * 20 - 10, random.nextDouble() * 10, random.nextDouble() * 20 - 10),
                    30, 0.5, 0.5, 0.5, 0
                );
            }
            
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.sendMessage("§aThe World Tree heals all!");
            }
            
            player.sendTitle("§6🌿 WORLD TREE §e🌿", "§fNature prevails!", 10, 40, 20);
        });
    }
    
    // ========== GRAVITY ABILITIES ==========
    
    private void gravityAbility(Player player) {
        player.sendMessage("§d§l⚡ GRAVITY SURGE ⚡");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        
                        if (ticks < 20) {
                            e.setVelocity(pull.multiply(0.5));
                        } else {
                            e.setVelocity(pull.multiply(-0.5));
                        }
                    }
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = 4 * Math.cos(angle);
                    double z = 4 * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(x, 1, z),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedGravityAbility(Player player, int level) {
        player.sendMessage("§d§l⚡ ADVANCED GRAVITY: BLACK HOLE ⚡");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.6f);
        
        Location center = player.getLocation();
        int pullStrength = level;
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 12);
                    double radius = 5.0 * (1 - ticks / 80.0);
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.PORTAL,
                        center.clone().add(x, 1, z),
                        2, 0.1, 0.1, 0.1, 0.2
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 10, 10, 10)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.3 * pullStrength));
                        
                        if (e.getLocation().distance(center) < 2) {
                            ((LivingEntity) e).damage(level * 2, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateGravityAbility(Player player, int level) {
        player.sendMessage("§6§l⚡ ULTIMATE GRAVITY: SINGULARITY ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "gravity", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.PORTAL,
                    player.getLocation().add(random.nextDouble() * 30 - 15, random.nextDouble() * 10, random.nextDouble() * 30 - 15),
                    20, 0.5, 0.5, 0.5, 0.5
                );
            }
            
            player.getWorld().createExplosion(player.getLocation(), 0, false, false);
            player.sendTitle("§6⚡ SINGULARITY §e⚡", "§fGravity defeated all!", 10, 40, 20);
        });
    }
    
    // ========== PHANTOM ABILITIES ==========
    
    private void phantomAbility(Player player) {
        player.sendMessage("§7§l👻 PHANTOM FORM 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 1.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
        player.setAllowFlight(true);
        player.setFlying(true);
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    player.getLocation().add(0, 1, 0),
                    5, 0.3, 0.5, 0.3, 0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedPhantomAbility(Player player, int level) {
        player.sendMessage("§7§l👻 ADVANCED PHANTOM: ECTOPLASM 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 6);
                    double radius = 3.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        center.clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, level));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, level));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimatePhantomAbility(Player player, int level) {
        player.sendMessage("§6§l👻 ULTIMATE PHANTOM: SPIRIT REALM 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.5f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "phantom", level, () -> {
            for (int i = 0; i < 50; i++) {
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    player.getLocation().add(random.nextDouble() * 30 - 15, random.nextDouble() * 10, random.nextDouble() * 30 - 15),
                    30, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.sendTitle("§6👻 SPIRIT KING §e👻", "§fThe realm of souls!", 10, 40, 20);
        });
    }
    
    // ========== DAWN ABILITIES ==========
    
    private void dawnAbility(Player player) {
        player.sendMessage("§e§l☀️ DAWN BREAKER ☀️");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 2.0f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (double y = 0; y < 10; y += 0.5) {
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(0, y, 0),
                        1, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof Monster) {
                        ((Monster) e).damage(4, player);
                        e.getWorld().strikeLightningEffect(e.getLocation());
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    private void advancedDawnAbility(Player player, int level) {
        player.sendMessage("§e§l☀️ ADVANCED DAWN: SOLAR FLARE ☀️");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                double radius = 5.0 * (ticks / 60.0);
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            center.clone().add(x, y, z),
                            2, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 5, radius)) {
                    if (e instanceof Monster) {
                        ((Monster) e).damage(level * 2, player);
                        ((Monster) e).setFireTicks(60);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateDawnAbility(Player player, int level) {
        player.sendMessage("§6§l☀️ ULTIMATE DAWN: JUDGMENT DAY ☀️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.6f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof Monster) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "dawn", level, () -> {
            for (int i = 0; i < 50; i++) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    player.getLocation().add(random.nextDouble() * 30 - 15, random.nextDouble() * 15, random.nextDouble() * 30 - 15),
                    50, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6☀️ SOLAR DEITY §e☀️", "§fLight purges all!", 10, 40, 20);
        });
    }
    
    // ========== TERRA ABILITIES ==========
    
    private void terraAbility(Player player) {
        player.sendMessage("§6§l⛰️ TERRA SHAPER ⛰️");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 0.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 3.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        center.clone().add(x, 0.1, z),
                        5, 0.1, 0.1, 0.1, 0,
                        Bukkit.createBlockData(Material.STONE)
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 2, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(3, player);
                        e.setVelocity(new Vector(0, 0.8, 0));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void advancedTerraAbility(Player player, int level) {
        player.sendMessage("§6§l⛰️ ADVANCED TERRA: EARTHQUAKE ⛰️");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 0.3f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                double intensity = 1.0 + Math.sin(ticks * 0.3) * 0.5;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 15);
                    double radius = 5.0 * intensity;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        center.clone().add(x, 0.1, z),
                        8, 0.2, 0.1, 0.2, 0,
                        Bukkit.createBlockData(Material.STONE)
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(level, player);
                        e.setVelocity(new Vector(
                            random.nextDouble() * 2 - 1,
                            0.5 + level * 0.2,
                            random.nextDouble() * 2 - 1
                        ));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateTerraAbility(Player player, int level) {
        player.sendMessage("§6§l⛰️ ULTIMATE TERRA: CONTINENTAL DRIFT ⛰️");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "terra", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.BLOCK,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 5, random.nextDouble() * 40 - 20),
                    30, 0.5, 0.5, 0.5, 0,
                    Bukkit.createBlockData(Material.STONE)
                );
            }
            
            player.getWorld().createExplosion(player.getLocation(), 2, false, true);
            player.sendTitle("§6⛰️ TERRA LORD §e⛰️", "§fThe earth moves!", 10, 40, 20);
        });
    }
    
    // ========== WIND ABILITIES ==========
    
    private void windAbility(Player player) {
        player.sendMessage("§f§l💨 WIND RIDER 💨");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 2.0f);
        
        player.setVelocity(player.getLocation().getDirection().multiply(3).setY(1.5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    player.getLocation().add(0, 1, 0),
                    10, 0.3, 0.3, 0.3, 0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedWindAbility(Player player, int level) {
        player.sendMessage("§f§l💨 ADVANCED WIND: TORNADO 💨");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 12);
                    double radius = 2.0 + Math.sin(ticks * 0.2) * 1.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = ticks * 0.1;
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        center.clone().add(x, y, z),
                        5, 0.1, 0.1, 0.1, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.3).setY(0.2));
                        ((LivingEntity) e).damage(1, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateWindAbility(Player player, int level) {
        player.sendMessage("§6§l💨 ULTIMATE WIND: STORM EYE 💨");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.7f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "wind", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 20, random.nextDouble() * 40 - 20),
                    50, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.sendTitle("§6💨 STORM GOD §e💨", "§fWinds of change!", 10, 40, 20);
        });
    }
    
    // ========== TIME ABILITIES ==========
    
    private void timeAbility(Player player) {
        player.sendMessage("§b§l⏳ TIME WEAVER ⏳");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1.0f, 0.5f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 3));
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 4));
            }
        }
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 2, 0);
                double angle = ticks * 0.3;
                
                for (int i = 0; i < 12; i++) {
                    double hourAngle = angle + (i * Math.PI / 6);
                    double x = 2 * Math.cos(hourAngle);
                    double z = 2 * Math.sin(hourAngle);
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        loc.clone().add(x, 0, z),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedTimeAbility(Player player, int level) {
        player.sendMessage("§b§l⏳ ADVANCED TIME: TIME FREEZE ⏳");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        center.clone().add(x, 1, z),
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setVelocity(new Vector(0, 0, 0));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 10));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateTimeAbility(Player player, int level) {
        player.sendMessage("§6§l⏳ ULTIMATE TIME: CHRONOS ⏳");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "time", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.GLOW,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 10, random.nextDouble() * 40 - 20),
                    30, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6⏳ CHRONOS §e⏳", "§fTime itself obeys!", 10, 40, 20);
        });
    }
    
    // ========== SOUL ABILITIES ==========
    
    private void soulAbility(Player player) {
        player.sendMessage("§4§l💀 SOUL REAPER 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 8, 8, 8)) {
            if (e instanceof LivingEntity && e != player) {
                LivingEntity target = (LivingEntity) e;
                
                target.damage(8, player);
                player.setHealth(Math.min(player.getHealth() + 4, player.getMaxHealth()));
                
                Location targetLoc = target.getLocation().add(0, 1, 0);
                Location playerLoc = player.getLocation().add(0, 1, 0);
                
                new BukkitRunnable() {
                    int steps = 0;
                    
                    @Override
                    public void run() {
                        if (steps++ >= 20) {
                            cancel();
                            return;
                        }
                        
                        double progress = steps / 20.0;
                        double x = targetLoc.getX() + (playerLoc.getX() - targetLoc.getX()) * progress;
                        double y = targetLoc.getY() + (playerLoc.getY() - targetLoc.getY()) * progress;
                        double z = targetLoc.getZ() + (playerLoc.getZ() - targetLoc.getZ()) * progress;
                        
                        Location soulLoc = new Location(player.getWorld(), x, y, z);
                        
                        player.getWorld().spawnParticle(
                            Particle.SOUL,
                            soulLoc,
                            5, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }
    }
    
    private void advancedSoulAbility(Player player, int level) {
        player.sendMessage("§4§l💀 ADVANCED SOUL: SOUL HARVEST 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.6f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 7);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL,
                        center.clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        center.clone().add(x, 1.5, z),
                        1, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(level, player);
                        player.setHealth(Math.min(player.getHealth() + level, player.getMaxHealth()));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateSoulAbility(Player player, int level) {
        player.sendMessage("§6§l💀 ULTIMATE SOUL: SOUL REAPER 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "soul", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 10, random.nextDouble() * 40 - 20),
                    40, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.setHealth(player.getMaxHealth());
            player.sendTitle("§6💀 SOUL REAPER §e💀", "§fSouls harvested!", 10, 40, 20);
        });
    }
    
    // ========== CRYSTAL ABILITIES ==========
    
    private void crystalAbility(Player player) {
        player.sendMessage("§d§l💎 CRYSTAL MAGE 💎");
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 2));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                Location center = player.getLocation().add(0, 1, 0);
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 2.5;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = 0.5 + Math.sin(angle + ticks) * 0.5;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(x, y, z),
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setVelocity(e.getVelocity().multiply(-0.5));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedCrystalAbility(Player player, int level) {
        player.sendMessage("§d§l💎 ADVANCED CRYSTAL: CRYSTAL PRISON 💎");
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 4);
                    double radius = 3.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 4; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.END_ROD,
                            center.clone().add(x, y, z),
                            1, 0.1, 0.1, 0.1, 0
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        e.setVelocity(new Vector(0, 0, 0));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 10));
                        ((LivingEntity) e).damage(level, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateCrystalAbility(Player player, int level) {
        player.sendMessage("§6§l💎 ULTIMATE CRYSTAL: CRYSTAL DRAGON 💎");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "crystal", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 15, random.nextDouble() * 40 - 20),
                    50, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6💎 CRYSTAL DRAGON §e💎", "§fCrystalline destruction!", 10, 40, 20);
        });
    }
    
    // ========== THUNDER ABILITIES ==========
    
    private void thunderAbility(Player player) {
        player.sendMessage("§e§l⚡ THUNDER GOD ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        
        List<Entity> targets = new ArrayList<>();
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 15, 15, 15)) {
            if (e instanceof LivingEntity && e != player) {
                targets.add(e);
            }
        }
        
        if (!targets.isEmpty()) {
            new BukkitRunnable() {
                int index = 0;
                Location lastLoc = player.getLocation();
                
                @Override
                public void run() {
                    if (index >= targets.size() || index >= 5) {
                        cancel();
                        return;
                    }
                    
                    Entity current = targets.get(index);
                    Location currentLoc = current.getLocation();
                    
                    player.getWorld().strikeLightningEffect(currentLoc);
                    
                    Vector direction = currentLoc.toVector().subtract(lastLoc.toVector());
                    double length = direction.length();
                    direction.normalize();
                    
                    for (double d = 0; d < length; d += 0.5) {
                        Location particleLoc = lastLoc.clone().add(direction.clone().multiply(d));
                        
                        player.getWorld().spawnParticle(
                            Particle.ELECTRIC_SPARK,
                            particleLoc,
                            10, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                    
                    if (current instanceof LivingEntity) {
                        ((LivingEntity) current).damage(10, player);
                    }
                    
                    lastLoc = currentLoc;
                    index++;
                }
            }.runTaskTimer(plugin, 0L, 5L);
        }
    }
    
    private void advancedThunderAbility(Player player, int level) {
        player.sendMessage("§e§l⚡ ADVANCED THUNDER: ELECTRIC STORM ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        int damage = 8 + level * 2;
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 50) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 10; i++) {
                    Location strikeLoc = center.clone().add(
                        random.nextInt(20) - 10,
                        0,
                        random.nextInt(20) - 10
                    );
                    
                    player.getWorld().strikeLightningEffect(strikeLoc);
                    
                    for (Entity e : player.getWorld().getNearbyEntities(strikeLoc, 4, 4, 4)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(damage, player);
                            
                            for (Entity nearby : e.getWorld().getNearbyEntities(e.getLocation(), 3, 3, 3)) {
                                if (nearby instanceof LivingEntity && nearby != player && nearby != e) {
                                    player.getWorld().strikeLightningEffect(nearby.getLocation());
                                    ((LivingEntity) nearby).damage(damage / 2, player);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    private void ultimateThunderAbility(Player player, int level) {
        player.sendMessage("§6§l⚡ ULTIMATE THUNDER: GOD OF LIGHTNING ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.6f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "thunder", level, () -> {
            for (int i = 0; i < 50; i++) {
                player.getWorld().strikeLightningEffect(
                    player.getLocation().add(random.nextDouble() * 30 - 15, 0, random.nextDouble() * 30 - 15)
                );
            }
            
            player.sendTitle("§6⚡ GOD OF LIGHTNING §e⚡", "§fThor's wrath!", 10, 40, 20);
        });
    }
    
    // ========== ICE ABILITIES ==========
    
    private void iceAbility(Player player) {
        player.sendMessage("§3§l❄️ ICE WARDEN ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 1));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 2.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        player.getLocation().clone().add(x, 0.5, z),
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(60);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void advancedIceAbility(Player player, int level) {
        player.sendMessage("§3§l❄️ ADVANCED ICE: ICE AGE ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 70) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 4);
                    double radius = 5.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        center.clone().add(x, 1, z),
                        4, 0.2, 0.2, 0.2, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(200);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 5));
                        ((LivingEntity) e).damage(level, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateIceAbility(Player player, int level) {
        player.sendMessage("§6§l❄️ ULTIMATE ICE: FROZEN HELL ❄️");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "ice", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 10, random.nextDouble() * 40 - 20),
                    50, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6❄️ FROZEN HELL §e❄️", "§fAbsolute zero!", 10, 40, 20);
        });
    }
    
    // ========== PYRO ABILITIES ==========
    
    private void pyroAbility(Player player) {
        player.sendMessage("§c§l🔥 PYROMANCER 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 30) {
                    cancel();
                    return;
                }
                
                double radius = ticks * 0.3;
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 20);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 2; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            center.clone().add(x, y, z),
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (e != player) {
                        e.setFireTicks(40);
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(2, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedPyroAbility(Player player, int level) {
        player.sendMessage("§c§l🔥 ADVANCED PYRO: FIRE STORM 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.6f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                double radius = 3.0 + Math.sin(ticks * 0.2) * 1.0;
                
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i + ticks * 12);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            center.clone().add(x, y, z),
                            4, 0.1, 0.1, 0.1, 0.02
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius + 1, 4, radius + 1)) {
                    if (e != player) {
                        e.setFireTicks(100);
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(level * 2, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimatePyroAbility(Player player, int level) {
        player.sendMessage("§6§l🔥 ULTIMATE PYRO: HELLFIRE 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "pyro", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 10, random.nextDouble() * 40 - 20),
                    50, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.sendTitle("§6🔥 HELLFIRE §e🔥", "§fThe underworld rises!", 10, 40, 20);
        });
    }
    
    // ========== SPIRIT ABILITIES ==========
    
    private void spiritAbility(Player player) {
        player.sendMessage("§7§l👾 SPIRIT WARDEN 👾");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 1.0f);
        
        for (int i = 0; i < 3; i++) {
            Location spawnLoc = player.getLocation().add(
                random.nextInt(3) - 1,
                0,
                random.nextInt(3) - 1
            );
            
            Wolf wolf = (Wolf) player.getWorld().spawnEntity(spawnLoc, EntityType.WOLF);
            wolf.setTamed(true);
            wolf.setOwner(player);
            wolf.setCustomName("§7Spirit Wolf");
            wolf.setCustomNameVisible(true);
            wolf.setHealth(40);
            wolf.setGlowing(true);
            
            Bukkit.getScheduler().runTaskLater(plugin, wolf::remove, 400L);
        }
    }
    
    private void advancedSpiritAbility(Player player, int level) {
        player.sendMessage("§7§l👾 ADVANCED SPIRIT: SPIRIT ARMY 👾");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 0.8f);
        
        int count = level * 2;
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = player.getLocation().add(
                random.nextInt(5) - 2,
                0,
                random.nextInt(5) - 2
            );
            
            Wolf wolf = (Wolf) player.getWorld().spawnEntity(spawnLoc, EntityType.WOLF);
            wolf.setTamed(true);
            wolf.setOwner(player);
            wolf.setCustomName("§7Spirit Warrior");
            wolf.setCustomNameVisible(true);
            wolf.setHealth(30 + level * 10);
            wolf.setGlowing(true);
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (wolf.isDead() || !wolf.isValid()) {
                        cancel();
                        return;
                    }
                    
                    for (Entity e : wolf.getWorld().getNearbyEntities(wolf.getLocation(), 5, 5, 5)) {
                        if (e instanceof Monster) {
                            wolf.setTarget((LivingEntity) e);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
            
            Bukkit.getScheduler().runTaskLater(plugin, wolf::remove, 600L);
        }
    }
    
    private void ultimateSpiritAbility(Player player, int level) {
        player.sendMessage("§6§l👾 ULTIMATE SPIRIT: SPIRIT REALM 👾");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof Monster) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "spirit", level, () -> {
            for (int i = 0; i < 50; i++) {
                Wolf wolf = (Wolf) player.getWorld().spawnEntity(
                    player.getLocation().add(random.nextDouble() * 20 - 10, 0, random.nextDouble() * 20 - 10),
                    EntityType.WOLF
                );
                wolf.setTamed(true);
                wolf.setOwner(player);
                wolf.setCustomName("§7Spirit Guardian");
                wolf.setGlowing(true);
                wolf.setHealth(50);
                Bukkit.getScheduler().runTaskLater(plugin, wolf::remove, 200L);
            }
            
            player.sendTitle("§6👾 SPIRIT REALM §e👾", "§fThe ancestors fight!", 10, 40, 20);
        });
    }
    
    // ========== NECRO ABILITIES ==========
    
    private void necroAbility(Player player) {
        player.sendMessage("§5§l⚰️ NECROLORD ⚰️");
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 0.5f);
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof Zombie || e instanceof Skeleton) {
                Monster undead = (Monster) e;
                undead.setTarget(null);
                undead.setCustomName("§5" + player.getName() + "'s Minion");
                undead.setCustomNameVisible(true);
                undead.setGlowing(true);
                undead.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (undead.isValid()) {
                        undead.remove();
                    }
                }, 300L);
            }
        }
    }
    
    private void advancedNecroAbility(Player player, int level) {
        player.sendMessage("§5§l⚰️ ADVANCED NECRO: UNDEAD ARMY ⚰️");
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 0.3f);
        
        int count = level * 3;
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = player.getLocation().add(
                random.nextInt(6) - 3,
                0,
                random.nextInt(6) - 3
            );
            
            Monster undead;
            if (random.nextBoolean()) {
                undead = (Zombie) player.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            } else {
                undead = (Skeleton) player.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
            }
            
            undead.setTarget(null);
            undead.setCustomName("§5" + player.getName() + "'s Minion");
            undead.setCustomNameVisible(true);
            undead.setGlowing(true);
            undead.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, level));
            undead.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            
            Bukkit.getScheduler().runTaskLater(plugin, undead::remove, 600L);
        }
    }
    
    private void ultimateNecroAbility(Player player, int level) {
        player.sendMessage("§6§l⚰️ ULTIMATE NECRO: ARMY OF DARKNESS ⚰️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "necro", level, () -> {
            for (int i = 0; i < 30; i++) {
                Location spawnLoc = player.getLocation().add(
                    random.nextInt(20) - 10,
                    0,
                    random.nextInt(20) - 10
                );
                
                Monster undead;
                if (random.nextBoolean()) {
                    undead = (Zombie) player.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
                } else {
                    undead = (Skeleton) player.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
                }
                
                undead.setCustomName("§5Undead Warrior");
                undead.setGlowing(true);
                undead.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 3));
                Bukkit.getScheduler().runTaskLater(plugin, undead::remove, 200L);
            }
            
            player.sendTitle("§6⚰️ ARMY OF DARKNESS §e⚰️", "§fDeath marches!", 10, 40, 20);
        });
    }
    
    // ========== SERAPH ABILITIES ==========
    
    private void seraphAbility(Player player) {
        player.sendMessage("§f§l👼 SERAPHIM 👼");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 3));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 1, 0);
                
                for (int i = -3; i <= 3; i++) {
                    double offset = Math.abs(i) * 0.3;
                    
                    Location leftWing = loc.clone().add(-1.5 - offset, i * 0.3, i * 0.3);
                    player.getWorld().spawnParticle(Particle.END_ROD, leftWing, 3, 0.1, 0.1, 0.1, 0);
                    
                    Location rightWing = loc.clone().add(1.5 + offset, i * 0.3, i * 0.3);
                    player.getWorld().spawnParticle(Particle.END_ROD, rightWing, 3, 0.1, 0.1, 0.1, 0);
                }
                
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(player.getLocation()) <= 8 && p != player) {
                        if (p.getHealth() < p.getMaxHealth()) {
                            p.setHealth(Math.min(p.getHealth() + 2, p.getMaxHealth()));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    private void advancedSeraphAbility(Player player, int level) {
        player.sendMessage("§f§l👼 ADVANCED SERAPH: HOLY GROUND 👼");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 4);
                    double radius = 5.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(center) <= 7) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, level));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, level - 1));
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 7, 7, 7)) {
                    if (e instanceof Monster) {
                        ((Monster) e).damage(level * 2, player);
                        ((Monster) e).setFireTicks(40);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void ultimateSeraphAbility(Player player, int level) {
        player.sendMessage("§6§l👼 ULTIMATE SERAPH: DIVINE INTERVENTION 👼");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof Monster) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "seraph", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 15, random.nextDouble() * 40 - 20),
                    50, 0.5, 0.5, 0.5, 0
                );
            }
            
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setHealth(p.getMaxHealth());
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 4));
                p.sendMessage("§dDivine power protects you!");
            }
            
            player.sendTitle("§6👼 DIVINE INTERVENTION §e👼", "§fThe heavens smile!", 10, 40, 20);
        });
    }
    
    // ========== ABYSS ABILITIES ==========
    
    private void abyssAbility(Player player) {
        player.sendMessage("§8§l🌑 ABYSS 🌑");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        center.clone().add(x, 0.5, z),
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));
                        
                        Vector away = e.getLocation().toVector().subtract(center.toVector()).normalize();
                        e.setVelocity(away.multiply(0.3));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void advancedAbyssAbility(Player player, int level) {
        player.sendMessage("§8§l🌑 ADVANCED ABYSS: DARKNESS CONSUMES 🌑");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                double radius = 5.0 * (1 - Math.cos(ticks * 0.05));
                
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        center.clone().add(x, 1, z),
                        3, 0.2, 0.1, 0.2, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 5, radius)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, level));
                        ((LivingEntity) e).damage(level * 2, player);
                        
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize();
                        e.setVelocity(pull.multiply(0.2));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateAbyssAbility(Player player, int level) {
        player.sendMessage("§6§l🌑 ULTIMATE ABYSS: VOID CALLS 🌑");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.2f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "abyss", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 10, random.nextDouble() * 40 - 20),
                    40, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.sendTitle("§6🌑 THE VOID §e🌑", "§fAll light fades!", 10, 40, 20);
        });
    }
    
    // ========== CHAOS ABILITIES ==========
    
    private void chaosAbility(Player player) {
        player.sendMessage("§5§l🌀 CHAOS WEAVER 🌀");
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 1.0f);
        
        PotionEffectType[] effects = {
            PotionEffectType.SPEED, PotionEffectType.SLOWNESS,
            PotionEffectType.HASTE, PotionEffectType.MINING_FATIGUE,
            PotionEffectType.STRENGTH, PotionEffectType.WEAKNESS,
            PotionEffectType.REGENERATION, PotionEffectType.POISON,
            PotionEffectType.JUMP_BOOST, PotionEffectType.NAUSEA,
            PotionEffectType.RESISTANCE, PotionEffectType.HUNGER
        };
        
        player.addPotionEffect(new PotionEffect(
            effects[random.nextInt(effects.length)], 
            100, 
            random.nextInt(3)
        ));
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 8, 8, 8)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(
                    effects[random.nextInt(effects.length)], 
                    80, 
                    random.nextInt(2)
                ));
            }
        }
    }
    
    private void advancedChaosAbility(Player player, int level) {
        player.sendMessage("§5§l🌀 ADVANCED CHAOS: REALITY FRACTURE 🌀");
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 0.7f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 20; i++) {
                    Particle randomParticle = Particle.values()[random.nextInt(Particle.values().length)];
                    Location randomLoc = center.clone().add(
                        random.nextDouble() * 8 - 4,
                        random.nextDouble() * 4,
                        random.nextDouble() * 8 - 4
                    );
                    
                    player.getWorld().spawnParticle(
                        randomParticle,
                        randomLoc,
                        5, 0.3, 0.3, 0.3, 0.1
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        if (random.nextInt(100) < 20 * level) {
                            Location randomLoc = e.getLocation().clone().add(
                                random.nextDouble() * 10 - 5,
                                random.nextDouble() * 3,
                                random.nextDouble() * 10 - 5
                            );
                            e.teleport(randomLoc);
                            ((LivingEntity) e).damage(level * 2, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateChaosAbility(Player player, int level) {
        player.sendMessage("§6§l🌀 ULTIMATE CHAOS: PRIMORDIAL CHAOS 🌀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "chaos", level, () -> {
            for (int i = 0; i < 200; i++) {
                Particle randomParticle = Particle.values()[random.nextInt(Particle.values().length)];
                player.getWorld().spawnParticle(
                    randomParticle,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 15, random.nextDouble() * 40 - 20),
                    30, 0.5, 0.5, 0.5, 0.1
                );
            }
            
            player.sendTitle("§6🌀 PRIMORDIAL CHAOS §e🌀", "§fReality unravels!", 10, 40, 20);
        });
    }
    
    // ========== JUDGE ABILITIES ==========
    
    private void judgeAbility(Player player) {
        player.sendMessage("§b§l⚖️ JUDGEMENT ⚖️");
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 2));
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 12, 12, 12)) {
            if (e instanceof Monster) {
                Monster monster = (Monster) e;
                monster.damage(15, player);
                player.getWorld().strikeLightningEffect(monster.getLocation());
            }
        }
    }
    
    private void advancedJudgeAbility(Player player, int level) {
        player.sendMessage("§b§l⚖️ ADVANCED JUDGE: DIVINE WRATH ⚖️");
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 50) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 6);
                    double radius = 5.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(x, 1, z),
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                    if (e instanceof Monster) {
                        player.getWorld().strikeLightningEffect(e.getLocation());
                        ((Monster) e).damage(level * 3, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void ultimateJudgeAbility(Player player, int level) {
        player.sendMessage("§6§l⚖️ ULTIMATE JUDGE: FINAL JUDGEMENT ⚖️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.4f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof Monster) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "judge", level, () -> {
            for (int i = 0; i < 50; i++) {
                player.getWorld().strikeLightningEffect(
                    player.getLocation().add(random.nextDouble() * 30 - 15, 0, random.nextDouble() * 30 - 15)
                );
            }
            
            player.sendTitle("§6⚖️ FINAL JUDGEMENT §e⚖️", "§fThe guilty are punished!", 10, 40, 20);
        });
    }
    
    // ========== DREAM ABILITIES ==========
    
    private void dreamAbility(Player player) {
        player.sendMessage("§d§l💫 DREAMCATCHER 💫");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1.0f, 1.0f);
        
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 8, 8, 8)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 5));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
            }
        }
    }
    
    private void advancedDreamAbility(Player player, int level) {
        player.sendMessage("§d§l💫 ADVANCED DREAM: NIGHTMARE 💫");
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 70) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.WAX_OFF,
                        center.clone().add(x, 1 + Math.sin(angle) * 0.5, z),
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 10));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, level));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void ultimateDreamAbility(Player player, int level) {
        player.sendMessage("§6§l💫 ULTIMATE DREAM: DREAM REALM 💫");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "dream", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.WAX_OFF,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 15, random.nextDouble() * 40 - 20),
                    40, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6💫 DREAM REALM §e💫", "§fReality blurs!", 10, 40, 20);
        });
    }
    
    // ========== FEAR ABILITIES ==========
    
    private void fearAbility(Player player) {
        player.sendMessage("§8§l👹 NIGHT TERROR 👹");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.6f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 2));
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                LivingEntity target = (LivingEntity) e;
                
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 3));
                target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 2));
                
                Vector away = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                target.setVelocity(away.multiply(0.8));
            }
        }
    }
    
    private void advancedFearAbility(Player player, int level) {
        player.sendMessage("§8§l👹 ADVANCED FEAR: TERRIFYING PRESENCE 👹");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.4f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 7);
                    double radius = 5.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        center.clone().add(x, 1, z),
                        4, 0.2, 0.1, 0.2, 0.02
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 7, 7, 7)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector away = e.getLocation().toVector().subtract(center.toVector()).normalize();
                        e.setVelocity(away.multiply(0.5));
                        
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, level + 1));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, level + 1));
                        ((LivingEntity) e).damage(level, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateFearAbility(Player player, int level) {
        player.sendMessage("§6§l👹 ULTIMATE FEAR: PARADOX OF TERROR 👹");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.2f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "fear", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    player.getLocation().add(random.nextDouble() * 40 - 20, random.nextDouble() * 10, random.nextDouble() * 40 - 20),
                    50, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.sendTitle("§6👹 PARADOX OF TERROR §e👹", "§fFear incarnate!", 10, 40, 20);
        });
    }
    
    // ========== AURORA ABILITIES ==========
    
    private void auroraAbility(Player player) {
        player.sendMessage("§b§l🌈 AURORA 🌈");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 150, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 150, 1));
    }
    
    private void advancedAuroraAbility(Player player, int level) {
        player.sendMessage("§b§l🌈 ADVANCED AURORA: NORTHERN LIGHTS 🌈");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 6.0;
                    double wave = Math.sin(angle + ticks * 0.2) * 2;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = 1 + wave;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(x, y, z),
                        2, 0.1, 0.1, 0.1, 0
                    );
                }
                
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(center) <= 8) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, level));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 40, level - 1));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void ultimateAuroraAbility(Player player, int level) {
        player.sendMessage("§6§l🌈 ULTIMATE AURORA: COSMIC LIGHTS 🌈");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "aurora", level, () -> {
            for (int i = 0; i < 200; i++) {
                player.getWorld().spawnParticle(
                    Particle.END_ROD,
                    player.getLocation().add(random.nextDouble() * 50 - 25, random.nextDouble() * 20, random.nextDouble() * 50 - 25),
                    30, 0.5, 0.5, 0.5, 0
                );
            }
            
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 200, 2));
            }
            
            player.sendTitle("§6🌈 COSMIC LIGHTS §e🌈", "§fThe aurora empowers all!", 10, 40, 20);
        });
    }
    
    // ========== STAR ABILITIES ==========
    
    private void starAbility(Player player) {
        player.sendMessage("§d§l✨ STARFALL ✨");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 3; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * 5;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location meteorLoc = center.clone().add(x, 10, z);
                    Location targetLoc = center.clone().add(x, 0, z);
                    
                    for (double y = 10; y > 0; y -= 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            meteorLoc.clone().subtract(0, 10 - y, 0),
                            1, 0, 0, 0, 0
                        );
                    }
                    
                    player.getWorld().createExplosion(targetLoc, 2, false, true);
                    
                    for (Entity e : player.getWorld().getNearbyEntities(targetLoc, 3, 3, 3)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(8, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    private void advancedStarAbility(Player player, int level) {
        player.sendMessage("§d§l✨ ADVANCED STAR: METEOR SHOWER ✨");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.2f);
        
        Location center = player.getLocation();
        int count = 5 + level * 2;
        
        new BukkitRunnable() {
            int wave = 0;
            
            @Override
            public void run() {
                if (wave++ >= 3) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < count; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * 8;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location meteorLoc = center.clone().add(x, 15, z);
                    Location targetLoc = center.clone().add(x, 0, z);
                    
                    for (double y = 15; y > 0; y -= 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            meteorLoc.clone().subtract(0, 15 - y, 0),
                            2, 0.1, 0.1, 0.1, 0
                        );
                    }
                    
                    player.getWorld().createExplosion(targetLoc, level, false, true);
                    
                    for (Entity e : player.getWorld().getNearbyEntities(targetLoc, 4, 4, 4)) {
                        if (e instanceof LivingEntity && e != player) {
                            ((LivingEntity) e).damage(level * 3, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 15L);
    }
    
    private void ultimateStarAbility(Player player, int level) {
        player.sendMessage("§6§l✨ ULTIMATE STAR: SUPERNOVA ✨");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.3f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "star", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    player.getLocation().add(random.nextDouble() * 50 - 25, random.nextDouble() * 20, random.nextDouble() * 50 - 25),
                    50, 0.5, 0.5, 0.5, 0.1
                );
            }
            
            player.getWorld().createExplosion(player.getLocation(), 3, false, true);
            player.sendTitle("§6✨ SUPERNOVA §e✨", "§fA star is born!", 10, 40, 20);
        });
    }
    
    // ========== INFERNO ABILITIES ==========
    
    private void infernoAbility(Player player) {
        player.sendMessage("§c§l🔥 INFERNUS 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        player.getWorld().createExplosion(center, 4, false, true);
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                double radius = ticks * 0.5;
                
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i + ticks * 20);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 3; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            center.clone().add(x, y, z),
                            8, 0.1, 0.1, 0.1, 0.02
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (e != player) {
                        e.setFireTicks(80);
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(3, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void advancedInfernoAbility(Player player, int level) {
        player.sendMessage("§c§l🔥 ADVANCED INFERNO: HELL GATE 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                double radius = 5.0 * (ticks / 60.0);
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 4; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            center.clone().add(x, y, z),
                            5, 0.1, 0.1, 0.1, 0.02
                        );
                        
                        if (level >= 3 && ticks % 10 == 0) {
                            player.getWorld().spawnParticle(
                                Particle.LAVA,
                                center.clone().add(x, y, z),
                                2, 0, 0, 0, 0
                            );
                        }
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, 5, radius)) {
                    if (e != player) {
                        e.setFireTicks(200);
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(level * 2, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateInfernoAbility(Player player, int level) {
        player.sendMessage("§6§l🔥 ULTIMATE INFERNO: HELL ON EARTH 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.2f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "inferno", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    player.getLocation().add(random.nextDouble() * 50 - 25, random.nextDouble() * 15, random.nextDouble() * 50 - 25),
                    60, 0.5, 0.5, 0.5, 0.02
                );
            }
            
            player.getWorld().createExplosion(player.getLocation(), 4, false, true);
            player.sendTitle("§6🔥 HELL ON EARTH §e🔥", "§fThe underworld rises!", 10, 40, 20);
        });
    }
    
    // ========== AVALANCHE ABILITIES ==========
    
    private void avalancheAbility(Player player) {
        player.sendMessage("§3§l🏔️ AVALANCHE 🏔️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 50) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 5; i++) {
                    Snowball snowball = player.getWorld().spawn(
                        center.clone().add(
                            random.nextDouble() * 4 - 2,
                            3,
                            random.nextDouble() * 4 - 2
                        ),
                        Snowball.class
                    );
                    snowball.setShooter(player);
                    snowball.setVelocity(new Vector(
                        random.nextDouble() * 2 - 1,
                        -0.5,
                        random.nextDouble() * 2 - 1
                    ));
                }
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        center.clone().add(x, 0.5, z),
                        10, 0.2, 0.2, 0.2, 0
                    );
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(80);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private void advancedAvalancheAbility(Player player, int level) {
        player.sendMessage("§3§l🏔️ ADVANCED AVALANCHE: BLIZZARD 🏔️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.3f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 70) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 10; i++) {
                    Snowball snowball = player.getWorld().spawn(
                        center.clone().add(
                            random.nextDouble() * 8 - 4,
                            5,
                            random.nextDouble() * 8 - 4
                        ),
                        Snowball.class
                    );
                    snowball.setShooter(player);
                    snowball.setVelocity(new Vector(
                        random.nextDouble() * 3 - 1.5,
                        -0.7,
                        random.nextDouble() * 3 - 1.5
                    ));
                }
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 8);
                    double radius = 6.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    for (double y = 0; y < 4; y += 0.5) {
                        player.getWorld().spawnParticle(
                            Particle.SNOWFLAKE,
                            center.clone().add(x, y, z),
                            5, 0.2, 0.2, 0.2, 0
                        );
                    }
                }
                
                for (Entity e : player.getWorld().getNearbyEntities(center, 7, 7, 7)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(200);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, level + 2));
                        ((LivingEntity) e).damage(level * 2, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private void ultimateAvalancheAbility(Player player, int level) {
        player.sendMessage("§6§l🏔️ ULTIMATE AVALANCHE: ICE AGE 🏔️");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.2f);
        
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (e instanceof LivingEntity && e != player && !(e instanceof ArmorStand)) {
                targets.add((LivingEntity) e);
            }
        }
        
        if (targets.isEmpty()) {
            player.sendMessage("§cNo enemies nearby for cinematic battle!");
            return;
        }
        
        plugin.getCinematicWarManager().startCinematicFight(player, targets, "avalanche", level, () -> {
            for (int i = 0; i < 100; i++) {
                player.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
                    player.getLocation().add(random.nextDouble() * 50 - 25, random.nextDouble() * 15, random.nextDouble() * 50 - 25),
                    80, 0.5, 0.5, 0.5, 0
                );
            }
            
            player.sendTitle("§6🏔️ ICE AGE §e🏔️", "§fThe world freezes!", 10, 40, 20);
        });
    }
}
