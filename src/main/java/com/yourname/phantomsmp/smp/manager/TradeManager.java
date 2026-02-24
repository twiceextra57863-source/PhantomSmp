package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TradeManager {
    
    private final PhantomSMP plugin;
    private final Map<UUID, TradeRequest> activeRequests = new HashMap<>();
    private final Map<UUID, UUID> activeTrades = new HashMap<>();
    
    public TradeManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void sendTradeRequest(Player sender, Player target, boolean isSelfTrade) {
        // Check if sender has a book
        ItemStack book = getHeldBook(sender);
        if (book == null) {
            sender.sendMessage("§c❌ You must be holding a Phantom Book to trade!");
            return;
        }
        
        // Check if target has space
        if (target.getInventory().firstEmpty() == -1) {
            sender.sendMessage("§c❌ " + target.getName() + " has a full inventory!");
            return;
        }
        
        // Create request
        TradeRequest request = new TradeRequest(sender.getUniqueId(), target.getUniqueId(), book.clone(), isSelfTrade);
        activeRequests.put(target.getUniqueId(), request);
        
        // Send messages
        sender.sendMessage("§d§l✨ Trade Request Sent!");
        sender.sendMessage("§fWaiting for §e" + target.getName() + "§f to respond...");
        
        target.sendMessage("");
        target.sendMessage("§d§l════════════════════════════");
        target.sendMessage("§d§l✨ TRADE REQUEST ✨");
        target.sendMessage("§e" + sender.getName() + " §fwants to trade with you!");
        
        if (isSelfTrade) {
            target.sendMessage("§fThey want to give you their book:");
        } else {
            target.sendMessage("§fThey want to exchange books:");
        }
        
        target.sendMessage("§7" + getBookName(book));
        target.sendMessage("");
        target.sendMessage("§a§l[CLICK HERE TO ACCEPT]");
        target.sendMessage("§c§l[CLICK HERE TO DENY]");
        target.sendMessage("§d§l════════════════════════════");
        target.sendMessage("");
        
        // Play sounds
        sender.playSound(sender.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
    
    public void acceptRequest(Player target) {
        TradeRequest request = activeRequests.remove(target.getUniqueId());
        if (request == null) {
            target.sendMessage("§c❌ You have no pending trade requests!");
            return;
        }
        
        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender == null || !sender.isOnline()) {
            target.sendMessage("§c❌ The player who sent the request is no longer online!");
            return;
        }
        
        // Check if sender still has the book
        ItemStack senderBook = getHeldBook(sender);
        if (senderBook == null || !booksAreEqual(senderBook, request.getBook())) {
            target.sendMessage("§c❌ The player no longer has that book!");
            sender.sendMessage("§c❌ You no longer have the book to trade!");
            return;
        }
        
        if (request.isSelfTrade()) {
            // Self trade - sender gives book to target
            executeSelfTrade(sender, target, request.getBook());
        } else {
            // Exchange trade - both give books
            ItemStack targetBook = getHeldBook(target);
            if (targetBook == null) {
                target.sendMessage("§c❌ You must be holding a book to exchange!");
                return;
            }
            executeExchangeTrade(sender, target, request.getBook(), targetBook);
        }
    }
    
    public void denyRequest(Player target) {
        TradeRequest request = activeRequests.remove(target.getUniqueId());
        if (request == null) {
            target.sendMessage("§c❌ You have no pending trade requests!");
            return;
        }
        
        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender != null && sender.isOnline()) {
            sender.sendMessage("§c❌ " + target.getName() + " denied your trade request!");
        }
        
        target.sendMessage("§c❌ Trade request denied!");
        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
    }
    
    private void executeSelfTrade(Player sender, Player target, ItemStack book) {
        // Remove book from sender
        sender.getInventory().setItemInMainHand(null);
        
        // Start epic animation
        plugin.getTradeAnimationManager().playSelfTradeAnimation(sender, target, book, () -> {
            // Give book to target
            target.getInventory().addItem(book);
            
            // Update levels if needed
            String bookKey = getBookKey(book);
            int senderLevel = plugin.getLevelManager().getBookLevel(sender, bookKey);
            int senderKills = plugin.getLevelManager().getKills(sender, bookKey);
            
            plugin.getLevelManager().setAdminLevel(target, bookKey, senderLevel);
            for (int i = 0; i < senderKills; i++) {
                plugin.getLevelManager().addKill(target, bookKey);
            }
            
            // Messages
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§d§l✨ PHANTOM TRANSFER ✨");
            Bukkit.broadcastMessage("§e" + sender.getName() + " §fhas gifted their book to §e" + target.getName());
            Bukkit.broadcastMessage("§7" + getBookName(book));
            Bukkit.broadcastMessage("");
        });
    }
    
    private void executeExchangeTrade(Player sender, Player target, ItemStack senderBook, ItemStack targetBook) {
        // Remove books from both
        sender.getInventory().setItemInMainHand(null);
        target.getInventory().setItemInMainHand(null);
        
        // Start epic animation
        plugin.getTradeAnimationManager().playExchangeAnimation(sender, target, senderBook, targetBook, () -> {
            // Give books to each other
            sender.getInventory().addItem(targetBook);
            target.getInventory().addItem(senderBook);
            
            // Update levels
            String senderKey = getBookKey(senderBook);
            String targetKey = getBookKey(targetBook);
            
            int senderLevel = plugin.getLevelManager().getBookLevel(sender, senderKey);
            int senderKills = plugin.getLevelManager().getKills(sender, senderKey);
            int targetLevel = plugin.getLevelManager().getBookLevel(target, targetKey);
            int targetKills = plugin.getLevelManager().getKills(target, targetKey);
            
            plugin.getLevelManager().setAdminLevel(sender, targetKey, targetLevel);
            plugin.getLevelManager().setAdminLevel(target, senderKey, senderLevel);
            
            for (int i = 0; i < targetKills; i++) {
                plugin.getLevelManager().addKill(sender, targetKey);
            }
            for (int i = 0; i < senderKills; i++) {
                plugin.getLevelManager().addKill(target, senderKey);
            }
            
            // Messages
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§d§l✨ PHANTOM EXCHANGE ✨");
            Bukkit.broadcastMessage("§e" + sender.getName() + " §fand §e" + target.getName());
            Bukkit.broadcastMessage("§fhave exchanged their books!");
            Bukkit.broadcastMessage("");
        });
    }
    
    private ItemStack getHeldBook(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String line : item.getItemMeta().getLore()) {
                if (line.contains("Phantom SMP")) {
                    return item;
                }
            }
        }
        return null;
    }
    
    private String getBookName(ItemStack book) {
        if (book != null && book.hasItemMeta() && book.getItemMeta().hasDisplayName()) {
            return book.getItemMeta().getDisplayName();
        }
        return "Unknown Book";
    }
    
    private String getBookKey(ItemStack book) {
        if (book != null && book.hasItemMeta() && book.getItemMeta().hasLore()) {
            for (String line : book.getItemMeta().getLore()) {
                if (line.contains("Ability:")) {
                    return org.bukkit.ChatColor.stripColor(line).replace("Ability:", "").trim();
                }
            }
        }
        return null;
    }
    
    private boolean booksAreEqual(ItemStack book1, ItemStack book2) {
        if (book1 == null || book2 == null) return false;
        String key1 = getBookKey(book1);
        String key2 = getBookKey(book2);
        return key1 != null && key1.equals(key2);
    }
    
    private class TradeRequest {
        private final UUID sender;
        private final UUID target;
        private final ItemStack book;
        private final boolean isSelfTrade;
        
        public TradeRequest(UUID sender, UUID target, ItemStack book, boolean isSelfTrade) {
            this.sender = sender;
            this.target = target;
            this.book = book;
            this.isSelfTrade = isSelfTrade;
        }
        
        public UUID getSender() { return sender; }
        public UUID getTarget() { return target; }
        public ItemStack getBook() { return book; }
        public boolean isSelfTrade() { return isSelfTrade; }
    }
                                      }
