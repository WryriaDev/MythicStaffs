package com.mythicstaff;

import com.mythicstaff.command.MythicStaffCommand;
import com.mythicstaff.command.StaffGiveCommand;
import com.mythicstaff.listener.StaffComboListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MythicStaffPlugin extends JavaPlugin {

    private static MythicStaffPlugin instance;
    private StaffManager staffManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        staffManager = new StaffManager(this);
        cooldownManager = new CooldownManager();

        getServer().getPluginManager().registerEvents(new StaffComboListener(this), this);
        getServer().getPluginManager().registerEvents(new LavaWalkerManager(this), this);

        getCommand("mythicstaff").setExecutor(new MythicStaffCommand(this));
        getCommand("staff").setExecutor(new StaffGiveCommand(this));

        getLogger().info("========================================");
        getLogger().info("  MythicStaffs v1.0 has been enabled!");
        getLogger().info("  Fire Staff loaded with 5 abilities.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("MythicStaffs disabled.");
    }

    public static MythicStaffPlugin getInstance() {
        return instance;
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
