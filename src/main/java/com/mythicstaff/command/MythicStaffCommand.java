package com.mythicstaff.command;

import com.mythicstaff.MythicStaffPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MythicStaffCommand implements CommandExecutor {

    private final MythicStaffPlugin plugin;

    public MythicStaffCommand(MythicStaffPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/staff give [player]" + ChatColor.GRAY + " to get a staff.");
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                                                    ");
        sender.sendMessage("");
        sender.sendMessage("  " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "MYTHIC STAFFS " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "v1.0");
        sender.sendMessage("");
        sender.sendMessage("  " + ChatColor.GOLD + "Commands");
        sender.sendMessage("  " + ChatColor.YELLOW + "/staff give " + ChatColor.DARK_GRAY + "[player] " + ChatColor.GRAY + "- Obtain the Fire Staff");
        sender.sendMessage("  " + ChatColor.YELLOW + "/mythicstaff help " + ChatColor.GRAY + "- Show this menu");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "          " + ChatColor.RESET + ChatColor.GOLD + " Abilities " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                             ");
        sender.sendMessage("");
        sender.sendMessage("  " + ChatColor.WHITE + "" + ChatColor.BOLD + "Left Click");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "Fire Dash");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Dash forward leaving a blazing trail.");
        sender.sendMessage("");
        sender.sendMessage("  " + ChatColor.WHITE + "" + ChatColor.BOLD + "Sneak + Right Click");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "Inferno Ring");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Summon a spinning fire ring.");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Then " + ChatColor.YELLOW + "Left Click" + ChatColor.GRAY + " to shoot it!");
        sender.sendMessage("");
        sender.sendMessage("  " + ChatColor.WHITE + "" + ChatColor.BOLD + "Right Click");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "Flame Burst");
        sender.sendMessage("  " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Release a cone of flames ahead.");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                                                    ");
        sender.sendMessage("");
    }
}
