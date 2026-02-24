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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class LevelGUI implements Listener {
    
    private final PhantomSMP plugin;
    private final String MAIN_TITLE = "§8§l📚 Select Book";
    private final String LEVEL_TITLE = "§8§l⚡ Select Level";
    private final String PLAYER_TITLE = "§8§l👤 Select Player";
    
    // Store selected book for each admin
    private final Map<UUID, String> selectedBook = new HashMap<>();
    private final Map<UUID, Integer> selectedLevel = new HashMap<>();
    
    public LevelGUI(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void openBookSelection(Player admin) {
        Inventory gui = Bukkit.createInventory(null, 54, MAIN_TITLE);
        
        // Add all books
        MagicBook[] books = MagicBook.values();
        for (int i = 0; i < books.length; i++) {
            if (i < 54) {
                MagicBook book = books[i];
                ItemStack item = createBookDisplay(book);
                gui.setItem(i, item);
            }
        }
        
        // Add decorative border
        ItemStack border = createBorderItem();
        for (int i = 45; i < 54; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, border);
            }
        }
        
        admin.openInventory(gui);
        admin.playSound(admin.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.0f);
    }
    
    private void openLevelSelection(Player admin, String bookKey) {
        Inventory gui = Bukkit.createInventory(null, 27, LEVEL_TITLE);
        
        // Level 1 - Initiate
        ItemStack level1 = new ItemStack(Material.GRAY_DYE);
        ItemMeta meta1 = level1.getItemMeta();
        meta1.setDisplayName("§7§lLevel 1 - Initiate");
        meta1.setLore(Arrays.asList(
            "§7✧ 0 kills required",
            "§7✧ Basic abilities",
            "§7✧ 100% cooldown",
            "",
            "§eClick to select"
        ));
        level1.setItemMeta(meta1);
        gui.setItem(11, level1);
        
        // Level 2 - Ascended
        ItemStack level2 = new ItemStack(Material.LIGHT_BLUE_DYE);
        ItemMeta meta2 = level2.getItemMeta();
        meta2.setDisplayName("§b§lLevel 2 - Ascended");
        meta2.setLore(Arrays.asList(
            "§b✧ 15 kills required",
            "§b✧ Enhanced abilities",
            "§b✧ 20% faster cooldown",
            "",
            "§eClick to select"
        ));
        level2.setItemMeta(meta2);
        gui.setItem(13, level2);
        
        // Level 3 - Godly
        ItemStack level3 = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta3 = level3.getItemMeta();
        meta3.setDisplayName("§6§lLevel 3 - Godly");
        meta3.setLore(Arrays.asList(
            "§6✧ 30 kills required",
            "§6✧ Ultimate abilities",
            "§6✧ 40% faster cooldown",
            "§6✧ Epic particle effects",
            "",
            "§eClick to select"
        ));
        level3.setItemMeta(meta3);
        gui.setItem(15, level3);
        
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c§lBack");
        backMeta.setLore(Arrays.asList("§7Return to book selection"));
        back.setItemMeta(backMeta);
        gui.setItem(18, back);
        
        // Store selected book
        selectedBook.put(admin.getUniqueId(), bookKey);
        
        admin.openInventory(gui);
        admin.playSound(admin.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
    }
    
    private void openPlayerSelection(Player admin) {
        Inventory gui = Bukkit.createInventory(null, 54, PLAYER_TITLE);
        
        int slot = 0;
        
        // Add all online players as heads
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (slot >= 54) break;
            
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName("§e§l" + player.getName());
            
            // Add player stats
            String bookKey = selectedBook.get(admin.getUniqueId());
            int level = selectedLevel.get(admin.getUniqueId());
            int kills = plugin.getLevelManager().getKills(player, bookKey);
            int currentLevel = plugin.getLevelManager().getBookLevel(player, bookKey);
            
            skullMeta.setLore(Arrays.asList(
                "§7Current Level: " + getLevelColor(currentLevel) + currentLevel,
                "§7Kills: §f" + kills,
                "§7Will receive: " + getLevelColor(level) + "Level " + level,
                "",
                "§eClick to give book"
            ));
            
            head.setItemMeta(skullMeta);
            gui.setItem(slot, head);
            slot++;
        }
        
        // Add "ALL PLAYERS" head
        if (slot < 53) {
            ItemStack allHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta allMeta = (SkullMeta) allHead.getItemMeta();
            allMeta.setDisplayName("§d§lALL PLAYERS");
            allMeta.setLore(Arrays.asList(
                "§7Give this book to",
                "§7every online player",
                "",
                "§eClick to give to all"
            ));
            allHead.setItemMeta(allMeta);
            gui.setItem(49, allHead);
        }
        
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§c§lBack");
        backMeta.setLore(Arrays.asList("§7Return to level selection"));
        back.setItemMeta(backMeta);
        gui.setItem(45, back);
        
        admin.openInventory(gui);
        admin.playSound(admin.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.0f);
    }
    
    private String getLevelColor(int level) {
        switch(level) {
            case 1: return "§7";
            case 2: return "§b";
            case 3: return "§6";
            default: return "§f";
        }
    }
    
    private ItemStack createBookDisplay(MagicBook book) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(book.getDisplayName());
        meta.setLore(Arrays.asList(
            "§7" + book.getDescription(),
            "",
            "§e⏱️ Base Cooldown: §f" + book.getCooldown() + "s",
            "§bLevel 2: 20% faster",
            "§6Level 3: 40% faster",
            "",
            "§eClick to select level"
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
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player admin = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.equals(MAIN_TITLE)) {
            event.setCancelled(true);
            
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            
            // Find which book was clicked
            String displayName = clicked.getItemMeta().getDisplayName();
            for (MagicBook book : MagicBook.values()) {
                if (book.getDisplayName().equals(displayName)) {
                    openLevelSelection(admin, book.getAbilityKey());
                    break;
                }
            }
        }
        
        else if (title.equals(LEVEL_TITLE)) {
            event.setCancelled(true);
            
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            
            String displayName = clicked.getItemMeta().getDisplayName();
            
            if (displayName.contains("Level 1")) {
                selectedLevel.put(admin.getUniqueId(), 1);
                openPlayerSelection(admin);
            } else if (displayName.contains("Level 2")) {
                selectedLevel.put(admin.getUniqueId(), 2);
                openPlayerSelection(admin);
            } else if (displayName.contains("Level 3")) {
                selectedLevel.put(admin.getUniqueId(), 3);
                openPlayerSelection(admin);
            } else if (displayName.contains("Back")) {
                openBookSelection(admin);
            }
        }
        
        else if (title.equals(PLAYER_TITLE)) {
            event.setCancelled(true);
            
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            
            String displayName = clicked.getItemMeta().getDisplayName();
            String bookKey = selectedBook.get(admin.getUniqueId());
            int level = selectedLevel.get(admin.getUniqueId());
            
            if (displayName.equals("§c§lBack")) {
                openLevelSelection(admin, bookKey);
                return;
            }
            
            if (displayName.equals("§d§lALL PLAYERS")) {
                // Give to all players
                MagicBook book = MagicBook.getByAbilityKey(bookKey);
                if (book != null) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        plugin.getLevelManager().setAdminLevel(player, bookKey, level);
                        player.getInventory().addItem(book.createBookWithLevel(level, 
                            plugin.getLevelManager().getKills(player, bookKey)));
                        player.sendMessage("§d§l✨ You received a " + 
                            getLevelColor(level) + "Level " + level + " §d" + book.getDisplayName());
                    }
                    admin.sendMessage("§aGiven " + getLevelColor(level) + "Level " + level + 
                        " §a" + book.getDisplayName() + " §ato all players!");
                }
            } else {
                // Give to specific player
                Player target = Bukkit.getPlayer(displayName.replace("§e§l", ""));
                if (target != null) {
                    MagicBook book = MagicBook.getByAbilityKey(bookKey);
                    if (book != null) {
                        plugin.getLevelManager().setAdminLevel(target, bookKey, level);
                        target.getInventory().addItem(book.createBookWithLevel(level, 
                            plugin.getLevelManager().getKills(target, bookKey)));
                        target.sendMessage("§d§l✨ You received a " + 
                            getLevelColor(level) + "Level " + level + " §d" + book.getDisplayName());
                        admin.sendMessage("§aGiven " + getLevelColor(level) + "Level " + level + 
                            " §a" + book.getDisplayName() + " §ato " + target.getName());
                    }
                }
            }
            
            // Play sound
            admin.playSound(admin.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
              }
