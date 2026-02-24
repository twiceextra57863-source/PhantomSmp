package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ScreenHeadDisplay {
    
    private final PhantomSMP plugin;
    private final HeadRenderer headRenderer;
    private final Map<UUID, AnimationSession> activeSessions = new HashMap<>();
    
    public ScreenHeadDisplay(PhantomSMP plugin) {
        this.plugin = plugin;
        this.headRenderer = new HeadRenderer(plugin);
    }
    
    public void startTradeAnimation(Player player1, Player player2, 
                                    String book1Name, String book2Name, 
                                    boolean isSelfTrade, Runnable onComplete) {
        
        UUID sessionId = UUID.randomUUID();
        AnimationSession session = new AnimationSession(sessionId, player1, player2, isSelfTrade, onComplete);
        
        // Create map items for heads
        ItemStack player1HeadMap = headRenderer.createPlayerHeadMap(player1, 128);
        ItemStack player2HeadMap = headRenderer.createPlayerHeadMap(player2, 128);
        
        // Create map items for books
        ItemStack book1Map = headRenderer.createBookMap(player1, book1Name, "§c", 128);
        ItemStack book2Map = headRenderer.createBookMap(player2, book2Name, "§b", 128);
        
        // Store maps in session
        session.setMaps(player1HeadMap, player2HeadMap, book1Map, book2Map);
        
        // Give maps to players (they'll see them in inventory)
        player1.getInventory().setItem(4, player1HeadMap); // Slot 5
        player1.getInventory().setItem(5, book1Map);       // Slot 6
        
        player2.getInventory().setItem(4, player2HeadMap); // Slot 5
        player2.getInventory().setItem(5, book2Map);       // Slot 6
        
        // Open map views (force them to see maps)
        player1.openInventory(player1.getInventory());
        player2.openInventory(player2.getInventory());
        
        activeSessions.put(sessionId, session);
        
        // Start animation
        runAnimation(session);
    }
    
    private void runAnimation(AnimationSession session) {
        new BukkitRunnable() {
            int step = 0;
            final int TOTAL_STEPS = 100;
            
            @Override
            public void run() {
                if (step >= TOTAL_STEPS) {
                    // Clean up
                    cleanup(session);
                    session.getOnComplete().run();
                    cancel();
                    return;
                }
                
                double progress = (double) step / TOTAL_STEPS;
                
                if (session.isSelfTrade()) {
                    animateSelfTrade(session, progress);
                } else {
                    animateExchange(session, progress);
                }
                
                // Update titles
                updateTitles(session, progress);
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private void animateSelfTrade(AnimationSession session, double progress) {
        Player player1 = session.getPlayer1();
        Player player2 = session.getPlayer2();
        
        // Calculate positions for book movement
        int startX = 30; // Starting X position
        int endX = 70;   // Ending X position
        int currentX = (int) (startX + (endX - startX) * progress);
        
        // Send position updates via packets (simplified)
        // In real implementation, would use MapCanvas updates
        String positionData = "pos:" + currentX + ",50";
        player1.sendPluginMessage(plugin, "phantomsmp:animation", positionData.getBytes());
        player2.sendPluginMessage(plugin, "phantomsmp:animation", positionData.getBytes());
    }
    
    private void animateExchange(AnimationSession session, double progress) {
        Player player1 = session.getPlayer1();
        Player player2 = session.getPlayer2();
        
        // Book1 moves right
        int book1X = (int) (30 + 40 * progress);
        // Book2 moves left
        int book2X = (int) (70 - 40 * progress);
        
        // When books cross, create effect
        if (Math.abs(book1X - book2X) < 10) {
            createCrossingEffect(session, progress);
        }
        
        String data1 = "pos:" + book1X + ",50";
        String data2 = "pos:" + book2X + ",50";
        
        player1.sendPluginMessage(plugin, "phantomsmp:animation", data1.getBytes());
        player2.sendPluginMessage(plugin, "phantomsmp:animation", data2.getBytes());
    }
    
    private void createCrossingEffect(AnimationSession session, double progress) {
        Player player1 = session.getPlayer1();
        Player player2 = session.getPlayer2();
        
        // Send crossing effect
        String effect = "effect:cross:" + (int)(progress * 100);
        player1.sendPluginMessage(plugin, "phantomsmp:animation", effect.getBytes());
        player2.sendPluginMessage(plugin, "phantomsmp:animation", effect.getBytes());
        
        // Titles
        if ((int)(progress * 100) % 10 == 0) {
            player1.sendTitle("§6✨ BOOKS CROSSING ✨", "§eEnergy Exchange!", 0, 10, 0);
            player2.sendTitle("§6✨ BOOKS CROSSING ✨", "§eEnergy Exchange!", 0, 10, 0);
        }
    }
    
    private void updateTitles(AnimationSession session, double progress) {
        Player player1 = session.getPlayer1();
        Player player2 = session.getPlayer2();
        
        int percent = (int)(progress * 100);
        
        if (percent == 20) {
            player1.sendTitle("§d⚡", "§fInitiating Trade...", 0, 20, 0);
            player2.sendTitle("§d⚡", "§fInitiating Trade...", 0, 20, 0);
        } else if (percent == 40) {
            player1.sendTitle("§5✨", "§fBooks Moving...", 0, 20, 0);
            player2.sendTitle("§5✨", "§fBooks Moving...", 0, 20, 0);
        } else if (percent == 60) {
            player1.sendTitle("§6🌟", "§fEnergy Flow...", 0, 20, 0);
            player2.sendTitle("§6🌟", "§fEnergy Flow...", 0, 20, 0);
        } else if (percent == 80) {
            player1.sendTitle("§e⚡", "§fAlmost There...", 0, 20, 0);
            player2.sendTitle("§e⚡", "§fAlmost There...", 0, 20, 0);
        }
    }
    
    private void cleanup(AnimationSession session) {
        // Remove maps from inventory
        session.getPlayer1().getInventory().setItem(4, null);
        session.getPlayer1().getInventory().setItem(5, null);
        session.getPlayer2().getInventory().setItem(4, null);
        session.getPlayer2().getInventory().setItem(5, null);
        
        // Close any open inventories
        session.getPlayer1().closeInventory();
        session.getPlayer2().closeInventory();
        
        activeSessions.remove(session.getSessionId());
    }
    
    private class AnimationSession {
        private final UUID sessionId;
        private final Player player1;
        private final Player player2;
        private final boolean isSelfTrade;
        private final Runnable onComplete;
        private ItemStack player1HeadMap;
        private ItemStack player2HeadMap;
        private ItemStack book1Map;
        private ItemStack book2Map;
        
        public AnimationSession(UUID sessionId, Player player1, Player player2, 
                               boolean isSelfTrade, Runnable onComplete) {
            this.sessionId = sessionId;
            this.player1 = player1;
            this.player2 = player2;
            this.isSelfTrade = isSelfTrade;
            this.onComplete = onComplete;
        }
        
        public void setMaps(ItemStack p1Head, ItemStack p2Head, ItemStack b1, ItemStack b2) {
            this.player1HeadMap = p1Head;
            this.player2HeadMap = p2Head;
            this.book1Map = b1;
            this.book2Map = b2;
        }
        
        public UUID getSessionId() { return sessionId; }
        public Player getPlayer1() { return player1; }
        public Player getPlayer2() { return player2; }
        public boolean isSelfTrade() { return isSelfTrade; }
        public Runnable getOnComplete() { return onComplete; }
    }
      }
