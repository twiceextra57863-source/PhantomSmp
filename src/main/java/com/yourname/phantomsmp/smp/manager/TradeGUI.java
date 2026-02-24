package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class TradeGUI implements Listener {
    
    private final PhantomSMP plugin;
    private final String TITLE = "§8§l🤝 Select Player to Trade";
    private final Map<UUID, Boolean> guiType = new HashMap<>(); // true = self-trade, false = exchange
    
    public TradeGUI(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void openTradeGUI(Player player, boolean isSelfTrade) {
        Inventory gui = Bukkit.createInventory(null, 54, TITLE);
        
        int slot = 0;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players); // Random order for fun
        
        for (Player target : players) {
            if (target.equals(player)) continue; // Skip self
            
            if (slot >= 54) break;
            
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            skullMeta.setOwningPlayer(target);
            skullMeta.setDisplayName("§e§l" + target.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to send trade request");
            
            if (isSelfTrade) {
                lore.add("§dYou will give your book");
            } else {
                lore.add("§dYou will exchange books");
            }
            
            // Add player stats
            ItemStack heldBook = getHeldBook(target);
            if (heldBook != null) {
                lore.add("§7Holding: " + getBookName(heldBook));
            } else {
                lore.add("§cNot holding a book");
            }
            
            skullMeta.setLore(lore);
            head.setItemMeta(skullMeta);
            
            gui.setItem(slot, head);
            slot++;
        }
        
        // Add close button
        ItemStack close = new ItemStack(Material.BARRIER);
        org.bukkit.inventory.meta.ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§c§lCancel");
        closeMeta.setLore(Arrays.asList("§7Close the trade menu"));
        close.setItemMeta(closeMeta);
        gui.setItem(49, close);
        
        guiType.put(player.getUniqueId(), isSelfTrade);
        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.0f);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(TITLE)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.5f, 1.0f);
            return;
        }
        
        if (clicked.getType() == Material.PLAYER_HEAD) {
            String targetName = org.bukkit.ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            Player target = Bukkit.getPlayer(targetName);
            
            if (target == null || !target.isOnline()) {
                player.sendMessage("§c❌ That player is no longer online!");
                player.closeInventory();
                return;
            }
            
            boolean isSelfTrade = guiType.getOrDefault(player.getUniqueId(), false);
            
            player.closeInventory();
            plugin.getTradeManager().sendTradeRequest(player, target, isSelfTrade);
        }
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
}
