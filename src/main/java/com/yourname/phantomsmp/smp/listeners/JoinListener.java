package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public JoinListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Check if player has played before
        if (!player.hasPlayedBefore()) {
            // New player - give book with ceremony
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage("§d§l✨ Welcome to Phantom SMP! ✨");
                player.sendMessage("§fThe ancient spirits have chosen you...");
                
                MagicBook randomBook = MagicBook.getRandomBook();
                plugin.getBookManager().giveBookWithCeremony(player, randomBook);
            }, 40L); // Delay 2 seconds
        } else {
            // Returning player - add to grace if active
            if (plugin.getGraceManager().isGraceActive()) {
                plugin.getGraceManager().addPlayer(player);
                player.sendMessage("§aYou are now protected by the active grace period!");
            }
        }
    }
}
