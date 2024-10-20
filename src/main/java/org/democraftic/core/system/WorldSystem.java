package org.democraftic.core.system;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.democraftic.core.Democraftic;

import java.io.File;
import java.util.List;
import java.util.Random;

public final class WorldSystem {
    public final Democraftic plugin;

    public WorldSystem(Democraftic plugin) {
        this.plugin = plugin;
    }


    public void regenWorld(String worldName){
        World world = plugin.getServer().getWorld(worldName);

        kickAllPlayers(world);

        plugin.getServer().unloadWorld(world,false);

        deleteWorld(world.getWorldFolder());

        WorldCreator c = new WorldCreator(worldName);
        c.seed((new Random()).nextLong());

        c.type(WorldType.NORMAL);
        c.generateStructures(true);
        c.generator();



        plugin.getServer().getWorlds().add(Bukkit.createWorld(c));

    }

    public void kickAllPlayers(World world){
        World mainWorld = plugin.getServer().getWorlds().get(0);
        Location spawn = mainWorld.getSpawnLocation();

        List<Player> players = world.getPlayers();

        for (int i = 0; i < players.size(); i++) {

            players.get(i).teleport(spawn);
        }
    }


    private boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }


    public void loadWorld(String worldName){
        WorldCreator c = new WorldCreator(worldName);
        c.type(WorldType.NORMAL);
        c.generateStructures(true);
        plugin.getServer().getWorlds().add(Bukkit.createWorld(c));

    }

}
