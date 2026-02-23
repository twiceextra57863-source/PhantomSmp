package com.yourname.smpstarter.manager;

import com.yourname.smpstarter.SMPStarter;
import com.yourname.smpstarter.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class BookManager {
    
    private final SMPStarter plugin;
    private final Random random = new Random();
    
    public BookManager(SMPStarter plugin) {
        this.plugin = plugin;
    }
    
    public void giveBookToPlayer(Player player, String bookName) {
        // Find book by name
        for (MagicBook book : MagicBook.values()) {
            if (book.getDisplayName().contains(bookName) || 
                book.name().equalsIgnoreCase(bookName) ||
                book.getAbilityKey().equalsIgnoreCase(bookName)) {
                giveBookWithCeremony(player, book);
                return;
            }
        }
        
        // If not found, give random
        giveRandomBook(player);
    }
    
    public void giveRandomBook(Player player) {
        MagicBook randomBook = MagicBook.getRandomBook();
        giveBookWithCeremony(player, randomBook);
    }
    
    private void giveBookWithCeremony(Player player, MagicBook book) {
        // Protect player during ceremony
        plugin.getTimerManager().getProtectedPlayers().add(player.getUniqueId());
        
        player.sendMessage("§d§l✨ Magical ceremony starting for §e" + player.getName() + " §d§l✨");
        
        // Start particle effect
        ParticleManager particleManager = new ParticleManager(plugin);
        particleManager.startCircleEffect(player, () -> {
            // Give book
            player.getInventory().addItem(book.createBook());
            
            // Remove protection
            plugin.getTimerManager().getProtectedPlayers().remove(player.getUniqueId());
            
            // Celebration message
            Bukkit.broadcastMessage("§d§l" + player.getName() + " §ereceived: §6" + book.getDisplayName());
            
            // Play sound
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            
            // Final effect
            for (int i = 0; i < 20; i++) {
                player.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    player.getLocation().add(0, 1, 0),
                    10, 0.5, 0.5, 0.5, 0.1
                );
            }
        });
    }
    
    public void useBookAbility(Player player, ItemStack book) {
        if (book == null || !book.hasItemMeta()) return;
        
        ItemMeta meta = book.getItemMeta();
        if (!meta.hasLore()) return;
        
        // Extract ability key from lore
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
        
        // Execute ability based on key
        executeAbility(player, magicBook);
    }
    
    private void executeAbility(Player player, MagicBook book) {
        switch (book.getAbilityKey().toLowerCase()) {
            case "thor":
                thorAbility(player);
                break;
            case "storm":
                stormAbility(player);
                break;
            case "phoenix":
                phoenixAbility(player);
                break;
            case "ice":
                iceAbility(player);
                break;
            case "dragon":
                dragonAbility(player);
                break;
            case "void":
                voidAbility(player);
                break;
            case "nature":
                natureAbility(player);
                break;
            case "cosmic":
                cosmicAbility(player);
                break;
            case "shadow":
                shadowAbility(player);
                break;
            case "light":
                lightAbility(player);
                break;
            case "earth":
                earthAbility(player);
                break;
            case "wind":
                windAbility(player);
                break;
            case "time":
                timeAbility(player);
                break;
            case "soul":
                soulAbility(player);
                break;
            case "crystal":
                crystalAbility(player);
                break;
            case "thunder":
                thunderAbility(player);
                break;
            case "frost":
                frostAbility(player);
                break;
            case "flame":
                flameAbility(player);
                break;
            case "spirit":
                spiritAbility(player);
                break;
            case "necro":
                necroAbility(player);
                break;
            case "angel":
                angelAbility(player);
                break;
            case "demon":
                demonAbility(player);
                break;
            case "chaos":
                chaosAbility(player);
                break;
            case "order":
                orderAbility(player);
                break;
            case "dream":
                dreamAbility(player);
                break;
            case "nightmare":
                nightmareAbility(player);
                break;
            case "aurora":
                auroraAbility(player);
                break;
            case "galaxy":
                galaxyAbility(player);
                break;
            case "inferno":
                infernoAbility(player);
                break;
            case "avalanche":
                avalancheAbility(player);
                break;
        }
    }
    
    // 1. THOR - Lightning Strikes
    private void thorAbility(Player player) {
        player.sendMessage("§e§l⚡ THOR'S WRATH ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        
        Location targetLoc = player.getTargetBlock(null, 50).getLocation().add(0, 1, 0);
        
        // Multiple lightning strikes
        for (int i = 0; i < 5; i++) {
            Location strikeLoc = targetLoc.clone().add(
                random.nextInt(5) - 2,
                0,
                random.nextInt(5) - 2
            );
            
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getWorld().strikeLightningEffect(strikeLoc);
                
                // Damage nearby entities
                for (Entity e : player.getWorld().getNearbyEntities(strikeLoc, 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(10, player);
                    }
                }
            }, i * 5L);
        }
        
        // Particle effect
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    player.getLocation().add(0, 2, 0),
                    10, 0.5, 0.5, 0.5, 0.1
                );
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 2. STORM - Tornado Effect
    private void stormAbility(Player player) {
        player.sendMessage("§b§l🌪️ STORM BREAKER 🌪️");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                // Tornado particles
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 2.0;
                    double y = ticks * 0.1;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location particleLoc = center.clone().add(x, y, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.CLOUD,
                        particleLoc,
                        5, 0.1, 0.1, 0.1, 0.1
                    );
                    
                    // Pull entities
                    for (Entity e : player.getWorld().getNearbyEntities(particleLoc, 2, 2, 2)) {
                        if (e instanceof LivingEntity && e != player) {
                            Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.3);
                            e.setVelocity(pull);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 3. PHOENIX - Rebirth and Fire
    private void phoenixAbility(Player player) {
        player.sendMessage("§c§l🔥 PHOENIX FLAME 🔥");
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
        
        // Heal player
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(100);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 1));
        
        // Phoenix wings effect
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 1, 0);
                
                // Left wing
                for (int i = -3; i <= 3; i++) {
                    Location wingLoc = loc.clone().add(
                        -1.5, 
                        i * 0.3, 
                        i * 0.5
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        wingLoc,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    // Right wing
                    wingLoc = loc.clone().add(
                        1.5,
                        i * 0.3,
                        i * 0.5
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        wingLoc,
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 4. ICE - Freeze Enemies
    private void iceAbility(Player player) {
        player.sendMessage("§3§l❄️ FROST WEAVER ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 1.0f);
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                LivingEntity target = (LivingEntity) e;
                
                // Freeze effect
                target.setFreezeTicks(200);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 4));
                
                // Ice particles
                new BukkitRunnable() {
                    int ticks = 0;
                    @Override
                    public void run() {
                        if (ticks++ >= 40 || target.isDead()) {
                            cancel();
                            return;
                        }
                        
                        target.getWorld().spawnParticle(
                            Particle.SNOWFLAKE,
                            target.getLocation().add(0, 1, 0),
                            5, 0.3, 0.5, 0.3, 0.01
                        );
                        
                        target.getWorld().spawnParticle(
                            Particle.ITEM_SNOWBALL,
                            target.getLocation().add(0, 1, 0),
                            3, 0.2, 0.3, 0.2, 0.01
                        );
                    }
                }.runTaskTimer(plugin, 0L, 5L);
            }
        }
    }
    
    // 5. DRAGON - Dragon Breath
    private void dragonAbility(Player player) {
        player.sendMessage("§5§l🐉 DRAGON'S FURY 🐉");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        
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
                
                // Dragon breath particles
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
                
                // Damage entities
                for (Entity e : player.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(4, player);
                        e.setFireTicks(100);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 6. VOID - Teleport and Phase
    private void voidAbility(Player player) {
        player.sendMessage("§8§l🌌 VOID WALKER 🌌");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        Location targetLoc = player.getTargetBlock(null, 30).getLocation().add(0, 1, 0);
        Location startLoc = player.getLocation();
        
        // Void portal effect at start
        for (int i = 0; i < 50; i++) {
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                startLoc,
                1, 1, 1, 1, 0.5
            );
        }
        
        // Teleport
        player.teleport(targetLoc);
        
        // Void portal effect at end
        for (int i = 0; i < 50; i++) {
            player.getWorld().spawnParticle(
                Particle.PORTAL,
                targetLoc,
                1, 1, 1, 1, 0.5
            );
        }
        
        // Give temporary invulnerability
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
    }
    
    // 7. NATURE - Grow Trees and Heal
    private void natureAbility(Player player) {
        player.sendMessage("§2§l🌿 NATURE'S BLESSING 🌿");
        player.playSound(player.getLocation(), Sound.BLOCK_GROWING_PLANT_CROP, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        // Heal nearby players
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(center) <= 10) {
                p.setHealth(Math.min(p.getHealth() + 8, p.getMaxHealth()));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
            }
        }
        
        // Nature particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                double radius = 3.0;
                double angle = ticks * 0.3;
                
                for (int i = 0; i < 4; i++) {
                    double x = radius * Math.cos(angle + i * Math.PI / 2);
                    double z = radius * Math.sin(angle + i * Math.PI / 2);
                    
                    Location particleLoc = center.clone().add(x, 0.5 + Math.sin(ticks * 0.2) * 0.5, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.HAPPY_VILLAGER,
                        particleLoc,
                        5, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.COMPOSTER,
                        particleLoc,
                        2, 0.2, 0.2, 0.2, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 8. COSMIC - Gravity Control
    private void cosmicAbility(Player player) {
        player.sendMessage("§d§l✨ COSMIC POWER ✨");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                // Gravity sphere
                for (int i = 0; i < 360; i += 30) {
                    double phi = Math.toRadians(i + ticks * 5);
                    double theta = Math.toRadians(i);
                    
                    double x = 3 * Math.sin(theta) * Math.cos(phi);
                    double y = 3 * Math.sin(theta) * Math.sin(phi);
                    double z = 3 * Math.cos(theta);
                    
                    Location particleLoc = center.clone().add(x, y + 1, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        particleLoc,
                        1, 0, 0, 0, 0
                    );
                    
                    // Pull/push entities
                    for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                        if (e instanceof LivingEntity && e != player) {
                            Vector force = center.toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.2);
                            e.setVelocity(force);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 9. SHADOW - Invisibility and Backstab
    private void shadowAbility(Player player) {
        player.sendMessage("§7§l👻 SHADOW STRIKE 👻");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.0f);
        
        // Invisibility and effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1)); // Trade-off
        
        // Shadow particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.SMOKE,
                    player.getLocation().add(0, 1, 0),
                    5, 0.3, 0.5, 0.3, 0.01
                );
                
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    player.getLocation().add(0, 1, 0),
                    2, 0.2, 0.3, 0.2, 0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 10. LIGHT - Holy Healing and Blind
    private void lightAbility(Player player) {
        player.sendMessage("§e§l☀️ LIGHT BRINGER ☀️");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 2.0f);
        
        Location center = player.getLocation();
        
        // Heal self
        player.setHealth(player.getMaxHealth());
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
        
        // Blind enemies and heal allies
        for (Entity e : player.getWorld().getNearbyEntities(center, 15, 15, 15)) {
            if (e instanceof Player) {
                Player target = (Player) e;
                
                if (target != player) {
                    // Check if ally (you can modify this logic)
                    if (target.getHealth() < target.getMaxHealth()) {
                        target.setHealth(Math.min(target.getHealth() + 4, target.getMaxHealth()));
                        target.sendMessage("§eYou feel blessed by holy light!");
                    } else {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                    }
                }
            }
        }
        
        // Light pillar
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (double y = 0; y < 10; y += 0.5) {
                    Location particleLoc = center.clone().add(0, y, 0);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        particleLoc,
                        1, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        particleLoc,
                        2, 0.2, 0.1, 0.2, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 11. EARTH - Earthquake and Walls (FIXED)
    private void earthAbility(Player player) {
        player.sendMessage("§6§l⛰️ EARTH SHAKER ⛰️");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 0.5f);
        
        Location center = player.getLocation();
        
        // Earthquake effect
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                // Screen shake for nearby players
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(center) <= 20) {
                        p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 0.5f, 0.5f);
                    }
                }
                
                // Ground particles
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 3.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location particleLoc = center.clone().add(x, 0.1, z);
                    
                    // FIXED: Using Particle.BLOCK instead of Particle.BLOCK_CRACK
                    player.getWorld().spawnParticle(
                        Particle.BLOCK,
                        particleLoc,
                        5, 0.1, 0.1, 0.1, 0,
                        Bukkit.createBlockData(Material.STONE)
                    );
                }
                
                // Damage and knockback
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 2, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(2, player);
                        e.setVelocity(new Vector(0, 0.5, 0));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 12. WIND - Launch and Double Jump
    private void windAbility(Player player) {
        player.sendMessage("§f§l💨 WIND RUNNER 💨");
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 2.0f);
        
        // Launch player
        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(1.5));
        
        // Allow double jump for next 10 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1));
        
        // Wind particles trail
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    player.getLocation().add(0, 1, 0),
                    5, 0.2, 0.2, 0.2, 0.01
                );
                
                player.getWorld().spawnParticle(
                    Particle.GUST,
                    player.getLocation().add(0, 1, 0),
                    2, 0.3, 0.2, 0.3, 0.1
                );
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 13. TIME - Slow Time
    private void timeAbility(Player player) {
        player.sendMessage("§b§l⏰ TIME KEEPER ⏰");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1.0f, 0.5f);
        
        // Speed up self
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 2));
        
        // Slow down enemies
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 3));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
            }
        }
        
        // Clock particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 2, 0);
                double angle = ticks * 0.5;
                
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
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 14. SOUL - Life Steal
    private void soulAbility(Player player) {
        player.sendMessage("§4§l💀 SOUL REAPER 💀");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 8, 8, 8)) {
            if (e instanceof LivingEntity && e != player) {
                LivingEntity target = (LivingEntity) e;
                
                // Damage and heal
                target.damage(6, player);
                player.setHealth(Math.min(player.getHealth() + 3, player.getMaxHealth()));
                
                // Soul particles
                Location targetLoc = target.getLocation().add(0, 1, 0);
                Location playerLoc = player.getLocation().add(0, 1, 0);
                
                // Soul transfer effect
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
                            3, 0.1, 0.1, 0.1, 0.01
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.SCULK_SOUL,
                            soulLoc,
                            1, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }
    }
    
    // 15. CRYSTAL - Crystal Shield and Projectiles
    private void crystalAbility(Player player) {
        player.sendMessage("§d§l💎 CRYSTAL MAGE 💎");
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 2.0f);
        
        // Crystal shield effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 1));
        
        // Crystal particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 80) {
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
                    
                    Location crystalLoc = center.clone().add(x, y, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        crystalLoc,
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        crystalLoc,
                        1, 0, 0, 0, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 16. THUNDER - Chain Lightning
    private void thunderAbility(Player player) {
        player.sendMessage("§e§l🌩️ THUNDER GOD 🌩️");
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        
        List<Entity> targets = new ArrayList<>();
        
        // Find initial targets
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                targets.add(e);
            }
        }
        
        // Chain lightning between targets
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
                    
                    // Lightning effect
                    player.getWorld().strikeLightningEffect(currentLoc);
                    
                    // Chain particles
                    Vector direction = currentLoc.toVector().subtract(lastLoc.toVector());
                    double length = direction.length();
                    direction.normalize();
                    
                    for (double d = 0; d < length; d += 0.5) {
                        Location particleLoc = lastLoc.clone().add(direction.clone().multiply(d));
                        
                        player.getWorld().spawnParticle(
                            Particle.ELECTRIC_SPARK,
                            particleLoc,
                            5, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                    
                    // Damage
                    if (current instanceof LivingEntity) {
                        ((LivingEntity) current).damage(8, player);
                    }
                    
                    lastLoc = currentLoc;
                    index++;
                }
            }.runTaskTimer(plugin, 0L, 5L);
        }
    }
    
    // 17. FROST - Ice Armor
    private void frostAbility(Player player) {
        player.sendMessage("§3§l❄️ FROST KNIGHT ❄️");
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 2.0f);
        
        // Ice armor effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1)); // Trade-off
        
        // Freeze aura
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                // Ice armor particles
                for (int i = 0; i < 360; i += 20) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 2.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location armorLoc = player.getLocation().clone().add(x, 0.5, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        armorLoc,
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.ITEM_SNOWBALL,
                        armorLoc,
                        1, 0.1, 0.1, 0.1, 0
                    );
                }
                
                // Freeze nearby enemies
                for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(40);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 2));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 18. FLAME - Fire Ring
    private void flameAbility(Player player) {
        player.sendMessage("§c§l🔥 FLAME EMPEROR 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        
        Location center = player.getLocation();
        
        // Fire ring
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                double radius = 3.0;
                double angle = ticks * 0.3;
                
                for (int i = 0; i < 360; i += 15) {
                    double theta = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta);
                    
                    Location flameLoc = center.clone().add(x, 0.5, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        flameLoc,
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.LAVA,
                        flameLoc,
                        1, 0.1, 0.1, 0.1, 0
                    );
                    
                    // Set fire to entities
                    for (Entity e : player.getWorld().getNearbyEntities(flameLoc, 1, 1, 1)) {
                        if (e instanceof LivingEntity && e != player) {
                            e.setFireTicks(60);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 19. SPIRIT - Summon Spirit Wolves
    private void spiritAbility(Player player) {
        player.sendMessage("§7§l👾 SPIRIT GUIDE 👾");
        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 1.0f);
        
        // Summon 3 spirit wolves
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
            wolf.setHealth(30);
            wolf.setAngry(false);
            
            // Make them glow
            wolf.setGlowing(true);
            
            // Despawn after 30 seconds
            Bukkit.getScheduler().runTaskLater(plugin, wolf::remove, 600L);
            
            // Spirit particles
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (wolf.isDead() || !wolf.isValid()) {
                        cancel();
                        return;
                    }
                    
                    wolf.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        wolf.getLocation().add(0, 1, 0),
                        3, 0.2, 0.3, 0.2, 0.01
                    );
                }
            }.runTaskTimer(plugin, 0L, 5L);
        }
    }
    
    // 20. NECRO - Raise Dead
    private void necroAbility(Player player) {
        player.sendMessage("§5§l⚰️ NECROMANCER ⚰️");
        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 0.5f);
        
        // Find dead mobs nearby
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof Zombie || e instanceof Skeleton) {
                Monster undead = (Monster) e;
                
                // Make them allies
                undead.setTarget(null);
                undead.setCustomName("§5" + player.getName() + "'s Minion");
                undead.setCustomNameVisible(true);
                undead.setGlowing(true);
                
                // Give them strength
                undead.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
                
                // Despawn after 20 seconds
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (undead.isValid()) {
                        undead.remove();
                    }
                }, 400L);
            }
        }
        
        // Necromancy particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                player.getWorld().spawnParticle(
                    Particle.SOUL,
                    player.getLocation().add(0, 2, 0),
                    10, 1, 1, 1, 0.1
                );
                
                player.getWorld().spawnParticle(
                    Particle.SCULK_SOUL,
                    player.getLocation().add(0, 1, 0),
                    5, 0.5, 0.5, 0.5, 0.05
                );
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 21. ANGEL - Healing Aura
    private void angelAbility(Player player) {
        player.sendMessage("§f§l👼 ANGEL'S GRACE 👼");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 2.0f);
        
        // Slow falling and healing
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
        
        // Healing aura for allies
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                // Angel wings effect
                Location loc = player.getLocation().add(0, 1, 0);
                
                for (int i = -3; i <= 3; i++) {
                    double offset = Math.abs(i) * 0.3;
                    
                    // Left wing
                    Location leftWing = loc.clone().add(-1.5 - offset, i * 0.3, i * 0.3);
                    player.getWorld().spawnParticle(Particle.END_ROD, leftWing, 2, 0.1, 0.1, 0.1, 0);
                    
                    // Right wing
                    Location rightWing = loc.clone().add(1.5 + offset, i * 0.3, i * 0.3);
                    player.getWorld().spawnParticle(Particle.END_ROD, rightWing, 2, 0.1, 0.1, 0.1, 0);
                }
                
                // Heal nearby allies
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(player.getLocation()) <= 10 && p != player) {
                        if (p.getHealth() < p.getMaxHealth()) {
                            p.setHealth(Math.min(p.getHealth() + 1, p.getMaxHealth()));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    // 22. DEMON - Rage Mode
    private void demonAbility(Player player) {
        player.sendMessage("§4§l👿 DEMON'S RAGE 👿");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.5f);
        
        // Strength and fire
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 1));
        
        // Demon aura
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 1, 0);
                
                // Fire ring
                double radius = 3.0;
                double angle = ticks * 0.3;
                
                for (int i = 0; i < 360; i += 30) {
                    double theta = Math.toRadians(i + ticks * 10);
                    double x = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta);
                    
                    player.getWorld().spawnParticle(
                        Particle.FLAME,
                        loc.clone().add(x, 0, z),
                        2, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        loc.clone().add(x, 1, z),
                        1, 0.1, 0.1, 0.1, 0.01
                    );
                }
                
                // Damage nearby enemies
                for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).damage(2, player);
                        e.setFireTicks(40);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 23. CHAOS - Random Effects
    private void chaosAbility(Player player) {
        player.sendMessage("§5§l🌀 CHAOS MAGIC 🌀");
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 1.0f);
        
        PotionEffectType[] effects = {
            PotionEffectType.SPEED,
            PotionEffectType.SLOWNESS,
            PotionEffectType.HASTE,
            PotionEffectType.MINING_FATIGUE,
            PotionEffectType.STRENGTH,
            PotionEffectType.WEAKNESS,
            PotionEffectType.INSTANT_HEALTH,
            PotionEffectType.INSTANT_DAMAGE,
            PotionEffectType.JUMP_BOOST,
            PotionEffectType.NAUSEA,
            PotionEffectType.REGENERATION,
            PotionEffectType.RESISTANCE,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.WATER_BREATHING,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.BLINDNESS,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.HUNGER,
            PotionEffectType.POISON
        };
        
        // Give random effect to self
        PotionEffectType selfEffect = effects[random.nextInt(effects.length)];
        player.addPotionEffect(new PotionEffect(selfEffect, 200, random.nextInt(3)));
        
        // Give random effect to nearby entities
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                PotionEffectType targetEffect = effects[random.nextInt(effects.length)];
                ((LivingEntity) e).addPotionEffect(new PotionEffect(targetEffect, 100, random.nextInt(2)));
            }
        }
        
        // Chaos particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 10; i++) {
                    player.getWorld().spawnParticle(
                        Particle.values()[random.nextInt(Particle.values().length)],
                        player.getLocation().add(
                            random.nextDouble() * 4 - 2,
                            random.nextDouble() * 3,
                            random.nextDouble() * 4 - 2
                        ),
                        1, 0, 0, 0, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 24. ORDER - Smite Evil
    private void orderAbility(Player player) {
        player.sendMessage("§b§l⚖️ ORDER'S JUDGEMENT ⚖️");
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.0f, 2.0f);
        
        // Protection for allies
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2));
        
        // Smite undead
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 15, 15, 15)) {
            if (e instanceof Monster) {
                Monster monster = (Monster) e;
                monster.damage(20, player);
                player.getWorld().strikeLightningEffect(monster.getLocation());
                
                // Holy particles
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 10 || monster.isDead()) {
                            cancel();
                            return;
                        }
                        
                        monster.getWorld().spawnParticle(
                            Particle.END_ROD,
                            monster.getLocation().add(0, 1, 0),
                            5, 0.3, 0.5, 0.3, 0.01
                        );
                    }
                }.runTaskTimer(plugin, 0L, 2L);
            }
        }
    }
    
    // 25. DREAM - Sleep Effect (FIXED)
    private void dreamAbility(Player player) {
        player.sendMessage("§d§l💭 DREAM WEAVER 💭");
        // FIXED: Using correct Allay sound
        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1.0f, 1.0f);
        
        // Heal self
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(10);
        
        // Put enemies to sleep (slowness + blindness)
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 8, 8, 8)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 4));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
            }
        }
        
        // Dream particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 80) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 2, 0);
                
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 5);
                    double radius = 2.5;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        loc.clone().add(x, 0, z),
                        1, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.WAX_OFF,
                        loc.clone().add(x, 0.5, z),
                        1, 0.1, 0.1, 0.1, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 26. NIGHTMARE - Fear Effect
    private void nightmareAbility(Player player) {
        player.sendMessage("§8§l👹 NIGHTMARE 👹");
        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.8f);
        
        // Give self strength
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 1));
        
        // Fear effect on enemies
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                LivingEntity target = (LivingEntity) e;
                
                // Give negative effects
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 2));
                target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 2));
                
                // Make them run away
                Vector away = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                target.setVelocity(away.multiply(0.5));
                
                // Nightmare particles
                new BukkitRunnable() {
                    int steps = 0;
                    @Override
                    public void run() {
                        if (steps++ >= 20 || target.isDead()) {
                            cancel();
                            return;
                        }
                        
                        target.getWorld().spawnParticle(
                            Particle.SCULK_SOUL,
                            target.getLocation().add(0, 1, 0),
                            3, 0.3, 0.5, 0.3, 0.01
                        );
                        
                        target.getWorld().spawnParticle(
                            Particle.SMOKE,
                            target.getLocation().add(0, 1, 0),
                            2, 0.3, 0.3, 0.3, 0.01
                        );
                    }
                }.runTaskTimer(plugin, 0L, 2L);
            }
        }
    }
    
    // 27. AURORA - Colorful Lights
    private void auroraAbility(Player player) {
        player.sendMessage("§b§l🌈 AURORA 🌈");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 2.0f);
        
        // Speed boost
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 200, 1));
        
        // Aurora particles
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 120) {
                    cancel();
                    return;
                }
                
                Location center = player.getLocation().add(0, 5, 0);
                
                // Waving aurora effect
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double radius = 5.0 + Math.sin(ticks * 0.1) * 2;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = Math.sin(angle + ticks * 0.2) * 3;
                    
                    Location particleLoc = center.clone().add(x, y, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        particleLoc,
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        particleLoc,
                        1, 0, 0, 0, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 28. GALAXY - Star Power
    private void galaxyAbility(Player player) {
        player.sendMessage("§d§l🌌 GALAXY 🌌");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
        
        Location center = player.getLocation();
        
        // Galaxy effect
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                // Spiral galaxy
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 3);
                    double radius = 3.0 + Math.sin(ticks * 0.1) * 1;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = Math.cos(angle * 2) * 1.5;
                    
                    Location starLoc = center.clone().add(x, y + 1, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.FIREWORK,
                        starLoc,
                        3, 0.1, 0.1, 0.1, 0.01
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        starLoc,
                        1, 0, 0, 0, 0
                    );
                }
                
                // Pull entities
                for (Entity e : player.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (e instanceof LivingEntity && e != player) {
                        Vector pull = center.toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.1);
                        e.setVelocity(pull);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 29. INFERNO - Massive Fire Explosion
    private void infernoAbility(Player player) {
        player.sendMessage("§c§l🔥 INFERNO 🔥");
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
        
        Location center = player.getLocation();
        
        // Massive fire explosion
        player.getWorld().createExplosion(center, 3, false, true);
        
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
                        Location fireLoc = center.clone().add(x, y, z);
                        
                        player.getWorld().spawnParticle(
                            Particle.FLAME,
                            fireLoc,
                            5, 0.1, 0.1, 0.1, 0.02
                        );
                        
                        player.getWorld().spawnParticle(
                            Particle.LAVA,
                            fireLoc,
                            2, 0.1, 0.1, 0.1, 0.01
                        );
                    }
                }
                
                // Set everything on fire
                for (Entity e : player.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                    if (e != player) {
                        e.setFireTicks(100);
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(4, player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    // 30. AVALANCHE - Snow Barrage
    private void avalancheAbility(Player player) {
        player.sendMessage("§3§l🏔️ AVALANCHE 🏔️");
        player.playSound(player.getLocation(), Sound.BLOCK_POWDER_SNOW_BREAK, 1.0f, 0.5f);
        
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                // Snowball barrage
                for (int i = 0; i < 5; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double x = Math.cos(angle) * 2;
                    double z = Math.sin(angle) * 2;
                    
                    Location snowLoc = center.clone().add(x, 2 + random.nextDouble() * 2, z);
                    
                    // Launch snowballs
                    Snowball snowball = player.getWorld().spawn(snowLoc, Snowball.class);
                    snowball.setShooter(player);
                    snowball.setVelocity(new Vector(
                        random.nextDouble() * 2 - 1,
                        -0.5,
                        random.nextDouble() * 2 - 1
                    ));
                }
                
                // Snow particles
                for (int i = 0; i < 360; i += 30) {
                    double angle = Math.toRadians(i + ticks * 10);
                    double radius = 4.0;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    
                    Location particleLoc = center.clone().add(x, 0.5, z);
                    
                    player.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        particleLoc,
                        5, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.ITEM_SNOWBALL,
                        particleLoc,
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
                
                // Freeze effect
                for (Entity e : player.getWorld().getNearbyEntities(center, 4, 4, 4)) {
                    if (e instanceof LivingEntity && e != player) {
                        ((LivingEntity) e).setFreezeTicks(60);
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 3));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
