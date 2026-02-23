package com.yourname.smpstarter.commands;

import com.yourname.smpstarter.manager.BookManager;
import com.yourname.smpstarter.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomBookCommand implements CommandExecutor {
    private final BookManager bookManager;

    public RandomBookCommand(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smpstarter.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /randombook <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        MagicBook book = bookManager.getRandomBook();
        if (book == null) {
            sender.sendMessage("§cNo books are registered in the BookManager.");
            return true;
        }

        target.getInventory().addItem(book.toItemStack());
        sender.sendMessage("§aGave a random book (" + book.getDisplayName() + "§a) to " + target.getName() + ".");
        target.sendMessage("§aYou have received a random magical book!");

        return true;
    }
}