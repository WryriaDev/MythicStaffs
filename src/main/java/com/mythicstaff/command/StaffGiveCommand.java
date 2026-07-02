package com.mythicstaff.command;

import com.mythicstaff.MythicStaffPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffGiveCommand implements CommandExecutor {

    private final MythicStaffPlugin plugin;

    public StaffGiveCommand(MythicStaffPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mythicstaff.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
            return true;
        }

        Player target = null;

        if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            target = Bukkit.getPlayer(args[1]);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("give")) {
            if (sender instanceof Player) {
                target = (Player) sender;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Usage: /staff give [player]");
            return true;
        }

        ItemStack staff = plugin.getStaffManager().createFireStaff();
        target.getInventory().addItem(staff);

        sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "MythicStaffs" + ChatColor.DARK_GRAY + "] "
                + ChatColor.GRAY + "Gave " + ChatColor.RED + "Fire Staff" + ChatColor.GRAY + " to " + ChatColor.WHITE + target.getName());

        if (!target.equals(sender)) {
            target.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "MythicStaffs" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.GRAY + "You received the " + ChatColor.RED + "Fire Staff" + ChatColor.GRAY + ".");
        }

        return true;
    }
}
