package com.yourname.smpstarter.commands;

import com.yourname.smpstarter.SMPStarter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveBookCommand implements CommandExecutor {
    
    private final SMPStarter plugin;
    
    public GiveBookCommand(SMPStarter plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /givebook <player> [bookname]");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }
        
        if (args.length == 1) {
            // Give random book
            plugin.getBookManager().giveRandomBook(target);
            sender.sendMessage("§aGiving random book to §e" + target.getName());
        } else {
            // Give specific book
            String bookName = args[1];
            plugin.getBookManager().giveBookToPlayer(target, bookName);
            sender.sendMessage("§aGiving book §e" + bookName + " §ato §e" + target.getName());
        }
        
        return true;
    }
}
