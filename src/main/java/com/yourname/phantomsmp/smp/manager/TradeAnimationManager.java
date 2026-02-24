package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeAnimationManager {
    
    private final PhantomSMP plugin;
    
    public TradeAnimationManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void playSelfTradeAnimation(Player sender, Player target, ItemStack book, Runnable onComplete) {
        String bookName = getBookName(book);
        
        // Play sound effects
        sender.playSound(sender.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
        target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
        
        // Start screen animation with actual player heads
        plugin.getScreenHeadDisplay().startTradeAnimation(
            sender, target, bookName, "", true, () -> {
                
                // Transfer complete sound
                sender.playSound(sender.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                target.playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                
                // Final message
                sender.sendTitle("§a✅ TRADE COMPLETE", "§eBook sent to " + target.getName(), 10, 40, 20);
                target.sendTitle("§a✅ TRADE COMPLETE", "§eBook received from " + sender.getName(), 10, 40, 20);
                
                onComplete.run();
            }
        );
    }
    
    public void playExchangeAnimation(Player player1, Player player2, 
                                      ItemStack book1, ItemStack book2, Runnable onComplete) {
        String book1Name = getBookName(book1);
        String book2Name = getBookName(book2);
        
        // Play sound effects
        player1.playSound(player1.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.0f);
        player2.playSound(player2.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.0f);
        
        // Start screen animation with actual player heads
        plugin.getScreenHeadDisplay().startTradeAnimation(
            player1, player2, book1Name, book2Name, false, () -> {
                
                // Exchange complete sound
                player1.playSound(player1.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                player2.playSound(player2.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                
                // Final message
                player1.sendTitle("§a✅ EXCHANGE COMPLETE", "§eYou received " + book2Name, 10, 40, 20);
                player2.sendTitle("§a✅ EXCHANGE COMPLETE", "§eYou received " + book1Name, 10, 40, 20);
                
                onComplete.run();
            }
        );
    }
    
    private String getBookName(ItemStack book) {
        if (book != null && book.hasItemMeta() && book.getItemMeta().hasDisplayName()) {
            return book.getItemMeta().getDisplayName();
        }
        return "Unknown Book";
    }
}
