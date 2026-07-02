package com.mythicstaff;

import com.mythicstaff.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class StaffManager {

    private final MythicStaffPlugin plugin;
    private final NamespacedKey staffKey;

    public StaffManager(MythicStaffPlugin plugin) {
        this.plugin = plugin;
        this.staffKey = new NamespacedKey(plugin, "mythic_staff_id");
    }

    public ItemStack createFireStaff() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Clean display name with proper MC-compatible formatting
            meta.setDisplayName(TextUtil.color("<#FF4400>&l" + TextUtil.smallCaps("Fire Staff")));

            List<String> lore = Arrays.asList(
                    "",
                    TextUtil.color("<#AAAAAA>" + TextUtil.smallCaps("Mythic Weapon")),
                    "",
                    TextUtil.color("<#FFB700>" + TextUtil.smallCaps("Abilities:")),
                    TextUtil.color("<#FFEE55> \u25aa Left Click <#666666>| <#FF3300>" + TextUtil.smallCaps("Fire Dash")),
                    TextUtil.color("<#FFEE55> \u25aa Snk + L-Click <#666666>| <#FF3300>" + TextUtil.smallCaps("Fire Vortex")),
                    TextUtil.color("<#FFEE55> \u25aa Right Click <#666666>| <#FF3300>" + TextUtil.smallCaps("Flame Burst")),
                    TextUtil.color("<#FFEE55> \u25aa Snk + R-Click <#666666>| <#FF3300>" + TextUtil.smallCaps("Inferno Ring")),
                    TextUtil.color("<#FFEE55> \u25aa Passive <#666666>| <#FF3300>" + TextUtil.smallCaps("Lava Walker")),
                    ""
            );

            meta.setLore(lore);
            meta.setCustomModelData(1000);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.getPersistentDataContainer().set(staffKey, PersistentDataType.STRING, "fire_staff");

            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isFireStaff(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(staffKey, PersistentDataType.STRING);
    }
}
