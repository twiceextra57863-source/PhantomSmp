package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AbilityMenuManager implements Listener {
    
    private final PhantomSMP plugin;
    private final Map<UUID, Integer> crouchCount = new HashMap<>();
    private final Map<UUID, Long> lastCrouchTime = new HashMap<>();
    private final String MENU_TITLE = "§8§l⚡ Select Ability";
    
    public AbilityMenuManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return; // Only count when starting to sneak
        
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check if player is holding a Phantom book
        if (!isHoldingPhantomBook(player)) return;
        
        // Reset if last crouch was more than 2 seconds ago
        if (lastCrouchTime.containsKey(playerId) && 
            currentTime - lastCrouchTime.get(playerId) > 2000) {
            crouchCount.put(playerId, 0);
        }
        
        // Increment crouch count
        int count = crouchCount.getOrDefault(playerId, 0) + 1;
        crouchCount.put(playerId, count);
        lastCrouchTime.put(playerId, currentTime);
        
        // Show progress in action bar
        String progressBar = getProgressBar(count);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
            TextComponent.fromLegacyText("§d⚡ Crouches: " + progressBar + " §f" + count + "§7/3"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 1.0f);
        
        // If 3 crouches reached, open menu
        if (count >= 3) {
            crouchCount.remove(playerId);
            openAbilityMenu(player);
        }
        
        // Schedule reset after 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lastCrouchTime.containsKey(playerId) && 
                    System.currentTimeMillis() - lastCrouchTime.get(playerId) > 2000) {
                    crouchCount.remove(playerId);
                    lastCrouchTime.remove(playerId);
                }
            }
        }.runTaskLater(plugin, 40L);
    }
    
    private String getProgressBar(int count) {
        StringBuilder bar = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            if (i <= count) {
                bar.append("§a■");
            } else {
                bar.append("§7■");
            }
        }
        return bar.toString();
    }
    
    private void openAbilityMenu(Player player) {
        ItemStack book = player.getInventory().getItemInMainHand();
        if (!isPhantomBook(book)) return;
        
        MagicBook magicBook = getBookFromItem(book);
        if (magicBook == null) return;
        
        int level = plugin.getLevelManager().getBookLevel(player, magicBook.getAbilityKey());
        
        // Only open if player has level 2 or higher
        if (level < 2) {
            player.sendMessage("§c❌ You need Level 2 (Ascended) to use advanced abilities!");
            return;
        }
        
        Inventory menu = Bukkit.createInventory(null, 27, MENU_TITLE);
        
        // Level 2 Ability (always available if level >= 2)
        ItemStack ability2 = createAbilityItem(magicBook, 2, level);
        menu.setItem(11, ability2);
        
        // Level 3 Ability (only if level >= 3)
        if (level >= 3) {
            ItemStack ability3 = createAbilityItem(magicBook, 3, level);
            menu.setItem(15, ability3);
        }
        
        // Decorative items (glass panes)
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName("§r");
        border.setItemMeta(borderMeta);
        
        // Fill empty slots with border
        for (int i = 0; i < 27; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, border);
            }
        }
        
        // Info item (center)
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§6§l" + magicBook.getDisplayName());
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Your current book");
        lore.add("§7Level: " + getLevelColor(level) + level + " " + getLevelName(level));
        lore.add("");
        lore.add("§eRight-click for Normal ability");
        lore.add("§bMenu for Advanced/Ultimate abilities");
        lore.add("");
        lore.add("§8Ability Key: " + magicBook.getAbilityKey());
        
        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);
        menu.setItem(13, info);
        
        player.openInventory(menu);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.0f);
    }
    
    private ItemStack createAbilityItem(MagicBook book, int abilityLevel, int playerLevel) {
        Material material = abilityLevel == 2 ? Material.BLAZE_POWDER : Material.NETHER_STAR;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        String levelName = abilityLevel == 2 ? "§b§lASCENDED" : "§6§lGODLY";
        String abilityType = abilityLevel == 2 ? "Advanced" : "Ultimate";
        
        meta.setDisplayName(levelName + " §7Ability");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to use the " + abilityType + " ability");
        lore.add("");
        
        // Show if available or locked
        if (playerLevel >= abilityLevel) {
            lore.add("§a✔ Available");
            lore.add("§e⏱️ Cooldown: §f" + (book.getCooldown() * (abilityLevel == 2 ? 2 : 3)) + "s");
        } else {
            lore.add("§c✘ Locked");
            lore.add("§7Requires Level " + abilityLevel);
        }
        
        lore.add("");
        lore.add("§8Ability: " + book.getAbilityKey());
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Only handle our menu
        if (!event.getView().getTitle().equals(MENU_TITLE)) return;
        
        // Cancel ALL clicks in this inventory - prevents taking items
        event.setCancelled(true);
        
        // Make sure it's a player
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        // If clicked item is null or air, do nothing
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (!clicked.hasItemMeta()) return;
        
        String displayName = clicked.getItemMeta().getDisplayName();
        
        // Check if clicked on ability item
        if (displayName.contains("ASCENDED")) {
            // Use Level 2 ability
            player.closeInventory();
            ItemStack book = player.getInventory().getItemInMainHand();
            MagicBook magicBook = getBookFromItem(book);
            
            if (magicBook != null) {
                int level = plugin.getLevelManager().getBookLevel(player, magicBook.getAbilityKey());
                if (level >= 2) {
                    plugin.getBookManager().executeAdvancedAbility(player, magicBook, level);
                } else {
                    player.sendMessage("§c❌ You need Level 2 to use this ability!");
                }
            }
        } else if (displayName.contains("GODLY")) {
            // Use Level 3 ability
            player.closeInventory();
            ItemStack book = player.getInventory().getItemInMainHand();
            MagicBook magicBook = getBookFromItem(book);
            
            if (magicBook != null) {
                int level = plugin.getLevelManager().getBookLevel(player, magicBook.getAbilityKey());
                if (level >= 3) {
                    plugin.getBookManager().executeUltimateAbility(player, magicBook, level);
                } else {
                    player.sendMessage("§c❌ You need Level 3 to use this ability!");
                }
            }
        }
        // If clicked on info book or glass pane, do nothing (already cancelled)
    }
    
    /**
     * Also prevent dragging items in the menu
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equals(MENU_TITLE)) {
            event.setCancelled(true);
        }
    }
    
    private boolean isHoldingPhantomBook(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        return isPhantomBook(item);
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
    
    private MagicBook getBookFromItem(ItemStack item) {
        if (!isPhantomBook(item)) return null;
        
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains("Ability:")) {
                String key = org.bukkit.ChatColor.stripColor(line).replace("Ability:", "").trim();
                return MagicBook.getByAbilityKey(key);
            }
        }
        return null;
    }
    
    private String getLevelColor(int level) {
        switch(level) {
            case 1: return "§7";
            case 2: return "§b";
            case 3: return "§6";
            default: return "§f";
        }
    }
    
    private String getLevelName(int level) {
        switch(level) {
            case 1: return "Initiate";
            case 2: return "Ascended";
            case 3: return "Godly";
            default: return "Unknown";
        }
    }
}
