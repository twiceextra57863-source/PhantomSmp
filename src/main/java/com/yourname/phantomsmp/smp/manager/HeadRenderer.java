package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.bukkit.map.MapFont.CharacterSprite;
import org.bukkit.map.MapPalette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeadRenderer {
    
    private final PhantomSMP plugin;
    private final Map<UUID, MapRenderer> headRenderers = new HashMap<>();
    private final Map<UUID, Integer> mapIds = new HashMap<>();
    
    public HeadRenderer(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public ItemStack createPlayerHeadMap(Player player, int size) {
        // Create map item
        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) map.getItemMeta();
        
        // Create new map view
        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.setScale(MapView.Scale.CLOSEST);
        mapView.setCenterX(0);
        mapView.setCenterZ(0);
        mapView.setWorld(player.getWorld());
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);
        
        // Create custom renderer for player head
        PlayerHeadRenderer renderer = new PlayerHeadRenderer(player);
        mapView.addRenderer(renderer);
        
        meta.setMapView(mapView);
        map.setItemMeta(meta);
        
        // Store renderer and map ID
        headRenderers.put(player.getUniqueId(), renderer);
        mapIds.put(player.getUniqueId(), mapView.getId());
        
        return map;
    }
    
    public ItemStack createBookMap(Player player, String bookName, String bookColor, int size) {
        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) map.getItemMeta();
        
        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.setScale(MapView.Scale.CLOSEST);
        mapView.setTrackingPosition(false);
        
        BookRenderer renderer = new BookRenderer(bookName, bookColor);
        mapView.addRenderer(renderer);
        
        meta.setMapView(mapView);
        map.setItemMeta(meta);
        
        return map;
    }
    
    public int getMapId(Player player) {
        return mapIds.getOrDefault(player.getUniqueId(), -1);
    }
    
    private class PlayerHeadRenderer extends MapRenderer {
        
        private final Player targetPlayer;
        private boolean rendered = false;
        
        public PlayerHeadRenderer(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }
        
        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            if (rendered) return;
            
            // Get player's skin
            BufferedImage skinImage = getPlayerSkin(targetPlayer);
            
            // Draw head (8x8 area at top of skin)
            for (int x = 8; x < 16; x++) {
                for (int y = 8; y < 16; y++) {
                    int color = skinImage.getRGB(x, y);
                    if ((color >> 24) != 0) { // If not transparent
                        canvas.setPixel(x - 8, y - 8, MapPalette.matchColor(new Color(color)));
                    }
                }
            }
            
            // Draw hat/overlay layer
            for (int x = 40; x < 48; x++) {
                for (int y = 8; y < 16; y++) {
                    int color = skinImage.getRGB(x, y);
                    if ((color >> 24) != 0) { // If not transparent
                        canvas.setPixel(x - 40, y - 8, MapPalette.matchColor(new Color(color)));
                    }
                }
            }
            
            // Add glow effect for animation
            drawGlowEffect(canvas);
            
            rendered = true;
        }
        
        private BufferedImage getPlayerSkin(Player player) {
            // This would need a skin API like MineSkin or similar
            // For now, return a default skin
            BufferedImage defaultSkin = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = defaultSkin.createGraphics();
            
            // Draw default Steve skin
            g.setColor(new Color(150, 100, 50)); // Brown hair
            g.fillRect(8, 8, 8, 8);
            
            g.setColor(new Color(255, 200, 150)); // Skin tone
            g.fillRect(8, 16, 8, 8);
            
            g.setColor(Color.BLACK); // Eyes
            g.fillRect(10, 18, 1, 1);
            g.fillRect(13, 18, 1, 1);
            
            g.setColor(new Color(200, 100, 50)); // Mouth
            g.fillRect(11, 21, 2, 1);
            
            g.dispose();
            return defaultSkin;
        }
        
        private void drawGlowEffect(MapCanvas canvas) {
            // Add animated glow around head
            long time = System.currentTimeMillis() / 100;
            int glowIntensity = (int) (Math.sin(time) * 50 + 50);
            
            for (int x = -2; x < 10; x++) {
                for (int y = -2; y < 10; y++) {
                    if (x < 0 || x >= 8 || y < 0 || y >= 8) {
                        if (Math.random() < 0.1) {
                            canvas.setPixel(x + 4, y + 4, MapPalette.matchColor(
                                new Color(100, 100, 255, glowIntensity)
                            ));
                        }
                    }
                }
            }
        }
    }
}
