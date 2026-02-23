package com.phantom.smp;

import com.phantom.smp.commands.GiveBookCommand;
import com.phantom.smp.commands.RandomBookCommand;
import com.phantom.smp.commands.ReloadCommand;
import com.phantom.smp.commands.SMPStartCommand;
import com.phantom.smp.listeners.BookListener;
import com.phantom.smp.listeners.HoldAnimationListener;
import com.phantom.smp.listeners.ProtectionListener;
import com.phantom.smp.manager.BookManager;
import com.phantom.smp.manager.CooldownManager;
import com.phantom.smp.manager.EmoteManager;
import com.phantom.smp.manager.TimerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomSMP extends JavaPlugin {
    
    private static PhantomSMP instance;
    private TimerManager timerManager;
    private BookManager bookManager;
    private CooldownManager cooldownManager;
    private EmoteManager emoteManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        timerManager = new TimerManager(this);
        bookManager = new BookManager(this);
        cooldownManager = new CooldownManager(this);
        emoteManager = new EmoteManager(this);
        
        // Register commands
        getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        getCommand("givebook").setExecutor(new GiveBookCommand(this));
        getCommand("randombook").setExecutor(new RandomBookCommand(this));
        getCommand("reloadphantom").setExecutor(new ReloadCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new HoldAnimationListener(this), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        
        getLogger().info("§a§lPhantomSMP v2.0.0 has been enabled!");
        getLogger().info("§e§l30 Epic Books Loaded!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("§c§lPhantomSMP has been disabled!");
    }
    
    public static PhantomSMP getInstance() {
        return instance;
    }
    
    public TimerManager getTimerManager() {
        return timerManager;
    }
    
    public BookManager getBookManager() {
        return bookManager;
    }
    
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    
    public EmoteManager getEmoteManager() {
        return emoteManager;
    }
}
