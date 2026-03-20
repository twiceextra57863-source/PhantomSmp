package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public CombatListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer == null) return;
        
        PowerBook book = plugin.getGemManager().getPlayerBook(killer.getUniqueId());
        if (book == null) return;
        
        int oldLevel = book.getLevel();
        book.addKill();
        
        // Update the book item in inventory
        updateBookInInventory(killer, book);
        
        // Show kill message
        killer.sendMessage(MessageUtils.colorize("&a+1 Kill! &7(&e" + book.getKills() + "/" + 
            (book.getLevel() == 1 ? 10 : book.getLevel() == 2 ? 25 : 0) + "&7)"));
        
        // Level up!
        if (book.getLevel() > oldLevel) {
            playLevelUpAnimation(killer, book.getLevel());
            killer.sendMessage(MessageUtils.colorize("&6&l✦ &e&lLEVEL UP! &6&l✦"));
            killer.sendMessage(MessageUtils.colorize("&7Your book is now &eLevel " + book.getLevel()));
        }
    }
    
    private void updateBookInInventory(Player player, PowerBook book) {
        // Remove old book
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && plugin.getGemManager().isPowerBook(item)) {
                item.setAmount(0);
            }
        }
        // Add updated book
        player.getInventory().addItem(book.createBookItem());
    }
    
    private void playLevelUpAnimation(Player player, int newLevel) {
        // Particles - epic explosion
        for (int i = 0; i < 5; i++) {
            player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation().add(0, 1, 0), 1);
        }
        
        // Spiral particles
        for (int angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            double x = Math.cos(rad) * 2;
            double z = Math.sin(rad) * 2;
            
            for (double y = 0; y <= 3; y += 0.3) {
                player.getWorld().spawnParticle(
                    Particle.TOTEM_OF_UNDYING,
                    player.getLocation().clone().add(x, y, z),
                    1, 0, 0, 0, 0.1
                );
            }
        }
        
        // Color based on level
        Particle colorParticle = newLevel == 2 ? Particle.FLAME : Particle.END_ROD;
        for (int i = 0; i < 50; i++) {
            double x = (Math.random() - 0.5) * 3;
            double y = Math.random() * 2;
            double z = (Math.random() - 0.5) * 3;
            player.getWorld().spawnParticle(colorParticle, player.getLocation().add(x, y + 1, z), 1);
        }
        
        // Sounds
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
        
        // Title
        player.sendTitle("§6§lLEVEL UP!", "§eLevel " + newLevel + " Unlocked!", 10, 40, 10);
    }
}
