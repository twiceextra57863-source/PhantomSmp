package com.yourname.smpstarter.listeners;

import com.yourname.smpstarter.manager.BookManager;
import com.yourname.smpstarter.manager.ParticleManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BookListener implements Listener {
    private final BookManager bookManager;
    private final ParticleManager particleManager;

    public BookListener(BookManager bookManager, ParticleManager particleManager) {
        this.bookManager = bookManager;
        this.particleManager = particleManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.ENCHANTED_BOOK || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String displayName = meta.getDisplayName();
        boolean used = false;

        if (displayName.contains("Fire")) {
            player.launchProjectile(Fireball.class);
            particleManager.spawnFireParticles(player.getLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
            player.sendMessage("§cYou cast a Fireball!");
            used = true;
        } else if (displayName.contains("Healing")) {
            double newHealth = Math.min(player.getHealth() + 10.0, player.getMaxHealth());
            player.setHealth(newHealth);
            particleManager.spawnHealParticles(player.getLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage("§aYou healed yourself!");
            used = true;
        } else if (displayName.contains("Speed")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 30, 1));
            particleManager.spawnSpeedParticles(player.getLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);
            player.sendMessage("§bYou gained a magical speed boost!");
            used = true;
        }

        if (used) {
            event.setCancelled(true);
            consumeItem(player, item);
        }
    }

    private void consumeItem(Player player, ItemStack item) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}