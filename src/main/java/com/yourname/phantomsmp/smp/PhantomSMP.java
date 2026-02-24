package com.phantom.smp;

import com.phantom.smp.commands.*;
import com.phantom.smp.listeners.*;
import com.phantom.smp.manager.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
    
    // Core Managers
    private TimerManager timerManager;
    private BookManager bookManager;
    private CooldownManager cooldownManager;
    private EmoteManager emoteManager;
    private ParticleManager particleManager;
    private GUIManager guiManager;
    private HoldParticleManager holdParticleManager;
    private UltimateHoldParticleManager ultimateHoldParticleManager;
    private CeremonyManager ceremonyManager;
    private GraceManager graceManager;
    private BookBindManager bookBindManager;
    private LevelManager levelManager;
    private LevelGUI levelGUI;
    private KillListener killListener;
    private ConfigManager configManager;
    private TransformationManager transformationManager;
    private AbilityMenuManager abilityMenuManager;
    
    // Trade System Managers
    private TradeManager tradeManager;
    private TradeGUI tradeGUI;
    private TradeAnimationManager tradeAnimationManager;
    private ScreenHeadDisplay screenHeadDisplay;
    private HeadRenderer headRenderer;
    private TitleAnimation titleAnimation;
    
    // Cinematic War Manager
    private CinematicWarManager cinematicWarManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize all managers
        initializeManagers();
        
        // Register plugin messaging channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, "phantomsmp:animation");
        getServer().getMessenger().registerIncomingPluginChannel(this, "phantomsmp:animation", (channel, player, message) -> {
            // Handle incoming animation data if needed
        });
        
        // Register all commands
        registerCommands();
        
        // Register all listeners
        registerListeners();
        
        // Log success message
        logEnableMessage();
    }
    
    private void initializeManagers() {
        configManager = new ConfigManager(this);
        timerManager = new TimerManager(this);
        bookManager = new BookManager(this);
        cooldownManager = new CooldownManager(this);
        emoteManager = new EmoteManager(this);
        particleManager = new ParticleManager(this);
        guiManager = new GUIManager(this);
        holdParticleManager = new HoldParticleManager(this);
        ultimateHoldParticleManager = new UltimateHoldParticleManager(this);
        ceremonyManager = new CeremonyManager(this);
        graceManager = new GraceManager(this);
        bookBindManager = new BookBindManager(this);
        levelManager = new LevelManager(this);
        levelGUI = new LevelGUI(this);
        killListener = new KillListener(this);
        transformationManager = new TransformationManager(this);
        abilityMenuManager = new AbilityMenuManager(this);
        
        // Trade System Managers
        headRenderer = new HeadRenderer(this);
        screenHeadDisplay = new ScreenHeadDisplay(this);
        tradeAnimationManager = new TradeAnimationManager(this);
        tradeManager = new TradeManager(this);
        tradeGUI = new TradeGUI(this);
        titleAnimation = new TitleAnimation(this);
        
        // Cinematic War Manager
        cinematicWarManager = new CinematicWarManager(this);
    }
    
    private void registerCommands() {
        getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        getCommand("grace").setExecutor(new GraceCommand(this));
        getCommand("givebook").setExecutor(new GiveBookCommand(this));
        getCommand("giveall").setExecutor(new GiveAllCommand(this));
        getCommand("getallbooks").setExecutor(new GetAllBooksCommand(this));
        getCommand("booklist").setExecutor(new BookListCommand(this));
        getCommand("bookinfo").setExecutor(new BookInfoCommand(this));
        getCommand("levelbook").setExecutor(new LevelBookCommand(this));
        getCommand("randombook").setExecutor(new RandomBookCommand(this));
        getCommand("reloadphantom").setExecutor(new ReloadCommand(this));
        
        // Trade commands
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("self-trade").setExecutor(new SelfTradeCommand(this));
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new HoldAnimationListener(this), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(guiManager, this);
        getServer().getPluginManager().registerEvents(bookBindManager, this);
        getServer().getPluginManager().registerEvents(killListener, this);
        getServer().getPluginManager().registerEvents(levelGUI, this);
        getServer().getPluginManager().registerEvents(tradeGUI, this);
        getServer().getPluginManager().registerEvents(abilityMenuManager, this);
        getServer().getPluginManager().registerEvents(new AbilityKeyListener(this), this);
    }
    
    private void logEnableMessage() {
        getLogger().info("§a§l╔════════════════════════════════════╗");
        getLogger().info("§a§l║     PhantomSMP v7.0.0 Enabled     ║");
        getLogger().info("§a§l╠════════════════════════════════════╣");
        getLogger().info("§a§l║  ✓ 30 Epic Books Loaded           ║");
        getLogger().info("§a§l║  ✓ 90 Total Abilities             ║");
        getLogger().info("§a§l║  ✓ 3-Level System Active          ║");
        getLogger().info("§a§l║  ✓ Kill Tracking Enabled          ║");
        getLogger().info("§a§l║  ✓ Optimized Particles            ║");
        getLogger().info("§a§l║  ✓ Book Binding System            ║");
        getLogger().info("§a§l║  ✓ Grace Period Ready             ║");
        getLogger().info("§a§l║  ✓ Ceremony Manager Active        ║");
        getLogger().info("§a§l║  ✓ Trade System with Heads        ║");
        getLogger().info("§a§l║  ✓ Screen Animation Ready         ║");
        getLogger().info("§a§l║  ✓ 3D Book Renderer               ║");
        getLogger().info("§a§l║  ✓ Title Animation System         ║");
        getLogger().info("§a§l║  ✓ Cinematic War Mode             ║");
        getLogger().info("§a§l║  ✓ Ability Menu System            ║");
        getLogger().info("§a§l╚════════════════════════════════════╝");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("§c§l╔════════════════════════════════════╗");
        getLogger().info("§c§l║     PhantomSMP v7.0.0 Disabled    ║");
        getLogger().info("§c§l╚════════════════════════════════════╝");
    }
    
    public static PhantomSMP getInstance() {
        return instance;
    }
    
    // ========== CORE MANAGER GETTERS ==========
    
    public TimerManager getTimerManager() { return timerManager; }
    public BookManager getBookManager() { return bookManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public EmoteManager getEmoteManager() { return emoteManager; }
    public ParticleManager getParticleManager() { return particleManager; }
    public GUIManager getGuiManager() { return guiManager; }
    public HoldParticleManager getHoldParticleManager() { return holdParticleManager; }
    public UltimateHoldParticleManager getUltimateHoldParticleManager() { return ultimateHoldParticleManager; }
    public CeremonyManager getCeremonyManager() { return ceremonyManager; }
    public GraceManager getGraceManager() { return graceManager; }
    public BookBindManager getBookBindManager() { return bookBindManager; }
    public LevelManager getLevelManager() { return levelManager; }
    public LevelGUI getLevelGUI() { return levelGUI; }
    public KillListener getKillListener() { return killListener; }
    public ConfigManager getConfigManager() { return configManager; }
    public TransformationManager getTransformationManager() { return transformationManager; }
    public AbilityMenuManager getAbilityMenuManager() { return abilityMenuManager; }
    
    // ========== TRADE SYSTEM GETTERS ==========
    
    public TradeManager getTradeManager() { return tradeManager; }
    public TradeGUI getTradeGUI() { return tradeGUI; }
    public TradeAnimationManager getTradeAnimationManager() { return tradeAnimationManager; }
    public ScreenHeadDisplay getScreenHeadDisplay() { return screenHeadDisplay; }
    public HeadRenderer getHeadRenderer() { return headRenderer; }
    public TitleAnimation getTitleAnimation() { return titleAnimation; }
    
    // ========== CINEMATIC WAR GETTER ==========
    
    public CinematicWarManager getCinematicWarManager() { return cinematicWarManager; }
}
