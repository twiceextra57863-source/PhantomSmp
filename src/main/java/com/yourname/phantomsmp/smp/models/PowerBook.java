package com.minetwice.phantomsmp.models;

import com.minetwice.phantomsmp.PhantomSMP;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PowerBook {
    
    private final String id;
    private final String name;
    private final String theme;
    private final BookAbility ability1;
    private final BookAbility ability2;
    private int level;
    private int kills;
    private final UUID ownerUUID;
    
    public PowerBook(String id, String name, String theme, 
                     BookAbility ability1, BookAbility ability2, UUID ownerUUID) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.ability1 = ability1;
        this.ability2 = ability2;
        this.level = 1;
        this.kills = 0;
        this.ownerUUID = ownerUUID;
    }
    
    public ItemStack createBookItem() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        meta.setDisplayName(getColoredName());
        
        // Create lore
        List<String> lore = new ArrayList<>();
        lore.add("§7" + getThemeColor() + "Theme: §f" + theme);
        lore.add("");
        lore.add("§6§lABILITIES:");
        lore.add("§e▶ §fRight Click: §7" + ability1.getName());
        lore.add("§e▶ §fShift+Right Click: §7" + ability2.getName());
        lore.add("");
        lore.add("§6§lSTATISTICS:");
        lore.add("§7Level: §e" + level + " §7(" + getLevelBar() + "§7)");
        lore.add("§7Kills: §e" + kills + " §7/ §e" + getRequiredKills());
        lore.add("");
        lore.add("§6§lNEXT LEVEL:");
        if (level == 1) {
            lore.add("§7Need §e" + (10 - kills) + " §7more kills");
            lore.add("§7Reward: §aStronger abilities");
        } else if (level == 2) {
            lore.add("§7Need §e" + (25 - kills) + " §7more kills");
            lore.add("§7Reward: §aMaximum power");
        } else {
            lore.add("§aMAX LEVEL REACHED!");
        }
        lore.add("");
        lore.add("§8§o\"The power of " + name + " flows within\"");
        
        meta.setLore(lore);
        meta.setEnchantmentGlintOverride(true);
        
        // Store data
        meta.getPersistentDataContainer().set(
            NamespacedKey.fromString("phantomsmp:book_id", PhantomSMP.getInstance()),
            PersistentDataType.STRING, id);
        meta.getPersistentDataContainer().set(
            NamespacedKey.fromString("phantomsmp:owner", PhantomSMP.getInstance()),
            PersistentDataType.STRING, ownerUUID.toString());
        meta.getPersistentDataContainer().set(
            NamespacedKey.fromString("phantomsmp:level", PhantomSMP.getInstance()),
            PersistentDataType.INTEGER, level);
        meta.getPersistentDataContainer().set(
            NamespacedKey.fromString("phantomsmp:kills", PhantomSMP.getInstance()),
            PersistentDataType.INTEGER, kills);
        
        book.setItemMeta(meta);
        return book;
    }
    
    private String getColoredName() {
        switch (theme.toLowerCase()) {
            case "mythic": return "§c§l" + name;
            case "ghost": return "§7§l" + name;
            case "elemental": return "§b§l" + name;
            default: return "§f§l" + name;
        }
    }
    
    private String getThemeColor() {
        switch (theme.toLowerCase()) {
            case "mythic": return "§c";
            case "ghost": return "§7";
            case "elemental": return "§b";
            default: return "§f";
        }
    }
    
    private String getLevelBar() {
        int current = level == 1 ? kills : (level == 2 ? kills - 10 : kills - 25);
        int max = level == 1 ? 10 : 25;
        int filled = (current * 10) / max;
        StringBuilder bar = new StringBuilder("§a");
        for (int i = 0; i < 10; i++) {
            bar.append(i < filled ? "■" : "□");
        }
        return bar.toString();
    }
    
    private int getRequiredKills() {
        if (level == 1) return 10;
        if (level == 2) return 25;
        return 0;
    }
    
    public void addKill() {
        this.kills++;
        int oldLevel = level;
        
        if (level == 1 && kills >= 10) {
            level = 2;
        } else if (level == 2 && kills >= 25) {
            level = 3;
        }
        
        // Update lore when kills increase
        if (level != oldLevel) {
            // Level up animation will be handled by CombatListener
        }
    }
    
    public int getAbilityDamage(int baseDamage, int level) {
        switch (this.level) {
            case 1: return baseDamage;
            case 2: return (int)(baseDamage * 1.5);
            case 3: return baseDamage * 2;
            default: return baseDamage;
        }
    }
    
    public int getAbilityDuration(int baseDuration, int level) {
        switch (this.level) {
            case 1: return baseDuration;
            case 2: return (int)(baseDuration * 1.3);
            case 3: return baseDuration * 1.6;
            default: return baseDuration;
        }
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTheme() { return theme; }
    public BookAbility getAbility1() { return ability1; }
    public BookAbility getAbility2() { return ability2; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills; }
    public UUID getOwnerUUID() { return ownerUUID; }
}
