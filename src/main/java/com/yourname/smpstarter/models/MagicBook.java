package com.yourname.smpstarter.models;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.List;

public enum MagicBook {
    
    // 30 Magical Books with unique names and abilities
    THOR("⚡ Thor's Wrath", Material.ENCHANTED_BOOK, 
         "Summon lightning strikes on enemies",
         "§e§lRIGHT CLICK §7to unleash Thor's power!"),
    
    STORM("🌪️ Storm Breaker", Material.ENCHANTED_BOOK,
          "Create a tornado that throws enemies in air",
          "§b§lRIGHT CLICK §7to summon a storm!"),
    
    PHOENIX("🔥 Phoenix Flame", Material.ENCHANTED_BOOK,
            "Rise from ashes with regeneration and fire",
            "§c§lRIGHT CLICK §7to be reborn in flames!"),
    
    ICE("❄️ Frost Weaver", Material.ENCHANTED_BOOK,
        "Freeze enemies in ice and create snow storms",
        "§3§lRIGHT CLICK §7to freeze the battlefield!"),
    
    DRAGON("🐉 Dragon's Fury", Material.ENCHANTED_BOOK,
           "Breathe fire and summon dragon spirits",
           "§5§lRIGHT CLICK §7to unleash dragon wrath!"),
    
    VOID("🌌 Void Walker", Material.ENCHANTED_BOOK,
         "Teleport through dimensions and avoid damage",
         "§8§lRIGHT CLICK §7to phase through reality!"),
    
    NATURE("🌿 Nature's Blessing", Material.ENCHANTED_BOOK,
           "Grow trees instantly and heal allies",
           "§2§lRIGHT CLICK §7to embrace nature!"),
    
    COSMIC("✨ Cosmic Power", Material.ENCHANTED_BOOK,
           "Manipulate gravity and throw enemies",
           "§d§lRIGHT CLICK §7to control the cosmos!"),
    
    SHADOW("👻 Shadow Strike", Material.ENCHANTED_BOOK,
           "Become invisible and deal bonus damage",
           "§7§lRIGHT CLICK §7to fade into shadows!"),
    
    LIGHT("☀️ Light Bringer", Material.ENCHANTED_BOOK,
          "Blind enemies and heal with holy light",
          "§e§lRIGHT CLICK §7to bring the light!"),
    
    EARTH("⛰️ Earth Shaker", Material.ENCHANTED_BOOK,
          "Create earthquakes and raise walls",
          "§6§lRIGHT CLICK §7to shake the ground!"),
    
    WIND("💨 Wind Runner", Material.ENCHANTED_BOOK,
         "Launch yourself in air and double jump",
         "§f§lRIGHT CLICK §7to ride the wind!"),
    
    TIME("⏰ Time Keeper", Material.ENCHANTED_BOOK,
         "Slow time for enemies and speed yourself",
         "§b§lRIGHT CLICK §7to control time!"),
    
    SOUL("💀 Soul Reaper", Material.ENCHANTED_BOOK,
         "Steal health from enemies and summon souls",
         "§4§lRIGHT CLICK §7to reap souls!"),
    
    CRYSTAL("💎 Crystal Mage", Material.ENCHANTED_BOOK,
            "Summon crystal shields and projectile",
            "§d§lRIGHT CLICK §7to crystallize!"),
    
    THUNDER("🌩️ Thunder God", Material.ENCHANTED_BOOK,
            "Chain lightning between enemies",
            "§e§lRIGHT CLICK §7to thunder strike!"),
    
    FROST("❄️ Frost Knight", Material.ENCHANTED_BOOK,
          "Ice armor and freezing aura",
          "§3§lRIGHT CLICK §7to freeze solid!"),
    
    FLAME("🔥 Flame Emperor", Material.ENCHANTED_BOOK,
          "Fire rings and burning ground",
          "§c§lRIGHT CLICK §7to ignite!"),
    
    SPIRIT("👾 Spirit Guide", Material.ENCHANTED_BOOK,
           "Summon spirit wolves to fight",
           "§7§lRIGHT CLICK §7to call spirits!"),
    
    NECRO("⚰️ Necromancer", Material.ENCHANTED_BOOK,
          "Raise dead mobs as allies",
          "§5§lRIGHT CLICK §7to raise dead!"),
    
    ANGEL("👼 Angel's Grace", Material.ENCHANTED_BOOK,
          "Slow falling and healing aura",
          "§f§lRIGHT CLICK §7to feel grace!"),
    
    DEMON("👿 Demon's Rage", Material.ENCHANTED_BOOK,
          "Strength boost and fire aura",
          "§4§lRIGHT CLICK §7to unleash rage!"),
    
    CHAOS("🌀 Chaos Magic", Material.ENCHANTED_BOOK,
          "Random effects on every use",
          "§5§lRIGHT CLICK §7to embrace chaos!"),
    
    ORDER("⚖️ Order's Judgement", Material.ENCHANTED_BOOK,
          "Smite evil and protect allies",
          "§b§lRIGHT CLICK §7to judge!"),
    
    DREAM("💭 Dream Weaver", Material.ENCHANTED_BOOK,
          "Put enemies to sleep and heal",
          "§d§lRIGHT CLICK §7to weave dreams!"),
    
    NIGHTMARE("👹 Nightmare", Material.ENCHANTED_BOOK,
              "Fear effect and damage over time",
              "§8§lRIGHT CLICK §7to terrorize!"),
    
    AURORA("🌈 Aurora", Material.ENCHANTED_BOOK,
           "Colorful lights and speed boost",
           "§b§lRIGHT CLICK §7to see lights!"),
    
    GALAXY("🌌 Galaxy", Material.ENCHANTED_BOOK,
           "Star projectiles and gravity wells",
           "§d§lRIGHT CLICK §7to reach stars!"),
    
    INFERNO("🔥 Inferno", Material.ENCHANTED_BOOK,
            "Massive fire explosion",
            "§c§lRIGHT CLICK §7to burn all!"),
    
    AVALANCHE("🏔️ Avalanche", Material.ENCHANTED_BOOK,
              "Snowball barrage and freeze",
              "§3§lRIGHT CLICK §7to avalanche!");

    private final String displayName;
    private final Material material;
    private final String description;
    private final String ability;

    MagicBook(String displayName, Material material, String description, String ability) {
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.ability = ability;
    }

    public ItemStack createBook() {
        ItemStack book = new ItemStack(material);
        ItemMeta meta = book.getItemMeta();
        
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(
            "§7" + description,
            "",
            ability,
            "",
            "§8§oMagical SMP Starter Book"
        ));
        
        book.setItemMeta(meta);
        return book;
    }

    public static MagicBook getRandomBook() {
        return values()[(int) (Math.random() * values().length)];
    }

    public String getDisplayName() {
        return displayName;
    }
}
