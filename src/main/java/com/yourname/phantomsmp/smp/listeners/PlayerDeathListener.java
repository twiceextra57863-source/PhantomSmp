package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public PlayerDeathListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Remove power books from drops - they stay with player
        event.getDrops().removeIf(item -> {
            if (item != null && plugin.getGemManager().isPowerBook(item)) {
                // Give back to player inventory
                player.getInventory().addItem(item);
                return true;
            }
            return false;
        });
    }
}
