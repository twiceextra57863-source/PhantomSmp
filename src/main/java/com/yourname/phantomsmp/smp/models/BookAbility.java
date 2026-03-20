package com.minetwice.phantomsmp.models;

import org.bukkit.entity.Player;

public class BookAbility {
    
    private final String name;
    private final String description;
    private final int baseCooldown;
    private final int level2Cooldown;
    private final int level3Cooldown;
    private final AbilityExecutor executor;
    
    public BookAbility(String name, String description, int baseCooldown, 
                       int level2Cooldown, int level3Cooldown, AbilityExecutor executor) {
        this.name = name;
        this.description = description;
        this.baseCooldown = baseCooldown;
        this.level2Cooldown = level2Cooldown;
        this.level3Cooldown = level3Cooldown;
        this.executor = executor;
    }
    
    public int getCooldown(int level) {
        switch (level) {
            case 1: return baseCooldown;
            case 2: return level2Cooldown;
            case 3: return level3Cooldown;
            default: return baseCooldown;
        }
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public AbilityExecutor getExecutor() { return executor; }
    
    @FunctionalInterface
    public interface AbilityExecutor {
        void execute(Player player, int level);
    }
}
