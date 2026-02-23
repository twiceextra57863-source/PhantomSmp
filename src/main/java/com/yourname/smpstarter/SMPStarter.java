package com.yourname.smpstarter;

import com.yourname.smpstarter.commands.GiveBookCommand;
import com.yourname.smpstarter.commands.RandomBookCommand;
import com.yourname.smpstarter.commands.SMPStartCommand;
import com.yourname.smpstarter.listeners.BookListener;
import com.yourname.smpstarter.listeners.ProtectionListener;
import com.yourname.smpstarter.manager.BookManager;
import com.yourname.smpstarter.manager.ParticleManager;
import com.yourname.smpstarter.manager.TimerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SMPStarter extends JavaPlugin {

    private TimerManager timerManager;
    private BookManager bookManager;
    private ParticleManager particleManager;

    @Override
    public void onEnable() {
        // Initialize managers
        timerManager = new TimerManager(this);
        bookManager = new BookManager();
        particleManager = new ParticleManager();

        // Register commands
        getCommand("smpstart").setExecutor(new SMPStartCommand(timerManager));
        getCommand("givebook").setExecutor(new GiveBookCommand(bookManager));
        getCommand("randombook").setExecutor(new RandomBookCommand(bookManager));

        // Register listeners
        getServer().getPluginManager().registerEvents(new ProtectionListener(timerManager), this);
        getServer().getPluginManager().registerEvents(new BookListener(bookManager, particleManager), this);

        getLogger().info("SMPStarter enabled! Waiting for the SMP to start.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SMPStarter disabled!");
    }
}