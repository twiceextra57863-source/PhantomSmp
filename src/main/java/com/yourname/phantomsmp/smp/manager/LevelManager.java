package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LevelManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, Map<String, Integer>> playerKills = new HashMap<>();
    private final Map<UUID, Map<String, Integer>> playerLevels = new HashMap<>();
    
    // Level requirements
    private final int LEVEL1_KILLS = 0;
    private final int LEVEL2_KILLS = 15;
    private final int LEVEL3_KILLS = 30;
    
    // Cooldown multipliers
    private final double LEVEL1_COOLDOWN = 1.0;
    private final double LEVEL2_COOLDOWN = 0.8;
    private final double LEVEL3_COOLDOWN = 0.6;
    
    public LevelManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void addKill(Player player, String bookKey) {
        UUID playerId = player.getUniqueId();
        
        // Initialize maps if needed
        playerKills.computeIfAbsent(playerId, k -> new HashMap<>());
        playerLevels.computeIfAbsent(playerId, k -> new HashMap<>());
        
        // Get current kills
        int currentKills = playerKills.get(playerId).getOrDefault(bookKey, 0);
        int newKills = currentKills + 1;
        playerKills.get(playerId).put(bookKey, newKills);
        
        // Check for level up
        int oldLevel = getBookLevel(player, bookKey);
        int newLevel = calculateLevel(newKills);
        
        if (newLevel > oldLevel) {
            setBookLevel(player, bookKey, newLevel);
            
            // Trigger transformation effect
            plugin.getTransformationManager().playTransformation(player, 
                MagicBook.getByAbilityKey(bookKey), newLevel);
            
            // Update book in hand if holding
            checkAndUpdateBook(player, bookKey);
        }
    }
    
    public void checkAndUpdateBook(Player player, String bookKey) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        
        if (isHoldingBook(mainHand, bookKey)) {
            updateBookInSlot(player, mainHand, bookKey, player.getInventory().getHeldItemSlot());
        } else if (isHoldingBook(offHand, bookKey)) {
            updateBookInSlot(player, offHand, bookKey, 40); // Offhand slot
        }
        
        // Also check all inventory slots for any copies of this book
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && isHoldingBook(item, bookKey) && item != mainHand && item != offHand) {
                updateBookInSlot(player, item, bookKey, i);
            }
        }
    }
    
    private boolean isHoldingBook(ItemStack item, String bookKey) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
        
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains("Ability: " + bookKey)) {
                return true;
            }
        }
        return false;
    }
    
    private void updateBookInSlot(Player player, ItemStack oldBook, String bookKey, int slot) {
        MagicBook book = MagicBook.getByAbilityKey(bookKey);
        if (book == null) return;
        
        int newLevel = getBookLevel(player, bookKey);
        int kills = getKills(player, bookKey);
        
        // Schedule update for next tick to avoid concurrent modification
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack newBook = book.createBookWithLevel(newLevel, kills);
                player.getInventory().setItem(slot, newBook);
                
                // Play level up effect
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.sendTitle("§6⚡ LEVEL UP! ⚡", "§fYour book evolved to Level " + newLevel, 10, 40, 10);
            }
        }.runTask(plugin);
    }
    
    private int calculateLevel(int kills) {
        if (kills >= LEVEL3_KILLS) return 3;
        if (kills >= LEVEL2_KILLS) return 2;
        return 1;
    }
    
    public int getBookLevel(Player player, String bookKey) {
        return playerLevels.getOrDefault(player.getUniqueId(), new HashMap<>())
                          .getOrDefault(bookKey, 1);
    }
    
    public void setBookLevel(Player player, String bookKey, int level) {
        playerLevels.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                   .put(bookKey, level);
    }
    
    public int getKills(Player player, String bookKey) {
        return playerKills.getOrDefault(player.getUniqueId(), new HashMap<>())
                         .getOrDefault(bookKey, 0);
    }
    
    public double getCooldownMultiplier(int level) {
        switch(level) {
            case 3: return LEVEL3_COOLDOWN;
            case 2: return LEVEL2_COOLDOWN;
            default: return LEVEL1_COOLDOWN;
        }
    }
    
    public int getRequiredKillsForNextLevel(int currentLevel) {
        switch(currentLevel) {
            case 1: return LEVEL2_KILLS;
            case 2: return LEVEL3_KILLS;
            default: return -1; // Max level
        }
    }
    
    public int getKillsNeeded(Player player, String bookKey) {
        int currentKills = getKills(player, bookKey);
        int currentLevel = getBookLevel(player, bookKey);
        
        if (currentLevel >= 3) return 0;
        
        int required = getRequiredKillsForNextLevel(currentLevel);
        return required - currentKills;
    }
    
    public void setAdminLevel(Player player, String bookKey, int level) {
        setBookLevel(player, bookKey, level);
        
        // Set kills to appropriate amount
        int kills = 0;
        if (level >= 2) kills = LEVEL2_KILLS;
        if (level >= 3) kills = LEVEL3_KILLS;
        
        playerKills.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                  .put(bookKey, kills);
        
        // Update book if holding
        checkAndUpdateBook(player, bookKey);
    }
    
    public void resetPlayerData(Player player) {
        playerKills.remove(player.getUniqueId());
        playerLevels.remove(player.getUniqueId());
    }
}
