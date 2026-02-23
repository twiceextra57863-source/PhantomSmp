package com.yourname.smpstarter.commands;

import com.yourname.smpstarter.manager.BookManager;
import com.yourname.smpstarter.models.MagicBook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveBookCommand implements CommandExecutor {
    private final BookManager bookManager;

    public GiveBookCommand(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smpstarter.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /givebook <player> <fireball|heal|speed>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        MagicBook book = bookManager.getBook(args[1]);
        if (book == null) {
            sender.sendMessage("§cInvalid book type. Available: fireball, heal, speed");
            return true;
        }

        target.getInventory().addItem(book.toItemStack());
        sender.sendMessage("§aGave a " + book.getDisplayName() + " §ato " + target.getName() + ".");
        target.sendMessage("§aYou have received a magical book!");

        return true;
    }
}