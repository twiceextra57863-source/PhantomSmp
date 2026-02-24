package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    
    private final PhantomSMP plugin;
    private boolean smpStarted = false;
    
    public JoinListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void setSmpStarted(boolean started) {
        this.smpStarted = started;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Check if player has played before
        if (!player.hasPlayedBefore()) {
            // New player
            player.sendMessage("§d§l✨ Welcome to Phantom SMP! ✨");
            
            if (smpStarted) {
                // SMP already started - give ceremony immediately
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    MagicBook randomBook = MagicBook.getRandomBook();
                    plugin.getBookManager().giveBookWithCeremony(player, randomBook);
                }, 40L);
            } else {
                // SMP not started yet - they'll get book when SMP starts
                player.sendMessage("§eThe SMP hasn't started yet. You'll receive your book when it begins!");
            }
        } else {
            // Returning player - add to grace if active
            if (plugin.getGraceManager().isGraceActive()) {
                plugin.getGraceManager().addPlayer(player);
                player.sendMessage("§aYou are now protected by the active grace period!");
            }
        }
    }
}
