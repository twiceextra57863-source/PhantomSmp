package com.phantom.smp.commands;

import com.phantom.smp.PhantomSMP;
import com.phantom.smp.models.MagicBook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookInfoCommand implements CommandExecutor {
    
    private final PhantomSMP plugin;
    
    public BookInfoCommand(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        
        // Check if holding a book
        if (mainHand == null || mainHand.getType() != Material.ENCHANTED_BOOK) {
            player.sendMessage("§cYou must be holding a Phantom Book!");
            return true;
        }
        
        // Get book key
        String bookKey = null;
        if (mainHand.hasItemMeta() && mainHand.getItemMeta().hasLore()) {
            for (String line : mainHand.getItemMeta().getLore()) {
                if (line.contains("Ability:")) {
                    bookKey = ChatColor.stripColor(line).replace("Ability:", "").trim();
                    break;
                }
            }
        }
        
        if (bookKey == null) {
            player.sendMessage("§cThis is not a valid Phantom Book!");
            return true;
        }
        
        MagicBook book = MagicBook.getByAbilityKey(bookKey);
        if (book == null) return true;
        
        // Get player stats
        int kills = plugin.getLevelManager().getKills(player, bookKey);
        int level = plugin.getLevelManager().getBookLevel(player, bookKey);
        int nextLevelKills = plugin.getLevelManager().getRequiredKillsForNextLevel(level);
        int killsNeeded = plugin.getLevelManager().getKillsNeeded(player, bookKey);
        
        // Calculate cooldown
        double cooldownMultiplier = plugin.getLevelManager().getCooldownMultiplier(level);
        int baseCooldown = book.getCooldown();
        int effectiveCooldown = (int)(baseCooldown * cooldownMultiplier);
        
        // Send detailed info
        player.sendMessage("");
        player.sendMessage("§d§l╔══════════════════════════════╗");
        player.sendMessage("§d§l║     " + book.getDisplayName());
        player.sendMessage("§d§l╠══════════════════════════════╣");
        player.sendMessage("§d§l║ §fDescription: §7" + book.getDescription());
        player.sendMessage("§d§l║");
        player.sendMessage("§d§l║ §fCurrent Level: " + getLevelColor(level) + level + " " + getLevelName(level));
        player.sendMessage("§d§l║ §fTotal Kills: §e" + kills);
        player.sendMessage("§d§l║");
        
        if (level < 3) {
            player.sendMessage("§d§l║ §fNext Level: §b" + (level + 1) + " " + getLevelName(level + 1));
            player.sendMessage("§d§l║ §fKills Needed: §e" + killsNeeded + " more");
            player.sendMessage("§d§l║ §fProgress: §a" + getProgressBar(kills, nextLevelKills));
        } else {
            player.sendMessage("§d§l║ §6§lMAX LEVEL REACHED!");
        }
        
        player.sendMessage("§d§l║");
        player.sendMessage("§d§l║ §fBase Cooldown: §7" + baseCooldown + "s");
        player.sendMessage("§d§l║ §fEffective Cooldown: §e" + effectiveCooldown + "s");
        player.sendMessage("§d§l║ §fMultiplier: §a" + (int)((1 - cooldownMultiplier) * 100) + "% faster");
        player.sendMessage("§d§l║");
        player.sendMessage("§d§l║ §fLevel 1: §7Initiate §7(0 kills)");
        player.sendMessage("§d§l║ §fLevel 2: §bAscended §b(15 kills)");
        player.sendMessage("§d§l║ §fLevel 3: §6Godly §6(30 kills)");
        player.sendMessage("§d§l╚══════════════════════════════╝");
        player.sendMessage("");
        
        return true;
    }
    
    private String getLevelColor(int level) {
        switch(level) {
            case 1: return "§7";
            case 2: return "§b";
            case 3: return "§6";
            default: return "§f";
        }
    }
    
    private String getLevelName(int level) {
        switch(level) {
            case 1: return "Initiate";
            case 2: return "Ascended";
            case 3: return "Godly";
            default: return "Unknown";
        }
    }
    
    private String getProgressBar(int current, int max) {
        int bars = 20;
        int progress = (int)((double)current / max * bars);
        if (progress > bars) progress = bars;
        
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < bars; i++) {
            if (i < progress) {
                bar.append("§a■");
            } else {
                bar.append("§7■");
            }
        }
        return bar.toString();
    }
}
