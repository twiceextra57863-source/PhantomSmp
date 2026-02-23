package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveAllCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public GiveAllCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!sender.hasPermission("phantomsmp.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            MagicBook randomBook = MagicBook.getRandomBook();
            player.getInventory().addItem(randomBook.createBook());
            player.sendMessage("§d§l✨ You received a random Phantom Book! ✨");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            count++;
        }
        
        Bukkit.broadcastMessage("§d§l✨ Phantom Blessing! ✨");
        Bukkit.broadcastMessage("§eAll §f" + count + " §eonline players received a random book!");
        
        return true;
    }
}
