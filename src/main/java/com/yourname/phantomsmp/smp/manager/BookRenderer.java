package com.phantom.smp.manager;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapPalette;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BookRenderer extends MapRenderer {
    
    private final String bookName;
    private final String bookColor;
    private boolean rendered = false;
    private int animationFrame = 0;
    
    public BookRenderer(String bookName, String bookColor) {
        this.bookName = bookName;
        this.bookColor = bookColor;
    }
    
    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        // Draw 3D book model
        draw3DBook(canvas, animationFrame);
        
        // Draw book title
        drawText(canvas, bookName, 20, 60);
        
        animationFrame++;
        if (animationFrame > 20) animationFrame = 0;
    }
    
    private void draw3DBook(MapCanvas canvas, int frame) {
        Color bookColor = parseColor(this.bookColor);
        
        // Book cover (front)
        for (int x = 15; x < 45; x++) {
            for (int y = 20; y < 50; y++) {
                canvas.setPixel(x, y, MapPalette.matchColor(bookColor));
            }
        }
        
        // Book spine (side)
        for (int x = 45; x < 50; x++) {
            for (int y = 20; y < 50; y++) {
                canvas.setPixel(x, y, MapPalette.matchColor(bookColor.darker()));
            }
        }
        
        // Book pages (white)
        for (int x = 16; x < 44; x++) {
            for (int y = 21; y < 49; y++) {
                if ((x + y) % 8 == frame % 8) { // Page turning effect
                    canvas.setPixel(x, y, MapPalette.matchColor(Color.WHITE));
                }
            }
        }
        
        // Book title
        drawText(canvas, "PHANTOM", 20, 30);
        
        // Glowing effect
        if (frame % 10 < 5) {
            for (int i = 0; i < 5; i++) {
                canvas.setPixel(30 + i, 55, MapPalette.matchColor(Color.YELLOW));
            }
        }
    }
    
    private void drawText(MapCanvas canvas, String text, int x, int y) {
        // Simple text rendering
        char[] chars = text.toCharArray();
        int currentX = x;
        
        for (char c : chars) {
            // This is simplified - would need actual font rendering
            if (c == 'P') {
                canvas.setPixel(currentX, y, MapPalette.DARK_GRAY);
                canvas.setPixel(currentX, y+1, MapPalette.DARK_GRAY);
                canvas.setPixel(currentX+1, y, MapPalette.DARK_GRAY);
                canvas.setPixel(currentX+2, y, MapPalette.DARK_GRAY);
            }
            currentX += 6;
        }
    }
    
    private Color parseColor(String colorCode) {
        switch(colorCode) {
            case "§c": return Color.RED;
            case "§b": return Color.CYAN;
            case "§6": return Color.ORANGE;
            case "§a": return Color.GREEN;
            case "§d": return Color.MAGENTA;
            default: return new Color(150, 75, 0); // Brown
        }
    }
}
