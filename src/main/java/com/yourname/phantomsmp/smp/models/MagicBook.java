package com.phantom.smp.models;

import com.phantom.smp.PhantomSMP;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MagicBook {
    
    // ========== DEMON SLAYER THEME ==========
    SUN_BREATHING("☀️ Sun Breathing", Material.ENCHANTED_BOOK, 
         "12th Form: Flame Dance - Execute blazing sword strikes", 25, "sun"),
    
    WATER_BREATHING("💧 Water Breathing", Material.ENCHANTED_BOOK,
          "11th Form: Dead Calm - Create water dragons that crush enemies", 22, "water"),
    
    THUNDER_BREATHING("⚡ Thunder Breathing", Material.ENCHANTED_BOOK,
            "7th Form: Honoikazuchi - Lightning-fast dash with thunder clap", 20, "thunder"),
    
    FLAME_BREATHING("🔥 Flame Breathing", Material.ENCHANTED_BOOK,
        "9th Form: Rengoku - Unwavering flame slash that ignites the area", 24, "flame"),
    
    WIND_BREATHING("🌪️ Wind Breathing", Material.ENCHANTED_BOOK,
           "7th Form: Gale - Create tornado blades that slice everything", 23, "wind"),
    
    STONE_BREATHING("⛰️ Stone Breathing", Material.ENCHANTED_BOOK,
         "5th Form: Arcs of Justice - Ground-shattering slam", 28, "stone"),
    
    MIST_BREATHING("🌫️ Mist Breathing", Material.ENCHANTED_BOOK,
          "7th Form: Obscuring Clouds - Vanish and strike from nowhere", 26, "mist"),
    
    BEAST_BREATHING("🐗 Beast Breathing", Material.ENCHANTED_BOOK,
           "11th Form: Fang Sharpening - Ferocious beast-mode attacks", 21, "beast"),
    
    SOUND_BREATHING("🔊 Sound Breathing", Material.ENCHANTED_BOOK,
         "5th Form: String Performance - Sonic waves that disorient", 24, "sound"),
    
    SERPENT_BREATHING("🐍 Serpent Breathing", Material.ENCHANTED_BOOK,
          "5th Form: Slithering Strike - Coiling snake attacks", 23, "serpent"),
    
    LOVE_BREATHING("💖 Love Breathing", Material.ENCHANTED_BOOK,
           "6th Form: Constant Flux - Whirling dance of blades", 22, "love"),
    
    // ========== SOLO LEVELING THEME ==========
    SHADOW_SOVEREIGN("👑 Shadow Sovereign", Material.ENCHANTED_BOOK,
         "Monarch's Domain - Raise shadow soldiers from fallen enemies", 30, "sovereign"),
    
    DEMON_KING("👹 Demon King", Material.ENCHANTED_BOOK,
          "Frost Monarch - Absolute zero ice domain", 28, "demonking"),
    
    BEAST_LORD("🐺 Beast Lord", Material.ENCHANTED_BOOK,
           "Monarch of Beasts - Summon spectral wolves to hunt", 25, "beastlord"),
    
    SNOW_FIEND("❄️ Snow Fiend", Material.ENCHANTED_BOOK,
          "Monarch of Snow - Blizzard that freezes everything", 26, "snow"),
    
    // ========== JUJUTSU KAISEN THEME ==========
    LIMITLESS("∞ Limitless", Material.ENCHANTED_BOOK,
         "Hollow Purple - Create imaginary mass that erases targets", 35, "limitless"),
    
    TEN_SHADOWS("🕷️ Ten Shadows", Material.ENCHANTED_BOOK,
          "Mahoraga - Summon shikigami to overwhelm enemies", 32, "shadows"),
    
    DISASTER_FLAMES("🔥 Disaster Flames", Material.ENCHANTED_BOOK,
           "Volcano - Erupting cursed energy", 27, "disaster"),
    
    BLOOD_MANIPULATION("🩸 Blood Manipulation", Material.ENCHANTED_BOOK,
          "Slicing Exorcism - Blood blades from all directions", 26, "blood"),
    
    COMEDY("🎭 Comedy", Material.ENCHANTED_BOOK,
           "Cursed Speech - Force enemies to stop", 20, "comedy"),
    
    // ========== DRAGON BALL Z THEME ==========
    SPIRIT_BOMB("💫 Spirit Bomb", Material.ENCHANTED_BOOK,
          "Ultimate Technique - Massive energy sphere", 40, "spirit"),
    
    KAMEHAMEHA("🌊 Kamehameha", Material.ENCHANTED_BOOK,
           "Super Kamehameha - Concentrated energy wave", 30, "kame"),
    
    INSTANT_TRANSMISSION("⚡ Instant Transmission", Material.ENCHANTED_BOOK,
          "Afterimage Strike - Teleport and attack", 22, "instant"),
    
    SOLAR_FLARE("☀️ Solar Flare", Material.ENCHANTED_BOOK,
          "Bright Explosion - Blind and damage all", 18, "solar"),
    
    GALAXY_BREAKER("🌌 Galaxy Breaker", Material.ENCHANTED_BOOK,
          "God of Destruction - Universe-shattering blast", 45, "galaxy"),
    
    // ========== ORIGINAL CREATIONS ==========
    VOID_REAVER("🌑 Void Reaver", Material.ENCHANTED_BOOK,
          "Reality Slash - Cut through dimensions", 33, "reaver"),
    
    SOUL_EATER("💀 Soul Eater", Material.ENCHANTED_BOOK,
          "Consume - Absorb life force from enemies", 28, "eater"),
    
    STAR_FALL("✨ Star Fall", Material.ENCHANTED_BOOK,
          "Meteor Shower - Rain of celestial bodies", 32, "starfall"),
    
    TIME_STOP("⏰ Time Stop", Material.ENCHANTED_BOOK,
          "The World - Freeze time for enemies", 38, "time"),
    
    REALITY_WRITER("📝 Reality Writer", Material.ENCHANTED_BOOK,
          "Rewrite - Change the battlefield", 35, "writer");

    private final String displayName;
    private final Material material;
    private final String description;
    private final int cooldown;
    private final String abilityKey;
    
    // Static instance for persistent data key
    private static PhantomSMP plugin;

    MagicBook(String displayName, Material material, String description, int cooldown, String abilityKey) {
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.cooldown = cooldown;
        this.abilityKey = abilityKey;
    }
    
    /**
     * Initialize plugin instance (call from main class)
     */
    public static void init(PhantomSMP instance) {
        plugin = instance;
    }

    /**
     * Create a new book item with persistent data
     */
    public ItemStack createBook() {
        ItemStack book = new ItemStack(material);
        ItemMeta meta = book.getItemMeta();
        
        meta.setDisplayName("§r§6§l" + displayName);
        meta.setLore(Arrays.asList(
            "§7" + description,
            "",
            "§e§lRIGHT CLICK §7to unleash power!",
            "§8⏱️ Cooldown: §f" + cooldown + "s",
            "",
            "§8§oPhantom SMP Artifact",
            "§8Ability: " + abilityKey
        ));
        
        // CRITICAL: Add persistent data to identify books
        if (plugin != null) {
            NamespacedKey key = new NamespacedKey(plugin, "phantom-book");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, abilityKey);
        }
        
        // Add enchantment glow
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Create a book with level information
     */
    public ItemStack createBookWithLevel(int level, int kills) {
        ItemStack book = new ItemStack(material);
        ItemMeta meta = book.getItemMeta();
        
        String levelColor = getLevelColor(level);
        String levelName = getLevelName(level);
        
        meta.setDisplayName("§r" + levelColor + "§l" + displayName + " §7[" + levelName + "]");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7" + description);
        lore.add("");
        lore.add("§e§lRIGHT CLICK §7- Primary Ability");
        lore.add("§b§lTRIPLE CROUCH §7- Open Ability Menu");
        lore.add("§8⏱️ Cooldown: §f" + cooldown + "s §7(base)");
        lore.add("");
        lore.add("§d§l⚡ LEVEL " + level + " " + levelName);
        lore.add("§7Kills: §f" + kills + " §7/ 30");
        
        if (level < 3) {
            int needed = (level == 1) ? 15 : 30;
            int remaining = needed - kills;
            lore.add("§7Next Level: §e" + remaining + " §7more kills");
        } else {
            lore.add("§6§lMAX LEVEL REACHED!");
        }
        
        lore.add("");
        lore.add("§8§oPhantom SMP Artifact");
        lore.add("§8Ability: " + abilityKey);
        
        meta.setLore(lore);
        
        // CRITICAL: Add persistent data
        if (plugin != null) {
            NamespacedKey key = new NamespacedKey(plugin, "phantom-book");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, abilityKey);
        }
        
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Get level color based on level
     */
    private String getLevelColor(int level) {
        switch(level) {
            case 1: return "§7";
            case 2: return "§b";
            case 3: return "§6";
            default: return "§f";
        }
    }

    /**
     * Get level name based on level
     */
    private String getLevelName(int level) {
        switch(level) {
            case 1: return "Initiate";
            case 2: return "Ascended";
            case 3: return "Godly";
            default: return "Unknown";
        }
    }

    /**
     * Get a random book
     */
    public static MagicBook getRandomBook() {
        return values()[(int) (Math.random() * values().length)];
    }

    /**
     * Get book by ability key
     */
    public static MagicBook getByAbilityKey(String key) {
        for (MagicBook book : values()) {
            if (book.abilityKey.equalsIgnoreCase(key)) {
                return book;
            }
        }
        return null;
    }
    
    /**
     * CRITICAL: Check if an item is a Phantom Book using persistent data
     */
    public static boolean isPhantomBook(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return false;
        if (!item.hasItemMeta()) return false;
        if (plugin == null) return false;
        
        NamespacedKey key = new NamespacedKey(plugin, "phantom-book");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
    
    /**
     * Get ability key from book item
     */
    public static String getAbilityKeyFromItem(ItemStack item) {
        if (!isPhantomBook(item)) return null;
        if (plugin == null) return null;
        
        NamespacedKey key = new NamespacedKey(plugin, "phantom-book");
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    // ========== GETTERS ==========

    public String getDisplayName() {
        return displayName;
    }

    public String getAbilityKey() {
        return abilityKey;
    }
    
    public int getCooldown() {
        return cooldown;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Material getMaterial() {
        return material;
    }
}
