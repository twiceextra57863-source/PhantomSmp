package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUIManager implements Listener {
    
    private final PhantomSMP plugin;
    private final String GUI_TITLE = "§8§l📚 Phantom Books §7(30)";
    
    public GUIManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void openBookGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);
        
        // Add all books to GUI
        MagicBook[] books = MagicBook.values();
        for (int i = 0; i < books.length; i++) {
            if (i < 54) { // Max 54 slots
                MagicBook book = books[i];
                ItemStack displayItem = createDisplayItem(book);
                gui.setItem(i, displayItem);
            }
        }
        
        // Add decorative items
        ItemStack border = createBorderItem();
        for (int i = 45; i < 54; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, border);
            }
        }
        
        // Add close button
        ItemStack closeButton = createCloseButton();
        gui.setItem(49, closeButton);
        
        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.0f);
    }
    
    private ItemStack createDisplayItem(MagicBook book) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(book.getDisplayName());
        meta.setLore(Arrays.asList(
            "§7" + book.getDescription(),
            "",
            "§e⏱️ Cooldown: §f" + book.getCooldown() + "s",
            "§d✨ Click to view info",
            "",
            "§8Ability: " + book.getAbilityKey()
        ));
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createBorderItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r");
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createCloseButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c§lClose");
        meta.setLore(Arrays.asList("§7Click to close the menu"));
        item.setItemMeta(meta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        // Close button
        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.5f, 1.0f);
            return;
        }
        
        // Book clicked
        if (clicked.getType() == Material.BOOK && clicked.getItemMeta().hasDisplayName()) {
            String displayName = clicked.getItemMeta().getDisplayName();
            
            // Find the book
            for (MagicBook book : MagicBook.values()) {
                if (book.getDisplayName().equals(displayName)) {
                    // Show book details
                    player.sendMessage("");
                    player.sendMessage("§d§l" + book.getDisplayName());
                    player.sendMessage("§7" + book.getDescription());
                    player.sendMessage("§e⏱️ Cooldown: §f" + book.getCooldown() + " seconds");
                    player.sendMessage("§8Ability Key: " + book.getAbilityKey());
                    player.sendMessage("");
                    
                    // Give a sample? (optional - remove if you don't want this)
                    // player.getInventory().addItem(book.createBook());
                    
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.5f);
                    break;
                }
            }
        }
    }
}
