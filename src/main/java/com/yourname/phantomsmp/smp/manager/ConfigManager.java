package com.phantom.smp.manager;

import com.phantom.smp.PhantomSMP;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    
    private final PhantomSMP plugin;
    private FileConfiguration config;
    
    // Kill tracking settings
    private boolean countMobKills;
    private boolean countPlayerKills;
    private boolean killMessages;
    
    // Level requirements
    private int level1Kills;
    private int level2Kills;
    private int level3Kills;
    
    // Cooldown multipliers
    private double level1Cooldown;
    private double level2Cooldown;
    private double level3Cooldown;
    
    // Transformation settings
    private String particleIntensity;
    private boolean screenShake;
    private boolean transformationSound;
    private boolean titleMessages;
    
    public ConfigManager(PhantomSMP plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        
        // Load kill tracking settings
        this.countMobKills = config.getBoolean("kill-tracking.count-mob-kills", false);
        this.countPlayerKills = config.getBoolean("kill-tracking.count-player-kills", true);
        this.killMessages = config.getBoolean("kill-tracking.kill-messages", true);
        
        // Load level requirements
        this.level1Kills = config.getInt("level-requirements.level1.kills", 0);
        this.level2Kills = config.getInt("level-requirements.level2.kills", 15);
        this.level3Kills = config.getInt("level-requirements.level3.kills", 30);
        
        // Load cooldown multipliers
        this.level1Cooldown = config.getDouble("level-requirements.level1.cooldown-multiplier", 1.0);
        this.level2Cooldown = config.getDouble("level-requirements.level2.cooldown-multiplier", 0.8);
        this.level3Cooldown = config.getDouble("level-requirements.level3.cooldown-multiplier", 0.6);
        
        // Load transformation settings
        this.particleIntensity = config.getString("transformations.particle-intensity", "high");
        this.screenShake = config.getBoolean("transformations.screen-shake", true);
        this.transformationSound = config.getBoolean("transformations.transformation-sound", true);
        this.titleMessages = config.getBoolean("transformations.title-messages", true);
    }
    
    public void reloadConfig() {
        loadConfig();
    }
    
    // ========== GETTERS ==========
    
    public boolean isCountMobKills() {
        return countMobKills;
    }
    
    public boolean isCountPlayerKills() {
        return countPlayerKills;
    }
    
    public boolean isKillMessages() {
        return killMessages;
    }
    
    public int getLevel1Kills() {
        return level1Kills;
    }
    
    public int getLevel2Kills() {
        return level2Kills;
    }
    
    public int getLevel3Kills() {
        return level3Kills;
    }
    
    public int getKillsForLevel(int level) {
        switch(level) {
            case 1: return level1Kills;
            case 2: return level2Kills;
            case 3: return level3Kills;
            default: return 0;
        }
    }
    
    public double getCooldownMultiplier(int level) {
        switch(level) {
            case 1: return level1Cooldown;
            case 2: return level2Cooldown;
            case 3: return level3Cooldown;
            default: return 1.0;
        }
    }
    
    public String getParticleIntensity() {
        return particleIntensity;
    }
    
    public boolean isScreenShake() {
        return screenShake;
    }
    
    public boolean isTransformationSound() {
        return transformationSound;
    }
    
    public boolean isTitleMessages() {
        return titleMessages;
    }
    
    // ========== SETTERS (for admin commands) ==========
    
    public void setCountMobKills(boolean value) {
        this.countMobKills = value;
        config.set("kill-tracking.count-mob-kills", value);
        plugin.saveConfig();
    }
    
    public void setKillMessages(boolean value) {
        this.killMessages = value;
        config.set("kill-tracking.kill-messages", value);
        plugin.saveConfig();
    }
    
    public void setLevelRequirements(int level2, int level3) {
        this.level2Kills = level2;
        this.level3Kills = level3;
        config.set("level-requirements.level2.kills", level2);
        config.set("level-requirements.level3.kills", level3);
        plugin.saveConfig();
    }
    
    public void setCooldownMultipliers(double level2, double level3) {
        this.level2Cooldown = level2;
        this.level3Cooldown = level3;
        config.set("level-requirements.level2.cooldown-multiplier", level2);
        config.set("level-requirements.level3.cooldown-multiplier", level3);
        plugin.saveConfig();
    }
}
