package org.democraftic.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.democraftic.core.Democraftic;

import java.io.File;
import java.util.Random;

public class RegenerateWorld implements CommandExecutor {

    private final Democraftic plugin;

    public RegenerateWorld(Democraftic plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        Democraftic.worldSystem.regenWorld("World-Mining");

        return false;
    }



}
