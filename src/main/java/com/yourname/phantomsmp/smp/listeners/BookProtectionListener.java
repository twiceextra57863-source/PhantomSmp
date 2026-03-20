package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class BookProtectionListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public BookProtectionListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (plugin.getGemManager().isPowerBook(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtils.colorize("&cYou cannot drop your power book!"));
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        
        // Prevent moving books out of player inventory
        if (event.getInventory().getType() != InventoryType.PLAYER && 
            event.getInventory().getType() != InventoryType.CRAFTING) {
            
            if ((current != null && plugin.getGemManager().isPowerBook(current)) ||
                (cursor != null && plugin.getGemManager().isPowerBook(cursor))) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(MessageUtils.colorize("&cPower books cannot be stored in containers!"));
            }
        }
    }
    
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (plugin.getGemManager().isPowerBook(event.getEntity().getItemStack())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        if (plugin.getGemManager().isPowerBook(event.getItem())) {
            event.setCancelled(true);
        }
    }
}
