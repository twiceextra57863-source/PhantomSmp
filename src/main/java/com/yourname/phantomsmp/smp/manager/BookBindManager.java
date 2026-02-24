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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BookBindManager implements Listener {
    
    private final PhantomSMP plugin;
    private final Map<UUID, List<ItemStack>> savedBooks = new HashMap<>();
    
    public BookBindManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> booksToKeep = new ArrayList<>();
        
        // Remove books from drops and save them
        Iterator<ItemStack> dropsIterator = event.getDrops().iterator();
        while (dropsIterator.hasNext()) {
            ItemStack item = dropsIterator.next();
            if (isPhantomBook(item)) {
                dropsIterator.remove();
                booksToKeep.add(item.clone());
            }
        }
        
        // Also check inventory contents (for items not in drops due to keepInventory)
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isPhantomBook(item)) {
                booksToKeep.add(item.clone());
            }
        }
        
        // Save books for respawn
        if (!booksToKeep.isEmpty()) {
            savedBooks.put(player.getUniqueId(), booksToKeep);
            player.sendMessage("§d✨ Your Phantom Books will be waiting for you on respawn!");
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (savedBooks.containsKey(playerId)) {
            // Give books back after respawn
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                List<ItemStack> books = savedBooks.remove(playerId);
                for (ItemStack book : books) {
                    player.getInventory().addItem(book);
                }
                player.sendMessage("§d✨ Your Phantom Books have been returned to you!");
            }, 20L); // 1 second delay
        }
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
