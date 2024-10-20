package org.democraftic.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.democraftic.core.Democraftic;

public class MinageTp implements CommandExecutor {

    private final Democraftic plugin;

    public MinageTp(Democraftic plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {



        if(sender instanceof Player) {
            World worldM = plugin.getServer().getWorld("World-mining");

            Location loc = new Location(worldM,0,worldM.getHighestBlockYAt(0,0),0);
            Player player = (Player) sender;

            player.teleport(loc);
        }
        return false;
    }
}
