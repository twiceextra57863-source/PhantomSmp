package com.yourname.smpstarter.commands;

import com.yourname.smpstarter.manager.TimerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SMPStartCommand implements CommandExecutor {
    private final TimerManager timerManager;

    public SMPStartCommand(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smpstarter.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (timerManager.isSmpStarted()) {
            sender.sendMessage("§cThe SMP has already started!");
            return true;
        }

        timerManager.startSMPCountdown();
        sender.sendMessage("§aInitiated the SMP countdown!");
        return true;
    }
}