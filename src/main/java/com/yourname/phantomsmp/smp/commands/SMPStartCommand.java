package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
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
            
            plugin.getTimerManager().startTimer(seconds);
            
            if (sender instanceof Player) {
                sender.sendMessage("§aSMP timer started for §e" + seconds + "§a seconds!");
            }
            
        } catch (NumberFormatException e) {
            sender.sendMessage("§cPlease enter a valid number!");
        }
        
        return true;
    }
}
