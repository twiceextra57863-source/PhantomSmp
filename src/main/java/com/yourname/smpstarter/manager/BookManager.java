package com.yourname.smpstarter.manager;

import com.yourname.smpstarter.models.MagicBook;
import java.util.*;

public class BookManager {
    private final Map<String, MagicBook> registeredBooks = new HashMap<>();
    private final Random random = new Random();

    public BookManager() {
        // Register default magical books
        registerBook(new MagicBook("fireball", "§cTome of Fire", Arrays.asList("§7Right-click to cast a fireball!", "§cWarning: Highly flammable.")));
        registerBook(new MagicBook("heal", "§aTome of Healing", Arrays.asList("§7Right-click to heal yourself!", "§aMends your wounds.")));
        registerBook(new MagicBook("speed", "§bTome of Speed", Arrays.asList("§7Right-click to gain a speed boost!", "§bSwift as the wind.")));
    }

    private void registerBook(MagicBook book) {
        registeredBooks.put(book.getId().toLowerCase(), book);
    }

    public MagicBook getBook(String id) {
        return registeredBooks.get(id.toLowerCase());
    }

    public MagicBook getRandomBook() {
        List<MagicBook> values = new ArrayList<>(registeredBooks.values());
        if (values.isEmpty()) return null;
        return values.get(random.nextInt(values.size()));
    }
}