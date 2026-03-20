package com.minetwice.phantomsmp.listeners;

import com.minetwice.phantomsmp.PhantomSMP;
import com.minetwice.phantomsmp.models.BookAbility;
import com.minetwice.phantomsmp.models.PowerBook;
import com.minetwice.phantomsmp.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityListener implements Listener {
    
    private final PhantomSMP plugin;
    
    public AbilityListener(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return;
        if (!plugin.getGemManager().isPowerBook(item)) return;
        
        event.setCancelled(true);
        
        PowerBook book = plugin.getGemManager().getPlayerBook(player.getUniqueId());
        if (book == null) return;
        
        if (plugin.getGraceManager().isGracePeriod()) {
            player.sendMessage(MessageUtils.colorize("&cAbilities disabled during grace period!"));
            return;
        }
        
        // Check which ability to use
        boolean isSneaking = player.isSneaking();
        BookAbility ability = isSneaking ? book.getAbility2() : book.getAbility1();
        
        if (ability == null) return;
        
        // Check cooldown
        String cooldownKey = player.getUniqueId() + "_" + book.getId() + "_" + (isSneaking ? 2 : 1);
        
        if (plugin.getCooldownManager().isOnCooldown(cooldownKey)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(cooldownKey);
            player.sendActionBar(MessageUtils.colorize("&cCooldown: &e" + remaining + "s"));
            return;
        }
        
        // Execute ability
        try {
            ability.getExecutor().execute(player, book.getLevel());
            
            int cooldown = ability.getCooldown(book.getLevel());
            plugin.getCooldownManager().setCooldown(cooldownKey, cooldown);
            
            startCooldownDisplay(player, cooldownKey, cooldown);
            
            String abilityName = isSneaking ? "§7[§6Shift§7] " : "";
            abilityName += ability.getName();
            player.sendActionBar(MessageUtils.colorize("&aUsed: &f" + abilityName));
            
        } catch (Exception e) {
            player.sendMessage(MessageUtils.colorize("&cAbility failed!"));
            e.printStackTrace();
        }
    }
    
    private void startCooldownDisplay(Player player, String key, int total) {
        new BukkitRunnable() {
            int remaining = total;
            @Override
            public void run() {
                if (remaining <= 0 || !plugin.getCooldownManager().isOnCooldown(key)) {
                    player.sendActionBar(MessageUtils.colorize("&a✓ Ability ready!"));
                    cancel();
                    return;
                }
                player.sendActionBar(MessageUtils.colorize("&cCooldown: &e" + remaining + "s"));
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
