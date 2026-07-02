package com.mythicstaff.command;

import com.mythicstaff.MythicStaffPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffCommand implements CommandExecutor {

    private final MythicStaffPlugin plugin;

    public StaffCommand(MythicStaffPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("staff.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length < 1 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(ChatColor.RED + "Usage: /staff give [player]");
            return true;
        }

        Player target = null;
        if (args.length >= 2) {
            target = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        ItemStack staff = plugin.getStaffManager().createFireStaff();
        target.getInventory().addItem(staff);
        
        sender.sendMessage(ChatColor.GREEN + "Gave a Fire Staff to " + target.getName() + "!");
        target.sendMessage(ChatColor.GREEN + "You have received the " + ChatColor.GOLD + "Fire Staff" + ChatColor.GREEN + "!");
        
        return true;
    }
}
