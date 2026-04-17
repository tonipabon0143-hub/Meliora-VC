package com.example.melioravc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.*;

public class GuiManager implements Listener {
    private JavaPlugin plugin;
    private PrivateGroupManager groupManager;
    private CharacterModeManager characterModeManager;

    public GuiManager(JavaPlugin plugin, PrivateGroupManager groupManager,
                     VoiceGroupSettingsManager settingsManager, CharacterModeManager characterModeManager) {
        this.plugin = plugin;
        this.groupManager = groupManager;
        this.characterModeManager = characterModeManager;
    }

    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§b§lMeliora VC");
        inv.setItem(10, createItem(Material.PLAYER_HEAD, "§aPrivate Groups", "§7Click to manage voice groups"));
        inv.setItem(12, createItem(Material.REDSTONE, "§6Voice Settings", "§7Configure voice quality"));
        inv.setItem(14, createItem(Material.LIME_DYE, "§eCharacter Mode", "§7Current: IC"));
        inv.setItem(16, createItem(Material.DIAMOND, "§bVoice Status", "§7View active players"));
        inv.setItem(26, createItem(Material.BOOK, "§3Help & Info", "§7View command information"));
        player.openInventory(inv);
    }

    public void openGroupsMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§b§lPrivate Groups");
        List<PrivateGroup> groups = groupManager.getPlayerGroups(player.getUniqueId());
        int slot = 0;
        for (PrivateGroup group : groups) {
            if (slot < 18) {
                inv.setItem(slot, createItem(Material.PLAYER_HEAD, "§a" + group.getGroupName(),
                    "§7Members: §f" + group.getMemberCount() + "/" + group.getMemberCount()));
                slot++;
            }
        }
        inv.setItem(19, createItem(Material.EMERALD_BLOCK, "§a+ Create Group", "§7Create a new voice group"));
        inv.setItem(26, createItem(Material.BARRIER, "§cBack", "§7Return to main menu"));
        player.openInventory(inv);
    }

    public void openCharacterModeMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§b§lSelect Character Mode");
        inv.setItem(2, createItem(Material.LIME_DYE, "§aIn-Character (IC)", "§7Roleplay in-character dialogue"));
        inv.setItem(6, createItem(Material.YELLOW_DYE, "§eOut-of-Character (OOC)", "§7Out-of-character conversation"));
        player.openInventory(inv);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String title = event.getView().getTitle();
            event.setCancelled(true);
        }
    }
}