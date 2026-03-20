package com.minetwice.phantomsmp.utils;

import net.md_5.bungee.api.ChatColor;

public class MessageUtils {
    
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String stripColor(String message) {
        return ChatColor.stripColor(message);
    }
}
