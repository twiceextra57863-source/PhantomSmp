package com.phantom.smp.listeners;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HoldAnimationListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public HoldAnimationListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        
        if (isPhantomBook(newItem)) {
            startHoldAnimation(player);
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
    
    private void startHoldAnimation(Player player) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                ItemStack offHand = player.getInventory().getItemInOffHand();
                
                if (!isPhantomBook(mainHand) && !isPhantomBook(offHand)) {
                    cancel();
                    return;
                }
                
                if (ticks++ >= 100) {
                    cancel();
                    return;
                }
                
                double angle = ticks * 0.3;
                double radius = 1.5;
                
                for (int i = 0; i < 3; i++) {
                    double x = radius * Math.cos(angle + (i * Math.PI * 2 / 3));
                    double z = radius * Math.sin(angle + (i * Math.PI * 2 / 3));
                    
                    player.getWorld().spawnParticle(
                        Particle.END_ROD,
                        player.getLocation().add(x, 1 + Math.sin(angle) * 0.5, z),
                        1, 0, 0, 0, 0
                    );
                    
                    player.getWorld().spawnParticle(
                        Particle.DUST,
                        player.getLocation().add(x, 1 + Math.cos(angle) * 0.5, z),
                        1, 0, 0, 0, 0,
                        new Particle.DustOptions(Color.fromRGB(255, 100, 255), 1)
                    );
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
