package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GraceCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public GraceCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!sender.hasPermission("phantomsmp.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /grace <seconds>");
            return true;
        }
        
        try {
            int seconds = Integer.parseInt(args[0]);
            
            if (seconds < 5 || seconds > 3600) {
                sender.sendMessage("§cSeconds must be between 5 and 3600!");
                return true;
            }
            
            plugin.getGraceManager().startGracePeriod(seconds);
            
            if (sender instanceof Player) {
                sender.sendMessage("§aGrace period started for §e" + seconds + "§a seconds!");
            }
            
        } catch (NumberFormatException e) {
            sender.sendMessage("§cPlease enter a valid number!");
        }
        
        return true;
    }
}
