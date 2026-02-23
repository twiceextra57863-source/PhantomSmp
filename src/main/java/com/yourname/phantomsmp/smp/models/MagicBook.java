package com.phantom.smp.models;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public enum MagicBook {
    
    // 30 EPIC BOOK NAMES
    STORMBRINGER("🌩️ Stormbringer", Material.ENCHANTED_BOOK, 
         "Call down lightning from the skies", 30, "storm"),
    
    SHADOWSTRIKE("👻 Shadowstrike", Material.ENCHANTED_BOOK,
          "Vanish and strike from the darkness", 25, "shadow"),
    
    FLAMEWALKER("🔥 Flamewalker", Material.ENCHANTED_BOOK,
            "Walk through fire and leave destruction", 20, "flame"),
    
    FROSTBITE("❄️ Frostbite", Material.ENCHANTED_BOOK,
        "Freeze your enemies solid", 25, "frost"),
    
    DRAGONSBREATH("🐉 Dragon's Breath", Material.ENCHANTED_BOOK,
           "Exhale devastating dragon fire", 35, "dragon"),
    
    VOIDWALKER("🌌 Voidwalker", Material.ENCHANTED_BOOK,
         "Phase through reality itself", 40, "void"),
    
    LIFEBINDER("🌿 Lifebinder", Material.ENCHANTED_BOOK,
           "Heal and nurture all life", 15, "life"),
    
    GRAVITY("⚡ Gravity", Material.ENCHANTED_BOOK,
           "Control the pull of the earth", 30, "gravity"),
    
    PHANTOM("👤 Phantom", Material.ENCHANTED_BOOK,
           "Become one with the shadows", 20, "phantom"),
    
    DAWNBRINGER("☀️ Dawnbringer", Material.ENCHANTED_BOOK,
          "Bring light to the darkest places", 25, "dawn"),
    
    TERRASHAPER("⛰️ Terrashaper", Material.ENCHANTED_BOOK,
          "Shape the earth at your will", 35, "terra"),
    
    ZEPHYR("💨 Zephyr", Material.ENCHANTED_BOOK,
         "Ride the winds of fate", 15, "wind"),
    
    TIMEWEAVER("⏳ Timeweaver", Material.ENCHANTED_BOOK,
         "Bend time to your advantage", 45, "time"),
    
    SOULREAPER("💀 Soulreaper", Material.ENCHANTED_BOOK,
         "Feast on the souls of enemies", 30, "soul"),
    
    CRYSTALMAGE("💎 Crystalmage", Material.ENCHANTED_BOOK,
            "Summon crystals of pure power", 25, "crystal"),
    
    THUNDERGOD("⚡ Thundergod", Material.ENCHANTED_BOOK,
            "Wield the power of storms", 35, "thunder"),
    
    ICEWARDEN("❄️ Icewarden", Material.ENCHANTED_BOOK,
          "Command the frozen wastes", 25, "ice"),
    
    PYROMANCER("🔥 Pyromancer", Material.ENCHANTED_BOOK,
          "Master of living flame", 20, "pyro"),
    
    SPIRITWARDEN("👾 Spiritwarden", Material.ENCHANTED_BOOK,
           "Call upon ancestral spirits", 30, "spirit"),
    
    NECROLORD("⚰️ Necrolord", Material.ENCHANTED_BOOK,
          "Raise the dead to serve you", 40, "necro"),
    
    SERAPHIM("👼 Seraphim", Material.ENCHANTED_BOOK,
          "Blessed with divine power", 25, "seraph"),
    
package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        plugin.getTimerManager().getProtectedPlayers().add(player.getUniqueId());
        
        player.sendMessage("§d§l✨ PHANTOM CEREMONY ✨");
        player.sendMessage("§fThe ancient spirits have chosen you...");
        
        ParticleManager particleManager = new ParticleManager(plugin);
        particleManager.startCircleEffect(player, () -> {
            player.getInventory().addItem(book.createBook());
            plugin.getTimerManager().getProtectedPlayers().remove(player.getUniqueId());
            
            Bukkit.broadcastMessage("§d§l" + player.getName() + " §ehas awakened: §6" + book.getDisplayName());
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
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
        
        if (plugin.getCooldownManager().isOnCooldown(player, magicBook)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player, magicBook);
            player.sendMessage("§c❌ " + magicBook.getDisplayName() + " §7is on cooldown for §f" + remaining + "s");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.5f);
            return;
        }
        
        plugin.getCooldownManager().setCooldown(player, magicBook);
        
        player.sendMessage("§d§l✨ " + magicBook.getDisplayName() + " §eAWAKENED! ✨");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 2.0f);
        
        // Add your ability implementations here
        switch (magicBook.getAbilityKey().toLowerCase()) {
            case "storm":
                player.sendMessage("§b§l⚡ Stormbringer calls the lightning!");
                break;
            case "shadow":
                player.sendMessage("§7§l👻 You fade into the shadows!");
                break;
            default:
                player.sendMessage("§cAbility not yet implemented!");
                break;
        }
    }
}
