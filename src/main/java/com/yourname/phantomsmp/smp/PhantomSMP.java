package com.minetwice.phantomsmp;

import com.minetwice.phantomsmp.commands.*;
import com.minetwice.phantomsmp.listeners.*;
import com.minetwice.phantomsmp.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
    
    // Managers
    private GemManager gemManager;
    private CooldownManager cooldownManager;
    private GraceManager graceManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        
        getLogger().info("§a╔════════════════════════════════════╗");
        getLogger().info("§a║     PhantomSMP v" + getDescription().getVersion() + " Enabled      ║");
        getLogger().info("§a║        Author: MineTwice           ║");
        getLogger().info("§a║      Paper 1.21.4 Compatible       ║");
        getLogger().info("§a╚════════════════════════════════════╝");
        
        // Initialize managers
        this.gemManager = new GemManager(this);
        this.cooldownManager = new CooldownManager(this);
        this.graceManager = new GraceManager(this);
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
    }
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("§cPhantomSMP Disabled!");
    }
    
    private void registerCommands() {
        if (getCommand("smpstart") != null)
            getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        if (getCommand("smprewind") != null)
            getCommand("smprewind").setExecutor(new SMPRewindCommand(this));
        if (getCommand("bookgive") != null)
            getCommand("bookgive").setExecutor(new BookGiveCommand(this));
        if (getCommand("bookinfo") != null)
            getCommand("bookinfo").setExecutor(new BookInfoCommand(this));
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BookProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
    }
    
    public static PhantomSMP getInstance() { return instance; }
    public GemManager getGemManager() { return gemManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public GraceManager getGraceManager() { return graceManager; }
}
