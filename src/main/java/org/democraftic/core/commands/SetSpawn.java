package org.democraftic.core.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetSpawn implements CommandExecutor {

    public final Plugin plugin;
    public final FileConfiguration config;

    public SetSpawn(Plugin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("admin.setspawn")){
                Location l = player.getLocation();

                config.set("Config.Spawn.x",l.getX());
                config.set("Config.Spawn.y",l.getY());
                config.set("Config.Spawn.z",l.getZ());
                config.set("Config.Spawn.world",l.getWorld().getName());
                config.set("Config.Spawn.yaw",l.getYaw());
                config.set("Config.Spawn.pitch",l.getPitch());

                plugin.saveConfig();

                player.sendMessage("Le spawn a été replacé.");
                return true;
            }
        }
        return false;
    }
}
