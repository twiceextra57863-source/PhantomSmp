package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KillListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public KillListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        
        if (killer == null) return;
        
        // Check if killer is holding a Phantom book
        ItemStack mainHand = killer.getInventory().getItemInMainHand();
        ItemStack offHand = killer.getInventory().getItemInOffHand();
        
        String bookKey = null;
        
        if (isPhantomBook(mainHand)) {
            bookKey = getBookKey(mainHand);
        } else if (isPhantomBook(offHand)) {
            bookKey = getBookKey(offHand);
        }
        
        if (bookKey == null) return;
        
        // Check if we should count this kill
        boolean countKill = false;
        
        if (entity instanceof Player) {
            countKill = true; // Always count player kills
        } else {
            // Check config for mob kills
            countKill = plugin.getConfig().getBoolean("kill-tracking.count-mob-kills", false);
        }
        
        if (countKill) {
            plugin.getLevelManager().addKill(killer, bookKey);
            
            if (plugin.getConfig().getBoolean("kill-tracking.kill-messages", true)) {
                int kills = plugin.getLevelManager().getKills(killer, bookKey);
                int level = plugin.getLevelManager().getBookLevel(killer, bookKey);
                killer.sendMessage("§d⚔️ §fKill counted! §7Total kills: §e" + kills + 
                    " §7| Level: " + getLevelColor(level) + level);
            }
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
    
    private String getBookKey(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        for (String line : meta.getLore()) {
            if (line.contains("Ability:")) {
                return org.bukkit.ChatColor.stripColor(line).replace("Ability:", "").trim();
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
