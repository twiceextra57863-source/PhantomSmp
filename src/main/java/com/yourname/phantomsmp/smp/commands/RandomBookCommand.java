package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomBookCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public RandomBookCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /randombook <player>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }
        
        plugin.getBookManager().giveRandomBook(target);
        sender.sendMessage("§aGiving random magical book to §e" + target.getName());
        
        return true;
    }
}
