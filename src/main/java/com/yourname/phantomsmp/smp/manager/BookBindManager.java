package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class BookBindManager implements Listener {
    
    private final PhantomSMP plugin;
    
    public BookBindManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isPhantomBook(item)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c❌ You cannot drop a Phantom Book! It is bound to you.");
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Remove books from drops
        event.getDrops().removeIf(item -> isPhantomBook(item));
        
        // Keep books in inventory - give them back
        for (ItemStack item : player.getInventory().getContents()) {
            if (isPhantomBook(item)) {
                // Book will stay in inventory due to keepInventory
                player.sendMessage("§d✨ Your Phantom Book remains with you in death!");
            }
        }
        
        // Force keep inventory for phantom books
        if (!event.getKeepInventory()) {
            // Save books before clear
            ItemStack[] contents = player.getInventory().getContents();
            player.getInventory().clear();
            
            // Restore only phantom books
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null && isPhantomBook(contents[i])) {
                    player.getInventory().setItem(i, contents[i]);
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        
        // Prevent moving to chests/containers
        if (event.getInventory().getType() != InventoryType.PLAYER && 
            event.getInventory().getType() != InventoryType.CRAFTING &&
            event.getInventory().getType() != InventoryType.CREATIVE) {
            
            if (isPhantomBook(current)) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage("§c❌ You cannot store Phantom Books in containers!");
            }
        }
        
        // Prevent moving with cursor into containers
        if (isPhantomBook(cursor) && 
            event.getInventory().getType() != InventoryType.PLAYER && 
            event.getInventory().getType() != InventoryType.CRAFTING &&
            event.getInventory().getType() != InventoryType.CREATIVE) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("§c❌ You cannot store Phantom Books in containers!");
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
}
