package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SMPStartCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public SMPStartCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!sender.hasPermission("phantomsmp.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /smpstart <seconds>");
            return true;
        }
        
        try {
            int seconds = Integer.parseInt(args[0]);
            
            if (seconds < 5 || seconds > 3600) {
                sender.sendMessage("§cSeconds must be between 5 and 3600!");
                return true;
            }
            
            // Mark SMP as started
            plugin.getJoinListener().setSmpStarted(true);
            
            // Start SMP timer
            plugin.getTimerManager().startTimer(seconds);
            
            // Automatically start grace period for same duration
            plugin.getGraceManager().startGracePeriod(seconds);
            
            Bukkit.broadcastMessage("§6§l═══ SMP STARTING ═══");
            Bukkit.broadcastMessage("§eSMP will start in §f" + seconds + " §eseconds");
            Bukkit.broadcastMessage("§aGrace period is active for the same duration!");
            Bukkit.broadcastMessage("§6§l═══════════════════════");
            
        } catch (NumberFormatException e) {
            sender.sendMessage("§cPlease enter a valid number!");
        }
        
        return true;
    }
}
