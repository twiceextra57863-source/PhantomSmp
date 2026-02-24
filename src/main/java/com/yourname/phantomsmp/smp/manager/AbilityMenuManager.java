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
        
        // Show progress using spigot's sendMessage with ChatMessageType
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
            TextComponent.fromLegacyText("§d⚡ Crouches: §f" + count + "§7/3"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 1.0f);
        
        // If 3 crouches reached, open menu
        if (count >= 3) {
            crouchCount.remove(playerId);
            openAbilityMenu(player);
        }
        
        // Reset after 2 seconds
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
    
    private void openAbilityMenu(Player player) {
        ItemStack book = player.getInventory().getItemInMainHand();
        if (!isPhantomBook(book)) return;
        
        MagicBook magicBook = getBookFromItem(book);
        if (magicBook == null) return;
        
        int level = plugin.getLevelManager().getBookLevel(player, magicBook.getAbilityKey());
        
        Inventory menu = Bukkit.createInventory(null, 27, MENU_TITLE);
        
        // Level 2 Ability (if available)
        if (level >= 2) {
            ItemStack ability2 = createAbilityItem(magicBook, 2);
            menu.setItem(11, ability2);
        }
        
        // Level 3 Ability (if available)
        if (level >= 3) {
            ItemStack ability3 = createAbilityItem(magicBook, 3);
            menu.setItem(15, ability3);
        }
        
        // Decorative items
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName("§r");
        border.setItemMeta(borderMeta);
        
        for (int i = 0; i < 27; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, border);
            }
        }
        
        // Info item
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§6§l" + magicBook.getDisplayName());
        infoMeta.setLore(Arrays.asList(
            "§7Level: " + getLevelColor(level) + level,
            "§7Right-click for normal ability",
            "§7Menu for advanced abilities"
        ));
        info.setItemMeta(infoMeta);
        menu.setItem(13, info);
        
        player.openInventory(menu);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.0f);
    }
    
    private ItemStack createAbilityItem(MagicBook book, int abilityLevel) {
        ItemStack item = new ItemStack(abilityLevel == 2 ? Material.BLAZE_POWDER : Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        
        String levelName = abilityLevel == 2 ? "§b§lASCENDED" : "§6§lGODLY";
        meta.setDisplayName(levelName + " §7Ability");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to use the " + (abilityLevel == 2 ? "Advanced" : "Ultimate") + " ability");
        lore.add("");
        lore.add("§e⏱️ Cooldown: §f" + (book.getCooldown() * (abilityLevel == 2 ? 2 : 3)) + "s");
        lore.add("");
        lore.add("§8Ability: " + book.getAbilityKey());
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(MENU_TITLE)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String displayName = clicked.getItemMeta().getDisplayName();
        
        if (displayName.contains("ASCENDED")) {
            // Use Level 2 ability
            player.closeInventory();
            ItemStack book = player.getInventory().getItemInMainHand();
            MagicBook magicBook = getBookFromItem(book);
            if (magicBook != null) {
                plugin.getBookManager().executeAdvancedAbility(player, magicBook, 2);
            }
        } else if (displayName.contains("GODLY")) {
            // Use Level 3 ability
            player.closeInventory();
            ItemStack book = player.getInventory().getItemInMainHand();
            MagicBook magicBook = getBookFromItem(book);
            if (magicBook != null) {
                plugin.getBookManager().executeUltimateAbility(player, magicBook, 3);
            }
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
}
