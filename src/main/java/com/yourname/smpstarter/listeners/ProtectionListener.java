package com.yourname.smpstarter.listeners;

import com.yourname.smpstarter.manager.TimerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ProtectionListener implements Listener {
    private final TimerManager timerManager;

    public ProtectionListener(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!timerManager.isSmpStarted() && !event.getPlayer().hasPermission("smpstarter.admin")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot break blocks until the SMP has started!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!timerManager.isSmpStarted() && !event.getPlayer().hasPermission("smpstarter.admin")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot place blocks until the SMP has started!");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Protect all entities (or just players) from taking damage before the SMP starts
        if (!timerManager.isSmpStarted()) {
            event.setCancelled(true);
        }
    }
}