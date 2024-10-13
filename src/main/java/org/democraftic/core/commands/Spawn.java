package org.democraftic.core.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.democraftic.core.Democraftic;

public class Spawn implements CommandExecutor {

    public final Plugin plugin;
    public final FileConfiguration config;

    public Spawn(Plugin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            boolean canTP = Democraftic.teleportationSystem.canPlayerTeleport(player);

            Democraftic.teleportationSystem.WaitTeleportPlayer(player,getSpawnLocationFromConfig());
            //player.teleport(getSpawnLocationFromConfig());
        }


        return false;
    }


    private Location getSpawnLocationFromConfig() {
        double x = config.getDouble("Config.Spawn.x");
        double y = config.getDouble("Config.Spawn.y");
        double z = config.getDouble("Config.Spawn.z");
        float yaw = (float) config.getDouble("Config.Spawn.yaw");
        float pitch = (float) config.getDouble("Config.Spawn.pitch");
        World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));
        return new Location(world, x, y, z, yaw, pitch);
    }

}
