package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProtectionListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public ProtectionListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            
            if (plugin.getTimerManager().isProtected(damaged) || 
                plugin.getTimerManager().isProtected(damager)) {
                event.setCancelled(true);
                damager.sendMessage("§cYou cannot attack during SMP protection period!");
            }
        }
    }
}
