package com.yourname.smpstarter.manager;

import com.yourname.smpstarter.SMPStarter;
import com.yourname.smpstarter.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BookManager {
    
    private final SMPStarter plugin;
    
    public BookManager(SMPStarter plugin) {
        this.plugin = plugin;
    }
    
    public void giveBookToPlayer(Player player, String bookName) {
        // Find book by name
        for (MagicBook book : MagicBook.values()) {
            if (book.getDisplayName().contains(bookName) || 
                book.name().equalsIgnoreCase(bookName)) {
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
        plugin.getTimerManager().protectedPlayers.add(player.getUniqueId());
        
        player.sendMessage("§d§l✨ Magical ceremony starting for §e" + player.getName() + " §d§l✨");
        
        // Start particle effect
        new ParticleManager(plugin).startCircleEffect(player, () -> {
            // Give book
            player.getInventory().addItem(book.createBook());
            
            // Remove protection
            plugin.getTimerManager().protectedPlayers.remove(player.getUniqueId());
            
            // Celebration message
            Bukkit.broadcastMessage("§d§l" + player.getName() + " §ereceived: §6" + book.getDisplayName());
            
            // Play sound
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        });
    }
    
    public void useBookAbility(Player player, ItemStack book) {
        if (book == null || !book.hasItemMeta()) return;
        
        String displayName = book.getItemMeta().getDisplayName();
        
        // Find which book it is
        for (MagicBook magicBook : MagicBook.values()) {
            if (magicBook.getDisplayName().equals(displayName)) {
                executeAbility(player, magicBook);
                break;
            }
        }
    }
    
    private void executeAbility(Player player, MagicBook book) {
        switch (book) {
            case THOR:
                // Lightning strike ability
                player.getWorld().strikeLightningEffect(player.getTargetBlock(null, 50).getLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                break;
                
            case PHOENIX:
                // Fire and regeneration
                player.setFireTicks(100);
                player.setHealth(player.getMaxHealth());
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                break;
                
            // Add more abilities for each book
            default:
                player.sendMessage("§cAbility not implemented yet!");
                break;
        }
    }
}
