package com.yourname.smpstarter;

import com.yourname.smpstarter.commands.GiveBookCommand;
import com.yourname.smpstarter.commands.RandomBookCommand;
import com.yourname.smpstarter.commands.SMPStartCommand;
import com.yourname.smpstarter.listeners.BookListener;
import com.yourname.smpstarter.listeners.ProtectionListener;
import com.yourname.smpstarter.manager.BookManager;
import com.yourname.smpstarter.manager.TimerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SMPStarter extends JavaPlugin {
    
    private static SMPStarter instance;
    private TimerManager timerManager;
    private BookManager bookManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        timerManager = new TimerManager(this);
        bookManager = new BookManager(this);
        
        // Register commands
        getCommand("smpstart").setExecutor(new SMPStartCommand(this));
        getCommand("givebook").setExecutor(new GiveBookCommand(this));
        getCommand("randombook").setExecutor(new RandomBookCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new BookListener(this), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        
        getLogger().info("SMPStarter has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SMPStarter has been disabled!");
    }
    
    public static SMPStarter getInstance() {
        return instance;
    }
    
    public TimerManager getTimerManager() {
        return timerManager;
    }
    
    public BookManager getBookManager() {
        return bookManager;
    }
}
