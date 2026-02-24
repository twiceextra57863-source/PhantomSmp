package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityKeyListener implements Listener {
    
    private final PhantomSMP plugin;
    private final Map<UUID, Boolean> sneakPressed = new HashMap<>();
    private final Map<UUID, Long> lastTKeyPress = new HashMap<>();
    private final Map<UUID, Boolean> tKeyCooldown = new HashMap<>();
    
    public AbilityKeyListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        sneakPressed.put(player.getUniqueId(), event.isSneaking());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && 
            event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Check if player is sneaking (crouching)
        if (!player.isSneaking()) {
            return;
        }
        
        // Check if holding a Phantom book
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isPhantomBook(item)) {
            return;
        }
        
        // Get book and level
        MagicBook book = getBookFromItem(item);
        if (book == null) return;
        
        int level = plugin.getLevelManager().getBookLevel(player, book.getAbilityKey());
        
        // Level 2 ability (Crouch + Left Click)
        if (level >= 2) {
            event.setCancelled(true);
            plugin.getBookManager().executeAdvancedAbility(player, book, level);
        } else {
            player.sendMessage("§c❌ You need Level 2 (Ascended) to use this ability!");
        }
    }
    
    // This would need a separate listener for T key
    // For now, we'll use a command or detection method
    public void checkTKeyPress(Player player) {
        if (!player.isSneaking()) return;
        
        // Check cooldown to prevent spam
        if (tKeyCooldown.getOrDefault(player.getUniqueId(), false)) {
            return;
        }
        
        // Check if holding Phantom book
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isPhantomBook(item)) return;
        
        MagicBook book = getBookFromItem(item);
        if (book == null) return;
        
        int level = plugin.getLevelManager().getBookLevel(player, book.getAbilityKey());
        
        // Level 3 ability (Crouch + T)
        if (level >= 3) {
            tKeyCooldown.put(player.getUniqueId(), true);
            
            // Execute ultimate ability
            plugin.getBookManager().executeUltimateAbility(player, book, level);
            
            // Reset cooldown after 1 second to prevent multiple triggers
            new BukkitRunnable() {
                @Override
                public void run() {
                    tKeyCooldown.remove(player.getUniqueId());
                }
            }.runTaskLater(plugin, 20L);
            
        } else {
            player.sendMessage("§c❌ You need Level 3 (Godly) to use this ability!");
        }
    }
    
    private boolean isPhantomBook(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
        
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains("Phantom SMP")) {
                return true;
            }
        }
        return false;
    }
    
    private MagicBook getBookFromItem(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;
        
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains("Ability:")) {
                String key = org.bukkit.ChatColor.stripColor(line).replace("Ability:", "").trim();
                return MagicBook.getByAbilityKey(key);
            }
        }
        return null;
    }
}
