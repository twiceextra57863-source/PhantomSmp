package com.phantom.smp.models;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MagicBook {
    
    STORMBRINGER("🌩️ Stormbringer", Material.ENCHANTED_BOOK, 
         "Call down lightning from the skies", 30, "storm"),
    
    SHADOWSTRIKE("👻 Shadowstrike", Material.ENCHANTED_BOOK,
          "Vanish and strike from the darkness", 25, "shadow"),
    
    FLAMEWALKER("🔥 Flamewalker", Material.ENCHANTED_BOOK,
            "Walk through fire and leave destruction", 20, "flame"),
    
    FROSTBITE("❄️ Frostbite", Material.ENCHANTED_BOOK,
        "Freeze your enemies solid", 25, "frost"),
    
    DRAGONSBREATH("🐉 Dragon's Breath", Material.ENCHANTED_BOOK,
           "Exhale devastating dragon fire", 35, "dragon"),
    
    VOIDWALKER("🌌 Voidwalker", Material.ENCHANTED_BOOK,
         "Phase through reality itself", 40, "void"),
    
    LIFEBINDER("🌿 Lifebinder", Material.ENCHANTED_BOOK,
           "Heal and nurture all life", 15, "life"),
    
    GRAVITY("⚡ Gravity", Material.ENCHANTED_BOOK,
           "Control the pull of the earth", 30, "gravity"),
    
    PHANTOM("👤 Phantom", Material.ENCHANTED_BOOK,
           "Become one with the shadows", 20, "phantom"),
    
    DAWNBRINGER("☀️ Dawnbringer", Material.ENCHANTED_BOOK,
          "Bring light to the darkest places", 25, "dawn"),
    
    TERRASHAPER("⛰️ Terrashaper", Material.ENCHANTED_BOOK,
          "Shape the earth at your will", 35, "terra"),
    
    ZEPHYR("💨 Zephyr", Material.ENCHANTED_BOOK,
         "Ride the winds of fate", 15, "wind"),
    
    TIMEWEAVER("⏳ Timeweaver", Material.ENCHANTED_BOOK,
         "Bend time to your advantage", 45, "time"),
    
    SOULREAPER("💀 Soulreaper", Material.ENCHANTED_BOOK,
         "Feast on the souls of enemies", 30, "soul"),
    
    CRYSTALMAGE("💎 Crystalmage", Material.ENCHANTED_BOOK,
            "Summon crystals of pure power", 25, "crystal"),
    
    THUNDERGOD("⚡ Thundergod", Material.ENCHANTED_BOOK,
            "Wield the power of storms", 35, "thunder"),
    
    ICEWARDEN("❄️ Icewarden", Material.ENCHANTED_BOOK,
          "Command the frozen wastes", 25, "ice"),
    
    PYROMANCER("🔥 Pyromancer", Material.ENCHANTED_BOOK,
          "Master of living flame", 20, "pyro"),
    
    SPIRITWARDEN("👾 Spiritwarden", Material.ENCHANTED_BOOK,
           "Call upon ancestral spirits", 30, "spirit"),
    
    NECROLORD("⚰️ Necrolord", Material.ENCHANTED_BOOK,
          "Raise the dead to serve you", 40, "necro"),
    
    SERAPHIM("👼 Seraphim", Material.ENCHANTED_BOOK,
          "Blessed with divine power", 25, "seraph"),
    
    ABYSS("🌑 Abyss", Material.ENCHANTED_BOOK,
          "Embrace the endless darkness", 30, "abyss"),
    
    CHAOSWEAVER("🌀 Chaosweaver", Material.ENCHANTED_BOOK,
          "Unleash pure randomness", 20, "chaos"),
    
    JUDGEMENT("⚖️ Judgement", Material.ENCHANTED_BOOK,
          "Smite the wicked", 35, "judge"),
    
    DREAMCATCHER("💫 Dreamcatcher", Material.ENCHANTED_BOOK,
          "Weave dreams into reality", 25, "dream"),
    
    NIGHTTERROR("👹 Nightterror", Material.ENCHANTED_BOOK,
              "Instill fear in your foes", 30, "fear"),
    
    AURORA("🌈 Aurora", Material.ENCHANTED_BOOK,
           "Paint the sky with light", 20, "aurora"),
    
    STARFALL("✨ Starfall", Material.ENCHANTED_BOOK,
           "Bring the heavens down", 35, "star"),
    
    INFERNUS("🔥 Infernus", Material.ENCHANTED_BOOK,
            "Unleash hell on earth", 40, "inferno"),
    
    AVALANCHE("🏔️ Avalanche", Material.ENCHANTED_BOOK,
              "Overwhelm with frozen fury", 30, "avalanche");

    private final String displayName;
    private final Material material;
    private final String description;
    private final int cooldown;
    private final String abilityKey;

    MagicBook(String displayName, Material material, String description, int cooldown, String abilityKey) {
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.cooldown = cooldown;
        this.abilityKey = abilityKey;
    }

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
        
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        book.setItemMeta(meta);
        return book;
    }

    public ItemStack createBookWithCooldown(long remainingSeconds) {
        ItemStack book = new ItemStack(material);
        ItemMeta meta = book.getItemMeta();
        
        String cooldownStatus = remainingSeconds > 0 
            ? "§c❌ On Cooldown: §f" + remainingSeconds + "s" 
            : "§a✅ Ready to use!";
        
        meta.setDisplayName("§r§6§l" + displayName);
        meta.setLore(Arrays.asList(
            "§7" + description,
            "",
            "§e§lRIGHT CLICK §7to unleash power!",
            "§8⏱️ Cooldown: §f" + cooldown + "s",
            cooldownStatus,
            "",
            "§8§oPhantom SMP Artifact",
            "§8Ability: " + abilityKey
        ));
        
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        book.setItemMeta(meta);
        return book;
    }

    public ItemStack createBookWithLevel(int level, int kills) {
        ItemStack book = new ItemStack(material);
        ItemMeta meta = book.getItemMeta();
        
        String levelColor = getLevelColor(level);
        String levelName = getLevelName(level);
        
        meta.setDisplayName("§r" + levelColor + "§l" + displayName + " §7[" + levelName + "]");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7" + description);
        lore.add("");
        lore.add("§e§lRIGHT CLICK §7to unleash power!");
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
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        book.setItemMeta(meta);
        return book;
    }

    private String getLevelColor(int level) {
        switch(level) {
            case 1: return "§7";
            case 2: return "§b";
            case 3: return "§6";
            default: return "§f";
        }
    }

    private String getLevelName(int level) {
        switch(level) {
            case 1: return "Initiate";
            case 2: return "Ascended";
            case 3: return "Godly";
            default: return "Unknown";
        }
    }

    public static MagicBook getRandomBook() {
        return values()[(int) (Math.random() * values().length)];
    }

    public static MagicBook getByAbilityKey(String key) {
        for (MagicBook book : values()) {
            if (book.abilityKey.equalsIgnoreCase(key)) {
                return book;
            }
        }
        return null;
    }

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
}
