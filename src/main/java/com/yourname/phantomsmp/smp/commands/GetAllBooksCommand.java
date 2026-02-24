package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetAllBooksCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public GetAllBooksCommand(PhantomSMP plugin) {
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
        
        player.sendMessage("§d§l⚡ COLLECTING ALL PHANTOM BOOKS ⚡");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
        
        int count = 0;
        for (MagicBook book : MagicBook.values()) {
            player.getInventory().addItem(book.createBook());
            count++;
        }
        
        player.sendMessage("§a✅ You received all §e" + count + " §aPhantom Books!");
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        
        // Celebration effect
        for (int i = 0; i < 30; i++) {
            player.getWorld().strikeLightningEffect(player.getLocation());
        }
        
        return true;
    }
}
