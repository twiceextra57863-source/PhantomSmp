package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelBookCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public LevelBookCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("phantomsmp.admin")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        // Open the level GUI
        plugin.getLevelGUI().openBookSelection(player);
        
        return true;
    }
}
