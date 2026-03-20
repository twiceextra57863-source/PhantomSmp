package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class GraceManager {
    
    private final PhantomSMP plugin;
    private boolean gracePeriod = false;
    private int timeRemaining = 0;
    private BukkitRunnable task;
    
    public GraceManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void startGracePeriod() {
        this.gracePeriod = true;
        this.timeRemaining = 600; // 10 minutes
        
        // Disable PvP
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(false);
        }
        
        // Start countdown
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeRemaining <= 0) {
                    endGracePeriod();
                    cancel();
                    return;
                }
                
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                String time = String.format("%02d:%02d", minutes, seconds);
                
                Bukkit.getOnlinePlayers().forEach(p -> 
                    p.sendActionBar(MessageUtils.colorize("&a&lGRACE PERIOD: &e&l" + time)));
                
                timeRemaining--;
            }
        };
        task.runTaskTimer(plugin, 0L, 20L);
        
        Bukkit.broadcastMessage(MessageUtils.colorize("&a&lGrace period started! PvP disabled for 10 minutes."));
    }
    
    private void endGracePeriod() {
        this.gracePeriod = false;
        
        // Enable PvP
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(true);
        }
        
        Bukkit.broadcastMessage(MessageUtils.colorize("&c&lGRACE PERIOD ENDED! PvP is now enabled!"));
        Bukkit.getOnlinePlayers().forEach(p -> 
            p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f));
    }
    
    public void cancelGracePeriod() {
        if (task != null) task.cancel();
        gracePeriod = false;
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(true);
        }
    }
    
    public boolean isGracePeriod() { return gracePeriod; }
    public int getTimeRemaining() { return timeRemaining; }
}
