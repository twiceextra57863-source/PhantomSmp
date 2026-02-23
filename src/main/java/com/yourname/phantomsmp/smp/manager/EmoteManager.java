package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EmoteManager {
    
    private final PhantomSMP plugin;
    
    public EmoteManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void playCeremonyEmote(Player player, Runnable onComplete) {
        new BukkitRunnable() {
            int tick = 0;
            String[] messages = {
                "§d✨ §fChanneling power...",
                "§5🔮 §fThe magic awakens...",
                "§6⚡ §fEnergy flows through you...",
                "§e🌟 §fAlmost there...",
                "§a✅ §fPower acquired!"
            };
            
            @Override
            public void run() {
                if (tick >= 100) {
                    player.chat("§d✨ I have received the power of the Phantom! ✨");
                    
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    
                    for (int i = 0; i < 10; i++) {
                        player.getWorld().spawnParticle(
                            Particle.FIREWORK,
                            player.getLocation().add(0, 2, 0),
                            20, 1, 1, 1, 0.1
                        );
                    }
                    
                    onComplete.run();
                    cancel();
                    return;
                }
                
                if (tick % 20 == 0) {
                    int index = Math.min(tick / 20, messages.length - 1);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                        TextComponent.fromLegacyText(messages[index]));
                }
                
                if (tick % 10 == 0) {
                    String[] emotes = {
                        "§d*channels arcane energy*",
                        "§5*eyes glow with power*",
                        "§6*surrounded by magic*",
                        "§e*reaches for the stars*"
                    };
                    player.chat(emotes[tick/10 % emotes.length]);
                }
                
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
