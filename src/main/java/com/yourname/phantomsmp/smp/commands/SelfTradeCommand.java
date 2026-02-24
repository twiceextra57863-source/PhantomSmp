package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelfTradeCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public SelfTradeCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check if player has a book to trade
        if (!hasPhantomBook(player)) {
            player.sendMessage("§c❌ You must be holding a Phantom Book to trade!");
            return true;
        }
        
        // Open trade GUI for self-trade (give your book)
        plugin.getTradeGUI().openTradeGUI(player, true);
        
        return true;
    }
    
    private boolean hasPhantomBook(Player player) {
        if (player.getInventory().getItemInMainHand() != null &&
            player.getInventory().getItemInMainHand().hasItemMeta() &&
            player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            
            for (String line : player.getInventory().getItemInMainHand().getItemMeta().getLore()) {
                if (line.contains("Phantom SMP")) {
                    return true;
                }
            }
        }
        return false;
    }
}
