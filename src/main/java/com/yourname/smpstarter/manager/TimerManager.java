package com.yourname.smpstarter.manager;

import com.yourname.smpstarter.SMPStarter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerManager {
    private final SMPStarter plugin;
    private boolean smpStarted = false;

    public TimerManager(SMPStarter plugin) {
        this.plugin = plugin;
    }

    public boolean isSmpStarted() {
        return smpStarted;
    }

    public void startSMPCountdown() {
        if (smpStarted) return;

        new BukkitRunnable() {
            int countdown = 10;

            @Override
            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage("§e§lSMP starting in §c§l" + countdown + " §e§lseconds!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                    countdown--;
                } else {
                    smpStarted = true;
                    Bukkit.broadcastMessage("§a§lThe SMP has officially started! Good luck!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}