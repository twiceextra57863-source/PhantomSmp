package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BookListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public BookListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBookUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        if (event.getItem() == null || event.getItem().getType() != Material.ENCHANTED_BOOK) {
            return;
        }
        
        if (!event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasLore()) {
            return;
        }
        
        event.setCancelled(true);
        plugin.getBookManager().useBookAbility(event.getPlayer(), event.getItem());
    }
}
