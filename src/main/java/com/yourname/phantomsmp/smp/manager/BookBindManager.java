package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class BookBindManager implements Listener {
    
    private final PhantomSMP plugin;
    private final Map<UUID, List<ItemStack>> savedBooks = new HashMap<>();
    private final Map<UUID, List<Integer>> savedBookSlots = new HashMap<>();
    
    public BookBindManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> booksToKeep = new ArrayList<>();
        List<Integer> bookSlots = new ArrayList<>();
        
        // CRITICAL FIX: First, remove all phantom books from drops
        Iterator<ItemStack> dropsIterator = event.getDrops().iterator();
        while (dropsIterator.hasNext()) {
            ItemStack item = dropsIterator.next();
            if (isPhantomBook(item)) {
                dropsIterator.remove();
            }
        }
        
        // CRITICAL FIX: Save books with their slot positions
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && isPhantomBook(item)) {
                booksToKeep.add(item.clone());
                bookSlots.add(i);
                // Clear the slot to prevent duplication
                inventory.setItem(i, null);
            }
        }
        
        // Also check offhand
        ItemStack offhand = inventory.getItemInOffHand();
        if (offhand != null && isPhantomBook(offhand)) {
            booksToKeep.add(offhand.clone());
            bookSlots.add(40); // Offhand slot number
            inventory.setItemInOffHand(null);
        }
        
        // Save books for respawn
        if (!booksToKeep.isEmpty()) {
            savedBooks.put(player.getUniqueId(), booksToKeep);
            savedBookSlots.put(player.getUniqueId(), bookSlots);
            player.sendMessage("§d✨ Your Phantom Books will be waiting for you on respawn!");
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (savedBooks.containsKey(playerId)) {
            // Give books back after respawn with a small delay
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                List<ItemStack> books = savedBooks.remove(playerId);
                List<Integer> slots = savedBookSlots.remove(playerId);
                
                PlayerInventory inventory = player.getInventory();
                
                // CRITICAL FIX: Try to restore books to original slots
                for (int i = 0; i < books.size(); i++) {
                    ItemStack book = books.get(i);
                    int slot = slots.get(i);
                    
                    // Check if slot is empty
                    if (slot == 40) { // Offhand
                        if (inventory.getItemInOffHand() == null || inventory.getItemInOffHand().getType() == Material.AIR) {
                            inventory.setItemInOffHand(book);
                        } else {
                            // If slot is occupied, add to first empty slot
                            inventory.addItem(book);
                        }
                    } else {
                        if (inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR) {
                            inventory.setItem(slot, book);
                        } else {
                            inventory.addItem(book);
                        }
                    }
                }
                
                player.sendMessage("§d✨ Your Phantom Books have been returned to you!");
            }, 10L); // 0.5 second delay
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isPhantomBook(item)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c❌ You cannot drop a Phantom Book! It is bound to you.");
            
            // Force refresh inventory to ensure item stays
            event.getPlayer().updateInventory();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        ItemStack hotbar = event.getHotbarButton() != -1 ? 
            event.getWhoClicked().getInventory().getItem(event.getHotbarButton()) : null;
        
        // CRITICAL FIX: Check all possible item sources
        if (isPhantomBook(current) || isPhantomBook(cursor) || isPhantomBook(hotbar)) {
            
            // Prevent moving to any non-player inventory
            if (event.getClickedInventory() != null && 
                event.getClickedInventory().getType() != InventoryType.PLAYER && 
                event.getClickedInventory().getType() != InventoryType.CRAFTING &&
                event.getClickedInventory().getType() != InventoryType.CREATIVE) {
                
                event.setCancelled(true);
                event.getWhoClicked().sendMessage("§c❌ You cannot store Phantom Books in containers!");
                ((Player)event.getWhoClicked()).updateInventory();
                return;
            }
            
            // Prevent moving between player inventory slots (allow reordering)
            if (event.getClickedInventory() != null && 
                event.getClickedInventory().getType() == InventoryType.PLAYER) {
                // Allow reordering in player inventory
                return;
            }
            
            // Prevent any other inventory actions
            event.setCancelled(true);
            ((Player)event.getWhoClicked()).updateInventory();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        // Check if any dragged item is a phantom book
        for (ItemStack item : event.getNewItems().values()) {
            if (isPhantomBook(item)) {
                // Check if dragging to non-player inventory
                if (event.getInventory().getType() != InventoryType.PLAYER && 
                    event.getInventory().getType() != InventoryType.CRAFTING) {
                    event.setCancelled(true);
                    ((Player)event.getWhoClicked()).updateInventory();
                    return;
                }
            }
        }
    }
    
    /**
     * CRITICAL FIX: Check if item is a Phantom Book
     */
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
