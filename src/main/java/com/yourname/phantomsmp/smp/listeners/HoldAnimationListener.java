package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HoldAnimationListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public HoldAnimationListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        
        // Stop particles for previous item
        plugin.getUltimateHoldParticleManager().stopHoldParticles(player);
        
        // Start particles for new item if it's a Phantom book
        if (isPhantomBook(newItem)) {
            MagicBook book = getBookFromItem(newItem);
            if (book != null) {
                int level = plugin.getLevelManager().getBookLevel(player, book.getAbilityKey());
                plugin.getUltimateHoldParticleManager().startHoldParticles(player, book, level);
            }
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
        
        ItemMeta meta = item.getItemMeta();
        String abilityKey = null;
        
        for (String line : meta.getLore()) {
            if (line.contains("Ability:")) {
                abilityKey = ChatColor.stripColor(line).replace("Ability:", "").trim();
                break;
            }
        }
        
        if (abilityKey != null) {
            return MagicBook.getByAbilityKey(abilityKey);
        }
        
        return null;
    }
}
