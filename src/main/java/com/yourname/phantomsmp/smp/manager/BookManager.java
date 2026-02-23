package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
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
    
    private final PhantomSMP plugin;
    private final Random random = new Random();
    
    public BookManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
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
    
    private void giveBookWithCeremony(Player player, MagicBook book) {
        player.sendMessage("§d§l✨ PHANTOM CEREMONY ✨");
        player.sendMessage("§fThe ancient spirits have chosen you...");
        
        // Use ceremony manager with freeze
        plugin.getCeremonyManager().startCeremony(player, book, () -> {
            // Give book after ceremony
            player.getInventory().addItem(book.createBook());
            
            // Celebration message
            Bukkit.broadcastMessage("§d§l" + player.getName() + " §ehas awakened: §6" + book.getDisplayName());
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            
            // Final effect
            for (int i = 0; i < 10; i++) {
                player.getWorld().spawnParticle(
                    Particle.FIREWORK,
                    player.getLocation().add(0, 2, 0),
                    20, 1, 1, 1, 0.1
                );
            }
        });
    }
    
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
        
        // Check cooldown
        if (plugin.getCooldownManager().isOnCooldown(player, magicBook)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, magicBook);
            player.sendMessage("§c❌ " + magicBook.getDisplayName() + " §7is on cooldown for §f" + remaining + "s");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.5f);
            return;
        }
        
        // Set cooldown
        plugin.getCooldownManager().setCooldown(player, magicBook);
        
        // Activation message
        player.sendMessage("§d§l✨ " + magicBook.getDisplayName() + " §eAWAKENED! ✨");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 2.0f);
        
        // Execute ability based on key
        executeAbility(player, magicBook);
    }
    
    private void executeAbility(Player player, MagicBook book) {
        switch (book.getAbilityKey().toLowerCase()) {
            case "storm":
                stormAbility(player);
                break;
            case "shadow":
                shadowAbility(player);
                break;
            case "flame":
                flameAbility(player);
                break;
            case "frost":
                frostAbility(player);
                break;
            case "dragon":
                dragonAbility(player);
                break;
            case "void":
                voidAbility(player);
                break;
            case "life":
                lifeAbility(player);
                break;
            case "gravity":
                gravityAbility(player);
                break;
            case "phantom":
                phantomAbility(player);
                break;
            case "dawn":
                dawnAbility(player);
                break;
            case "terra":
                terraAbility(player);
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
            case "ice":
                iceAbility(player);
                break;
            case "pyro":
                pyroAbility(player);
                break;
            case "spirit":
                spiritAbility(player);
                break;
            case "necro":
                necroAbility(player);
                break;
            case "seraph":
                seraphAbility(player);
                break;
            case "abyss":
                abyssAbility(player);
                break;
            case "chaos":
                chaosAbility(player);
                break;
            case "judge":
                judgeAbility(player);
                break;
            case "dream":
                dreamAbility(player);
                break;
            case "fear":
                fearAbility(player);
                break;
            case "aurora":
                auroraAbility(player);
                break;
            case "star":
                starAbility(player);
                break;
            case "inferno":
                infernoAbility(player);
                break;
            case "avalanche":
                avalancheAbility(player);
                break;
            default:
                player.sendMessage("§cThis ability is not yet implemented!");
                break;
        }
    }
    
    // ==================== 30 ABILITIES ====================
    
    // 1. STORM - Lightning Storm
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
                
                // Storm particles
                player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    center.clone().add(0, 5, 0),
                    50, 5, 2, 5, 0.1
                );
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    // 2. SHADOW - Vanish and Backstab
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
                
                // Teleport behind enemies
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
    
    // 3. FLAME - Fire Ring
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
                    
                    player.getWorld().spawnParticle(
                        Particle.LAVA,
                        flameLoc,
                        2, 0.1, 0.1, 0.1, 0
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
    
    // 4. FROST - Freeze Aura
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
    
    // 5. DRAGON - Dragon Breath
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
                
                player.getWorld().spawnParticle(
                    Particle.FLAME,
                    current,
                    10, 0.2, 0.2, 0.2, 0.01
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
    
    // 6. VOID - Teleportation
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
    
    // 7. LIFE - Healing Aura
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
    
    // 8. GRAVITY - Pull/Push
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
    
    // 9. PHANTOM - Ghost Form
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
    
    // 10. DAWN - Holy Light
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
                    } else if (e instanceof Player && e != player) {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    // 11. TERRA - Earth Shake
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
    
    // 12. WIND - Launch
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
                
                player.getWorld().spawnParticle(
                    Particle.GUST,
                    player.getLocation().add(0, 1, 0),
                    5, 0.3, 0.2, 0.3, 0.1
                );
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 13. TIME - Slow Motion
    private void timeAbility(Player player) {
        player.sendMessage("§b§l⏳ TIME WEAVER ⏳");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1.0f, 0.5f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 3));
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (e instanceof LivingEntity && e != player) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 4));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 2));
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
    
    // 14. SOUL - Life Steal
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
    
    // 15. CRYSTAL - Crystal Shield
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
                    
                    player.getWorld().spawnParticle(
                        Particle.GLOW,
                        center.clone().add(x, y, z),
                        1, 0, 0, 0, 0
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
    
    // 16. THUNDER - Chain Lightning
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
    
    // 17. ICE - Ice Armor
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
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 3));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    // 18. PYRO - Fire Burst
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
    
    // 19. SPIRIT - Summon Wolves
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
                        5, 0.2, 0.3, 0.2, 0.01
                    );
                }
            }.runTaskTimer(plugin, 0L, 5L);
        }
    }
    
    // 20. NECRO - Raise Dead
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
                undead.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (undead.isValid()) {
                        undead.remove();
                    }
                }, 300L);
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
                    Particle.SOUL,
                    player.getLocation().add(0, 2, 0),
                    20, 2, 2, 2, 0.1
                );
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    // 21. SERAPH - Angel Wings
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
    
    // 22. ABYSS - Darkness
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
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 2));
                        
                        Vector away = e.getLocation().toVector().subtract(center.toVector()).normalize();
                        e.setVelocity(away.multiply(0.3));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    // 23. CHAOS - Random Effects
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
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                for (int i = 0; i < 10; i++) {
                    player.getWorld().spawnParticle(
                        Particle.values()[random.nextInt(Particle.values().length)],
                        player.getLocation().add(
                            random.nextDouble() * 6 - 3,
                            random.nextDouble() * 3,
                            random.nextDouble() * 6 - 3
                        ),
                        2, 0, 0, 0, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    // 24. JUDGE - Smite
    private void judgeAbility(Player player) {
        player.sendMessage("§b§l⚖️ JUDGEMENT ⚖️");
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 2));
        
        for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 12, 12, 12)) {
            if (e instanceof Monster) {
                Monster monster = (Monster) e;
                monster.damage(15, player);
                player.getWorld().strikeLightningEffect(monster.getLocation());
                
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
                            10, 0.3, 0.5, 0.3, 0.01
                        );
                    }
                }.runTaskTimer(plugin, 0L, 3L);
            }
        }
    }
    
    // 25. DREAM - Sleep
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
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
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
                        2, 0.1, 0.1, 0.1, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.WAX_OFF,
                        loc.clone().add(x, 0.5, z),
                        1, 0.1, 0.1, 0.1, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    // 26. FEAR - Terror
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
                            5, 0.3, 0.5, 0.3, 0.02
                        );
                    }
                }.runTaskTimer(plugin, 0L, 3L);
            }
        }
    }
    
    // 27. AURORA - Lights
    private void auroraAbility(Player player) {
        player.sendMessage("§b§l🌈 AURORA 🌈");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 2.0f);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 150, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 150, 1));
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                Location center = player.getLocation().add(0, 5, 0);
                
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i + ticks * 2);
                    double radius = 4.0 + Math.sin(ticks * 0.1) * 2;
                    
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    double y = Math.sin(angle + ticks * 0.2) * 3;
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        center.clone().add(x, y, z),
                        3, 0.1, 0.1, 0.1, 0
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    // 28. STAR - Meteor
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
    
    // 29. INFERNO - Hellfire
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
    
    // 30. AVALANCHE - Snowstorm
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
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 4));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
}
