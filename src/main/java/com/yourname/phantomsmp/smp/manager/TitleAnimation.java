package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleAnimation {
    
    private final PhantomSMP plugin;
    
    public TitleAnimation(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void playTradeIntro(Player player1, Player player2) {
        new BukkitRunnable() {
            int step = 0;
            String[] messages = {
                "§d⚡ §fTRADE §d⚡",
                "§5✨ §fINITIATED §5✨",
                "§6🌟 §fCONNECTION §6🌟"
            };
            
            @Override
            public void run() {
                if (step >= messages.length) {
                    cancel();
                    return;
                }
                
                player1.sendTitle(messages[step], "§7with §e" + player2.getName(), 0, 20, 10);
                player2.sendTitle(messages[step], "§7with §e" + player1.getName(), 0, 20, 10);
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 15L);
    }
    
    public void playBookTransferAnimation(Player sender, Player receiver, String bookName) {
        new BukkitRunnable() {
            int step = 0;
            String[] senderTitles = {
                "§c📤 GIVING BOOK",
                "§c⚡ ENERGY FLOW",
                "§c✨ TRANSFERRING",
                "§a✅ COMPLETE!"
            };
            String[] receiverTitles = {
                "§b📥 RECEIVING BOOK",
                "§b⚡ ENERGY FLOW",
                "§b✨ ACCEPTING",
                "§a✅ COMPLETE!"
            };
            
            @Override
            public void run() {
                if (step >= senderTitles.length) {
                    cancel();
                    return;
                }
                
                sender.sendTitle(senderTitles[step], "§7" + bookName, 0, 15, 5);
                receiver.sendTitle(receiverTitles[step], "§7" + bookName, 0, 15, 5);
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    public void playExchangeAnimation(Player player1, Player player2, String book1, String book2) {
        new BukkitRunnable() {
            int step = 0;
            
            @Override
            public void run() {
                if (step >= 8) {
                    cancel();
                    return;
                }
                
                String title1, title2;
                String subtitle1, subtitle2;
                
                switch(step) {
                    case 0:
                        title1 = "§c⬆️ " + book1;
                        title2 = "§b⬆️ " + book2;
                        subtitle1 = "§7Moving...";
                        subtitle2 = "§7Moving...";
                        break;
                    case 1:
                        title1 = "§c↗️ " + book1;
                        title2 = "§b↖️ " + book2;
                        subtitle1 = "§7Crossing...";
                        subtitle2 = "§7Crossing...";
                        break;
                    case 2:
                        title1 = "§6✨ BOOKS CROSSING ✨";
                        title2 = "§6✨ BOOKS CROSSING ✨";
                        subtitle1 = "§eEnergy Exchange!";
                        subtitle2 = "§eEnergy Exchange!";
                        break;
                    case 3:
                        title1 = "§b⬇️ " + book2;
                        title2 = "§c⬇️ " + book1;
                        subtitle1 = "§7Receiving...";
                        subtitle2 = "§7Receiving...";
                        break;
                    case 4:
                        title1 = "§a✅ EXCHANGE COMPLETE";
                        title2 = "§a✅ EXCHANGE COMPLETE";
                        subtitle1 = "§7You got: " + book2;
                        subtitle2 = "§7You got: " + book1;
                        break;
                    default:
                        title1 = "";
                        title2 = "";
                        subtitle1 = "";
                        subtitle2 = "";
                }
                
                player1.sendTitle(title1, subtitle1, 0, 15, 5);
                player2.sendTitle(title2, subtitle2, 0, 15, 5);
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 15L);
    }
    
    public void playLevelUpAnimation(Player player, int level, String bookName) {
        String levelColor = level == 1 ? "§7" : (level == 2 ? "§b" : "§6");
        String levelName = level == 1 ? "INITIATE" : (level == 2 ? "ASCENDED" : "GODLY");
        
        new BukkitRunnable() {
            int step = 0;
            
            @Override
            public void run() {
                if (step >= 5) {
                    cancel();
                    return;
                }
                
                switch(step) {
                    case 0:
                        player.sendTitle("§d⚡", "§fPower building...", 0, 10, 0);
                        break;
                    case 1:
                        player.sendTitle("§5✨", "§fEnergy flowing...", 0, 10, 0);
                        break;
                    case 2:
                        player.sendTitle(levelColor + "⚡ LEVEL " + level + " ⚡", "§f" + levelName, 0, 20, 0);
                        break;
                    case 3:
                        player.sendTitle("§6🌟", "§f" + bookName, 0, 20, 0);
                        break;
                }
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 15L);
    }
    
    public void playCeremonyAnimation(Player player, String bookName) {
        new BukkitRunnable() {
            int step = 0;
            
            @Override
            public void run() {
                if (step >= 6) {
                    cancel();
                    return;
                }
                
                switch(step) {
                    case 0:
                        player.sendTitle("§d✨", "§fThe ceremony begins...", 0, 20, 0);
                        break;
                    case 1:
                        player.sendTitle("§5🔮", "§fMagic awakens...", 0, 20, 0);
                        break;
                    case 2:
                        player.sendTitle("§6⚡", "§fPower flows...", 0, 20, 0);
                        break;
                    case 3:
                        player.sendTitle("§e🌟", "§fYour book awaits...", 0, 20, 0);
                        break;
                    case 4:
                        player.sendTitle("§a📖", "§f" + bookName, 0, 40, 10);
                        break;
                }
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
