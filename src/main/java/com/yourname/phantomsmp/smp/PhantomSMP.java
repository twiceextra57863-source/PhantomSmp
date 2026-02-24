package com.phantom.smp;

import com.phantom.smp.commands.*;
import com.phantom.smp.listeners.*;
import com.phantom.smp.manager.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
    private TimerManager timerManager;
    private BookManager bookManager;
    private CooldownManager cooldownManager;
    private EmoteManager emoteManager;
    private ParticleManager particleManager;
    private GUIManager guiManager;
    private HoldParticleManager holdParticleManager;
    private CeremonyManager ceremonyManager;
    private GraceManager graceManager;
    private BookBindManager bookBindManager;
    private LevelManager levelManager;
    private LevelGUI levelGUI;
    private KillListener killListener;
    private ConfigManager configManager;
    private TransformationManager transformationManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        configManager = new ConfigManager(this);
        timerManager = new TimerManager(this);
        bookManager = new BookManager(this);
        cooldownManager = new CooldownManager(this);
        emoteManager = new EmoteManager(this);
        particleManager = new ParticleManager(this);
        guiManager = new GUIManager(this);
        holdParticleManager = new HoldParticleManager(this);
        ceremonyManager = new CeremonyManager(this);
        graceManager = new GraceManager(this);
        bookBindManager = new BookBindManager(this);
        levelManager = new LevelManager(this);
        levelGUI = new LevelGUI(this);
        killListener = new KillListener(this);
        transformationManager = new TransformationManager(this);
        
        // Register commands
        getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        getCommand("grace").setExecutor(new GraceCommand(this));
        getCommand("givebook").setExecutor(new GiveBookCommand(this));
        getCommand("giveall").setExecutor(new GiveAllCommand(this));
        getCommand("booklist").setExecutor(new BookListCommand(this));
        getCommand("bookinfo").setExecutor(new BookInfoCommand(this));
        getCommand("levelbook").setExecutor(new LevelBookCommand(this));
        getCommand("randombook").setExecutor(new RandomBookCommand(this));
        getCommand("reloadphantom").setExecutor(new ReloadCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new HoldAnimationListener(this), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(guiManager, this);
        getServer().getPluginManager().registerEvents(bookBindManager, this);
        getServer().getPluginManager().registerEvents(killListener, this);
        getServer().getPluginManager().registerEvents(levelGUI, this);
        
        getLogger().info("§a§lPhantomSMP v5.0.0 has been enabled!");
        getLogger().info("§e§l30 Epic Books with 3 Levels Each!");
        getLogger().info("§b§lNew: Level System, Kill Tracking, Transformation GUI!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("§c§lPhantomSMP has been disabled!");
    }
    
    public static PhantomSMP getInstance() {
        return instance;
    }
    
    // ========== GETTER METHODS ==========
    public TimerManager getTimerManager() { return timerManager; }
    public BookManager getBookManager() { return bookManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public EmoteManager getEmoteManager() { return emoteManager; }
    public ParticleManager getParticleManager() { return particleManager; }
    public GUIManager getGuiManager() { return guiManager; }
    public HoldParticleManager getHoldParticleManager() { return holdParticleManager; }
    public CeremonyManager getCeremonyManager() { return ceremonyManager; }
    public GraceManager getGraceManager() { return graceManager; }
    public BookBindManager getBookBindManager() { return bookBindManager; }
    public LevelManager getLevelManager() { return levelManager; }
    public LevelGUI getLevelGUI() { return levelGUI; }
    public ConfigManager getConfigManager() { return configManager; }
    public TransformationManager getTransformationManager() { return transformationManager; }
}
